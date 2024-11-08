// ClusterUtility.kt
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.util.Log
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.example.muapp.data.model.RestaurantItem
import kotlin.math.pow
import kotlin.math.sqrt

class ClusterUtility(private val naverMap: NaverMap,
                     private val onMarkerClick: (RestaurantItem) -> Unit) {
    private val markers = mutableListOf<Marker>()
    private val clusters = mutableListOf<Cluster>()

    private data class Cluster(
        val center: LatLng,
        val markers: MutableList<Marker> = mutableListOf(),
        var clusterMarker: Marker? = null
    )

    fun addMarkers(items: List<RestaurantItem>, clusterRadius: Double) {
        // 기존 마커와 클러스터 제거
        markers.forEach { it.map = null }
        clusters.forEach { it.clusterMarker?.map = null }
        markers.clear()
        clusters.clear()

        // 새로운 마커 생성
        items.forEach { item ->
            val lat = item.mapY.toDouble()
            val lng = item.mapX.toDouble()

            if (lat in -90.0..90.0 && lng in -180.0..180.0) {
                val marker = Marker().apply {
                    position = LatLng(lat, lng)
                    captionText = item.title
                    tag = item // 마커에 RestaurantItem 데이터 저장
                }
                markers.add(marker)
            }
        }
        markers.forEach { marker ->
            marker.setOnClickListener {
                val item = marker.tag as RestaurantItem
                onMarkerClick(item)

                val cameraUpdate = CameraUpdate.scrollAndZoomTo(marker.position, 15.0)
                    .animate(CameraAnimation.Easing)
                naverMap.moveCamera(cameraUpdate)
                true
            }
        }


        // 줌 레벨에 따른 클러스터링 처리
        updateClusters(clusterRadius)

        // 지도 줌 변경 이벤트 리스너
        naverMap.addOnCameraIdleListener {
            Log.e("utill" , "줌 레베루 ${naverMap.cameraPosition.zoom}")
            updateClusters(clusterRadius)
        }
    }

    private fun updateClusters(clusterRadius: Double) {
        // 기존 클러스터 마커 제거
        clusters.forEach { it.clusterMarker?.map = null }
        clusters.clear()

        // 현재 줌 레벨에 따라 클러스터링 여부 결정
        val zoom = naverMap.cameraPosition.zoom
        if (zoom < 11) { // 줌 레벨이 13 미만일 때 클러스터링 적용
            // 마커들을 클러스터로 그룹화
            markers.forEach { marker ->
                var addedToCluster = false
                for (cluster in clusters) {
                    if (isWithinDistance(cluster.center, marker.position, clusterRadius)) {
                        cluster.markers.add(marker)
                        marker.map = null
                        addedToCluster = true
                        break
                    }
                }
                if (!addedToCluster) {
                    clusters.add(Cluster(marker.position).apply {
                        markers.add(marker)
                        marker.map = null
                    })
                }
            }

            // 클러스터 마커 생성
            clusters.forEach { cluster ->
                if (cluster.markers.size >= 1) {
                    cluster.clusterMarker = Marker().apply {
                        position = cluster.center
                        // icon 설정 제거하고 width와 height 설정 추가
                        icon = createClusterImage(cluster.markers.size)
                        width = 180
                        height = 180
                        map = naverMap

                        setOnClickListener {
                            // 클러스터에 포함된 모든 마커가 보이도록 카메라 이동
//                            val bounds = LatLngBounds.Builder().apply {
//                                cluster.markers.forEach{include(it.position)}
//                            }.build()

                            val cameraUpdate = CameraUpdate.scrollAndZoomTo(cluster.center, 13.0)
                                .animate(CameraAnimation.Easing)
                            naverMap.moveCamera(cameraUpdate)
                            true
                        }
                    }
                } else {
                    // 단일 마커는 그대로 표시
                    cluster.markers.first().map = naverMap
                }
            }
        } else {
            // 줌 레벨이 높을 때는 개별 마커 표시
            markers.forEach { it.map = naverMap }
        }
    }
    private fun createClusterImage(count: Int): OverlayImage {
        val size = 120
        val padding = 20
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 원 그리기
        val paint = Paint().apply {
            color = Color.rgb(0, 123, 255)
            alpha = 180  // 투명도 설정 (0-255)
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(size/2f, size/2f, (size-padding)/2f, paint)

        // 텍스트 그리기
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 40f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
            isAntiAlias = true
        }

        // 텍스트 세로 중앙 정렬을 위한 계산
        val textBounds = Rect()
        val text = count.toString()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        val textHeight = textBounds.height()
        val textY = size/2f + textHeight/2f

        canvas.drawText(text, size/2f, textY, textPaint)

        return OverlayImage.fromBitmap(bitmap)
    }

    private fun isWithinDistance(point1: LatLng, point2: LatLng, distance: Double): Boolean {
        val earthRadius = 6371000.0 // 지구 반지름 (미터)
        val lat1 = Math.toRadians(point1.latitude)
        val lat2 = Math.toRadians(point2.latitude)
        val dLat = Math.toRadians(point2.latitude - point1.latitude)
        val dLng = Math.toRadians(point2.longitude - point1.longitude)

        val a = Math.sin(dLat / 2).pow(2) +
                Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(dLng / 2).pow(2)
        val c = 2 * Math.atan2(sqrt(a), sqrt(1 - a))
        val calculatedDistance = earthRadius * c

        return calculatedDistance <= distance
    }
}