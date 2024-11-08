package com.example.muapp.ui.MyPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.muapp.data.model.RestaurantFavorite
import com.example.muapp.databinding.ItemFavoriteBinding
import com.example.muapp.load

class FavoriteAdapter : ListAdapter<RestaurantFavorite, FavoriteAdapter.FavoriteViewHolder>(FavoriteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
       holder.bind(getItem(position))
    }


    class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root)
    {

        fun bind(item : RestaurantFavorite) {
            binding.tvFavoriteName.text = item.contentTitle
            binding.tvFavoriteAddress.text = item.contentAddress
            binding.ivRestaurantImage.load(item.imageUrl)
           // binding.tvFavoriteCreated.text = item.createdTime.toString()
        }

        companion object {
            fun from(parent: ViewGroup): FavoriteViewHolder {
                return FavoriteViewHolder(
                    ItemFavoriteBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}

class FavoriteDiffCallback : DiffUtil.ItemCallback<RestaurantFavorite>()
{
    override fun areItemsTheSame(
        oldItem: RestaurantFavorite,
        newItem: RestaurantFavorite
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: RestaurantFavorite,
        newItem: RestaurantFavorite
    ): Boolean {
        return oldItem == newItem
    }


}