package com.petersamokhin.notionapi

import com.petersamokhin.notionapi.model.NotionResponse
import io.ktor.client.HttpClient
import io.ktor.http.headersOf

abstract class NotionRequest<T>(protected val httpClient: HttpClient) {
    abstract suspend fun execute(requestBody: T): NotionResponse

    protected object Endpoint {
        const val QUERY_COLLECTION = "queryCollection"
        const val LOAD_PAGE_CHUNK = "loadPageChunk"
    }

    protected companion object {
        const val API_BASE_URL = "https://www.notion.so/api/v3"
        val BASE_HEADERS = headersOf(
            "Accept-Language" to listOf("en-US,en;q=0.9"),
            "User-Agent" to listOf("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
        )
    }
}