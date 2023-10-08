package io.github.astrit_veliu.landlauncher.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.astrit_veliu.landlauncher.domain.repository.ApplicationRepository
import io.github.astrit_veliu.landlauncher.domain.repository.ApplicationRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideApplicationRepository(appRepositoryImpl: ApplicationRepositoryImpl): ApplicationRepository = appRepositoryImpl
}