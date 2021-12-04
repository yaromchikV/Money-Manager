package com.yaromchikv.moneymanager.feature.presentation.ui.transactions

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.databinding.ItemDayInfoBinding
import com.yaromchikv.moneymanager.databinding.ItemTransactionBinding
import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.mapOfDrawables
import javax.inject.Inject

class TransactionsRVAdapter @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : ListAdapter<Any, TransactionsRVAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    private var onDeleteClickListener: OnDeleteClickListener? = null

    abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: Any)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TRANSACTION_VIEW_TYPE -> {
                val binding = ItemTransactionBinding.inflate(layoutInflater, parent, false)
                TransactionViewHolder(binding)
            }
            else -> {
                val binding = ItemDayInfoBinding.inflate(layoutInflater, parent, false)
                DayInfoViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (holder.itemViewType) {
            TRANSACTION_VIEW_TYPE -> {
                val viewHolder = holder as TransactionViewHolder
                viewHolder.bind(getItem(position))
            }
            else -> {
                val viewHolder = holder as DayInfoViewHolder
                viewHolder.bind(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is TransactionView) TRANSACTION_VIEW_TYPE else DAY_INFO_VIEW_TYPE
    }

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    fun clearSelectedPosition() {
        selectedPosition = RecyclerView.NO_POSITION
    }

    inner class TransactionViewHolder(
        private val binding: ItemTransactionBinding
    ) : ItemViewHolder(binding.root) {

        override fun bind(item: Any) {
            val transactionView = item as TransactionView

            binding.categoryName.text = transactionView.categoryName
            binding.cardName.text = transactionView.accountName
            binding.icon.setImageResource(
                mapOfDrawables[transactionView.icon] ?: R.drawable.ic_bank
            )

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                Color.parseColor(transactionView.iconColor)
            )

            binding.note.text = transactionView.note
            binding.textAmount.text = transactionView.amount.toAmountFormat(withMinus = true)
            binding.textCurrency.text = sharedPreferences.getString(
                "currency",
                context.resources.getStringArray(R.array.currency_values)[0]
            )

            binding.note.isSelected = true
            binding.categoryName.isSelected = true
            binding.cardName.isSelected = true

            val isSelected = bindingAdapterPosition == selectedPosition

            binding.note.visibility = if (isSelected) View.INVISIBLE else View.VISIBLE
            binding.textAmount.visibility = if (isSelected) View.INVISIBLE else View.VISIBLE
            binding.textCurrency.visibility = if (isSelected) View.INVISIBLE else View.VISIBLE
            binding.deleteButton.visibility = if (!isSelected) View.INVISIBLE else View.VISIBLE

            itemView.setOnClickListener {
                if (bindingAdapterPosition == selectedPosition)
                    itemSelecting(false)
            }

            itemView.setOnLongClickListener {
                itemSelecting(bindingAdapterPosition != selectedPosition)
                true
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClickListener?.onClick(transactionView)
            }
        }

        private fun itemSelecting(activate: Boolean) {
            notifyItemChanged(selectedPosition)
            selectedPosition = if (activate) bindingAdapterPosition else RecyclerView.NO_POSITION
            notifyItemChanged(selectedPosition)
        }
    }

    inner class DayInfoViewHolder(
        private val binding: ItemDayInfoBinding
    ) : ItemViewHolder(binding.root) {

        override fun bind(item: Any) {
            val dayInfo = item as DayInfo

            val date = dayInfo.transactionDate
            val amount = dayInfo.amountPerDay
            val monthAndYear = "${date.month} ${date.year}"

            binding.amount.text = amount.toAmountFormat(withMinus = true)
            binding.currency.text = sharedPreferences.getString(
                "currency",
                context.resources.getStringArray(R.array.currency_values)[0]
            )

            binding.day.text = date.dayOfMonth.toString()
            binding.monthAndYear.text = monthAndYear
            binding.dayOfWeek.text = date.dayOfWeek.name
        }
    }

    fun setOnDeleteClickListener(onDeleteClickListener: OnDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    class OnDeleteClickListener(val clickListener: (transaction: TransactionView) -> Unit) {
        fun onClick(transaction: TransactionView) = clickListener(transaction)
    }

    companion object {
        private const val TRANSACTION_VIEW_TYPE = 1
        private const val DAY_INFO_VIEW_TYPE = 0

        object DIFF_CALLBACK : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return if (oldItem is DayInfo && newItem is DayInfo)
                    oldItem.transactionDate == newItem.transactionDate
                else if (oldItem is TransactionView && newItem is TransactionView)
                    oldItem.id == newItem.id
                else false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return if (oldItem is DayInfo && newItem is DayInfo)
                    oldItem.hashCode() == newItem.hashCode()
                else if (oldItem is TransactionView && newItem is TransactionView)
                    oldItem.hashCode() == newItem.hashCode()
                else false
            }
        }
    }
}