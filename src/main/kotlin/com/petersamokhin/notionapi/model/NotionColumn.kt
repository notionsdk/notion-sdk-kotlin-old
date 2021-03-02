package com.petersamokhin.notionapi.model

import com.petersamokhin.notionapi.model.response.NotionColumnType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public sealed class NotionColumn {
    public abstract val name: String
    public abstract val type: NotionColumnType

    @Serializable
    @SerialName("single_value")
    public data class SingleValue(
        override val name: String,
        override val type: NotionColumnType,
        val value: NotionProperty?
    ) : NotionColumn()

    @Serializable
    @SerialName("multi_value")
    public data class MultiValue(
        override val name: String,
        override val type: NotionColumnType,
        val values: List<NotionProperty>
    ) : NotionColumn()
}