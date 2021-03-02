package com.petersamokhin.notionapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
public data class NotionProperty(val label: String, val value: Value? = null) {

    @Serializable
    public sealed class Value {
        public abstract fun simpleJson(json: Json): JsonElement

        @Serializable
        @SerialName("title")
        public data class Title(val text: String) : Value() {
            override fun simpleJson(json: Json): JsonPrimitive =
                JsonPrimitive(text)
        }

        @Serializable
        @SerialName("text")
        public data class Text(val text: String) : Value() {
            override fun simpleJson(json: Json): JsonPrimitive =
                JsonPrimitive(text)
        }

        @Serializable
        @SerialName("checkbox")
        public data class Checkbox(val checked: Boolean) : Value() {
            override fun simpleJson(json: Json): JsonPrimitive =
                JsonPrimitive(checked)
        }

        @Serializable
        @SerialName("number")
        public data class Number(val number: Double) : Value() {
            override fun simpleJson(json: Json): JsonPrimitive =
                JsonPrimitive(number)
        }

        @Serializable
        @SerialName("select")
        public data class Select(val option: String) : Value() {
            override fun simpleJson(json: Json): JsonPrimitive =
                JsonPrimitive(option)
        }

        @Serializable
        @SerialName("multi_select")
        public data class MultiSelect(val options: List<String>) : Value() {
            override fun simpleJson(json: Json): JsonArray =
                JsonArray(options.map(::JsonPrimitive))
        }

        @Serializable
        @SerialName("url")
        public data class Url(val url: String) : Value() {
            override fun simpleJson(json: Json): JsonPrimitive =
                JsonPrimitive(url)
        }

        @Serializable
        @SerialName("email")
        public data class Email(val email: String) : Value() {
            override fun simpleJson(json: Json): JsonPrimitive =
                JsonPrimitive(email)
        }

        @Serializable
        @SerialName("phone_number")
        public data class PhoneNumber(@SerialName("phone_number") val phoneNumber: String) : Value() {
            override fun simpleJson(json: Json): JsonPrimitive =
                JsonPrimitive(phoneNumber)
        }

        @Serializable
        @SerialName("entry")
        public sealed class Entry : Value() {

            @Serializable
            @SerialName("person")
            public data class Person(val id: String) : Entry() {
                override fun simpleJson(json: Json): JsonPrimitive =
                    JsonPrimitive(id)
            }

            @Serializable
            @SerialName("file")
            public data class File(val url: String) : Entry() {
                override fun simpleJson(json: Json): JsonPrimitive =
                    JsonPrimitive(url)
            }

            @Serializable
            @SerialName("date")
            public data class Date(
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
                public enum class Type {
                    @SerialName("date")
                    Date,

                    @SerialName("datetime")
                    DateTime,

                    @SerialName("datetimerange")
                    DateTimeRange
                }

                @Serializable
                public data class Reminder(
                    val time: String,
                    val unit: ReminderTimeUnit,
                    val value: Int
                ) {
                    @Serializable
                    public enum class ReminderTimeUnit {
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

                override fun simpleJson(json: Json): JsonElement =
                    json.encodeToJsonElement(this)
            }

            @Serializable
            public enum class Type {
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