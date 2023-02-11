package com.example.workoutdiary.presentation.favourites_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.databinding.FavouritesScreenBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class FavouriteTrainingsFragment : Fragment(R.layout.favourites_screen),
    FavouriteTrainingsAdapter.OnFavouriteTrainingClickListener {

    private val args: FavouriteTrainingsFragmentArgs by navArgs()
     val viewModel: FavouriteTrainingsViewModel by viewModels()
    private lateinit var date: LocalDate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FavouritesScreenBinding.bind(view)
        val adapter = FavouriteTrainingsAdapter(this)
        date = args.trainingDate
        with(binding) {
            favouriteTrainingsRecyclerView.adapter = adapter
            favouriteTrainingsRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedFavouriteTrainingName = viewModel.favouriteTrainings.value?.get(viewHolder.absoluteAdapterPosition)?.trainingName
                deletedFavouriteTrainingName?.let {
                    viewModel.deleteFavouriteTrainings(it)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.favouriteTrainingsRecyclerView)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is FavouriteTrainingsViewModel.UiEvent.TrainingsDeleted -> {
                        val snackbar = Snackbar.make(requireView(), "Тренировка была удалена из избранных!", Snackbar.LENGTH_SHORT)
                        snackbar.setAction("Отменить") {
                            viewModel.restoreFavouriteTrainings(event.trainingsName)
                        }
                        snackbar.show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.favouriteTrainings.collectLatest {trainings ->
                    if (trainings != null) {
                        if (trainings.isEmpty()) {
                            binding.emptyFavouritesMessageTextView.visibility = View.VISIBLE
                        }
                        else{
                            binding.emptyFavouritesMessageTextView.visibility = View.GONE
                            binding.favouriteTrainingsRecyclerView.visibility = View.VISIBLE
                            binding.favouritesDeleteMessageTextView.visibility = View.VISIBLE
                            binding.titleTextView.visibility = View.VISIBLE
                            adapter.submitList(trainings)
                        }
                    }
                }
            }
        }
    }



    override fun onFavouriteTrainingClick(training: Training) {
        val action = FavouriteTrainingsFragmentDirections.actionFavouriteTrainingsFragmentToAddEditTrainingScreenFragment(
            trainingDate = date,
            trainingName = training.trainingName,
            trainingId = training.trainingId,
            isFromFavourites = true
        )
        findNavController().navigate(action)
    }
}