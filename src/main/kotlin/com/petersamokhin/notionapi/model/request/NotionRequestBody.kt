package com.petersamokhin.notionapi.model.request

import kotlinx.serialization.Serializable

@Serializable
public data class QueryCollectionRequestBody(
    val collectionId: String,
    val collectionViewId: String,
    val loader: Loader
)

@Serializable
public data class Loader(
    val limit: Int,
    val loadContentCover: Boolean,
    val type: String
)

@Serializable
public data class LoadPageChunkRequestBody(
    val pageId: String,
    val limit: Int,
    val chunkNumber: Int,
    val verticalColumns: Boolean
)