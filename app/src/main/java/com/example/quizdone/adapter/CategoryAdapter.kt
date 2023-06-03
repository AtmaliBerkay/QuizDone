package com.example.quizdone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quizdone.databinding.CategoryRowBinding
import com.example.quizdone.model.CategoryModel

class CategoryAdapter(private val categoryList: ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryModel: CategoryModel = categoryList[position]
        holder.bind(categoryModel)

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(private val binding: CategoryRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryModel: CategoryModel) {
           /* val ivCategory: ImageView = binding.ivCategory
            Glide.with(itemView.context)
                .load(categoryModel.quizImgUrl)
                .into(ivCategory)*/
            binding.tvQuizDesc.text = categoryModel.quizDesc
            binding.tvQuizTitle.text = categoryModel.quizTitle

        }
    }
}
