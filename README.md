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
    implementation "com.github.notionsdk:notion-sdk-kotlin:$latestVersion"
}
```

Latest version: https://github.com/notionsdk/notion-sdk-kotlin/releases

### Auth
#### If you have a password
Just use your email and password:

```kotlin
val notion = Notion.fromEmailAndPassword(
    credentials = NotionCredentials(email = "test@test.com", password = "password"),
    /* ... */
)
```

#### If you use a randomly generated passcode
- Open any [notion.so](https://notion.so) page in browser (e.g. in Google Chrome)
- Open debugging tools (you must be logged in!)
- Obtain the `token_v2` cookie value of `https://www.notion.so/`

And then:
```kotlin
val notion = Notion.fromToken(
    token = "your_token",
    /* ... */
)
```

### Get page id
- Open any page
- Your link should look like `https://www.notion.so/<your_wokspace>/d822369169254cc4a1b2f5bbf3e8b87b`
- Last **path** part is page id (skip all query params), `d822369169254cc4a1b2f5bbf3e8b87b` for example above.

### Mapping

**Source collection:**

<img src="https://i.imgur.com/I7n9Cx0.png" data-canonical-src="https://i.imgur.com/I7n9Cx0.png" width="367" height="328" />

You can use the pre-defined mapping:

```kotlin
suspend fun main() {
    // you need to provide your httpClient; JsonFeature must be installed!
    val httpClient = HttpClient(CIO) {
        Json { serializer = KotlinxSerializer(kotlinx.serialization.json.Json { ignoreUnknownKeys = true }) }
    }
    val notion = Notion.fromEmailAndPassword(
        NotionCredentials("test@test.com", "password"),
        httpClient  // you need to provide your httpClient; JsonFeature must be installed!
    )

    // different instance for serialization 
    // because Notion's JSON is really weird for statically typed parsing
    val json = Json {
        ignoreUnknownKeys = true

        // you need to use the discriminator if you want to serialize the table back to json
        classDiscriminator = "object_type"
    }
    val table: NotionTable? = notion.getCollection(json, pageId = "73ef0bcb09424b00916e6e4f6759e310")

    println(table)
    // Output:
    // NotionTable(
    //    title=Some test table, 
    //    description=With test description, 
    //    rows=[
    //        NotionRow(
    //            properties={
    //                checked=SingleValue(name=checked, type=Checkbox, value=NotionProperty(label=Yes, value=Checkbox(checked=true))), 
    //                value=SingleValue(name=value, type=Text, value=NotionProperty(label=some_value_0, value=Text(text=some_value_0))), 
    //                key=SingleValue(name=key, type=Title, value=NotionProperty(label=some_key_0, value=Title(text=some_key_0)))
    //            }, 
    //            metaInfo=MetaInfo(lastEditedBy=d6a033eb-82bb-49dd-975c-82ccb739f30e, lastEditedTime=1606352460000, createdBy=d6a033eb-82bb-49dd-975c-82ccb739f30e, createdTime=1606350145225)), 
    //        NotionRow(
    //            properties={
    //                key=SingleValue(name=key, type=Title, value=NotionProperty(label=some_key_1, value=Title(text=some_key_1))), 
    //                value=SingleValue(name=value, type=Text, value=NotionProperty(label=some_value_1, value=Text(text=some_value_1)))
    //            }
    //            metaInfo=MetaInfo(lastEditedBy=d6a033eb-82bb-49dd-975c-82ccb739f30e, lastEditedTime=1606350180000, createdBy=d6a033eb-82bb-49dd-975c-82ccb739f30e, createdTime=1606350145225))
    //    ], 
    //    schema={
    //        key=Title(name=key, type=Title)
    //        checked=Checkbox(name=checked, type=Checkbox), 
    //        value=Text(name=value, type=Text)
    //     }
    // )

    // now the json is much more readable!
    println(json.encodeToString(table))

    // works like a charm as well
    println(json.decodeFromString<NotionTable>(json.encodeToString(table)))
    
    // or even better:
    val simpleJson: List<JsonElement>? = table.simpleJsonRows(json)
    
    println(simpleJson)
    // Output:
    // [{"checked":true,"value":"some_value_0","key":"some_key_0"},{"value":"some_value_1","key":"some_key_1"}]
    
    // map to your model:
    val yourModels = simpleJson?.map { json.decodeFromJsonElement<SomeItem>(it) }
    
    println(yourModels)
    // Output:
    // [SomeItem(key=some_key_0, value=some_value_0, checked=true), SomeItem(key=some_key_1, value=some_value_1, checked=false)]
}

@Serializable
data class SomeItem(
    val key: String,
    val value: String,
    val checked: Boolean = false
)
```

Supported types of columns:
- `Title`, `Text`, `Number`, `Checkbox`, `Select`, `MultiSelect`;
- `Person`, `Link`, `File`, `Email`, `PhoneNumber`
- `Date`

Unfortunately, Notion API is too dynamically typed to easy cover all the cases for the column types.
Also, some types, such as `Created by`, don't have the property value, but you can access it from the row meta info.
They added just because otherwise JSON deserialization will fail (they will appear only in row `schema`).

**Note**:
- sometimes Notion can return almost empty response, so just make a request again;
- after several requests, auth endpoint will throw 429, limitations are unknown.

Actually the main purpose of the library is to retrieve the data from the Notion database (collection).
You can only authenticate and read, but not write or listen for the updates.
It uses the private APIs, reverse engineering helped here, so it may sometimes fail, or be even banned when Notion will release their paid APIs.
Unfortunately it is the only way to retrieve any info from Notion so far.
Use it at your own risk. For educational purposes only.