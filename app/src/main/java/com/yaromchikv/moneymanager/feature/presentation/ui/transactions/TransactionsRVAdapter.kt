package com.yaromchikv.moneymanager.feature.presentation.ui.transactions

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.databinding.ItemDayInfoBinding
import com.yaromchikv.moneymanager.databinding.ItemTransactionBinding
import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.CURRENCY_PREFERENCE_KEY
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.MAIN_CURRENCY
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setIcon
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setTint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionsRVAdapter @Inject constructor(
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

            with(binding) {
                iconBackground.setTint(transactionView.iconColor)
                icon.setIcon(transactionView.icon)

                categoryName.text = transactionView.categoryName
                cardName.text = transactionView.accountName
                note.text = transactionView.note
                textAmount.text = transactionView.amount.toAmountFormat(withMinus = true)
                textCurrency.text =
                    sharedPreferences.getString(CURRENCY_PREFERENCE_KEY, MAIN_CURRENCY)

                note.isSelected = true
                categoryName.isSelected = true
                cardName.isSelected = true
            }

            configureIfSelected(transactionView)

            itemView.setOnClickListener {
                if (bindingAdapterPosition == selectedPosition)
                    itemSelecting(false)
            }

            itemView.setOnLongClickListener {
                itemSelecting(bindingAdapterPosition != selectedPosition)
                true
            }
        }

        private fun configureIfSelected(transactionView: TransactionView) {
            val isSelected = bindingAdapterPosition == selectedPosition
            with(binding) {
                note.visibility = if (isSelected) View.INVISIBLE else View.VISIBLE
                textAmount.visibility = if (isSelected) View.INVISIBLE else View.VISIBLE
                textCurrency.visibility = if (isSelected) View.INVISIBLE else View.VISIBLE
                deleteButton.visibility = if (!isSelected) View.INVISIBLE else View.VISIBLE

                deleteButton.setOnClickListener {
                    onDeleteClickListener?.onClick(transactionView)
                }
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

            val dateValue = dayInfo.transactionDate
            val amountValue = dayInfo.amountPerDay
            val monthAndYearValue = "${dateValue.month} ${dateValue.year}"

            with(binding) {
                amount.text = amountValue.toAmountFormat(withMinus = true)
                currency.text = sharedPreferences.getString(CURRENCY_PREFERENCE_KEY, MAIN_CURRENCY)

                day.text = dateValue.dayOfMonth.toString()
                monthAndYear.text = monthAndYearValue
                dayOfWeek.text = dateValue.dayOfWeek.name
            }
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
