# KNotion API

Unofficial Notion.so API wrapper, written in Kotlin. Can be used in Java.

### Install

- Add `jitpack.io` to your repositories list

```
repositories {
    // ...
    maven { url 'https://jitpack.io' }
}
```

- Add library to dependencies list

```
dependencies {
    implementation "com.github.petersamokhin:knotion-api:$kNotionApiVersion"
}
```

Latest version: https://github.com/petersamokhin/knotion-api/releases/latest

### Get authorization token

- Open any [notion.so](https://notion.so) page in browser (e.g. in Google Chrome)
- Open debugging tools (you must be logged in)
- Obtain the `token_v2` cookie value of `https://www.notion.so/`

### Get page id

- Open any page
- Your link should look like `https://www.notion.so/<your_wokspace>/d822369169254cc4a1b2f5bbf3e8b87b`
- Last **path** part is page id (skip all query params), `d822369169254cc4a1b2f5bbf3e8b87b` for example above.

### Example: map collection to human-readable JSON

```kotlin
val token = "abcd..." // see above

val httpClient = HttpClient(CIO) {
    // json feature is required to be installed to your client
    install(JsonFeature) {
        serializer = KotlinxSerializer(Json { ignoreUnknownKeys = true })
    }
}

val notion = Notion(token, httpClient) // there is no client by default, you need to provide it

val pageId = "abcd...".dashifyId() // see above
val page = notion.loadPage(pageId)

val collectionId = page.recordMap.collectionsMap.keys.first()
val collectionViewId = page.recordMap.collectionViewsMap.keys.first()

val collectionResponse = notion.queryCollection(collectionId, collectionViewId)
val collection = collectionResponse.recordMap.collectionsMap[collectionId]

val title = collection?.title()
val description = collection?.description()

val jsonArray: JsonArray? = collectionResponse.mapCollectionToJsonArray()

// Then print mapped data:

println(title)
println(description)

println(jsonArray)
```

**Source collection:**

<img src="https://i.imgur.com/w24wLPW.png" data-canonical-src="https://i.imgur.com/w24wLPW.png" width="384" height="348" />

**Output:**

```
'Test table'
'Test description'
[{"value":"some_value_0","key":"some_key_0"},{"checked":true,"value":"another_value","key":"some_key_1"}]
```

So starting from here you can use the JSON to map your models.

### Mapping

You can also use the pre-defined mapping:

```kotlin
// ... see the previous example for the above steps

val table: NotionTable<Map<String, NotionColumn<*>>>? = collectionResponse.mapTable()

// Table contains the (1) schema (map) of the collection:
println(table?.schema)
// {checked=Checkbox(name=checked, type=Checkbox), value=Text(name=value, type=Text), key=Title(name=key, type=Title)}

println(table)
// NotionTable(rows=[{value=NotionColumn(name=value, type=Text, value=Text(key=value, value=some_value_0)), key=NotionColumn(name=key, type=Title, value=Text(key=key, value=some_key_0))}, {checked=NotionColumn(name=checked, type=Checkbox, value=Bool(key=checked, value=true)), value=NotionColumn(name=value, type=Text, value=Text(key=value, value=another_value)), key=NotionColumn(name=key, type=Title, value=Text(key=key, value=some_key_1))}], schema={checked=Checkbox(name=checked, type=Checkbox), value=Text(name=value, type=Text), key=Title(name=key, type=Title)})
```

Unfortunately, Notion API is too dynamically typed to easy cover all the cases for the column types. Date, person,
email, URL etc. are not available in this library. Only simple text values, numbers, checkboxes, select/multiselect
values are supported.

**Important note**: if you will have any other columns with these types, serializer will fail.