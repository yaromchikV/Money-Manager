package com.yaromchikv.moneymanager.feature.presentation.ui.transactions

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.toAmountFormat
import com.yaromchikv.moneymanager.databinding.ItemDayInfoBinding
import com.yaromchikv.moneymanager.databinding.ItemTransactionBinding
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.Category
import com.yaromchikv.moneymanager.feature.domain.model.DayInfo
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.domain.usecase.AccountUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.CategoryUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.TransactionUseCases
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfColors
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfDrawables
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionsRVAdapter @Inject constructor(
    private val context: Context,
    private val transactionUseCases: TransactionUseCases,
    private val accountUseCases: AccountUseCases,
    private val categoryUseCases: CategoryUseCases
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var transactionsWithInfoList = emptyList<Any>()
    private var accountsList = emptyList<Account>()
    private var categoriesList = emptyList<Category>()

    inner class TransactionViewHolder(
        private val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            val currentCategory = categoriesList.find { category ->
                category.id == transaction.categoryId
            }
            val currentAccount = accountsList.find { account ->
                account.id == transaction.accountId
            }

            if (currentCategory != null && currentAccount != null) {
                binding.categoryName.text = currentCategory.name
                binding.cardName.text = currentAccount.name
                binding.icon.setImageResource(
                    mapOfDrawables[currentCategory.icon] ?: R.drawable.ic_bank
                )

                DrawableCompat.setTint(
                    binding.iconBackground.drawable,
                    ContextCompat.getColor(
                        context,
                        mapOfColors[currentCategory.iconColor] ?: R.color.orange_red
                    )
                )
            }
        }
    }

    inner class DayInfoViewHolder(
        private val binding: ItemDayInfoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dayInfo: DayInfo) {
            val date = dayInfo.transactionDate
            val amount = dayInfo.amountPerDay
            val monthAndYear = "${date.month} ${date.year}"

            binding.amount.text = amount.toAmountFormat()
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
                val transaction = transactionsWithInfoList[position] as Transaction
                viewHolder.bind(transaction)
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
        return if (transactionsWithInfoList[position] is Transaction) TRANSACTION_VIEW_TYPE else DAY_INFO_VIEW_TYPE
    }

    suspend fun updateData() {
        transactionsWithInfoList = transactionUseCases.getTransactionListForRV()
        accountsList = accountUseCases.getAccounts().first()
        categoriesList = categoryUseCases.getCategories().first()
        notifyDataSetChanged()
    }

    companion object {
        private const val TRANSACTION_VIEW_TYPE = 1
        private const val DAY_INFO_VIEW_TYPE = 0
    }
}