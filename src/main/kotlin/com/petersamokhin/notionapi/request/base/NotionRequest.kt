package com.petersamokhin.notionapi.request.base

import com.petersamokhin.notionapi.model.response.NotionResponse
import io.ktor.client.*
import io.ktor.http.*

public abstract class NotionRequest<T>(protected val httpClient: HttpClient) {
    public abstract suspend fun execute(requestBody: T): NotionResponse

    internal object Endpoint {
        const val QUERY_COLLECTION = "queryCollection"
        const val LOAD_PAGE_CHUNK = "loadPageChunk"
        const val LOGIN_WITH_EMAIL = "loginWithEmail"
    }

    public companion object {
        public const val API_BASE_URL: String = "https://www.notion.so/api/v3"
        public val BASE_HEADERS: Headers = headersOf(
            "Accept-Language" to listOf("en-US,en;q=0.9"),
            "User-Agent" to listOf("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
        )
    }
}