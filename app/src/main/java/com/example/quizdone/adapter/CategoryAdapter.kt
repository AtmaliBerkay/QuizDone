package com.example.quizdone.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.quizdone.databinding.CategoryRowBinding
import com.example.quizdone.model.CategoryModel

class CategoryAdapter(private val categoryList: ArrayList<CategoryModel>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){

    private lateinit var mListener : onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryModel: CategoryModel = categoryList[position]
        holder.bind(categoryModel)

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class ViewHolder(private val binding: CategoryRowBinding,listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {


        fun bind(categoryModel: CategoryModel) {
            val ivCategory: ImageView = binding.ivCategory
            Glide.with(itemView.context)
                .load(categoryModel.quizImgUrl)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        // Set the fetched image as the background of the CardView
                        binding.root.background = resource
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Optional: You can handle any placeholder or clear background here
                    }
                })
            binding.tvQuizDesc.text = categoryModel.quizDesc
            binding.tvQuizTitle.text = categoryModel.quizTitle
            binding.tvQuizId.text = categoryModel.quizId

        }
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }
}