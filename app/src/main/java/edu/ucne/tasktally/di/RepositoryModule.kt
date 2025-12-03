package edu.ucne.tasktally.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.tasktally.data.repositories.RecompensaRepositoryImpl
import edu.ucne.tasktally.data.repositories.TareaRepositoryImpl
import edu.ucne.tasktally.domain.repository.RecompensaRepository
import edu.ucne.tasktally.domain.repository.TareaRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTareaRepository(
        impl: TareaRepositoryImpl
    ): TareaRepository

    @Binds
    @Singleton
    abstract fun bindRecompensaRepository(
        impl: RecompensaRepositoryImpl
    ): RecompensaRepository
}