package io.github.astrit_veliu.landlauncher.ui.launcher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.astrit_veliu.landlauncher.domain.model.Application
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory
import io.github.astrit_veliu.landlauncher.domain.model.Application.ApplicationCategory.GAME
import io.github.astrit_veliu.landlauncher.domain.repository.ApplicationRepository
import io.github.astrit_veliu.landlauncher.ui.mvvm.LauncherIntent
import io.github.astrit_veliu.landlauncher.ui.mvvm.LauncherLoadApplicationIntent
import io.github.astrit_veliu.landlauncher.ui.mvvm.MVVMException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val applicationRepository: ApplicationRepository
) : ViewModel() {

    var error = MutableLiveData<MVVMException?>()
    var isLoading = MutableLiveData<Boolean>()

    private val _allAppsState = MutableStateFlow<List<Application>?>(null)
    val allAppsState: StateFlow<List<Application>?> = _allAppsState

    private val _homeAppsState = MutableStateFlow<List<Application>?>(null)
    val homeAppsState: StateFlow<List<Application>?> = _homeAppsState

    var handler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        error.postValue(MVVMException(LauncherIntent, exception))
    }

    init {
        getApplicationList()
    }

    fun getApplicationList(category: ApplicationCategory? = null) {
        viewModelScope.launch(handler) {
            try {
                isLoading.postValue(true)
                error.postValue(null)
                val apps = if (category == null) applicationRepository.getAllPackages()
                    .flatMap { it.value } else applicationRepository.getPackagesByCategory(category)
                _allAppsState.emit(apps)
                _homeAppsState.emit(applicationRepository.getPackagesByCategory(GAME))
                isLoading.postValue(false)
            } catch (e: Exception) {
                isLoading.postValue(false)
                error.postValue(MVVMException(LauncherLoadApplicationIntent(category), e))
            }
        }
    }

    companion object {
        const val LOAD_DATA_REQUEST = "LauncherViewModel::loadData"
    }
}