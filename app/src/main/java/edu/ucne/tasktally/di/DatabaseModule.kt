package edu.ucne.tasktally.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.tasktally.data.local.DAOs.RecompensaGemaDao
import edu.ucne.tasktally.data.local.DAOs.TransaccionDao
import edu.ucne.tasktally.data.local.TaskTallyDatabase
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
import edu.ucne.tasktally.data.local.DAOs.gema.GemaDao
import edu.ucne.tasktally.data.local.DAOs.mentor.MentorDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTaskTallyDatabase(@ApplicationContext context: Context): TaskTallyDatabase {
        return Room.databaseBuilder(
            context,
            TaskTallyDatabase::class.java,
            "TaskTally.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGemaDao(db: TaskTallyDatabase): GemaDao =
        db.gemaDao()

    @Provides
    fun provideMentorDao(db: TaskTallyDatabase): MentorDao =
        db.mentorDao()
    @Provides
    fun provideRecompensaGemaDao(db: TaskTallyDatabase): RecompensaGemaDao =
        db.recompensaGemaDao()
    @Provides
    fun provideTransaccionDao(db: TaskTallyDatabase): TransaccionDao =
        db.transaccionDao()
    @Provides
    fun provideZonaDao(db: TaskTallyDatabase): ZonaDao =
        db.zonaDao()
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
