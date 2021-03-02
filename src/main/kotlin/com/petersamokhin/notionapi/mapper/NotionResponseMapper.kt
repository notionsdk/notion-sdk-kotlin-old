package com.petersamokhin.notionapi.mapper

import com.petersamokhin.notionapi.model.NotionColumn
import com.petersamokhin.notionapi.model.NotionRow
import com.petersamokhin.notionapi.model.NotionTable
import com.petersamokhin.notionapi.model.response.NotionBlock
import com.petersamokhin.notionapi.model.response.NotionCollection
import com.petersamokhin.notionapi.model.response.NotionResponse
import kotlinx.serialization.json.Json

public fun NotionResponse.mapTable(json: Json, sortColumns: Boolean = false): NotionTable? {
    val collectionId = recordMap.collectionsMap?.keys?.firstOrNull() ?: return null
    val collection = recordMap.collectionsMap[collectionId]

    val collectionViewId = recordMap.collectionViewsMap?.keys?.firstOrNull()
    val collectionView = recordMap.collectionViewsMap?.get(collectionViewId)
    val collectionViewFormat = collectionView?.value?.format

    val sortMap = if (sortColumns && collectionViewFormat != null) {
        collectionViewFormat.tableProperties.mapIndexed { index, item -> item.property to index }.toMap()
    } else {
        null
    }

    val blocks = result?.blockIds?.mapNotNull { recordMap.blocksMap?.get(it) }

    return blocks?.let { collection?.mapTable(json, it, sortMap) }
}

public fun NotionCollection.mapTable(
    json: Json,
    blocks: List<NotionBlock>,
    sortMap: Map<String, Int>? = null
): NotionTable {
    val rows: List<NotionRow> = blocks.map { block ->
        val props = block.value.properties
        val propsKeys = props?.keys?.let { propsKeys ->
            if (sortMap != null) {
                propsKeys.sortedBy { sortMap[it] }
            } else {
                propsKeys
            }
        }
        val rowItems = mutableMapOf<String, NotionColumn>()

        propsKeys?.forEach { innerRowKey ->
            val schemaItem = value.schema[innerRowKey] ?: return@forEach

            schemaItem.name.also { name ->
                val field = props[innerRowKey]

                rowItems[name] = field.parseNotionColumn(json, name, schemaItem.type)
            }
        }

        NotionRow(
            properties = rowItems,
            metaInfo = NotionRow.MetaInfo(
                lastEditedBy = block.value.lastEditedById,
                lastEditedTime = block.value.lastEditedTime,
                createdBy = block.value.createdById,
                createdTime = block.value.createdTime
            )
        )
    }

    val schema = value.schema.mapKeys { it.value.name }

    return NotionTable(
        title(),
        description(),
        rows,
        schema
    )
}