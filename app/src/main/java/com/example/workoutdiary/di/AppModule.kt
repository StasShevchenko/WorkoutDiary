package com.example.workoutdiary.di

import android.app.Application
import androidx.room.Room
import com.example.workoutdiary.data.data_source.WorkoutDatabase
import com.example.workoutdiary.data.repository.ExerciseRepositoryImpl
import com.example.workoutdiary.data.repository.MuscleRepositoryImpl
import com.example.workoutdiary.data.repository.TrainingDetailsRepositoryImpl
import com.example.workoutdiary.data.repository.TrainingRepositoryImpl
import com.example.workoutdiary.domain.repository.ExerciseRepository
import com.example.workoutdiary.domain.repository.MuscleRepository
import com.example.workoutdiary.domain.repository.TrainingDetailsRepository
import com.example.workoutdiary.domain.repository.TrainingRepository
import com.example.workoutdiary.domain.use_case.*
import com.example.workoutdiary.domain.use_case.exercise_use_cases.*
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
    fun provideGetMuscle(repository: MuscleRepository): GetMuscle{
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
    fun provideGetExerciseByName(repository: ExerciseRepository): GetExerciseByName{
        return GetExerciseByName(repository)
    }

    @Provides
    fun provideDeleteExercise(
        exerciseRepository: ExerciseRepository,
        trainingDetailsRepository: TrainingDetailsRepository
    ) : DeleteExercise {
        return DeleteExercise(exerciseRepository, trainingDetailsRepository)
    }
}