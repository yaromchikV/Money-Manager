package com.yaromchikv.moneymanager.feature.presentation.ui.auth

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.ActivityAuthBinding
import com.yaromchikv.moneymanager.feature.presentation.ui.MainActivity
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setTint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    private val viewModel: AuthViewModel by viewModels()

    private val vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        setupCollectors()
        setupEventCollectors()

        if (viewModel.isAppLaunchedFirstTime()) {
            viewModel.startAuthSetup()
        }
    }

    private fun setupClickListeners() {
        with(binding) {
            number0.setOnClickListener { viewModel.numberButtonClick("0") }
            number1.setOnClickListener { viewModel.numberButtonClick("1") }
            number2.setOnClickListener { viewModel.numberButtonClick("2") }
            number3.setOnClickListener { viewModel.numberButtonClick("3") }
            number4.setOnClickListener { viewModel.numberButtonClick("4") }
            number5.setOnClickListener { viewModel.numberButtonClick("5") }
            number6.setOnClickListener { viewModel.numberButtonClick("6") }
            number7.setOnClickListener { viewModel.numberButtonClick("7") }
            number8.setOnClickListener { viewModel.numberButtonClick("8") }
            number9.setOnClickListener { viewModel.numberButtonClick("9") }
            forgotCode.setOnClickListener { viewModel.forgotButtonClick() }
            backspace.setOnClickListener { viewModel.backspaceButtonClick() }
        }
    }

    private fun setupCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.code.collectLatest { newCode ->
                with(binding) {
                    var count = newCode.length
                    for (item in listOf(circle1, circle2, circle3, circle4)) {
                        item.setTint(if (count > 0) R.color.yellow_orange else R.color.light_gray)
                        count--
                    }
                }
            }
        }
    }

    private fun startVibrating() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    ERROR_TIME,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(ERROR_TIME)
        }
    }

    private fun setupEventCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                Log.d("!!!", it.javaClass.simpleName)
                when (it) {
                    is AuthViewModel.Event.ForgotClick -> {
                        showAlert(
                            getString(R.string.forgot_alert_title),
                            getString(R.string.forgot_alert_message)
                        ) {
                            showAlert(
                                getString(R.string.erase_data_alert_title),
                                getString(R.string.erase_data_alert_message)
                            ) {
                                viewModel.eraseDataClick()
                            }
                        }
                    }
                    is AuthViewModel.Event.OpenMainActivity -> {
                        with(binding) {
                            for (item in listOf(circle1, circle2, circle3, circle4)) {
                                item.setTint(R.color.blue_green)
                            }
                        }
                        val intent = Intent(this@AuthActivity, MainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        finish()
                    }
                    is AuthViewModel.Event.DeleteCode -> {
                        lifecycleScope.launch {
                            with(binding) {
                                backspace.isClickable = false
                                forgotCode.isClickable = false

                                for (item in listOf(circle1, circle2, circle3, circle4)) {
                                    item.setTint(R.color.orange_red)
                                }
                                startVibrating()
                                delay(ERROR_TIME)
                                viewModel.clearCode()

                                backspace.isClickable = true
                                forgotCode.isClickable = true
                            }
                        }
                    }
                    is AuthViewModel.Event.EraseData -> {
                        (getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
                    }
                    is AuthViewModel.Event.SetThePin -> {
                        binding.enterPinText.text = getString(R.string.set_the_pin)
                        binding.forgotCode.isVisible = false
                    }
                    is AuthViewModel.Event.RepeatThePin -> {
                        binding.enterPinText.text = getString(R.string.repeat_the_pin)
                    }
                }
            }
        }
    }

    private fun showAlert(title: String, message: String, func: () -> Unit) {
        val alert = AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_warning)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> func() }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
        alert.show()
    }

    companion object {
        private const val ERROR_TIME: Long = 250
    }
}
