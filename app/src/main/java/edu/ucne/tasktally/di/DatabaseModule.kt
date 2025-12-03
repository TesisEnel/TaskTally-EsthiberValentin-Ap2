package edu.ucne.tasktally.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.tasktally.data.local.DAOs.GemaDao
import edu.ucne.tasktally.data.local.DAOs.MentorDao
import edu.ucne.tasktally.data.local.DAOs.RecompensaDao
import edu.ucne.tasktally.data.local.DAOs.TareaDao
import edu.ucne.tasktally.data.local.TaskTallyDatabase
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
    fun provideEstadoDao(db: TaskTallyDatabase): EstadoDao =
        db.estadoDao()

    @Provides
    fun provideGemaDao(db: TaskTallyDatabase): GemaDao =
        db.gemaDao()

    @Provides
    fun provideGemaZonaDao(db: TaskTallyDatabase): GemaZonaDao =
        db.gemaZonaDao()

    @Provides
    fun provideMentorDao(db: TaskTallyDatabase): MentorDao =
        db.mentorDao()

    @Provides
    fun provideProgresoDao(db: TaskTallyDatabase): ProgresoDao =
        db.progresoDao()

    @Provides
    fun provideRachaDao(db: TaskTallyDatabase): RachaDao =
        db.rachaDao()

    @Provides
    fun provideRecompensaDao(db: TaskTallyDatabase): RecompensaDao =
        db.recompensaDao()

    @Provides
    fun provideTareaDao(db: TaskTallyDatabase): TareaDao =
        db.tareaDao()

    @Provides
    fun provideTransaccionRecompensaDao(db: TaskTallyDatabase): TransaccionRecompensaDao =
        db.transaccionRecompensaDao()

    @Provides
    fun provideUserInfoDao(db: TaskTallyDatabase): UserInfoDao =
        db.userInfoDao()

    @Provides
    fun provideZonaDao(db: TaskTallyDatabase): ZonaDao =
        db.zonaDao()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }
}
