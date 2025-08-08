package ziyao.huang.savedstate.gson

import ziyao.huang.savedstate.Converter

inline fun <reified T> GsonSavableScope.byGson(): Converter<String?, T?> =
    byGson(type = T::class.java)
