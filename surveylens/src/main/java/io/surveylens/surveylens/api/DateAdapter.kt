package io.surveylens.surveylens.api

import com.google.gson.*
import java.lang.reflect.Type
import java.util.*

internal class DateAdapter : JsonDeserializer<Date>, JsonSerializer<Date> {
    override fun serialize(
        src: Date,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.time)
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date {
        return Date(json.asLong)
    }
}