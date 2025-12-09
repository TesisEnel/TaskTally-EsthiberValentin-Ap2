package edu.ucne.tasktally.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.tasktally.data.local.DAOs.RecompensaMentorDao
import edu.ucne.tasktally.data.local.DAOs.RecompensaGemaDao
import edu.ucne.tasktally.data.local.DAOs.TareaGemaDao
import edu.ucne.tasktally.data.local.DAOs.TareaMentorDao
import edu.ucne.tasktally.data.local.DAOs.TransaccionDao
import edu.ucne.tasktally.data.local.TaskTallyDatabase
import edu.ucne.tasktally.data.local.DAOs.UserInfoDao
import edu.ucne.tasktally.data.local.DAOs.ZonaDao
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
    fun provideRecompensaDao(db: TaskTallyDatabase): RecompensaMentorDao =
        db.recompensaDao()
    @Provides
    fun provideRecompensaGemaDao(db: TaskTallyDatabase): RecompensaGemaDao =
        db.recompensaGemaDao()
    @Provides
    fun provideTareaGemaDao(db: TaskTallyDatabase): TareaGemaDao =
        db.tareaGemaDao()
    @Provides
    fun provideTransaccionDao(db: TaskTallyDatabase): TransaccionDao =
        db.transaccionDao()
    @Provides
    fun provideUserInfoDao(db: TaskTallyDatabase): UserInfoDao =
        db.userInfoDao()
    @Provides
    fun provideZonaDao(db: TaskTallyDatabase): ZonaDao =
        db.zonaDao()
    @Provides
    fun provideTareaMentorDao(db: TaskTallyDatabase): TareaMentorDao =
        db.tareaDao()
    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
