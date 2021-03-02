package com.petersamokhin.notionapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class NotionRow(
    val properties: Map<String, NotionColumn>,
    @SerialName("meta_info")
    val metaInfo: MetaInfo
) {
    @Serializable
    public data class MetaInfo(
        val lastEditedBy: String,
        val lastEditedTime: Long,
        val createdBy: String,
        val createdTime: Long
    )
}