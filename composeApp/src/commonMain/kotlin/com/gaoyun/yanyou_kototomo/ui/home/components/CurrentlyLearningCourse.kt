package com.gaoyun.yanyou_kototomo.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.data.local.course.CourseWithInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor
import com.gaoyun.yanyou_kototomo.ui.card_details.getCourseMascot
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.learning

@Composable
fun CurrentlyLearningCourse(
    course: CourseWithInfo,
    learningDecks: List<DeckId>,
    decksState: LazyListState,
    onDeckClick: (CourseDeck) -> Unit,
    onCourseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val courseCardColor = course.id.courseCardColor()
    val courseTextColor = Color(0xFFEDE1D4)

    ElevatedCard(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Color(0x33000000)).platformStyleClickable(onClick = onCourseClick),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 8.dp, end = 8.dp)
            ) {
                AutoResizeText(
                    text = course.courseName,
                    color = courseTextColor,
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 1,
                    fontSizeRange = FontSizeRange(min = 24.sp, max = MaterialTheme.typography.displayMedium.fontSize),
                    modifier = Modifier.weight(5f).padding(bottom = 6.dp)
                )
                Image(
                    painter = painterResource(course.id.getCourseMascot()),
                    contentDescription = null,
                    alpha = 0.6f,
                    colorFilter = ColorFilter.tint(courseTextColor),
                    modifier = Modifier.heightIn(max = 56.dp).padding(start = 16.dp).weight(2f)
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = decksState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item { Spacer(Modifier.size(8.dp)) }
                items(course.decks, key = { it.id.identifier }) {
                    CurrentlyLearningCourseDeck(
                        deck = it,
                        courseName = course.courseName,
                        isLearned = learningDecks.contains(it.id),
                        onDeckClick = onDeckClick
                    )
                }
                item { Spacer(Modifier.size(8.dp)) }
            }

            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun CurrentlyLearningCourseDeck(
    deck: CourseDeck,
    courseName: String,
    isLearned: Boolean,
    onDeckClick: (CourseDeck) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.height(80.dp).widthIn(max = 150.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
    ) {
        Box {
            Column(modifier = Modifier.fillMaxSize().platformStyleClickable { onDeckClick(deck) }.padding(vertical = 4.dp)) {
                AutoResizeText(
                    text = deck.name.formatDeckName(courseName),
                    fontSizeRange = FontSizeRange(min = 14.sp, max = MaterialTheme.typography.titleLarge.fontSize),
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp, bottom = 4.dp),
                )
                Divider(height = 1.dp, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp))
                Text(
                    text = deck.preview,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding(horizontal = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (isLearned) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(Res.string.learning),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.size(24.dp).align(Alignment.TopEnd).padding(4.dp)
                )
            }
        }
    }
}

private fun String.formatDeckName(courseName: String): String {
    return if (startsWith(courseName)) {
        removePrefix(courseName)
    } else {
        val firstDelimiterIndex = indexOfAny(charArrayOf(' ', ','))
        if (firstDelimiterIndex != -1) {
            substring(firstDelimiterIndex).trim()
        } else {
            this
        }
    }
}