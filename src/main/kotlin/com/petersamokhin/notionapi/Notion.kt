package com.petersamokhin.notionapi

import com.petersamokhin.notionapi.model.LoadPageChunkRequestBody
import com.petersamokhin.notionapi.model.Loader
import com.petersamokhin.notionapi.model.NotionResponse
import com.petersamokhin.notionapi.model.QueryCollectionRequestBody
import com.petersamokhin.notionapi.request.LoadPageChunkRequest
import com.petersamokhin.notionapi.request.QueryNotionCollectionRequest
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*

class Notion(private val token: String, private var httpClient: HttpClient) {
    init {
        httpClient = httpClient.config {
            defaultRequest {
                header(HttpHeaders.Cookie, "$NOTION_TOKEN_COOKIE_KEY=$token")
            }
        }
    }

    @KtorExperimentalAPI
    suspend fun loadPage(pageId: String, limit: Int = 50): NotionResponse {
        return LoadPageChunkRequest(httpClient).execute(
            LoadPageChunkRequestBody(pageId, limit, 0, false)
        )
    }

    @KtorExperimentalAPI
    suspend fun queryCollection(collectionId: String, collectionViewId: String, limit: Int = 70): NotionResponse {
        return QueryNotionCollectionRequest(httpClient).execute(
            QueryCollectionRequestBody(
                collectionId, collectionViewId, Loader(limit, false, "table")
            )
        )
    }

    companion object {
        private const val NOTION_TOKEN_COOKIE_KEY = "token_v2"
    }
}