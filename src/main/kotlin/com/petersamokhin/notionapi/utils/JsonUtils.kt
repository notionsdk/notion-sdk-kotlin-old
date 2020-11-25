package com.petersamokhin.notionapi.utils

import kotlinx.serialization.json.*

val JsonElement.jsonArrayOrNull: JsonArray?
    get() = try { jsonArray } catch (e: Exception) { null }

val JsonElement.contentAsStringOrNull: String?
    get() = try { jsonPrimitive.contentOrNull } catch (e: Exception) { null }