package com.petersamokhin.notionapi

import com.petersamokhin.notionapi.mapper.mapTable
import com.petersamokhin.notionapi.model.NotionTable
import com.petersamokhin.notionapi.model.request.LoadPageChunkRequestBody
import com.petersamokhin.notionapi.model.request.Loader
import com.petersamokhin.notionapi.model.request.QueryCollectionRequestBody
import com.petersamokhin.notionapi.model.response.NotionResponse
import com.petersamokhin.notionapi.request.LoadPageChunkRequest
import com.petersamokhin.notionapi.request.QueryNotionCollectionRequest
import com.petersamokhin.notionapi.utils.dashifyId
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicReference

internal class NotionImpl internal constructor(
    token: String,
    httpClient: HttpClient
) : Notion {
    private val _token = AtomicReference(token)

    private var _httpClient: HttpClient =
        httpClient.withToken()

    override val token: String
        get() = _token.get()

    override suspend fun getCollection(json: Json, pageId: String, sortColumns: Boolean): NotionTable? {
        val normalPageId = pageId.dashifyId()
        val page = loadPage(normalPageId)

        val collectionId = page.recordMap.collectionsMap?.keys?.firstOrNull() ?: return null
        val collectionViewId = page.recordMap.collectionViewsMap?.keys?.firstOrNull() ?: return null

        val collectionResponse = queryCollection(collectionId, collectionViewId)

        return collectionResponse.mapTable(json, sortColumns = sortColumns)
    }

    override suspend fun loadPage(pageId: String, limit: Int): NotionResponse =
        LoadPageChunkRequest(_httpClient).execute(
            LoadPageChunkRequestBody(pageId, limit, 0, false)
        )

    override suspend fun queryCollection(
        collectionId: String,
        collectionViewId: String,
        limit: Int
    ): NotionResponse =
        QueryNotionCollectionRequest(_httpClient).execute(
            QueryCollectionRequestBody(
                collectionId, collectionViewId, Loader(limit, false, "table")
            )
        )

    override fun setHttpClient(newHttpClient: HttpClient) {
        close()
        _httpClient = newHttpClient.withToken()
    }

    override fun setToken(token: String) {
        _token.set(token)
    }

    override fun close(): Unit =
        _httpClient.close()

    private fun HttpClient.withToken(): HttpClient =
        config {
            defaultRequest {
                header(HttpHeaders.Cookie, "${Notion.TOKEN_COOKIE_KEY}=${_token.get()}")
            }
        }
}