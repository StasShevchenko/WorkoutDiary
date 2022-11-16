package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddExercisesAndMusclesScreenBinding
import com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen.ExercisesScreenFragment
import com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.muscles_screen.MusclesFragment
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
                tab.text = "Упражнения"
            } else {
                tab.text = "Мышцы"
            }
        }.attach()

    }


}

class ExerciseMuscleAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ExercisesScreenFragment()
        } else {
            MusclesFragment()
        }
    }

    override fun getItemCount(): Int = 2
}