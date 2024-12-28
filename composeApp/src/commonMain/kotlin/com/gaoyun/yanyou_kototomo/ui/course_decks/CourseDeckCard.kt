package com.gaoyun.yanyou_kototomo.ui.course_decks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.course.Course
import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor

@Composable
fun CourseDeckCard(course: Course, deck: CourseDeck, modifier: Modifier = Modifier, toCourse: () -> Unit) {
    val courseTextColor = Color(0xFFEDE1D4)
    val courseCardColor = course.id.courseCardColor()
    ElevatedCard(
        modifier = modifier.fillMaxWidth().platformStyleClickable { toCourse() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
                .background(Color(0x33000000))
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Text(
                text = deck.name.replace(course.courseName, "").trim(),
                color = Color(0xFFEDE1D4),
                style = MaterialTheme.typography.headlineMedium,
            )

            Text(
                text = deck.preview,
                color = courseTextColor.copy(alpha = 0.8f),
                textAlign = TextAlign.Left,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 32.sp,
            )

            Spacer(Modifier.size(6.dp))
        }
    }
}