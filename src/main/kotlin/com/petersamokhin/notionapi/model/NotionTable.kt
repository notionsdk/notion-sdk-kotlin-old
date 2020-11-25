package com.petersamokhin.notionapi.model

import com.petersamokhin.notionapi.model.response.NotionCollectionColumnSchema
import com.petersamokhin.notionapi.model.response.NotionColumnType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotionTable(
    val rows: List<NotionRow>,
    val schema: Map<String, NotionCollectionColumnSchema>
)

@Serializable
data class NotionRow(
    val properties: Map<String, NotionColumn>,
    @SerialName("meta_info")
    val metaInfo: MetaInfo
) {
    @Serializable
    data class MetaInfo(
        val lastEditedBy: String,
        val lastEditedTime: Long,
        val createdBy: String,
        val createdTime: Long
    )
}

@Serializable
sealed class NotionColumn {
    abstract val name: String
    abstract val type: NotionColumnType

    @Serializable
    @SerialName("single_value")
    data class SingleValue(
        override val name: String,
        override val type: NotionColumnType,
        val value: NotionProperty?
    ) : NotionColumn()

    @Serializable
    @SerialName("multi_value")
    data class MultiValue(
        override val name: String,
        override val type: NotionColumnType,
        val values: List<NotionProperty>
    ) : NotionColumn()
}

@Serializable
data class NotionProperty(val label: String, val value: Value? = null) {

    @Serializable
    sealed class Value {
        @Serializable
        @SerialName("title")
        data class Title(val text: String) : Value()

        @Serializable
        @SerialName("text")
        data class Text(val text: String) : Value()

        @Serializable
        @SerialName("checkbox")
        data class Checkbox(val checked: Boolean) : Value()

        @Serializable
        @SerialName("number")
        data class Number(val number: Double) : Value()

        @Serializable
        @SerialName("select")
        data class Select(val option: String) : Value()

        @Serializable
        @SerialName("multi_select")
        data class MultiSelect(val options: List<String>) : Value()

        @Serializable
        @SerialName("entry")
        sealed class Entry : Value() {
            @Serializable
            @SerialName("person")
            data class Person(val id: String) : Entry()

            @Serializable
            @SerialName("link")
            data class Link(val url: String) : Entry()

            @Serializable
            @SerialName("file")
            data class File(val url: String) : Entry()

            @Serializable
            @SerialName("email")
            data class Email(val email: String) : Entry()

            @Serializable
            @SerialName("phone_number")
            data class PhoneNumber(@SerialName("phone_number") val phoneNumber: String) : Entry()

            @Serializable
            @SerialName("date")
            data class Date(
                val type: Type,
                @SerialName("start_date")
                val startDate: String,
                @SerialName("time_zone")
                val timeZone: String? = null,
                val reminder: Reminder? = null,
                @SerialName("end_date")
                val endDate: String? = null,
                @SerialName("end_time")
                val endTime: String? = null,
                @SerialName("start_time")
                val startTime: String? = null
            ) : Entry() {
                @Serializable
                enum class Type {
                    @SerialName("date")
                    Date,

                    @SerialName("datetime")
                    DateTime,

                    @SerialName("datetimerange")
                    DateTimeRange
                }

                @Serializable
                data class Reminder(
                    val time: String,
                    val unit: ReminderTimeUnit,
                    val value: Int
                ) {
                    @Serializable
                    enum class ReminderTimeUnit {
                        @SerialName("week")
                        Week,

                        @SerialName("day")
                        Day,

                        @SerialName("hour")
                        Hour,

                        @SerialName("monute")
                        Minute
                    }
                }
            }

            @Serializable
            enum class Type {
                @SerialName("u")
                Person,

                @SerialName("d")
                Date,

                @SerialName("a")
                Link
            }
        }
    }
}