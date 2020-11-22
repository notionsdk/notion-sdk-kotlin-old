package com.petersamokhin.notionapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class NotionTable<T>(
    val rows: List<T>,
    val schema: Map<String, NotionCollectionColumnSchema>
)

data class NotionColumn<T>(val name: String, val type: NotionColumnType, val value: Entry<T>?) {
    @Serializable
    sealed class Entry<T> {
        abstract val key: String
        abstract val value: T?

        @Serializable
        @SerialName("text")
        data class Text(override val key: String, override val value: String? = null): Entry<String>()

        @Serializable
        @SerialName("text_list")
        data class TextList(override val key: String, override val value: List<String>? = null): Entry<List<String>>()

        @Serializable
        @SerialName("boolean")
        data class Bool(override val key: String, override val value: Boolean? = null): Entry<Boolean>()

        @Serializable
        @SerialName("number")
        data class Number(override val key: String, override val value: Double? = null): Entry<Double>()
    }
}