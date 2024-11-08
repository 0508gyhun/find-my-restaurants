package com.example.muapp.ui.Search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.muapp.data.model.RestaurantItem
import com.example.muapp.databinding.ItemSearchBinding

class SearchAdapter(private val clickListener: SearchResponseClickListener): ListAdapter<RestaurantItem, SearchAdapter.SearchViewHolder>(SearchDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        return holder.bind(getItem(position),clickListener)
    }
    class SearchViewHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind (item: RestaurantItem, clickListener: SearchResponseClickListener)
        {
            itemView.setOnClickListener{
                clickListener.onResponseClick(item.contentId, item.mapX, item.mapY)
            }
            binding.tvRestaurantName.text = item.title
            binding.tvRestaurantAddress.text = item.address
        }
        companion object{
            fun from(parent: ViewGroup): SearchViewHolder{
                return SearchViewHolder(
                    ItemSearchBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }


    //class
}

class SearchDiffCallback : DiffUtil.ItemCallback<RestaurantItem>() {
    override fun areItemsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem): Boolean {
        return oldItem.contentId == newItem.contentId
    }

    override fun areContentsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem): Boolean {
        return oldItem == newItem
    }

}

