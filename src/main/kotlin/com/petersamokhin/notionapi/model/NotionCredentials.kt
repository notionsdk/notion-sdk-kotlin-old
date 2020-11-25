package com.petersamokhin.notionapi.model

import kotlinx.serialization.Serializable

@Serializable
data class NotionCredentials(
    val email: String,
    val password: String
)
