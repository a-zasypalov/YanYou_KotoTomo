package com.gaoyun.yanyou_kototomo.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor
import com.gaoyun.yanyou_kototomo.ui.card_details.getCourseMascot
import com.gaoyun.yanyou_kototomo.util.localDateNow
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreenCurrentlyLearningDeck(deckWithInfo: DeckWithCourseInfo, onCourseClick: (DeckWithCourseInfo) -> Unit) {
    val courseCardColor = deckWithInfo.info.courseId.courseCardColor()
    val courseTextColor = Color(0xFFEDE1D4)
    val cardsReviewToday = deckWithInfo.deck.cards.count { it.progress?.nextReview == localDateNow() }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).platformStyleClickable { onCourseClick(deckWithInfo) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().background(Color(0x33000000)).padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
                Text(
                    text = deckWithInfo.deck.name,
                    color = courseTextColor,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Review: $cardsReviewToday",
                        color = courseTextColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = "Quiz",
                        color = courseTextColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

            }
            Image(
                painter = painterResource(deckWithInfo.info.courseId.getCourseMascot()),
                contentDescription = null,
                alpha = 0.6f,
                colorFilter = ColorFilter.tint(courseTextColor),
                modifier = Modifier.heightIn(max = 48.dp).padding(end = 16.dp)
            )
        }
    }
}