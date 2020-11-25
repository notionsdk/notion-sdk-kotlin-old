package com.petersamokhin.notionapi.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object NotionBooleanSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("NotionBoolean", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Boolean) =
        encoder.encodeString(
            if (value)
                NOTION_TRUE
            else
                NOTION_FALSE
        )

    override fun deserialize(decoder: Decoder): Boolean =
        decoder.decodeString() == NOTION_TRUE

    const val NOTION_TRUE = "Yes" // ingeniously
    const val NOTION_FALSE = "No"
}