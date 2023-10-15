@file:Suppress("DEPRECATION")

package com.example.workoutdiary.presentation.settings_screen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.workoutdiary.R
import com.example.workoutdiary.data.preferences.AppTheme
import com.example.workoutdiary.data.preferences.PreferencesManager
import com.example.workoutdiary.databinding.SettingsScreenFragmentBinding
import com.example.workoutdiary.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsScreenFragment : Fragment(R.layout.settings_screen_fragment) {

    @Inject
    lateinit var dataStore: PreferencesManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = SettingsScreenFragmentBinding.bind(view)

        binding.apply {
            if (resources.configuration.locales.get(0).language == "en") {
                englishLanguageRadioButton.isChecked = true
            } else{
                russianLanguageRadioButton.isChecked = true
            }
            englishLanguageRadioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags("en")
                    )
                }
            }
            russianLanguageRadioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags("ru")
                    )
                }
            }
            violetThemeRadioButton.setOnClickListener {
                binding.themeRadioGroup.clearCheck()
                binding.violetThemeRadioButton.isChecked = true
                viewLifecycleOwner.lifecycleScope.launch {
                    dataStore.updateAppTheme(AppTheme.VIOLET)
                    (requireActivity() as MainActivity).recreate()
                }
            }
            greenThemeRadioButton.setOnClickListener {
                binding.themeRadioGroup.clearCheck()
                binding.greenThemeRadioButton.isChecked = true
                viewLifecycleOwner.lifecycleScope.launch {
                    dataStore.updateAppTheme(AppTheme.GREEN)
                    (requireActivity() as MainActivity).recreate()
                }
            }
            redThemeRadioButton.setOnClickListener {
                binding.themeRadioGroup.clearCheck()
                binding.redThemeRadioButton.isChecked = true
                viewLifecycleOwner.lifecycleScope.launch{
                    dataStore.updateAppTheme(AppTheme.RED)
                    (requireActivity() as MainActivity).recreate()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            dataStore.preferencesFlow.collectLatest {preferences ->
                when (preferences.theme) {
                    AppTheme.VIOLET -> {
                        binding.violetThemeRadioButton.isChecked = true
                    }
                    AppTheme.GREEN -> {
                        binding.greenThemeRadioButton.isChecked = true
                    }
                    AppTheme.RED -> {
                        binding.redThemeRadioButton.isChecked = true
                    }
                }
            }
        }


    }
}