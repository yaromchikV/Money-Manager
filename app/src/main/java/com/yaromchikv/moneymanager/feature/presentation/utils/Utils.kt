package com.yaromchikv.moneymanager.feature.presentation.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.yaromchikv.moneymanager.R

object Utils {
    const val MAIN_COLOR = "#ffee423e"
    const val MAIN_CURRENCY = "USD"

    const val THEME_PREFERENCE_KEY = "theme"
    const val THEME_DARK = "dark"
    const val THEME_LIGHT = "light"
    const val THEME_DEFAULT = "default"

    const val CURRENCY_PREFERENCE_KEY = "currency"
    const val RESET_PIN_PREFERENCE_KEY = "reset_pin"

    const val AUTH_CODE_PREFERENCE_KEY = "auth_code"
    const val FIRST_TIME_PREFERENCE_KEY = "first_time"

    private val mapOfDrawables = mapOf(
        0 to R.drawable.ic_family,
        1 to R.drawable.ic_finance,
        2 to R.drawable.ic_giftbox,
        3 to R.drawable.ic_grocery,
        4 to R.drawable.ic_health,
        5 to R.drawable.ic_house,
        6 to R.drawable.ic_leisure,
        7 to R.drawable.ic_shopping,
        8 to R.drawable.ic_transport,
        9 to R.drawable.ic_other,
        10 to R.drawable.ic_restaurant,
        11 to R.drawable.ic_services
    )

    fun ImageView.setIcon(id: Int) {
        this.setImageResource(mapOfDrawables[id] ?: R.drawable.ic_image_error)
    }

    fun ImageView.setTint(value: String?) {
        DrawableCompat.setTint(this.drawable, Color.parseColor(value ?: MAIN_COLOR))
    }

    fun ImageView.setTint(@ColorRes colorRes: Int) {
        ImageViewCompat.setImageTintList(
            this,
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
        )
    }

    fun getImageViewTint(imageView: ImageView): String {
        val colorInt = imageView.imageTintList?.defaultColor
        return if (colorInt != null) String.format("#%06X", 0xFFFFFF and colorInt) else MAIN_COLOR
    }

    fun getDivider(context: Context) = DividerItemDecoration(
        context,
        DividerItemDecoration.VERTICAL
    ).apply {
        setDrawable(
            requireNotNull(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.divider_layer
                )
            )
        )
    }

    fun showToast(context: Context?, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}
