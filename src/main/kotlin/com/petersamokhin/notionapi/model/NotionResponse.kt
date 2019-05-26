package com.petersamokhin.notionapi.model

import com.google.gson.annotations.SerializedName

data class NotionResponse(
    val recordMap: NotionRecordMap,
    val result: NotionResult
)

data class NotionRecordMap(
    @SerializedName("block") val blocksMap: Map<String, NotionBlock>,
    @SerializedName("collection") val collectionsMap: Map<String, NotionCollection>,
    @SerializedName("collection_view") val collectionViewsMap: Map<String, NotionCollectionView>
)

data class NotionBlock(
    val role: String,
    val value: NotionBlockValue
)

data class NotionBlockValue(
    val alive: Boolean,
    @SerializedName("created_by")
    val createdBy: String,
    @SerializedName("created_time")
    val createdTime: Long,
    val id: String,
    @SerializedName("last_edited_by")
    val lastEditedBy: String,
    @SerializedName("last_edited_time")
    val lastEditedTime: Long,
    @SerializedName("parent_id")
    val parentId: String,
    @SerializedName("parent_table")
    val parentTable: String,
    val properties: Map<String, List<List<Any>>>,
    val type: String,
    val version: Int
)

data class NotionResult(
    val blockIds: List<String>,
    val aggregationResults: List<NotionCollectionAggregationResult>,
    val total: Int,
    val type: String
)

data class NotionCollectionAggregationResult(
    val id: String,
    val value: Int
)

data class NotionCollection(
    val role: String,
    val value: NotionCollectionValue
)

data class NotionCollectionView(
    val role: String,
    val value: NotionCollectionViewValue
)

data class NotionCollectionCoverFormat(
    @SerializedName("collection_cover_position")
    val collectionCoverPosition: Double
)

data class NotionCollectionValue(
    val alive: Boolean,
    val cover: String,
    val description: List<List<Any>>,
    val format: NotionCollectionCoverFormat,
    val icon: String,
    val id: String,
    val name: List<List<Any>>,
    @SerializedName("parent_id")
    val parentId: String,
    @SerializedName("parent_table")
    val parentTable: String,
    val schema: Map<String, NotionCollectionRow>,
    val version: Int
)

data class NotionCollectionViewValue(
    val id: String,
    val version: Int,
    val type: String,
    val name: String,
    val alive: Boolean,
    @SerializedName("parent_id")
    val parentId: String,
    @SerializedName("parent_table")
    val parentTable: String
)

data class NotionCollectionRow(
    val name: String,
    val type: String,
    @SerializedName("number_format")
    val numberFormat: String = ""
)