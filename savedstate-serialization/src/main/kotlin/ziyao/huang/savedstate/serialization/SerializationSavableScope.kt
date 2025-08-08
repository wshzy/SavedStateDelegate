package ziyao.huang.savedstate.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ziyao.huang.savedstate.Converter
import kotlin.reflect.KType

interface SerializationSavableScope {
    fun <T> bySerialization(type: KType): Converter<String?, T?>

    companion object {
        internal var default: Json = Json

        fun installDefaultJson(json: Json) {
            default = json
        }

        operator fun invoke(json: Json = default): SerializationSavableScope =
            SerializationSavableScopeImpl(json)
    }
}

private class SerializationSavableScopeImpl(val json: Json) : SerializationSavableScope {
    override fun <T> bySerialization(type: KType): Converter<String?, T?> = Converter(
        convert = { jsonString ->
            if (jsonString.isNullOrEmpty()) return@Converter null
            json.decodeFromString(deserializer = serializer(type) as KSerializer<T>, jsonString)
        },
        reverse = { value -> json.encodeToString(serializer = serializer(type), value) }
    )
}
