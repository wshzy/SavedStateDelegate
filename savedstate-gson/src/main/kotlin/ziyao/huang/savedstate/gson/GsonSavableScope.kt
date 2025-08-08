package ziyao.huang.savedstate.gson

import com.google.gson.Gson
import ziyao.huang.savedstate.Converter

interface GsonSavableScope {
    fun <T> byGson(type: Class<T>): Converter<String?, T?>

    companion object {
        internal var default = Gson()

        fun installDefaultGson(gson: Gson) {
            default = gson
        }

        operator fun invoke(gson: Gson = default): GsonSavableScope = GsonSavableScopeImpl(gson)
    }
}

private class GsonSavableScopeImpl(val gson: Gson) : GsonSavableScope {
    override fun <T> byGson(type: Class<T>): Converter<String?, T?> = Converter(
        convert = { json -> gson.fromJson(json, type) },
        reverse = { value -> gson.toJson(value) }
    )
}
