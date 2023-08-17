@file:JvmName("Mapper")

package dev.arbjerg.lavalink.protocol.v4

import dev.arbjerg.lavalink.protocol.v4.serialization.TimestampSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.plus
import kotlin.jvm.JvmName

/**
 * [SerializersModule] containing all required Lavalink configuration.
 *
 * @see json
 */
val LavalinkSerializersModule = SerializersModule {
    contextual(TimestampSerializer)
    contextual(Message.ReadyEvent::class, Message.ReadyEvent.serializer())
} + RoutePlannerModule

/**
 * [Json] object which should be used to serialize/deserialize Lavalink objects.
 *
 * @see LavalinkSerializersModule
 */
val json = Json {
    ignoreUnknownKeys = true
    encodeDefaults = false
    serializersModule = LavalinkSerializersModule
}
