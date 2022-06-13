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
    fun provideTrainingDetailsRepository(db: WorkoutDatabase): TrainingDetailsRepository {
        return TrainingDetailsRepositoryImpl(db.trainingDetailsDao)
    }

    @Provides
    @Singleton
    fun provideGetTrainingsByMonthUseCase(repository: TrainingRepository): GetTrainingsByMonth {
        return GetTrainingsByMonth(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTrainingUseCase(repository: TrainingRepository): DeleteTraining {
        return DeleteTraining(repository)
    }

    @Provides
    @Singleton
    fun provideInsertTrainingUseCase(repository: TrainingRepository): InsertTraining {
        return InsertTraining(repository)
    }

    @Provides
    @Singleton
    fun provideGetTrainingDetailsByTrainingIdUseCase(repository: TrainingRepository): GetTrainingDetailsByTrainingID {
        return GetTrainingDetailsByTrainingID(repository)
    }

    @Provides
    @Singleton
    fun provideGetMusclesUseCase(repository: MuscleRepository): GetMuscles {
        return GetMuscles(repository)
    }

    @Provides
    @Singleton
    fun provideGetExerciseByIdUseCase(repository: ExerciseRepository): GetExercisesByMuscleId {
        return GetExercisesByMuscleId(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTrainingBlock(repository: TrainingDetailsRepository): DeleteTrainingBlock {
        return DeleteTrainingBlock(repository)
    }

    @Provides
    @Singleton
    fun provideInsertTrainingBlock(repository: TrainingDetailsRepository): InsertTrainingBlock {
        return InsertTrainingBlock(repository)
    }

    @Provides
    @Singleton
    fun provideGetTrainingBlockWithDetailsByTrainingBlockId(repository: TrainingDetailsRepository):
            GetTrainingBlockWithDetailsByTrainingBlockId {
        return GetTrainingBlockWithDetailsByTrainingBlockId(repository)
    }

}