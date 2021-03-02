package com.petersamokhin.notionapi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
public data class NotionResponse(
    val recordMap: NotionRecordMap,
    val result: NotionResult? = null
)

@Serializable
public data class NotionRecordMap(
    @SerialName("block") val blocksMap: Map<String, NotionBlock>? = null,
    @SerialName("collection") val collectionsMap: Map<String, NotionCollection>? = null,
    @SerialName("collection_view") val collectionViewsMap: Map<String, NotionCollectionView>? = null
)

@Serializable
public data class NotionBlock(
    val role: String,
    val value: NotionBlockValue
)

@Serializable
public data class NotionBlockValue(
    val alive: Boolean,
    val version: Int,
    val type: String,
    @SerialName("view_ids")
    val viewIds: List<String>? = null,
    @SerialName("collection_id")
    val collectionId: String? = null,
    @SerialName("created_time")
    val createdTime: Long,
    @SerialName("last_edited_time")
    val lastEditedTime: Long,
    @SerialName("parent_id")
    val parentId: String,
    @SerialName("parent_table")
    val parentTable: String,
    @SerialName("ignore_block_count")
    val ignoreBlockCount: Boolean? = null,
    @SerialName("created_by_table")
    val createdByTable: String,
    @SerialName("created_by_id")
    val createdById: String,
    @SerialName("last_edited_by_table")
    val lastEditedByTable: String,
    @SerialName("last_edited_by_id")
    val lastEditedById: String,
    @SerialName("shard_id")
    val shardId: Int,
    @SerialName("space_id")
    val spaceId: String,
    val properties: Map<String, JsonArray>? = null
)

@Serializable
public data class NotionResult(
    val blockIds: List<String>? = null,
    val aggregationResults: List<NotionCollectionAggregationResult>? = null,
    val total: Int? = null,
    val type: String? = null
)