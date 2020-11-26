package com.petersamokhin.notionapi.model

import com.petersamokhin.notionapi.model.response.NotionCollectionColumnSchema
import com.petersamokhin.notionapi.model.response.NotionColumnType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
data class NotionTable(
    val title: String?,
    val description: String?,
    val rows: List<NotionRow>,
    val schema: Map<String, NotionCollectionColumnSchema>
) {
    fun simpleJson(json: Json): JsonElement {
        return JsonObject(
            mapOf(
                "title" to JsonPrimitive(title),
                "description" to JsonPrimitive(description),
                "schema" to json.encodeToJsonElement(schema),
                "rows" to rows.map { row ->
                    JsonObject(
                        mapOf(
                            "meta_info" to json.encodeToJsonElement(row.metaInfo),
                            "properties" to JsonObject(
                                row.properties.map { (key, value) ->
                                    key to when (value) {
                                        is NotionColumn.SingleValue -> value.value?.value?.simpleJson(json) ?: JsonNull
                                        is NotionColumn.MultiValue -> JsonArray(value.values.mapNotNull {
                                            it.value?.simpleJson(
                                                json
                                            )
                                        })
                                    }
                                }.toMap()
                            )
                        )
                    )
                }.let(::JsonArray)
            )
        )
    }

    fun simpleJsonRows(json: Json): JsonArray {
        val simpleJson = simpleJson(json)

        return simpleJson.jsonObject["rows"]?.jsonArray?.mapNotNull { it.jsonObject["properties"] }?.let(::JsonArray)
            ?: JsonArray(emptyList())
    }
}

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
        abstract fun simpleJson(json: Json): JsonElement

        @Serializable
        @SerialName("title")
        data class Title(val text: String) : Value() {
            override fun simpleJson(json: Json) = JsonPrimitive(text)
        }

        @Serializable
        @SerialName("text")
        data class Text(val text: String) : Value() {
            override fun simpleJson(json: Json) = JsonPrimitive(text)
        }

        @Serializable
        @SerialName("checkbox")
        data class Checkbox(val checked: Boolean) : Value() {
            override fun simpleJson(json: Json) = JsonPrimitive(checked)
        }

        @Serializable
        @SerialName("number")
        data class Number(val number: Double) : Value() {
            override fun simpleJson(json: Json) = JsonPrimitive(number)
        }

        @Serializable
        @SerialName("select")
        data class Select(val option: String) : Value() {
            override fun simpleJson(json: Json) = JsonPrimitive(option)
        }

        @Serializable
        @SerialName("multi_select")
        data class MultiSelect(val options: List<String>) : Value() {
            override fun simpleJson(json: Json) = JsonArray(options.map(::JsonPrimitive))
        }

        @Serializable
        @SerialName("url")
        data class Url(val url: String) : Value() {
            override fun simpleJson(json: Json) = JsonPrimitive(url)
        }

        @Serializable
        @SerialName("email")
        data class Email(val email: String) : Value() {
            override fun simpleJson(json: Json) = JsonPrimitive(email)
        }

        @Serializable
        @SerialName("phone_number")
        data class PhoneNumber(@SerialName("phone_number") val phoneNumber: String) : Value() {
            override fun simpleJson(json: Json) = JsonPrimitive(phoneNumber)
        }

        @Serializable
        @SerialName("entry")
        sealed class Entry : Value() {
            @Serializable
            @SerialName("person")
            data class Person(val id: String) : Entry() {
                override fun simpleJson(json: Json) = JsonPrimitive(id)
            }

            @Serializable
            @SerialName("file")
            data class File(val url: String) : Entry() {
                override fun simpleJson(json: Json) = JsonPrimitive(url)
            }

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

                override fun simpleJson(json: Json) = json.encodeToJsonElement(this)
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