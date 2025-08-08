package ziyao.huang.savedstate.serialization

import ziyao.huang.savedstate.Converter
import kotlin.reflect.typeOf

inline fun <reified T> SerializationSavableScope.bySerialization(): Converter<String?, T?> =
    bySerialization(type = typeOf<T>())
