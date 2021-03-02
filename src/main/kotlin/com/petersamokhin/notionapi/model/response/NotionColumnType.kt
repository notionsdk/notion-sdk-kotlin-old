package com.petersamokhin.notionapi.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class NotionColumnType {
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