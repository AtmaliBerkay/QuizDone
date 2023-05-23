package com.example.quizdone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quizdone.databinding.CategoryRowBinding
import com.example.quizdone.model.CategoryModel

import java.util.Locale.Category


class CategoryAdapter : ListAdapter<CategoryModel, CategoryAdapter.ViewHolder>(CategoryDiffCallback()) {

    class ViewHolder(private val binding: CategoryRowBinding): RecyclerView.ViewHolder(binding.root) {

        //CategoryRow xml Binding
        fun bind(addr: CategoryModel) {
            //binding.tvCategory.text= addr.category
            //binding.tvDescription.text= addr.description

        }
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = CategoryRowBinding.inflate(inflater,parent,false)
            return ViewHolder(binding)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }
}