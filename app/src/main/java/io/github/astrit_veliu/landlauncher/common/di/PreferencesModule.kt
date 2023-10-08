package io.github.astrit_veliu.landlauncher.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.astrit_veliu.landlauncher.common.preferences.UserPreferences
import io.github.astrit_veliu.landlauncher.common.preferences.UserPreferencesImpl

@InstallIn(SingletonComponent::class)
@Module
object PreferencesModule {

    @Provides
    fun bindUserPreference(userPreferences: UserPreferencesImpl): UserPreferences = userPreferences
}