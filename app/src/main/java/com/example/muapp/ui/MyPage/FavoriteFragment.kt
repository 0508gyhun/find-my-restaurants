package com.example.muapp.ui.MyPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.muapp.R
import com.example.muapp.data.model.RestaurantItem
import com.example.muapp.data.source.RestaurantFavoriteRepository
import com.example.muapp.data.source.RestaurantService
import com.example.muapp.data.source.SearchRepository
import com.example.muapp.data.source.local.AppDatabase
import com.example.muapp.data.source.local.RestaurantFavoriteDao
import com.example.muapp.databinding.FragmentFavoriteBinding
import com.example.muapp.ui.Search.SearchViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewPagerAdapter: FavoriteAdapter = FavoriteAdapter()
    private val viewPagerSubAdapter :FavoriteRecommendationAdapter = FavoriteRecommendationAdapter()
    @Inject lateinit var db :AppDatabase


    private val viewModel : FavoriteViewModel by viewModels()
//    {
//        val repository = RestaurantFavoriteRepository(db.restaurantFavoriteDao(), RestaurantService.create())
//        FavoriteViewModel.provideFactory(repository)
//    }
    private var auth= Firebase.auth
    //private val repository : RestaurantFavoriteRepository by lazy { RestaurantFavoriteRepository(db.restaurantFavoriteDao(), RestaurantService.create()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  db = AppDatabase.getInstance(requireContext().applicationContext)

        binding.tvUserid.text= auth.currentUser!!.email

        // 초기 어댑터 설정
        setViewPagerFavorite()


        setViewPagerFavoriteSub()


        setObservers()
        viewModel.loadDataFromDatabase(auth.currentUser!!.uid)
    }
        // 데이터 로딩
//        lifecycleScope.launch {
//            val itemList = repository.getAllFavoriteRestaurant(auth.currentUser!!.uid)
//            Log.e("favoriteee", "${itemList.toString()}")
//            viewPagerAdapter.submitList(itemList)
//
//            // secondList 초기화 및 데이터 로딩
//            val secondList: MutableList<List<RestaurantItem>> = mutableListOf()
//            itemList.forEach {
//                val restaurantEntity = repository.getRestaurantAreaRecommend(it.areaCode, it.sigunguCode)
//                val subItemList = restaurantEntity.response.body.restaurantItems.restaurantItem
//                secondList.add(subItemList)
//            }
//
//            // 데이터 로딩이 완료된 후에 콜백 등록
//            if (secondList.isNotEmpty()) {
//                Log.e("lms!!", "${secondList[0].toString()}")
//
//                binding.viewPagerFavorite.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                    override fun onPageSelected(position: Int) {
//                        super.onPageSelected(position)
//                        if (position < secondList.size) {
//                            viewPagerSubAdapter.submitList(secondList[position])
//                        }
//                    }
//                })
//
//                // 초기 페이지 데이터 설정
//                viewPagerSubAdapter.submitList(secondList[0])
//            }
//        }





    private fun setObservers() {
        viewModel.favoriteItems.observe(viewLifecycleOwner) { items ->
            viewPagerAdapter.submitList(items)
        }

        viewModel.recommendItems.observe(viewLifecycleOwner) { recommendList ->
            if (recommendList.isNotEmpty()) {
                // 첫 번째 추천 목록 표시
                viewPagerSubAdapter.submitList(recommendList[0])
                binding.progressBar.isVisible = false
                // ViewPager 페이지 변경 리스너 설정
                binding.viewPagerFavorite.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        viewModel.getRecommendationsForPosition(position)?.let { items ->
                            viewPagerSubAdapter.submitList(items)
                        }
                    }
                })
            }
        }
    }

    private fun setViewPagerFavoriteSub() {
        binding.viewPagerFavoriteSub.adapter = viewPagerSubAdapter
        val screenWidth = resources.displayMetrics.widthPixels
        val pageWidth = resources.getDimension(R.dimen.viewpager_sub_item_width)
        val pageMargin = resources.getDimension(R.dimen.viewpager_sub_item_margin)
        val offset = screenWidth - pageWidth - pageMargin

        binding.viewPagerFavoriteSub.setPageTransformer { page, position ->
            page.translationX = position * -offset
        }
        binding.viewPagerFavoriteSub.offscreenPageLimit = 2
    }

    private fun setViewPagerFavorite() {
        binding.viewPagerFavorite.adapter = viewPagerAdapter
        val screenWidth = resources.displayMetrics.widthPixels
        val pageWidth = resources.getDimension(R.dimen.viewpager_item_width)
        val pageMargin = resources.getDimension(R.dimen.viewpager_item_margin)
        val offset = screenWidth - pageWidth - pageMargin

        binding.viewPagerFavorite.setPageTransformer { page, position ->
            page.translationX = position * -offset
        }
        binding.viewPagerFavorite.offscreenPageLimit = 2
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}