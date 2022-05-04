package com.amary.sisosmed.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.amary.sisosmed.databinding.ItemListBinding
import com.amary.sisosmed.domain.model.Story
import com.bumptech.glide.Glide

class PagerAdapter(private val itemClickListener: (Story) -> Unit) : PagingDataAdapter<Story, PagerAdapter.ViewHolder>(DiffCallBack) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(itemView: ItemListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = ItemListBinding.bind(itemView.root)

        init {
            binding.root.setOnClickListener{
                val story = getItem(absoluteAdapterPosition)
                story?.let { itemClickListener(it) }
            }
        }

        fun bind(item: Story) {
            binding.apply {
                Glide.with(binding.root.context)
                    .load(item.photoUrl)
                    .into(imgList)

                tvName.text = item.name
            }
        }

    }

    companion object {
        object DiffCallBack : DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Story, newItem: Story) = oldItem == newItem
        }
    }
}