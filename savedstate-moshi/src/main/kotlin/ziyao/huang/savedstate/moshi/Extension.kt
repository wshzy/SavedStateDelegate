package ziyao.huang.savedstate.moshi

import ziyao.huang.savedstate.Converter

inline fun <reified T> MoshiSavableScope.byMoshi(): Converter<String?, T?> =
    byMoshi(type = T::class.java)
