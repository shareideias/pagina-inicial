package com.shareinstituto.utils

private val compressRegex = Regex("\\s+")

fun String.compressSpaces(): String {
    return replace(compressRegex, " ").trim()
}

fun String.limit(size: Int): String {
    return if (length <= size) this else substring(0, lastIndexOf(' ', size)).trim() + "..."
}
