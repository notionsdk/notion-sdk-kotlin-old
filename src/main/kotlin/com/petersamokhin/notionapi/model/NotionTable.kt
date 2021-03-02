package com.petersamokhin.notionapi.model

import com.petersamokhin.notionapi.model.response.NotionCollectionColumnSchema
import com.petersamokhin.notionapi.utils.get
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
public data class NotionTable(
    val title: String?,
    val description: String?,
    val rows: List<NotionRow>,
    val schema: Map<String, NotionCollectionColumnSchema>
) {
    /**
     * Converts the table into a readable json object,
     * which can be mapped into a model
     */
    public fun simpleJson(json: Json): JsonElement =
        JsonObject(
            mapOf(
                "title" to JsonPrimitive(title),
                "description" to JsonPrimitive(description),
                "schema" to json.encodeToJsonElement(schema),
                "rows" to rows.map { row ->
                    JsonObject(
                        mapOf(
                            "meta_info" to json.encodeToJsonElement(row.metaInfo),
                            "properties" to JsonObject(
                                row.properties.map { (key, value) ->
                                    key to when (value) {
                                        is NotionColumn.SingleValue -> value.value?.value?.simpleJson(json) ?: JsonNull
                                        is NotionColumn.MultiValue -> JsonArray(value.values.mapNotNull {
                                            it.value?.simpleJson(
                                                json
                                            )
                                        })
                                    }
                                }.toMap()
                            )
                        )
                    )
                }.let(::JsonArray)
            )
        )

    /**
     * Get this table's rows as an array of arrays
     */
    public fun simpleJsonRows(json: Json): JsonArray =
        simpleJson(json)["rows"]?.jsonArray
            ?.mapNotNull { it["properties"] }
            ?.let(::JsonArray)
            ?: JsonArray(emptyList())
}

