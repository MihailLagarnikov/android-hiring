package com.example.myapplication.presentation

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.FragmentStartDialogBinding
import com.example.myapplication.domain.model.Age
import com.example.myapplication.domain.model.Gender
import com.example.myapplication.domain.model.StartDialogScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        val array = resources.getStringArray(R.array.age)
        binding.apply {
            viewModel.viewModelScope.launch {
                viewModel.state
                    .flowWithLifecycle(this@MainActivity.lifecycle)
                    .collect { state ->
                        loader.isVisible = state.isLoading
                        loaderFrame.isVisible = state.isLoading
                        when (state) {
                            is StartState.NewScreen -> {
                                dialog.updateUi(
                                    screen = state.screen,
                                    array = array
                                )
                            }

                            is StartState.SendText -> {
                                Toast.makeText(this@MainActivity, state.text, Toast.LENGTH_LONG)
                                    .show()
                            }

                            is StartState.Loading -> {
                                viewModel.loadStartScreen()
                            }
                        }

                    }
            }
            dialog.setupUi(array)
        }
    }

    private fun FragmentStartDialogBinding.setupUi(array: Array<String>) {
        val adapter: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(
                this@MainActivity,
                R.array.age,
                android.R.layout.simple_spinner_item
            )
        spinnerAge.setAdapter(adapter)
        spinnerAge.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.userClickAge(array[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        maleImage.setOnClickListener {
            viewModel.userClickGender(Gender.Male)
        }
        femaleImage.setOnClickListener {
            viewModel.userClickGender(Gender.Female)
        }
        buttonStart.setOnClickListener {
            viewModel.userClickButton()
        }
        buttonStart.isEnabled = false
    }

    private fun FragmentStartDialogBinding.updateUi(
        screen: StartDialogScreen,
        array: Array<String>
    ) {
        maleImage.setSelected(
            isMale = true,
            selectedGender = screen.selectedGender
        )
        femaleImage.setSelected(
            isMale = false,
            selectedGender = screen.selectedGender
        )
        spinnerAge.setSelection(array.indexOf(screen.selectedAge.toString()))
        buttonStart.isEnabled = screen.selectedGender != Gender.Unknown
                && screen.selectedAge != Age.Unknown
    }

    private fun ImageView.setSelected(isMale: Boolean, selectedGender: Gender) {
        val drawable = when {
            isMale && selectedGender == Gender.Male -> {
                R.drawable.card_male_selected
            }

            !isMale && selectedGender == Gender.Female -> {
                R.drawable.card_female_selected
            }

            else -> {
                R.drawable.card_corners
            }
        }
        background = ContextCompat.getDrawable(this@MainActivity, drawable)
    }
}