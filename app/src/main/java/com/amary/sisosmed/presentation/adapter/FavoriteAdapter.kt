package com.amary.sisosmed.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.amary.sisosmed.base.BaseAdapter
import com.amary.sisosmed.databinding.ItemListBinding
import com.amary.sisosmed.domain.model.Story
import com.bumptech.glide.Glide

class FavoriteAdapter : BaseAdapter<Story, ItemListBinding>(ItemListBinding::inflate, DiffCallBack) {
    companion object {
        object DiffCallBack : DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Story, newItem: Story) = oldItem == newItem
        }
    }

    override fun itemViewHolder(binding: ItemListBinding, data: Story) {
        binding.apply {
            Glide.with(binding.root.context)
                .load(data.photoUrl)
                .into(imgList)

            tvName.text = data.name
        }
    }

}