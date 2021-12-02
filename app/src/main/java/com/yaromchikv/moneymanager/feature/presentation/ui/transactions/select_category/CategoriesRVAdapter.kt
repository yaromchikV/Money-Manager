package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.select_category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.ItemCategoryBinding
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfColors
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfDrawables
import javax.inject.Inject

class CategoriesRVAdapter @Inject constructor(
    private val context: Context
) : ListAdapter<CategoryView, CategoriesRVAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    private var onClickListener: OnClickListener? = null

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryView: CategoryView) {
            binding.name.text = categoryView.name
            binding.icon.setImageResource(mapOfDrawables[categoryView.icon] ?: R.drawable.ic_other)
            binding.amount.visibility = View.GONE

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                ContextCompat.getColor(
                    context,
                    mapOfColors[categoryView.iconColor] ?: R.color.orange_red
                )
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
            return oldItem == newItem
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