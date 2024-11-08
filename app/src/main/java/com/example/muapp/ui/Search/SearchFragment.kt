package com.example.muapp.ui.Search


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.muapp.data.model.RestaurantItem
import com.example.muapp.data.source.RestaurantService
import com.example.muapp.data.source.SearchRepository
import com.example.muapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val adapter = SearchAdapter(
        object : SearchResponseClickListener{
            override fun onResponseClick(contentId: String, mapX: String, mapY : String) {
                val action = SearchFragmentDirections.actionNavigationSearchToNavigationHome(contentId, mapX,mapY)
                findNavController().navigate(action)

            }
        }
    )

    private val viewModel : SearchViewModel by viewModels()

//    {
//        val repository = SearchRepository(RestaurantService.create())
//        SearchViewModel.provideFactory(repository)
//    }


    private var currentItems = mutableListOf<RestaurantItem>()  // 현재 표시된 아이템들
    private var isLoading = false                              // 로딩 상태
    private val initialLoadSize = 7                            // 처음 로드할 아이템 수
    private val pageSize = 3                                   // 추가로 로드할 아이템 수
    private var currentPosition = 0                            // 현재 위치
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        setSearchView()
        setObservers()
        //binding.rvSearch.adapter = adapter
        //submitInitialList()
    }

    private fun setObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner){ result->
            adapter.submitList(result)
        }
    }


    private fun setRecyclerView() {
        binding.rvSearch.adapter = adapter
        binding.rvSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !recyclerView.canScrollVertically(1) && viewModel.searchResults.value?.isNotEmpty() == true) {
                        viewModel.loadMore()
                }
            }

        })
    }

    private fun setSearchView() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()

                val imm = binding.searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                viewModel.searchRestaurants(newText)

                return false
            }

        })
    }
//
//    private fun submitInitialList() {
//        lifecycleScope.launch {
//            try {
//                val restaurantEntity = repository.getRestaurantEntity()
//                val allItems = restaurantEntity.response.body.restaurantItems.restaurantItem
//                Log.d("SearchFragment", "받은 데이터 크기: ${allItems.size}")
//                currentItems = allItems.take(initialLoadSize).toMutableList()
//                currentPosition = initialLoadSize
//                adapter.submitList(currentItems.toList())
//            } catch (e: Exception) {
//                Log.e("SearchFragment", "데이터 로딩 실패", e)
//            }
//        }
//    }
//    private fun loadMoreItems() {
//
//        lifecycleScope.launch {
//            try {
//                isLoading = true
//                val restaurantEntity = repository.getRestaurantEntity()
//                val allItems = restaurantEntity.response.body.restaurantItems.restaurantItem
//
//                val nextItems = allItems.drop(currentPosition).take(pageSize)
//                if (nextItems.isNotEmpty()) {
//                    currentItems.addAll(nextItems)
//                    currentPosition += pageSize
//                    adapter.submitList(currentItems.toList())
//                }
//            } catch (e: Exception) {
//                Log.e("SearchFragment", "추가 데이터 로드 실패", e)
//            } finally {
//                isLoading = false
//            }
//        }
//    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}