package io.github.astrit_veliu.landlauncher.common.snackbar

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SnackbarManager {
  private val messages: MutableStateFlow<SnackBarMessage?> = MutableStateFlow(null)
  val snackbarMessages: StateFlow<SnackBarMessage?>
    get() = messages.asStateFlow()

  fun showMessage(@StringRes message: Int) {
    messages.value = SnackBarMessage.ResourceSnackBar(message)
  }

  fun showMessage(message: SnackBarMessage) {
    messages.value = message
  }
}
