package com.petersamokhin.notionapi.utils

import kotlinx.serialization.json.*

internal operator fun JsonElement.get(key: String): JsonElement? =
    jsonObjectOrNull?.get(key)

internal val JsonElement.jsonObjectOrNull: JsonObject?
    get() = try { jsonObject } catch (e: Exception) { null }

internal val JsonElement.jsonArrayOrNull: JsonArray?
    get() = try { jsonArray } catch (e: Exception) { null }

internal val JsonElement.contentAsStringOrNull: String?
    get() = try { jsonPrimitive.contentOrNull } catch (e: Exception) { null }