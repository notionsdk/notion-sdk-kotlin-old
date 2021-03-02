package com.petersamokhin.notionapi.utils

private const val DASH_ID_LENGTH_VALID = 36
private const val DASH_ID_CLEAN_LENGTH_VALID = 32

/**
 * In the page URL, it's ID does not contain any hyphens,
 * but in requests it does.
 *
 * This extension implements the correct logic to convert
 * the URL page ID to the one for the API requests.
 */
public fun String.dashifyId(): String {
    if (isValidDashId()) return this

    val clean = replace("-", "")

    if (clean.length != DASH_ID_CLEAN_LENGTH_VALID)
        throw IllegalArgumentException("Incorrect id format: $this")

    val chars = clean.toCharArray()
    val resultChars = CharArray(DASH_ID_LENGTH_VALID)

    resultChars[8] = '-'
    resultChars[13] = '-'
    resultChars[18] = '-'
    resultChars[23] = '-'

    System.arraycopy(chars, 0, resultChars, 0, 8)
    System.arraycopy(chars, 8, resultChars, 9, 4)
    System.arraycopy(chars, 12, resultChars, 14, 4)
    System.arraycopy(chars, 16, resultChars, 19, 4)
    System.arraycopy(chars, 20, resultChars, 24, 12)

    return String(resultChars)
}

public fun String.isValidDashId(): Boolean {
    if (length != DASH_ID_LENGTH_VALID) return false
    return toCharArray().all { c -> c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F' || c == '-' }
}

/**
 * Text fields look like this in the responses:
 *
 * ```json
 * [ ["foo", "bar"] ]
 * ```
 */
public fun <T> List<List<T>>.trimNotionTextField(): String =
    flatten().joinToString("")