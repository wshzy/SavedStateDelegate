package ziyao.huang.savedstate.moshi

import com.squareup.moshi.Moshi
import ziyao.huang.savedstate.Converter

interface MoshiSavableScope {
    fun <T> byMoshi(type: Class<T>): Converter<String?, T?>

    companion object {
        internal var default = Moshi.Builder().build()

        fun installDefaultMoshi(moshi: Moshi) {
            default = moshi
        }

        operator fun invoke(json: Moshi = default): MoshiSavableScope =
            MoshiSavableScopeImpl(json)
    }
}

private class MoshiSavableScopeImpl(val moshi: Moshi) : MoshiSavableScope {
    override fun <T> byMoshi(type: Class<T>): Converter<String?, T?> = Converter(
        convert = { json ->
            if (json.isNullOrEmpty()) return@Converter null
            moshi.adapter(type).fromJson(json)
        },
        reverse = { value -> moshi.adapter(type).toJson(value) }
    )
}
