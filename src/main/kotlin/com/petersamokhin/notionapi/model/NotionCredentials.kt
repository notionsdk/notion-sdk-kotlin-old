package com.petersamokhin.notionapi.model

import kotlinx.serialization.Serializable

@Serializable
public data class NotionCredentials(
    val email: String,
    val password: String
)