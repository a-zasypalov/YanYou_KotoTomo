package com.gaoyun.yanyou_kototomo.ui.base

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId

object CourseColors {
    val colors = mapOf(
        "hsk1" to Color(0xFFFBB41E),
        "hsk2" to Color(0xFF1294AB),
        "hsk3" to Color(0xFFF2781A),
        "hsk4" to Color(0xFFB61F26),
        "hsk5" to Color(0xFF193F76),
        "hsk6" to Color(0xFF641F62),
        "genki1" to Color(0xFF0331B3),
        "jlpt5" to Color(0xFF0331B3),
        "jlpt4" to Color(0xFF01A79C),
        "jlpt3" to Color(0xFF90C122),
        "jlpt2" to Color(0xFFEC8A0A),
        "jlpt1" to Color(0xFFE40281),
        "kana" to Color(0xFFEF7794),
        "hiragana" to Color(0xFFEF7794),
        "katakana" to Color(0xFFEF7794)
    )
}

@Composable
fun CourseId.courseCardColor(): Color =
    CourseColors.colors[identifier.removeSuffix("_en")] ?: MaterialTheme.colorScheme.surface

@Composable
fun DeckId.deckColor(): Color =
    CourseColors.colors.entries.find { identifier.contains(it.key) }?.value ?: MaterialTheme.colorScheme.surface
