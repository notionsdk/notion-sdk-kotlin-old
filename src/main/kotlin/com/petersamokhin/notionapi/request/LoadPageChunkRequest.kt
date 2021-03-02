package com.petersamokhin.notionapi.request

import com.petersamokhin.notionapi.model.request.LoadPageChunkRequestBody
import com.petersamokhin.notionapi.model.response.NotionResponse
import com.petersamokhin.notionapi.request.base.NotionRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

public class LoadPageChunkRequest(
    httpClient: HttpClient
) : NotionRequest<LoadPageChunkRequestBody>(httpClient) {

    override suspend fun execute(requestBody: LoadPageChunkRequestBody): NotionResponse =
        httpClient.post("$API_BASE_URL/${Endpoint.LOAD_PAGE_CHUNK}") {
            headers.appendAll(BASE_HEADERS)
            contentType(ContentType.Application.Json)
            body = requestBody
        }
}