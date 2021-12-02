package com.yaromchikv.moneymanager.feature.presentation.ui.accounts

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.toAmountFormat
import com.yaromchikv.moneymanager.databinding.ItemAccountBinding
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfColors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountsRVAdapter @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : ListAdapter<Account, AccountsRVAdapter.AccountViewHolder>(DIFF_CALLBACK) {

    private var onClickListener: OnClickListener? = null

    inner class AccountViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) {
            binding.name.text = account.name
            binding.amount.text = account.amount.toAmountFormat(withMinus = false)
            binding.currency.text = sharedPreferences.getString(
                "currency",
                context.resources.getStringArray(R.array.currency_values)[0]
            )

            DrawableCompat.setTint(
                binding.iconBackground.drawable,
                ContextCompat.getColor(
                    context,
                    mapOfColors[account.color] ?: R.color.orange_red
                )
            )

            itemView.setOnClickListener {
                onClickListener?.onClick(account)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAccountBinding.inflate(layoutInflater, parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener;
    }

    class OnClickListener(val clickListener: (account: Account) -> Unit) {
        fun onClick(account: Account) = clickListener(account)
    }
}