package acidicoala.koalageddon.core.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class OpenState(initial: Boolean = false) {
    private var _open = mutableStateOf(initial)
    val open: State<Boolean> = _open

    fun open() {
        _open.value = true
    }

    fun close() {
        _open.value = false
    }
}
