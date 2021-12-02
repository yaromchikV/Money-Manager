package com.yaromchikv.moneymanager.feature.presentation.ui.transactions

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.toAmountFormat
import com.yaromchikv.moneymanager.databinding.ItemDayInfoBinding
import com.yaromchikv.moneymanager.databinding.ItemTransactionBinding
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.domain.usecase.TransactionUseCases
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfColors
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfDrawables
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionsRVAdapter @Inject constructor(
    private val context: Context,
    private val transactionUseCases: TransactionUseCases
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactionsWithInfoList = emptyList<Any>()

    inner class TransactionViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transactionView: TransactionView) {
            binding.categoryName.text = transactionView.categoryName
            binding.cardName.text = transactionView.accountName
            binding.textAmount.text = transactionView.amount.toAmountFormat(withMinus = true)

            binding.icon.setImageResource(
                mapOfDrawables[transactionView.icon] ?: R.drawable.ic_bank
            )

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                ContextCompat.getColor(
                    context,
                    mapOfColors[transactionView.iconColor] ?: R.color.orange_red
                )
            )
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
    suspend fun updateData(transactionViews: List<TransactionView>) {
        transactionsWithInfoList = transactionUseCases.getTransactionListForRV(transactionViews)
        notifyDataSetChanged()
    }

    companion object {
        private const val TRANSACTION_VIEW_TYPE = 1
        private const val DAY_INFO_VIEW_TYPE = 0
    }
}