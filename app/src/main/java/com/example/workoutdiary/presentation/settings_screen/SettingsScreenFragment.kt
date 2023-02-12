package com.example.workoutdiary.presentation.settings_screen

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.SettingsScreenFragmentBinding

class SettingsScreenFragment : Fragment(R.layout.settings_screen_fragment) {
    @RequiresApi(Build.VERSION_CODES.N)
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

        }
    }
}