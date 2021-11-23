package com.yaromchikv.moneymanager.feature.presentation.ui.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.common.toAmountFormat
import com.yaromchikv.moneymanager.databinding.ItemAccountBinding
import com.yaromchikv.moneymanager.feature.domain.model.Account

class AccountsRVAdapter : RecyclerView.Adapter<AccountsRVAdapter.AccountViewHolder>() {

    private var listOfAccounts = emptyList<Account>()

    class AccountViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) {
            binding.name.text = account.name
            binding.amount.text = account.amount.toAmountFormat()
            binding.currency.text = account.currency

            DrawableCompat.setTint(
                DrawableCompat.wrap(binding.iconBackground.drawable),
                account.color
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(layoutInflater, parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(listOfAccounts[position])
    }

    override fun getItemCount(): Int {
        return listOfAccounts.size
    }

    fun setData(newList: List<Account>) {
        this.listOfAccounts = newList
        notifyDataSetChanged()
    }
}