package com.example.workoutdiary.di

import android.app.Application
import androidx.room.Room
import com.example.workoutdiary.data.data_source.WorkoutDatabase
import com.example.workoutdiary.data.repository.TrainingRepositoryImpl
import com.example.workoutdiary.domain.repository.TrainingRepository
import com.example.workoutdiary.domain.use_case.GetTrainingsByMonth
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
    fun provideWorkoutDatabase(app: Application): WorkoutDatabase{
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
    fun provideGetTrainingsByMonthUseCase(repository: TrainingRepository): GetTrainingsByMonth {
        return GetTrainingsByMonth(repository)
    }

}