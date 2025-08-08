package ziyao.huang.savedstate

import androidx.lifecycle.SavedStateHandle

class SaveDelegate<T> private constructor(
    val saver: (SavedStateHandle, String, T) -> Unit,
    val loader: (SavedStateHandle, String) -> T,
) {
    fun <R> map(saveF: (R) -> T, loadF: (T) -> R): SaveDelegate<R> = SaveDelegate(
        saver = { handle, key, value -> saver(handle, key, saveF(value)) },
        loader = { handle, key -> loadF(loader(handle, key)) },
    )

    fun <R> convert(converter: Converter<T, R>): SaveDelegate<R> = map(
        saveF = converter::reverse,
        loadF = converter::convert,
    )

    companion object {
        private fun <A> create(): SaveDelegate<A?> = SaveDelegate(
            saver = { handle, key, value -> handle[key] = value },
            loader = { handle, key -> handle[key] }
        )

        internal fun <T> of(savable: Savable<T>): SaveDelegate<T?> = create()

        internal fun <T> listOf(savable: Savable<T>): SaveDelegate<List<T>?> = create()
    }
}
