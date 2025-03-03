package com.gaoyun.yanyou_kototomo.ui.courses

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.course.Course
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor
import com.gaoyun.yanyou_kototomo.ui.card_details.getCourseMascot
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.decks_in_course_number

@Composable
fun CourseCard(course: Course, toCourse: () -> Unit) {
    val courseTextColor = Color(0xFFEDE1D4)
    val courseCardColor = course.id.courseCardColor()
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().platformStyleClickable { toCourse() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().background(Color(0x33000000))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
                Text(
                    text = course.courseName,
                    color = courseTextColor,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = stringResource(Res.string.decks_in_course_number, course.decks.count()),
                    color = courseTextColor,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Image(
                    painter = painterResource(course.id.getCourseMascot()),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(courseTextColor),
                    modifier = Modifier.size(24.dp).padding(top = 8.dp)
                )
                Spacer(Modifier.size(6.dp))
            }
            Text(
                text = course.preview,
                color = courseTextColor.copy(alpha = 0.4f),
                textAlign = TextAlign.Left,
                maxLines = 3,
                lineHeight = 32.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
                    .padding(vertical = 4.dp)
                    .offset(x = 16.dp)
            )
        }
    }
}