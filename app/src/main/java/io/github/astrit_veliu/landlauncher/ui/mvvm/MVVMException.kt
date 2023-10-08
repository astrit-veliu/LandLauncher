package io.github.astrit_veliu.landlauncher.ui.mvvm

import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory
import java.io.Serializable

data class MVVMException(val intent: MVVMIntent, val throwable: Throwable) : Exception(), Serializable

interface MVVMIntent

data class LauncherLoadApplicationIntent(val category: ApplicationCategory?) : MVVMIntent
object LauncherIntent : MVVMIntent