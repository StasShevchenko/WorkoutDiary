package com.example.workoutdiary.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.presentation.utils.FabButtonClick
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var fabButtonClick: FabButtonClick? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val bottomBar: BottomAppBar = findViewById(R.id.bottomAppBar)
        navView.background = null
        val fab = findViewById<FloatingActionButton>(R.id.training_fab)
        fab.setOnClickListener {
           fabButtonClick?.onFabClicked()
        }
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener{_, destination, _ ->
            when (destination.id) {
                R.id.addEditTrainingScreenFragment -> {
                   fab.hide()
                   bottomBar.performHide()
                }
                else -> {
                   fab.show()
                   bottomBar.performShow()

                }
            }
        }
    }

     fun setFabListener(listener: FabButtonClick) {
        fabButtonClick = listener
    }
}