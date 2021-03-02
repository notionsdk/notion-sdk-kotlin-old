package com.petersamokhin.notionapi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

public typealias NotionTextField = List<List<String>>

@Serializable
public sealed class NotionCollectionColumnSchema {
    public abstract val name: String
    public abstract val type: NotionColumnType

    @Serializable
    @SerialName("title")
    public data class Title(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("text")
    public data class Text(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("number")
    public data class Number(
        override val name: String,
        override val type: NotionColumnType,
        @SerialName("number_format") val numberFormat: String? = null
    ) : NotionCollectionColumnSchema() {
        @Serializable
        public enum class Format {
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
    public data class Email(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("checkbox")
    public data class Checkbox(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("select")
    public data class Select(
        override val name: String,
        override val type: NotionColumnType,
        val options: List<NotionSelectOption>
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("multi_select")
    public data class MultiSelect(
        override val name: String,
        override val type: NotionColumnType,
        val options: List<NotionSelectOption>
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("url")
    public data class Url(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("date")
    public data class Date(
        override val name: String,
        override val type: NotionColumnType,
        @SerialName("time_format") val timeFormat: String? = null,
        @SerialName("date_format") val dateFormat: String? = null
    ) : NotionCollectionColumnSchema() {
        @Serializable
        public enum class TimeFormat {
            @SerialName("LT")
            H_12,

            @SerialName("H:mm")
            H_24;
        }

        @Serializable
        public enum class DateFormat {
            @SerialName("ll") // or null
            Full,

            @SerialName("relative")
            Relative,

            @SerialName("MM/DD/YYYY")
            MM_DD_YYYY,

            @SerialName("DD/MM/YYYY")
            DD_MM_YYYY,

            @SerialName("YYYY/MM/DD")
            YYYY_MM_DD;
        }
    }

    @Serializable
    @SerialName("person")
    public data class Person(
        override val name: String,
        override val type: NotionColumnType
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("phone_number")
    public data class PhoneNumber(
        override val name: String,
        override val type: NotionColumnType
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("file")
    public data class File(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("last_edited_time")
    public data class LastEditedTime(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("last_edited_by")
    public data class LastEditedBy(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("created_time")
    public data class CreatedTime(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("created_by")
    public data class CreatedBy(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("rollup")
    public data class Rollup(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("relation")
    public data class Relation(
        override val name: String,
        override val type: NotionColumnType,
        val property: String,
        @SerialName("collection_id")
        val collectionId: String
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("formula")
    public data class Formula(
        override val name: String,
        override val type: NotionColumnType,
        val formula: JsonElement
    ) : NotionCollectionColumnSchema()
}

@Serializable
public data class NotionPropertyFormat(
    val width: Int? = null,
    val visible: Boolean,
    val property: String
)

@Serializable
public data class NotionSelectOption(val id: String, val color: String, val value: String)