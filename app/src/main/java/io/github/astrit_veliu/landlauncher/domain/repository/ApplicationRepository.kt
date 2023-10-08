package io.github.astrit_veliu.landlauncher.domain.repository

import io.github.astrit_veliu.landlauncher.domain.model.Application
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory

interface ApplicationRepository {

    suspend fun getAllPackages(): Map<ApplicationCategory, List<Application>>

    suspend fun getPackagesByCategory(category: ApplicationCategory): List<Application>
}