package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.select_category

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.ItemCategoryBinding
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.mapOfDrawables

class CategoriesRVAdapter :
    ListAdapter<CategoryView, CategoriesRVAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    private var onClickListener: OnClickListener? = null

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryView: CategoryView) {
            binding.name.text = categoryView.name
            binding.icon.setImageResource(mapOfDrawables[categoryView.icon] ?: R.drawable.ic_other)
            binding.amount.visibility = View.GONE

            binding.name.isSelected = true

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                Color.parseColor(categoryView.iconColor)
            )

            itemView.setOnClickListener {
                onClickListener?.onClick(categoryView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<CategoryView>() {
        override fun areItemsTheSame(oldItem: CategoryView, newItem: CategoryView): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryView, newItem: CategoryView): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class OnClickListener(val clickListener: (category: CategoryView) -> Unit) {
        fun onClick(category: CategoryView) = clickListener(category)
    }
}