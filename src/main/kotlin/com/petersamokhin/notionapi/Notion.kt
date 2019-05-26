package com.petersamokhin.notionapi

import com.petersamokhin.notionapi.model.Loader
import com.petersamokhin.notionapi.model.NotionResponse
import com.petersamokhin.notionapi.model.QueryCollectionRequestBody
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.util.KtorExperimentalAPI
import okhttp3.Interceptor
import java.text.DateFormat

class Notion(private val token: String) {
    @KtorExperimentalAPI
    private val httpClient = HttpClient(OkHttp) {
        engine {
            addInterceptor(Interceptor {
                val request = it.request().newBuilder().addHeader("cookie", "token_v2=$token").build()
                it.proceed(request).newBuilder().removeHeader("Set-Cookie").build()
            })
        }
        install(JsonFeature) {
            serializer = GsonSerializer {
                serializeNulls()
                setDateFormat(DateFormat.FULL)
            }
        }
    }

    @KtorExperimentalAPI
    suspend fun queryCollection(collectionId: String, collectionViewId: String, limit: Int = 70): NotionResponse {
        return QueryNotionCollectionRequest(httpClient).execute(
            QueryCollectionRequestBody(
                collectionId, collectionViewId, Loader(limit, false, "table")
            )
        )
    }
}