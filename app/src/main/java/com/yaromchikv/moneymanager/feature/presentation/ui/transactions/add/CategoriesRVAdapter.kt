package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.add

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.ItemCategoryBinding
import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfColors
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfDrawables
import javax.inject.Inject

class CategoriesRVAdapter @Inject constructor(
    private val context: Context
) : RecyclerView.Adapter<CategoriesRVAdapter.CategoryViewHolder>() {

    private var categoryList = emptyList<Category>()

    private var onClickListener: OnClickListener? = null

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.name.text = category.name
            binding.icon.setImageResource(mapOfDrawables[category.icon] ?: R.drawable.ic_other)

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                ContextCompat.getColor(
                    context,
                    mapOfColors[category.iconColor] ?: R.color.orange_red
                )
            )

            itemView.setOnClickListener {
                onClickListener?.onClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setData(newList: List<Category>) {
        this.categoryList = newList
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class OnClickListener(val clickListener: (category: Category) -> Unit) {
        fun onClick(category: Category) = clickListener(category)
    }
}