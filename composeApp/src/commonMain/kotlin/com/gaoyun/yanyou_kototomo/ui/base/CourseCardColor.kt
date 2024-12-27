package com.gaoyun.yanyou_kototomo.ui.base

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gaoyun.yanyou_kototomo.data.local.course.Course

@Composable
fun Course.courseCardColor(): Color = when (this.id.identifier) {
    "hsk1_en" -> Color(0xFFFBB41E)
    "hsk2_en" -> Color(0xFF1294AB)
    "hsk3_en" -> Color(0xFFF2781A)
    "hsk4_en" -> Color(0xFFB61F26)
    "hsk5_en" -> Color(0xFF193F76)
    "hsk6_en" -> Color(0xFF641F62)
    "jlpt5_en" -> Color(0xFF0331B3)
    "jlpt4_en" -> Color(0xFF01A79C)
    "jlpt3_en" -> Color(0xFF90C122)
    "jlpt2_en" -> Color(0xFFEC8A0A)
    "jlpt1_en" -> Color(0xFFE40281)
    "kana_en" -> Color(0xFFEF7794)
    else -> MaterialTheme.colorScheme.surface
}