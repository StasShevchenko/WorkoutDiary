package com.example.workoutdiary.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.workoutdiary.data.model.entities.*
import com.example.workoutdiary.utils.ExerciseTypeConverter
import com.example.workoutdiary.utils.LocalDateConverter


@Database(
    entities =
    [Exercise::class, Muscle::class, OrderedSet::class,
        Training::class, TrainingBlock::class, TrainingParameters::class,
        ExerciseStatisticsParameters::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class, ExerciseTypeConverter::class)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract val trainingDao: TrainingDao
    abstract val trainingDetailsDao: TrainingDetailsDao
    abstract val exerciseDao: ExerciseDao
    abstract val muscleDao: MuscleDao
    abstract val statisticsDao: StatisticsDao

    companion object{
        const val DATABASE_NAME = "workout_db"
    }
}