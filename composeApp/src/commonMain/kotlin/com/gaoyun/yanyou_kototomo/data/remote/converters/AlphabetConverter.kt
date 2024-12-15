package com.gaoyun.yanyou_kototomo.data.remote.converters

import com.gaoyun.yanyou_kototomo.data.local.AlphabetType

internal fun String.toAlphabet(): AlphabetType? = when (lowercase()) {
    "hiragana" -> AlphabetType.Hiragana
    "katakana" -> AlphabetType.Katakana
    else -> null
}