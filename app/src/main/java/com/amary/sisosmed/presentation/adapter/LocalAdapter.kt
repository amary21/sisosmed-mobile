package com.amary.sisosmed.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amary.sisosmed.databinding.ItemLocBinding
import com.amary.sisosmed.domain.model.Localization

class LocalAdapter(private val itemClickListener: (Localization) -> Unit): ListAdapter<Localization, LocalAdapter.ViewHolder>(DiffCallBack) {
    var selected = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(itemView: ItemLocBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val binding = ItemLocBinding.bind(itemView.root)

        init {
            binding.root.setOnClickListener{
                val local = getItem(absoluteAdapterPosition)
                local?.let { itemClickListener(it) }
            }
        }

        fun bind(item: Localization) {
            if (item.name == selected){
                binding.lyItem.isSelected = true
                binding.tvLocal.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.white))
            } else {
                binding.lyItem.isSelected = false
                binding.tvLocal.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.black))
            }

            binding.tvLocal.text = item.name
        }

    }


    companion object {
        object DiffCallBack : DiffUtil.ItemCallback<Localization>(){
            override fun areItemsTheSame(oldItem: Localization, newItem: Localization) = oldItem.code == newItem.code
            override fun areContentsTheSame(oldItem: Localization, newItem: Localization) = oldItem == newItem
        }
    }
}