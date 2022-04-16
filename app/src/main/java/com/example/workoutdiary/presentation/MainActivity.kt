package com.example.workoutdiary.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.workoutdiary.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "Date time"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeScreenFragment,
            R.id.calendarScreenFragment
        ))
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        navView.background = null
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        val date: LocalDateTime = LocalDateTime.now()
        val data: LocalDateTime = LocalDateTime.parse(date.toString())
        val dateTime: ZonedDateTime = ZonedDateTime.now()
        Log.d(TAG, data.month.getDisplayName(TextStyle.FULL_STANDALONE, ConfigurationCompat.getLocales(resources.configuration)[0]))

    }
}