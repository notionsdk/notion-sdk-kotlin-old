package com.petersamokhin.notionapi.model

import kotlinx.serialization.Serializable

@Serializable
data class QueryCollectionRequestBody(
    val collectionId: String,
    val collectionViewId: String,
    val loader: Loader
)

@Serializable
data class Loader(
    val limit: Int,
    val loadContentCover: Boolean,
    val type: String
)

@Serializable
data class LoadPageChunkRequestBody(
    val pageId: String,
    val limit: Int,
    val chunkNumber: Int,
    val verticalColumns: Boolean
)