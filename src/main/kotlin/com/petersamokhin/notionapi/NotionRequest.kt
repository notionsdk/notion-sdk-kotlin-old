package com.petersamokhin.notionapi

import com.petersamokhin.notionapi.model.NotionResponse
import com.petersamokhin.notionapi.model.QueryCollectionRequestBody
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.util.KtorExperimentalAPI

abstract class NotionRequest<T>(protected val httpClient: HttpClient) {
    abstract suspend fun execute(requestBody: T): NotionResponse

    protected object Endpoint {
        const val QUERY_COLLECTION = "queryCollection"
    }

    protected companion object {
        const val API_BASE_URL = "https://www.notion.so/api/v3"
        val BASE_HEADERS = headersOf(
            "Accept-Language" to listOf("en-US,en;q=0.9"),
            "User-Agent" to listOf("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
        )
    }
}

class QueryNotionCollectionRequest(httpClient: HttpClient) : NotionRequest<QueryCollectionRequestBody>(httpClient) {
    @KtorExperimentalAPI
    override suspend fun execute(requestBody: QueryCollectionRequestBody): NotionResponse {
        return httpClient.post("$API_BASE_URL/${Endpoint.QUERY_COLLECTION}") {
            headers.appendAll(BASE_HEADERS)
            contentType(ContentType.Application.Json)
            body = requestBody
        }
    }
}