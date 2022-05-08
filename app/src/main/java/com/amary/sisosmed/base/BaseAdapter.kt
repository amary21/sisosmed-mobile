package com.amary.sisosmed.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, VB: ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseAdapter.ViewHolder<VB>>(diffCallback) {

    protected abstract fun itemViewHolder(binding: VB, data: T)
    private var itemClickListener: ((T) -> Unit?)? = null

    fun setOnClickListener(listener : (T) -> Unit){
        itemClickListener = { listener(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        val viewBinding = bindingInflater.invoke(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder<VB>, position: Int) {
        with(holder){
            itemViewHolder(binding, getItem(position))
            binding.root.setOnClickListener {
                if (itemClickListener != null){
                    itemClickListener?.invoke(getItem(position))
                }
            }
        }
    }

    class ViewHolder<VB: ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}