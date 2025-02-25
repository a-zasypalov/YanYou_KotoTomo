package com.gaoyun.yanyou_kototomo.ui.base

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId

@Composable
fun CourseId.courseCardColor(): Color = when (identifier) {
    "hsk1_en" -> Color(0xFFFBB41E)
    "hsk2_en" -> Color(0xFF1294AB)
    "hsk3_en" -> Color(0xFFF2781A)
    "hsk4_en" -> Color(0xFFB61F26)
    "hsk5_en" -> Color(0xFF193F76)
    "hsk6_en" -> Color(0xFF641F62)
    "genki1_en" -> Color(0xFF0331B3)
    "jlpt5_en" -> Color(0xFF0331B3)
    "jlpt4_en" -> Color(0xFF01A79C)
    "jlpt3_en" -> Color(0xFF90C122)
    "jlpt2_en" -> Color(0xFFEC8A0A)
    "jlpt1_en" -> Color(0xFFE40281)
    "kana_en" -> Color(0xFFEF7794)
    else -> MaterialTheme.colorScheme.surface
}

@Composable
fun DeckId.deckColor(): Color = when {
    identifier.contains("hsk1") -> Color(0xFFFBB41E)
    identifier.contains("hsk2") -> Color(0xFF1294AB)
    identifier.contains("hsk3") -> Color(0xFFF2781A)
    identifier.contains("hsk4") -> Color(0xFFB61F26)
    identifier.contains("hsk5") -> Color(0xFF193F76)
    identifier.contains("hsk6") -> Color(0xFF641F62)
    identifier.contains("genki1") -> Color(0xFF0331B3)
    identifier.contains("jlpt5") -> Color(0xFF0331B3)
    identifier.contains("jlpt4") -> Color(0xFF01A79C)
    identifier.contains("jlpt3") -> Color(0xFF90C122)
    identifier.contains("jlpt2") -> Color(0xFFEC8A0A)
    identifier.contains("jlpt1") -> Color(0xFFE40281)
    identifier.contains("hiragana") -> Color(0xFFEF7794)
    identifier.contains("katakana") -> Color(0xFFEF7794)
    else -> MaterialTheme.colorScheme.surface
}

