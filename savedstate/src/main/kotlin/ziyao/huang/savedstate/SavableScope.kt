package ziyao.huang.savedstate

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.PropertyDelegateProvider

interface SavableScope {
    fun <T> SaveDelegate<T>.bind(key: String? = null): PropertyDelegateProvider<Any, SavedStateHandleDelegate<T>>

    fun <T> SaveDelegate<T>.bindFlow(key: String? = null): PropertyDelegateProvider<Any, SavedStateFlowHandleDelegate<T>>
}

fun SavableScope(savedStateHandle: SavedStateHandle): SavableScope =
    SavableScopeImpl(savedStateHandle)

private class SavableScopeImpl(private val savedStateHandle: SavedStateHandle) : SavableScope {
    override fun <T> SaveDelegate<T>.bind(key: String?): PropertyDelegateProvider<Any, SavedStateHandleDelegate<T>> =
        PropertyDelegateProvider { _, property ->
            SavedStateHandleDelegate(
                savedStateHandle = savedStateHandle,
                key = key ?: property.name,
                delegate = this,
            )
        }

    override fun <T> SaveDelegate<T>.bindFlow(key: String?): PropertyDelegateProvider<Any, SavedStateFlowHandleDelegate<T>> =
        PropertyDelegateProvider { _, property ->
            SavedStateFlowHandleDelegate(
                savedStateHandle = savedStateHandle,
                key = key ?: property.name,
                delegate = this,
            )
        }
}
