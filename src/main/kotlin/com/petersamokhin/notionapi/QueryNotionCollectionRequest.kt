package com.petersamokhin.notionapi

import com.petersamokhin.notionapi.model.LoadPageChunkRequestBody
import com.petersamokhin.notionapi.model.NotionResponse
import com.petersamokhin.notionapi.model.QueryCollectionRequestBody
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.KtorExperimentalAPI

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

class LoadPageChunkRequest(httpClient: HttpClient) : NotionRequest<LoadPageChunkRequestBody>(httpClient) {
    @KtorExperimentalAPI
    override suspend fun execute(requestBody: LoadPageChunkRequestBody): NotionResponse {
        return httpClient.post("$API_BASE_URL/${Endpoint.LOAD_PAGE_CHUNK}") {
            headers.appendAll(BASE_HEADERS)
            contentType(ContentType.Application.Json)
            body = requestBody
        }
    }
}