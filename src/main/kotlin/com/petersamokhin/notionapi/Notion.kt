package com.petersamokhin.notionapi

import com.petersamokhin.notionapi.model.NotionCredentials
import com.petersamokhin.notionapi.model.NotionTable
import com.petersamokhin.notionapi.model.response.NotionResponse
import com.petersamokhin.notionapi.request.base.NotionRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.json.Json

public interface Notion {
    public val token: String

    public suspend fun getCollection(
        json: Json,
        pageId: String,
        sortColumns: Boolean = false
    ): NotionTable?

    public suspend fun loadPage(
        pageId: String,
        limit: Int = 50
    ): NotionResponse

    public suspend fun queryCollection(
        collectionId: String,
        collectionViewId: String,
        limit: Int = 70
    ): NotionResponse

    public fun setHttpClient(newHttpClient: HttpClient)

    public fun setToken(token: String)

    public fun close()

    public companion object {
        public const val TOKEN_COOKIE_KEY: String = "token_v2"

        @JvmStatic
        public fun fromToken(token: String, httpClient: HttpClient): Notion =
            NotionImpl(token, httpClient)

        @OptIn(KtorExperimentalAPI::class)
        @JvmStatic
        public suspend fun fromEmailAndPassword(credentials: NotionCredentials, httpClient: HttpClient): Notion {
            val endpoint = "${NotionRequest.API_BASE_URL}/${NotionRequest.Endpoint.LOGIN_WITH_EMAIL}"
            val response = httpClient.post<HttpResponse>(endpoint) {
                headers.appendAll(NotionRequest.BASE_HEADERS)
                contentType(ContentType.Application.Json)
                body = credentials
            }

            val token = response.headers.getAll(HttpHeaders.SetCookie)
                ?.asSequence()
                ?.map(::parseServerSetCookieHeader)
                ?.find { it.name == TOKEN_COOKIE_KEY }
                ?.value
                .orEmpty()

            return fromToken(
                token = token,
                httpClient = httpClient
            )
        }
    }
}