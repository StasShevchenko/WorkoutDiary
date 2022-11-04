package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddExercisesAndMusclesScreenBinding
import com.google.android.material.tabs.TabLayoutMediator

class ExercisesAndMusclesScreenFragment : Fragment(R.layout.add_exercises_and_muscles_screen) {
    private lateinit var tabsAdapter: ExerciseMuscleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabsAdapter = ExerciseMuscleAdapter(this)
        val binding = AddExercisesAndMusclesScreenBinding.bind(requireView())
        binding.viewPager.adapter = tabsAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            if (position == 0) {
                tab.text = "Exercises"
            } else {
                tab.text = "Muscles"
            }
        }.attach()

    }


}

class ExerciseMuscleAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return ExercisesFragment()
        } else {
            return MusclesFragment()
        }
    }

    override fun getItemCount(): Int = 2
}