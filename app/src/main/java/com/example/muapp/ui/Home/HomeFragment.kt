package com.example.muapp.ui.Home

import ClusterUtility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.muapp.R
import com.example.muapp.data.model.RestaurantAccessInfoItem
import com.example.muapp.data.model.RestaurantFavorite
import com.example.muapp.data.model.RestaurantInfoItem
import com.example.muapp.data.model.RestaurantItem
import com.example.muapp.data.source.RestaurantFavoriteRepository
import com.example.muapp.data.source.RestaurantRepository
import com.example.muapp.data.source.RestaurantService
import com.example.muapp.data.source.local.AppDatabase
import com.example.muapp.databinding.FragmentHomeBinding
import com.example.muapp.load
import com.example.muapp.ui.MyPage.FavoriteViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(),OnMapReadyCallback {


    private var _binding : FragmentHomeBinding? = null
    private val binding get() =  _binding!!

//    private val repository : RestaurantRepository by lazy {
//        RestaurantRepository(
//            RestaurantService.create(),
//            db.restaurantFavoriteDao()
//        )
//    }

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private var isMapInit = false
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private lateinit var bottomSheetBehavior : BottomSheetBehavior<ConstraintLayout>

    private val args : HomeFragmentArgs by navArgs()
    private var auth= Firebase.auth

   // private lateinit var deliveryFavoriteRestaurant : RestaurantFavorite

    private lateinit var clusterUtility: ClusterUtility //

    // 초기 클러스터 반경 설정 (단위: 미터)
    private val clusterRadius = 30000.0

    @Inject lateinit var db :AppDatabase

    private val viewModel : HomeViewModel by viewModels()
//    {
//        val repository = RestaurantRepository(RestaurantService.create(),db.restaurantFavoriteDao())
//        HomeViewModel.provideFactory(repository)
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getInstance(requireContext().applicationContext)


        //Log.e("homefrag!!", "${auth.currentUser!!.email}")

        //initializeData()
        setMap()
        setBottomSheet()
        setObservers()
    }

    private fun setObservers() {
        viewModel.restaurantList.observe(viewLifecycleOwner) {itemList->
            clusterUtility = ClusterUtility(
                naverMap = naverMap,
                onMarkerClick = { item ->
                    showBottomSheetInfo(item)
                }
            )
            clusterUtility.addMarkers(itemList,clusterRadius)
        }

        viewModel.isFavorite.observe(viewLifecycleOwner){ isFavorite->
            binding.bottmSheet.ibFavoriteStar.isSelected = isFavorite
        }

        viewModel.restaurantInfo.observe(viewLifecycleOwner) { info: RestaurantInfoItem ->
            with(binding.bottmSheet) {
                tvRestaurantDetailInfoFirstMenu.text = info.firstMenu
                tvRestaurantDetailInfoTreatMenu.text = info.treatMenu
                tvRestaurantDetailInfoPacking.text = info.packing
                tvRestaurantDetailInfoParking.text = info.parking
                tvRestaurantDetailInfoOpenTime.text = info.openTime
                tvRestaurantDetailInfoRestDate.text = info.restDate
            }
        }

        viewModel.restaurantAccessInfo.observe(viewLifecycleOwner) { info: RestaurantAccessInfoItem ->
            with(binding.bottmSheet) {
                tvRestaurantAccessExitLabel.setValue(info.exit)
                tvRestaurantAccessAccessInfoLabel.setValue(info.accessInfo)
                tvRestaurantAccessRouteLabel.setValue(info.route)
                tvRestaurantAccessElevatorLabel.setValue(info.elevator)
                tvRestaurantAccessParkingLabel.setValue(info.parking)
            }
        }
    }
//
//
//    private fun initializeData() {
//        db = AppDatabase.getInstance(requireContext().applicationContext)
//
//        deliveryFavoriteRestaurant = RestaurantFavorite(
//            userId = auth.currentUser?.uid ?: "",
//            contentId = "",
//            contentTitle = "",
//            contentAddress = "",
//            imageUrl = "",
//            areaCode = "",
//            sigunguCode = ""
//        )
//    }

    private fun setMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun setBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottmSheet.constBottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.bottmSheet.ibFavoriteStar.setOnClickListener {
            viewModel.toggleFavorite(auth.currentUser!!.uid)
        }
    }


    override fun onMapReady(mapObject : NaverMap) {
        naverMap = mapObject
        isMapInit= true
        naverMap.locationSource = locationSource
        viewModel.loadInitialRestaurant()
       // showInitialMarker() //초기 마커 보여주기
        showRestaurantInfoAfterSearchAndMoveCamera()
    }

//    private fun showInitialMarker() {
//
//        lifecycleScope.launch {
//            val response = repository.getRestaurantEntity()
//            val itemList = response.response.body.restaurantItems.restaurantItem
//
//            // makeMarkerAndBottomSheetInfo(itemList)
//            clusterUtility = ClusterUtility( //
//                naverMap = naverMap,
//                onMarkerClick = { item ->
//                    showBottomSheetInfo(item)
//                }
//            )
//            clusterUtility.addMarkers(itemList, clusterRadius)   //
//
//        }
//    }


    private fun showBottomSheetInfo(item: RestaurantItem) {
        with(binding.bottmSheet) {
            tvRestaurantName.text = item.title
            tvRestaurantAddress.text = item.address
            evRestaurant1.load(item.imageUrl1)
            evRestaurant2.load(item.imageUrl2)
            tvRestaurantTel.text = item.tel

        }
        val uid = auth.currentUser!!.uid
        viewModel.readyForFavoriteRestaurant(item,uid)
        viewModel.checkFavoriteStatus(item.contentId,uid) // 즐겨찾기에 이미 추가된 상태인지 아닌지 체크
        viewModel.loadRestaurantInfo(item.contentId)
        viewModel.loadRestaurantAccessInfo(item.contentId)
//
//        lifecycleScope.launch {
//            val existingFavorite = repository.getFavoriteIfExists(
//                auth.currentUser!!.uid,
//                item.contentId
//            )
//            binding.bottmSheet.ibFavoriteStar.isSelected = existingFavorite != null
//        }
//
//        addRestaurantInfo(item.contentId)
//        addRestaurantAccessInfo(item.contentId)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


    private fun showRestaurantInfoAfterSearchAndMoveCamera() {
        if (args.contentId != null && args.mapX != null && args.mapY != null) // 검색 결과 받아서 클릭 했을 때
        {
            val lat = args.mapY
            val lng = args.mapX
            val position = LatLng(lat!!.toDouble(), lng!!.toDouble()) // mapY가 먼저 mapX가 나중
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(position, 15.0)
                .animate(CameraAnimation.Easing)
            naverMap.moveCamera(cameraUpdate)
        }
    }

//    private fun addToFavoriteAndDeleteToFavorite()
//    {
//        lifecycleScope.launch {
//            val restaurantFavoriteDao = db.restaurantFavoriteDao()
//
//            val existingFavorite = restaurantFavoriteDao.getFavoriteIfExists(
//                auth.currentUser!!.uid,
//                deliveryFavoriteRestaurant.contentId
//            )
//
//            if (existingFavorite == null) {
//                restaurantFavoriteDao.insert(
//                    RestaurantFavorite(
//                        userId = auth.currentUser!!.uid,
//                        contentId = deliveryFavoriteRestaurant.contentId,
//                        contentTitle = deliveryFavoriteRestaurant.contentTitle,
//                        contentAddress = deliveryFavoriteRestaurant.contentAddress,
//                        imageUrl = deliveryFavoriteRestaurant.imageUrl,
//                        areaCode = deliveryFavoriteRestaurant.areaCode,
//                        sigunguCode = deliveryFavoriteRestaurant.sigunguCode
//                    )
//                )
//                binding.bottmSheet.ibFavoriteStar.isSelected = true // 선택 상태로 변경
//                Toast.makeText(context, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show()
//            } else {
//                restaurantFavoriteDao.delete(existingFavorite)
//                binding.bottmSheet.ibFavoriteStar.isSelected = false // 선택 해제 상태로 변경
//                Toast.makeText(context, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }





//    private fun addRestaurantAccessInfo(contentId: String) {
//        lifecycleScope.launch {
//            val restaurantAccessInfoEntity = repository.getRestaurantAccessInfoEntity(contentId)
//            val itemList = restaurantAccessInfoEntity.response.body.restaurantItems.restaurantItem
//            withContext(Dispatchers.Main)
//            {
//                binding.bottmSheet.tvRestaurantAccessExitLabel.setValue(itemList.first().exit)
//                binding.bottmSheet.tvRestaurantAccessAccessInfoLabel.setValue(itemList.first().accessInfo)
//                binding.bottmSheet.tvRestaurantAccessRouteLabel.setValue(itemList.first().route)
//                binding.bottmSheet.tvRestaurantAccessElevatorLabel.setValue(itemList.first().elevator)
//                binding.bottmSheet.tvRestaurantAccessParkingLabel.setValue(itemList.first().parking)
//
//            }
//        }
//
//    }


//    private fun addRestaurantInfo(contentId : String) {
//        lifecycleScope.launch {
//            val restaurantInfoEntity = repository.getRestaurantInfoEntity(contentId)
//            val itemList = restaurantInfoEntity.response.body.restaurantItems.restaurantItem
//            withContext(Dispatchers.Main)
//            {
//                binding.bottmSheet.tvRestaurantDetailInfoFirstMenu.text = itemList.first().firstMenu
//                binding.bottmSheet.tvRestaurantDetailInfoTreatMenu.text = itemList.first().treatMenu
//                binding.bottmSheet.tvRestaurantDetailInfoPacking.text = itemList.first().packing
//                binding.bottmSheet.tvRestaurantDetailInfoParking.text = itemList.first().parking
//                binding.bottmSheet.tvRestaurantDetailInfoOpenTime.text = itemList.first().openTime
//                binding.bottmSheet.tvRestaurantDetailInfoRestDate.text = itemList.first().restDate
//
//
//            }
//        }
//    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



//
//    private fun makeMarkerAndBottomSheetInfo(itemList : List<RestaurantItem>)
//    {
//        itemList.forEach { item ->
//            val lat = item.mapY.toDouble()
//            val lng = item.mapX.toDouble()
//
//            if (lat in -90.0..90.0 && lng in -180.0..180.0) {
//                val marker = Marker()
//                marker.position = LatLng(lat, lng)
//                marker.map = naverMap
//                marker.captionText = item.title
//
//
//
//                // 마커 클릭 리스너 설정
//                marker.setOnClickListener {
//                    val markerPosition = marker.position // 클릭된 마커의 위치
//
//                    // 카메라 업데이트 설정
//                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(markerPosition, 15.0)
//                        .animate(CameraAnimation.Easing) // 애니메이션 설정
//
//                    // 카메라 이동
//                    naverMap.moveCamera(cameraUpdate)
//
//                    //바텀 시트 정보 불러오기
//                    with(binding.bottmSheet)
//                    {
//                        tvRestaurantName.text = item.title
//                        tvRestaurantAddress.text = item.address
//                        evRestaurant1.load(item.imageUrl1)
//                        evRestaurant2.load(item.imageUrl2)
//                        tvRestaurantTel.text = item.tel
//
//                        deliveryFavoriteRestaurant = deliveryFavoriteRestaurant.copy(
//                            contentId = item.contentId,
//                            contentTitle = item.title,
//                            contentAddress = item.address,
//                            imageUrl = item.imageUrl1,
//                            areaCode = item.areaCode,
//                            sigunguCode = item.sigunguCode
//                        )
//
//
//                    }
//                    lifecycleScope.launch {
//                        val existingFavorite= db.restaurantFavoriteDao().getFavoriteIfExists(
//                            auth.currentUser!!.uid,
//                            item.contentId
//                        )
//
//                        binding.bottmSheet.ibFavoriteStar.isSelected = existingFavorite != null
//
//                    }
//                    addRestaurantInfo(item.contentId) // 바텀시트 상세 정보 불러오기
//                    addRestaurantAccessInfo(item.contentId)
//                    //바텀 시트 보이기
//                    bottomSheetBehavior.state= BottomSheetBehavior.STATE_EXPANDED
//                    // 마커 클릭 이벤트 소비 (true로 설정하면 기본 마커 클릭 동작을 막음)
//                    false
//                }
//
//
//
//
//            }
//        }
//    }
}