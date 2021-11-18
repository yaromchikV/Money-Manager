package com.yaromchikv.moneymanager.ui.categories

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private val binding: FragmentCategoriesBinding by viewBinding()

    private val categoriesViewModel: CategoriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textCategories
        categoriesViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
    }
}
