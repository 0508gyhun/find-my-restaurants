package com.example.muapp.ui.MyPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.muapp.data.model.RestaurantFavorite
import com.example.muapp.data.model.RestaurantItem
import com.example.muapp.databinding.ItemFavoriteBinding
import com.example.muapp.databinding.ItemFavoriteRecommendBinding
import com.example.muapp.load

class FavoriteRecommendationAdapter : ListAdapter<RestaurantItem, FavoriteRecommendationAdapter.FavoriteRecommendationViewHolder>(FavoriteRecommendationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteRecommendationViewHolder {
        return FavoriteRecommendationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoriteRecommendationViewHolder, position: Int) {
       holder.bind(getItem(position))
    }


    class FavoriteRecommendationViewHolder(private val binding: ItemFavoriteRecommendBinding) : RecyclerView.ViewHolder(binding.root)
    {

        fun bind(item : RestaurantItem) {
            binding.tvFavoriteSubName.text = item.title
            binding.tvFavoriteSubAddress.text = item.address
            binding.ivRestaurantImageSub.load(item.imageUrl1)


        }

        companion object {
            fun from(parent: ViewGroup): FavoriteRecommendationViewHolder {
                return FavoriteRecommendationViewHolder(
                    ItemFavoriteRecommendBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}

class FavoriteRecommendationDiffCallback : DiffUtil.ItemCallback<RestaurantItem>()
{
    override fun areItemsTheSame(
        oldItem: RestaurantItem,
        newItem: RestaurantItem
    ): Boolean {
        return oldItem.contentId == newItem.contentId
    }

    override fun areContentsTheSame(
        oldItem: RestaurantItem,
        newItem: RestaurantItem
    ): Boolean {
        return oldItem == newItem
    }


}