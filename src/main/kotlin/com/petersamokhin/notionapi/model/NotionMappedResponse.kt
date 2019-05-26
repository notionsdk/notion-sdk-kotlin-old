package com.petersamokhin.notionapi.model

data class NotionTable(
    val rows: List<Map<String, String>>
)

fun NotionCollection.mapTable(blocks: List<NotionBlock>) = NotionTable(blocks.map {
    val props = it.value.properties
    mutableMapOf<String, String>().also { map ->
        props.keys.forEach { innerRowKey ->
            value.schema[innerRowKey]?.name?.also { name ->
                map[name] = props[innerRowKey]?.first()?.first().orEmpty()
            }
        }
    }
})

fun NotionResponse.mapTable(): NotionTable? {
    val collectionId = recordMap.collectionsMap.keys.first()
    val collection = recordMap.collectionsMap[collectionId]

    val blocks = result.blockIds.map { recordMap.blocksMap.getValue(it) }
    return collection?.mapTable(blocks)
}