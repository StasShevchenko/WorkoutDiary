package com.example.workoutdiary.di

import android.app.Application
import androidx.room.Room
import com.example.workoutdiary.data.data_source.WorkoutDatabase
import com.example.workoutdiary.data.repository.*
import com.example.workoutdiary.domain.repository.*
import com.example.workoutdiary.domain.use_case.*
import com.example.workoutdiary.domain.use_case.exercise_use_cases.*
import com.example.workoutdiary.domain.use_case.muscles_use_cases.*
import com.example.workoutdiary.domain.use_case.statistic_use_cases.*
import com.example.workoutdiary.domain.use_case.training_detailse_use_cases.*
import com.example.workoutdiary.domain.use_case.trainings_use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWorkoutDatabase(app: Application): WorkoutDatabase {
        return Room.databaseBuilder(
            app,
            WorkoutDatabase::class.java,
            WorkoutDatabase.DATABASE_NAME
        )
            .createFromAsset("database/workoutDb.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideTrainingRepository(db: WorkoutDatabase): TrainingRepository {
        return TrainingRepositoryImpl(db.trainingDao)
    }

    @Provides
    @Singleton
    fun provideMuscleRepository(db: WorkoutDatabase): MuscleRepository {
        return MuscleRepositoryImpl(db.muscleDao)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(db: WorkoutDatabase): ExerciseRepository {
        return ExerciseRepositoryImpl(db.exerciseDao)
    }

    @Provides
    @Singleton
    fun provideStatisticsRepository(db: WorkoutDatabase): StatisticsRepository {
        return StatisticsRepositoryImpl(db.statisticsDao)
    }

    @Provides
    fun provideTrainingDetailsRepository(db: WorkoutDatabase): TrainingDetailsRepository {
        return TrainingDetailsRepositoryImpl(db.trainingDetailsDao)
    }

    @Provides
    fun provideGetTrainingsByMonthUseCase(repository: TrainingRepository): GetTrainingsByMonth {
        return GetTrainingsByMonth(repository)
    }

    @Provides
    fun provideDeleteTrainingUseCase(
        trainingRepository: TrainingRepository,
        trainingDetailsRepository: TrainingDetailsRepository
    ): DeleteTraining {
        return DeleteTraining(trainingRepository, trainingDetailsRepository)
    }

    @Provides
    fun provideInsertTrainingUseCase(repository: TrainingRepository): InsertTraining {
        return InsertTraining(repository)
    }

    @Provides
    fun provideGetTrainingDetailsByTrainingIdUseCase(repository: TrainingRepository): GetTrainingDetailsByTrainingID {
        return GetTrainingDetailsByTrainingID(repository)
    }

    @Provides
    fun provideGetMusclesUseCase(repository: MuscleRepository): GetMuscles {
        return GetMuscles(repository)
    }

    @Provides
    fun provideGetExerciseByIdUseCase(repository: ExerciseRepository): GetExercisesByMuscleId {
        return GetExercisesByMuscleId(repository)
    }

    @Provides
    fun provideGetExercise(repository: ExerciseRepository): GetExercise {
        return GetExercise(repository)
    }

    @Provides
    fun provideGetMuscle(repository: MuscleRepository): GetMuscle {
        return GetMuscle(repository)
    }

    @Provides
    fun provideDeleteTrainingBlock(repository: TrainingDetailsRepository): DeleteTrainingBlock {
        return DeleteTrainingBlock(repository)
    }

    @Provides
    fun provideInsertTrainingBlock(repository: TrainingDetailsRepository): InsertTrainingBlock {
        return InsertTrainingBlock(repository)
    }

    @Provides
    fun provideGetTrainingBlockWithDetailsByTrainingBlockId(repository: TrainingDetailsRepository):
            GetTrainingBlockWithDetailsByTrainingBlockId {
        return GetTrainingBlockWithDetailsByTrainingBlockId(repository)
    }

    @Provides
    fun provideUpdateTrainingBlocks(repository: TrainingRepository): UpdateTrainingBlocks {
        return UpdateTrainingBlocks(repository)
    }
    @Provides
    fun provideGetExercisesByName(repository: ExerciseRepository): GetExercisesByName {
        return GetExercisesByName(repository)
    }

    @Provides
    fun provideAddExercise(repository: ExerciseRepository): AddExercise {
        return AddExercise(repository)
    }

    @Provides
    fun provideGetExerciseByName(repository: ExerciseRepository): GetExerciseByName {
        return GetExerciseByName(repository)
    }

    @Provides
    fun provideDeleteExercise(
        exerciseRepository: ExerciseRepository,
        trainingDetailsRepository: TrainingDetailsRepository
    ): DeleteExercise {
        return DeleteExercise(exerciseRepository, trainingDetailsRepository)
    }

    @Provides
    fun provideDeleteMuscle(
        muscleRepository: MuscleRepository,
        exerciseRepository: ExerciseRepository,
        trainingDetailsRepository: TrainingDetailsRepository
    ): DeleteMuscle {
        return DeleteMuscle(
            muscleRepository,
            exerciseRepository,
            trainingDetailsRepository
        )
    }

    @Provides
    fun provideAddMuscle(
        repository: MuscleRepository
    ): AddMuscle {
        return AddMuscle(repository)
    }


    @Provides
    fun provideGetMuscleByName(
        repository: MuscleRepository
    ): GetMuscleByName {
        return GetMuscleByName(repository)
    }

    @Provides
    fun provideGetMusclesByName(
        repository: MuscleRepository
    ): GetMusclesByName {
        return GetMusclesByName(repository)
    }

    @Provides
    fun provideGetTotalTrainingsCount(
        repository: StatisticsRepository
    ): GetTotalTrainingsCount {
        return GetTotalTrainingsCount(repository)
    }

    @Provides
    fun provideGetTotalSetsCount(
        repository: StatisticsRepository
    ): GetTotalSetsCount {
        return GetTotalSetsCount(repository)
    }

    @Provides
    fun provideGetTotalWeightCount(
        repository: StatisticsRepository
    ): GetTotalWeightCount{
        return GetTotalWeightCount(repository)
    }

    @Provides
    fun provideGetTotalRepsCount(
        repository: StatisticsRepository
    ): GetTotalRepsCount {
        return GetTotalRepsCount(repository)
    }

    @Provides
    fun provideGetExerciseStatisticsInfo(
        repository: StatisticsRepository
    ): GetExerciseStatisticsInfo {
        return GetExerciseStatisticsInfo(repository)
    }

    @Provides
    fun provideUpdateExerciseStatisticsInfo(
        repository: StatisticsRepository
    ): UpdateExerciseStatisticsParameters {
        return UpdateExerciseStatisticsParameters(repository)
    }

    @Provides
    fun provideGetExerciseParametersInfo(
        repository: StatisticsRepository
    ): GetExerciseStatisticsParameters{
        return GetExerciseStatisticsParameters(repository)
    }

    @Provides
    fun provideDeleteExerciseParametersInfo(
        repository: StatisticsRepository
    ): DeleteExerciseStatisticsParameters {
        return DeleteExerciseStatisticsParameters(repository)
    }

    @Provides
    fun provideGetAllFavouriteTrainings(
        repository: TrainingRepository
    ): GetAllFavouriteTrainings {
        return GetAllFavouriteTrainings(repository)
    }

    @Provides
    fun provideClearFavouriteTrainingsByName(
        repository: TrainingRepository
    ): ClearFavouriteTrainingsByName {
        return ClearFavouriteTrainingsByName(repository)
    }

    @Provides
    fun provideGetTrainingById(
        repository: TrainingRepository
    ): GetTrainingById {
        return GetTrainingById(repository)
    }

    @Provides
    fun provideRestoreFavouriteTrainingsByName(
        repository: TrainingRepository
    ): RestoreFavouriteTrainingsByName {
        return RestoreFavouriteTrainingsByName(repository)
    }
}