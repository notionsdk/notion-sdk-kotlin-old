package com.petersamokhin.notionapi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class NotionCollection(
    val role: String,
    val value: NotionCollectionValue
)

@Serializable
public data class NotionCollectionValue(
    val id: String,
    val name: NotionTextField? = null,
    @SerialName("parent_id")
    val parentId: String,
    @SerialName("parent_table")
    val parentTable: String,
    val version: Int,
    val alive: Boolean,
    val migrated: Boolean,
    val cover: String? = null,
    val description: NotionTextField? = null,
    val format: NotionCollectionCoverFormat? = null,
    val icon: String? = null,
    val schema: Map<String, NotionCollectionColumnSchema>
)

@Serializable
public data class NotionCollectionCoverFormat(
    @SerialName("collection_cover_position")
    val collectionCoverPosition: Double
)

@Serializable
public data class NotionCollectionAggregationResult(
    val id: String? = null,
    val value: Int? = null
)