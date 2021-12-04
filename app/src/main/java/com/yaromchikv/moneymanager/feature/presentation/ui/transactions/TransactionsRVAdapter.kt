package com.yaromchikv.moneymanager.feature.presentation.ui.transactions

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.toAmountFormat
import com.yaromchikv.moneymanager.databinding.ItemDayInfoBinding
import com.yaromchikv.moneymanager.databinding.ItemTransactionBinding
import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.mapOfDrawables
import javax.inject.Inject

class TransactionsRVAdapter @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactionsWithInfoList = emptyList<Any>()

    private var onDeleteClickListener: OnDeleteClickListener? = null

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class TransactionViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transactionView: TransactionView) {
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
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dayInfo: DayInfo) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TRANSACTION_VIEW_TYPE -> {
                val viewHolder = holder as TransactionViewHolder
                val transactionView = transactionsWithInfoList[position] as TransactionView
                viewHolder.bind(transactionView)
            }
            else -> {
                val viewHolder = holder as DayInfoViewHolder
                val dayInfo = transactionsWithInfoList[position] as DayInfo
                viewHolder.bind(dayInfo)
            }
        }
    }

    override fun getItemCount(): Int {
        return transactionsWithInfoList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (transactionsWithInfoList[position] is TransactionView) TRANSACTION_VIEW_TYPE else DAY_INFO_VIEW_TYPE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(transactionList: List<Any>) {
        transactionsWithInfoList = transactionList
        notifyDataSetChanged()
    }

    fun clearSelectedPosition() {
        selectedPosition = RecyclerView.NO_POSITION
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
    }
}