package com.petersamokhin.notionapi.model

import com.google.gson.Gson
import com.petersamokhin.notionapi.utils.trimNotionTextField

data class NotionTable<T>(
    val rows: List<T>
)

fun NotionCollection.mapTable(blocks: List<NotionBlock>) = NotionTable<Map<String, String>>(blocks.map {
    val props = it.value.properties
    mutableMapOf<String, String>().also { map ->
        props.keys.forEach { innerRowKey ->
            value.schema[innerRowKey]?.name?.also { name ->
                map[name] = props[innerRowKey]?.trimNotionTextField().orEmpty()
            }
        }
    }
})

fun NotionResponse.mapTable(): NotionTable<Map<String, String>>? {
    val collectionId = recordMap.collectionsMap.keys.first()
    val collection = recordMap.collectionsMap[collectionId]
    val blocks = result.blockIds.map { recordMap.blocksMap.getValue(it) }

    return collection?.mapTable(blocks)
}

inline fun <reified T> NotionResponse.mapDeserializeTable(gson: Gson): NotionTable<T>? = mapTable()?.let { map ->
    NotionTable(map.rows.map { gson.fromJson(gson.toJson(it), T::class.java) })
}