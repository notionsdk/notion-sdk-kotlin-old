package com.petersamokhin.notionapi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

typealias NotionTextField = List<List<String>>

@Serializable
sealed class NotionCollectionColumnSchema {
    abstract val name: String
    abstract val type: NotionColumnType

    @Serializable
    @SerialName("title")
    data class Title(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("text")
    data class Text(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("number")
    data class Number(
        override val name: String,
        override val type: NotionColumnType,
        @SerialName("number_format") val numberFormat: String? = null
    ) : NotionCollectionColumnSchema() {
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
    data class Email(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("checkbox")
    data class Checkbox(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("select")
    data class Select(
        override val name: String,
        override val type: NotionColumnType,
        val options: List<NotionSelectOption>
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("multi_select")
    data class MultiSelect(
        override val name: String,
        override val type: NotionColumnType,
        val options: List<NotionSelectOption>
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("url")
    data class Url(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("date")
    data class Date(
        override val name: String,
        override val type: NotionColumnType,
        @SerialName("time_format") val timeFormat: String? = null,
        @SerialName("date_format") val dateFormat: String?
    ) : NotionCollectionColumnSchema() {
        @Serializable
        enum class TimeFormat {
            @SerialName("LT")
            H_12,

            @SerialName("H:mm")
            H_24;
        }

        @Serializable
        enum class DateFormat {
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
    data class Person(
        override val name: String,
        override val type: NotionColumnType
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("phone_number")
    data class PhoneNumber(
        override val name: String,
        override val type: NotionColumnType
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("file")
    data class File(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("last_edited_time")
    data class LastEditedTime(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("last_edited_by")
    data class LastEditedBy(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("created_time")
    data class CreatedTime(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("created_by")
    data class CreatedBy(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("rollup")
    data class Rollup(override val name: String, override val type: NotionColumnType) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("relation")
    data class Relation(
        override val name: String,
        override val type: NotionColumnType,
        val property: String,
        @SerialName("collection_id")
        val collectionId: String
    ) : NotionCollectionColumnSchema()

    @Serializable
    @SerialName("formula")
    data class Formula(
        override val name: String,
        override val type: NotionColumnType,
        val formula: JsonElement
    ) : NotionCollectionColumnSchema()
}

@Serializable
data class NotionPropertyFormat(
    val width: Int? = null,
    val visible: Boolean,
    val property: String
)

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
    MultiSelect,

    @SerialName("email")
    Email,

    @SerialName("url")
    Url,

    @SerialName("date")
    Date,

    @SerialName("person")
    Person,

    @SerialName("phone_number")
    PhoneNumber,

    @SerialName("file")
    File,

    @SerialName("last_edited_time")
    LastEditedTime,

    @SerialName("last_edited_by")
    LastEditedBy,

    @SerialName("created_time")
    CreatedTime,

    @SerialName("created_by")
    CreatedBy,

    @SerialName("rollup")
    Rollup,

    @SerialName("relation")
    Relation,

    @SerialName("formula")
    Formula
}