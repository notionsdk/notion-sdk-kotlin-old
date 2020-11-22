package com.petersamokhin.notionapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotionResponse(
    val recordMap: NotionRecordMap,
    val result: NotionResult? = null
)

@Serializable
data class NotionRecordMap(
    @SerialName("block") val blocksMap: Map<String, NotionBlock>,
    @SerialName("collection") val collectionsMap: Map<String, NotionCollection>,
    @SerialName("collection_view") val collectionViewsMap: Map<String, NotionCollectionView>
)

@Serializable
data class NotionBlock(
    val role: String,
    val value: NotionBlockValue
)

@Serializable
data class NotionBlockValue(
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
    val properties: Map<String, List<List<String>>>? = null
)

@Serializable
data class NotionResult(
    val blockIds: List<String>? = null,
    val aggregationResults: List<NotionCollectionAggregationResult>? = null,
    val total: Int? = null,
    val type: String? = null
)

@Serializable
data class NotionCollectionAggregationResult(
    val id: String? = null,
    val value: Int? = null
)

@Serializable
data class NotionCollection(
    val role: String,
    val value: NotionCollectionValue
)

@Serializable
data class NotionCollectionView(
    val role: String,
    val value: NotionCollectionViewValue
)

@Serializable
data class NotionCollectionCoverFormat(
    @SerialName("collection_cover_position")
    val collectionCoverPosition: Double
)

@Serializable
data class NotionCollectionValue(
    val id: String,
    val name: List<List<String>>? = null,
    @SerialName("parent_id")
    val parentId: String,
    @SerialName("parent_table")
    val parentTable: String,
    val version: Int,
    val alive: Boolean,
    val migrated: Boolean,
    val cover: String? = null,
    val description: List<List<String>>? = null,
    val format: NotionCollectionCoverFormat? = null,
    val icon: String? = null,
    val schema: Map<String, NotionCollectionColumnSchema>
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
    val parentTable: String
)

@Serializable
sealed class NotionCollectionColumnSchema {
    abstract val name: String
    abstract val type: NotionColumnType

    @Serializable
    @SerialName("title")
    data class Title(override val name: String, override val type: NotionColumnType): NotionCollectionColumnSchema()

    @Serializable
    @SerialName("text")
    data class Text(override val name: String, override val type: NotionColumnType): NotionCollectionColumnSchema()

    @Serializable
    @SerialName("number")
    data class Number(override val name: String, override val type: NotionColumnType, @SerialName("number_format") val numberFormat: String): NotionCollectionColumnSchema() {
        @Serializable
        enum class Format {
            @SerialName("number")
            Number,
            @SerialName("number_with_commas")
            NumberWithCommas,
            @SerialName("percent")
            Percent,
            @SerialName("dollar")
            Dollar,
            @SerialName("euro")
            Euro,
            @SerialName("pound")
            Pound,
            @SerialName("yen")
            Yen,
            @SerialName("ruble")
            Ruble,
            @SerialName("rupee")
            Rupee,
            @SerialName("won")
            Won,
            @SerialName("yuan")
            Yuan,
            @SerialName("real")
            Real,
            @SerialName("lira")
            Lira,
            @SerialName("rupiah")
            Rupiah
        }
    }

    @Serializable
    @SerialName("email")
    data class Email(override val name: String, override val type: NotionColumnType): NotionCollectionColumnSchema()

    @Serializable
    @SerialName("checkbox")
    data class Checkbox(override val name: String, override val type: NotionColumnType): NotionCollectionColumnSchema()

    @Serializable
    @SerialName("select")
    data class Select(override val name: String, override val type: NotionColumnType, val options: List<NotionSelectOption>): NotionCollectionColumnSchema()

    @Serializable
    @SerialName("multi_select")
    data class MultiSelect(override val name: String, override val type: NotionColumnType, val options: List<NotionSelectOption>): NotionCollectionColumnSchema()
}

@Serializable
data class NotionSelectOption(val id: String, val color: String, val value: String)

@Serializable
enum class NotionColumnType {
    @SerialName("title")
    Title,
    @SerialName("text")
    Text,
    @SerialName("number")
    Number,
    @SerialName("checkbox")
    Checkbox,
    @SerialName("select")
    Select,
    @SerialName("multi_select")
    MultiSelect
}