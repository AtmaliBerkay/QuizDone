package com.example.quizdone.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.quizdone.model.CategoryModel

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryModel>() {
    override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
        return oldItem == newItem
    }
}