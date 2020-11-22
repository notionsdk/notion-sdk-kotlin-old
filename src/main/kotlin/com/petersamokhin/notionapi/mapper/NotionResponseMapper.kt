package com.petersamokhin.notionapi.mapper

import com.petersamokhin.notionapi.model.*
import com.petersamokhin.notionapi.serializer.NotionBooleanSerializer
import com.petersamokhin.notionapi.utils.trimNotionTextField
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun NotionCollection.mapTable(blocks: List<NotionBlock>): NotionTable<Map<String, NotionColumn<*>>> {
    val rows = blocks.map {
        val props = it.value.properties

        mutableMapOf<String, NotionColumn<*>>().also { map ->
            props?.keys?.forEach { innerRowKey ->
                val schemaItem = value.schema[innerRowKey] ?: return@forEach

                schemaItem.name.also { name ->
                    val fieldText = props[innerRowKey]?.trimNotionTextField()
                    val value: NotionColumn.Entry<*> = when (schemaItem.type) {
                        NotionColumnType.Title, NotionColumnType.Text, NotionColumnType.Select -> {
                            NotionColumn.Entry.Text(name, fieldText)
                        }
                        NotionColumnType.Number -> {
                            NotionColumn.Entry.Number(name, fieldText?.toDoubleOrNull())
                        }
                        NotionColumnType.Checkbox -> {
                            NotionColumn.Entry.Bool(name, fieldText?.equals(NotionBooleanSerializer.NOTION_TRUE))
                        }
                        NotionColumnType.MultiSelect -> {
                            NotionColumn.Entry.TextList(name, fieldText?.split(","))
                        }
                    }

                    map[name] = NotionColumn(name, schemaItem.type, value)
                }
            }
        }
    }

    val schema = value.schema.mapKeys { it.value.name }

    return NotionTable(rows, schema)
}

fun NotionResponse.mapTable(): NotionTable<Map<String, NotionColumn<*>>>? {
    val collectionId = recordMap.collectionsMap.keys.firstOrNull()
    val collection = recordMap.collectionsMap[collectionId]
    val blocks = result?.blockIds?.map { recordMap.blocksMap.getValue(it) }

    return blocks?.let { collection?.mapTable(it) }
}

fun NotionResponse.mapCollectionToJsonArray(): JsonArray? {
    val collectionId = recordMap.collectionsMap.keys.firstOrNull() ?: return null
    val collection = recordMap.collectionsMap[collectionId] ?: return null
    val blocks = result?.blockIds?.map { recordMap.blocksMap.getValue(it) } ?: return null

    val list = blocks.map {
        val props = it.value.properties ?: return null

        val map = props.keys.mapNotNull { innerRowKey ->
            val schemaItem = collection.value.schema[innerRowKey] ?: return@mapNotNull null

            schemaItem.name.let { name ->
                val fieldText = props[innerRowKey]?.trimNotionTextField()
                val value = when (schemaItem.type) {
                    NotionColumnType.Title, NotionColumnType.Text, NotionColumnType.Select -> {
                        JsonPrimitive(fieldText)
                    }
                    NotionColumnType.Number -> {
                        JsonPrimitive(fieldText?.toDoubleOrNull())
                    }
                    NotionColumnType.Checkbox -> {
                        JsonPrimitive(fieldText?.equals(NotionBooleanSerializer.NOTION_TRUE))
                    }
                    NotionColumnType.MultiSelect -> {
                        fieldText?.let {
                            JsonArray(fieldText.split(",").map(::JsonPrimitive))
                        } ?: JsonNull
                    }
                }

                name to value
            }
        }.toMap()

        JsonObject(map)
    }

    return JsonArray(list)
}