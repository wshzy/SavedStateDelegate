package ziyao.huang.savedstate

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SavedStateHandleDelegate<T>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String,
    private val delegate: SaveDelegate<T>,
) : ReadWriteProperty<Any, T> {
    private val state: MutableState<T>

    init {
        val savedValue = delegate.loader(savedStateHandle, key)
        state = mutableStateOf(savedValue)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return state.value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        state.value = value
        delegate.saver(savedStateHandle, key, value)
    }
}

private class MutableStateFlowWrapper<T>(
    private val originFlow: MutableStateFlow<T>,
    private val onSet: (T) -> Unit
): MutableStateFlow<T> by originFlow {
    override var value: T
        get() = originFlow.value
        set(value) {
            onSet(value)
            originFlow.value = value
        }

    override fun compareAndSet(expect: T, update: T): Boolean {
        onSet(update)
        return originFlow.compareAndSet(expect, update)
    }
}

class SavedStateFlowHandleDelegate<T>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String,
    private val delegate: SaveDelegate<T>,
) : ReadOnlyProperty<Any, MutableStateFlow<T>> {
    private val state: MutableStateFlow<T>

    init {
        val savedValue = delegate.loader(savedStateHandle, key)
        state = MutableStateFlowWrapper(MutableStateFlow(savedValue)) {
            delegate.saver(savedStateHandle, key, it)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): MutableStateFlow<T> = state
}
