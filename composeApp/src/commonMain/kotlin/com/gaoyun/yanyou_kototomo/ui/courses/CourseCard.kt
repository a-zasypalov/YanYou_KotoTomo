package com.gaoyun.yanyou_kototomo.ui.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.Course
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable

@Composable
fun CourseCard(course: Course, toCourse: () -> Unit) {
    val courseTextColor = Color(0xFFEDE1D4)
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().platformStyleClickable { toCourse() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = course.courseCardColor())
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(Color(0x44000000))) {
            Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
                Text(
                    text = course.courseName,
                    color = Color(0xFFEDE1D4),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = "Decks in course: ${course.decks.count()}",
                    color = Color(0xFFEDE1D4),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(Modifier.size(6.dp))
            }
            Text(
                text = splitStringEqually(course.preview, 3),
                color = courseTextColor.copy(alpha = 0.3f),
                maxLines = 3,
                lineHeight = 32.sp,
                modifier = Modifier.width(180.dp).align(Alignment.CenterEnd).offset(x = 32.dp).rotate(-25f)
            )
        }
    }
}

@Composable
private fun Course.courseCardColor(): Color = when (this.id.identifier) {
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

fun splitStringEqually(input: String, n: Int): String {
    val chunkSize = input.length / n
    val remainder = input.length % n
    return (0 until n).joinToString("\n") {
        input.substring(it * chunkSize + minOf(it, remainder), (it + 1) * chunkSize + minOf(it + 1, remainder))
    }
}