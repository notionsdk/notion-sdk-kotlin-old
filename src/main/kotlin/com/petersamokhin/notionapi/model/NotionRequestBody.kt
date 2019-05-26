package com.petersamokhin.notionapi.model

data class QueryCollectionRequestBody(
    val collectionId: String,
    val collectionViewId: String,
    val loader: Loader
)

data class Loader(
    val limit: Int,
    val loadContentCover: Boolean,
    val type: String
)

data class LoadPageChunkRequestBody(
    val pageId: String,
    val limit: Int,
    val chunkNumber: Int,
    val verticalColumns: Boolean
)