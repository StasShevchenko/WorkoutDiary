package com.example.workoutdiary.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.data.data_source.WorkoutDatabase
import com.example.workoutdiary.presentation.utils.FabButtonClick
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var fabButtonClick: FabButtonClick? = null
    @Inject
    lateinit var db: WorkoutDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //TODO: resolve this hack with empty trainings deleting in the future
        runBlocking {
            db.trainingDao.deleteEmptyTrainings()
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        val bottomBar: BottomAppBar = findViewById(R.id.bottomAppBar)
        navView.background = null
        val fab = findViewById<FloatingActionButton>(R.id.training_fab)
        fab.setOnClickListener {
            fabButtonClick?.onFabClicked()
        }
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeScreenFragment -> {
                    fab.show()
                    bottomBar.visibility = View.VISIBLE
                    navView.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    bottomBar.fabCradleMargin = 24f
                    fab.isEnabled = true
                }
                R.id.calendarScreenFragment ->{
                    fab.show()
                    bottomBar.visibility = View.VISIBLE
                    navView.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    bottomBar.fabCradleMargin = 24f
                    fab.isEnabled = true
                }
                R.id.exercisesAndMusclesScreenFragment -> {
                    bottomBar.visibility = View.VISIBLE
                    navView.visibility = View.VISIBLE
                    fab.hide()
                    bottomBar.fabCradleMargin = 24f
                    fab.isEnabled = false
                }
                else -> {
                    fab.visibility = View.GONE
                    fab.isEnabled = false
                    navView.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val ret = super.dispatchTouchEvent(ev)
        ev?.let { event ->
            if (event.action == MotionEvent.ACTION_UP) {
                currentFocus?.let { view ->
                    if (view is EditText || view is AutoCompleteTextView) {
                        val touchCoordinates = IntArray(2)
                        view.getLocationOnScreen(touchCoordinates)
                        val x: Float = event.rawX + view.getLeft() - touchCoordinates[0]
                        val y: Float = event.rawY + view.getTop() - touchCoordinates[1]
                        if (x < view.getLeft() || x >= view.getRight() || y < view.getTop() || y > view.getBottom()) {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                            view.clearFocus()
                        }
                    }
                }
            }
        }
        return ret
    }
    fun setFabListener(listener: FabButtonClick) {
        fabButtonClick = listener
    }
}