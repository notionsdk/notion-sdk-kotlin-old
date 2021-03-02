package com.petersamokhin.notionapi.mapper

import com.petersamokhin.notionapi.model.NotionColumn
import com.petersamokhin.notionapi.model.NotionProperty
import com.petersamokhin.notionapi.model.response.NotionCollection
import com.petersamokhin.notionapi.model.response.NotionColumnType
import com.petersamokhin.notionapi.serializer.NotionBooleanSerializer
import com.petersamokhin.notionapi.utils.contentAsStringOrNull
import com.petersamokhin.notionapi.utils.jsonArrayOrNull
import com.petersamokhin.notionapi.utils.trimNotionTextField
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

public fun NotionCollection.title(): String? =
    value.name?.trimNotionTextField()

public fun NotionCollection.description(): String? =
    value.description?.trimNotionTextField()

public fun JsonArray?.parseNotionColumn(json: Json, name: String, type: NotionColumnType): NotionColumn {
    if (isNullOrEmpty()) return NotionColumn.SingleValue(name, type, null)
    val masterList = filterIsInstance<JsonArray>()

    return when (type) {
        NotionColumnType.Title, NotionColumnType.Text, NotionColumnType.Number,
        NotionColumnType.Checkbox, NotionColumnType.Select, NotionColumnType.MultiSelect,
        NotionColumnType.Email, NotionColumnType.Url, NotionColumnType.PhoneNumber -> {
            val label = masterList.firstOrNull()?.getOrNull(0)?.contentAsStringOrNull
                ?: return NotionColumn.SingleValue(name, type, null)

            return NotionColumn.SingleValue(
                name = name, type = type,
                value = when (type) {
                    NotionColumnType.Title -> NotionProperty.Value.Title(label)
                    NotionColumnType.Text -> NotionProperty.Value.Text(label)
                    NotionColumnType.Number -> NotionProperty.Value.Number(label.toDouble())
                    NotionColumnType.Checkbox -> NotionProperty.Value.Checkbox(label == NotionBooleanSerializer.NOTION_TRUE)
                    NotionColumnType.Select -> NotionProperty.Value.Select(label)
                    NotionColumnType.MultiSelect -> NotionProperty.Value.MultiSelect(label.split(","))
                    NotionColumnType.Email -> NotionProperty.Value.Email(label)
                    NotionColumnType.Url -> NotionProperty.Value.Url(label)
                    NotionColumnType.PhoneNumber -> NotionProperty.Value.PhoneNumber(label)
                    else -> throw IllegalStateException("exhaustive")
                }.let { NotionProperty(label = label, it) }
            )
        }
        NotionColumnType.Person, NotionColumnType.File -> {
            NotionColumn.MultiValue(
                name = name,
                type = type,
                values = masterList.map { currentItemList ->
                    currentItemList.filterIsInstance<JsonArray>().mapNotNull {
                        currentItemList.firstOrNull()?.contentAsStringOrNull?.let { label ->
                            it.getOrNull(0)?.jsonArrayOrNull?.getOrNull(1)?.contentAsStringOrNull?.let {
                                label to it
                            }
                        }
                    }
                }.flatten()
                    .map { (label, item) ->
                        val v = when (type) {
                            NotionColumnType.Person -> NotionProperty.Value.Entry.Person(item)
                            NotionColumnType.File -> NotionProperty.Value.Entry.File(item)
                            else -> null
                        }

                        NotionProperty(label, v)
                    }
            )
        }
        NotionColumnType.Date -> {
            NotionColumn.SingleValue(
                name = name,
                type = type,
                value = NotionProperty(
                    label = masterList.firstOrNull()?.getOrNull(0)?.contentAsStringOrNull
                        ?: return NotionColumn.SingleValue(name, type, null),
                    value = masterList.firstOrNull()?.getOrNull(1)
                        ?.jsonArrayOrNull?.getOrNull(0)
                        ?.jsonArrayOrNull?.getOrNull(1)
                        ?.let<JsonElement, NotionProperty.Value.Entry.Date>(json::decodeFromJsonElement)
                )
            )
        }
        NotionColumnType.LastEditedTime, NotionColumnType.LastEditedBy,
        NotionColumnType.CreatedTime, NotionColumnType.CreatedBy,
        NotionColumnType.Rollup, NotionColumnType.Relation, NotionColumnType.Formula -> {
            NotionColumn.SingleValue(name, type, null)
        }
    }
}