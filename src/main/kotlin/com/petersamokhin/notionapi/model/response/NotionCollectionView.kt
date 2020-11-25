package com.petersamokhin.notionapi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotionCollectionView(
    val role: String,
    val value: NotionCollectionViewValue
)

@Serializable
data class NotionCollectionViewValue(
    val id: String,
    val version: Int,
    val type: String,
    val name: String,
    val alive: Boolean,
    @SerialName("parent_id")
    val parentId: String,
    @SerialName("parent_table")
    val parentTable: String,
    val format: NotionCollectionViewValueFormat? = null
)

@Serializable
data class NotionCollectionViewValueFormat(
    @SerialName("table_wrap")
    val tableWrap: Boolean,
    @SerialName("table_properties")
    val tableProperties: List<NotionPropertyFormat>
)