package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.select_category

import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.databinding.ItemCategoryBinding
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.CURRENCY_PREFERENCE_KEY
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.MAIN_CURRENCY
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setIcon
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setTint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriesRVAdapter @Inject constructor(
    private val sharedPreferences: SharedPreferences
) :
    ListAdapter<CategoryView, CategoriesRVAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    private var onClickListener: OnClickListener? = null

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(categoryView: CategoryView) {
            binding.name.text = categoryView.name
            binding.icon.setIcon(categoryView.icon)
            binding.iconBackground.setTint(categoryView.iconColor)

            binding.amount.text = categoryView.amount.toAmountFormat(withMinus = false)
            binding.currency.text = sharedPreferences.getString(CURRENCY_PREFERENCE_KEY, MAIN_CURRENCY)

            binding.name.isSelected = true

            itemView.alpha = if (categoryView.amount == 0.0) 0.3f else 1f

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