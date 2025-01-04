package com.gaoyun.yanyou_kototomo.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.card.countForReview
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor
import com.gaoyun.yanyou_kototomo.ui.card_details.getCourseMascot
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreenCurrentlyLearningDeck(
    deckWithInfo: DeckWithCourseInfo,
    onCourseClick: (DeckWithCourseInfo) -> Unit,
    onCardDetailsClick: (CardWithProgress<*>, LanguageId) -> Unit,
    onReviewClick: (DeckWithCourseInfo) -> Unit,
    onQuizClick: (DeckWithCourseInfo) -> Unit,
) {
    val courseCardColor = deckWithInfo.info.courseId.courseCardColor()
    val courseTextColor = Color(0xFFEDE1D4)
    val cardsReviewToday = deckWithInfo.deck.cards.count {
        !deckWithInfo.info.pausedCardIds.contains(it.card.id.identifier) && it.progress.countForReview()
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).platformStyleClickable { onCourseClick(deckWithInfo) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(Color(0x33000000)).padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
                    Text(
                        text = deckWithInfo.deck.name,
                        color = courseTextColor,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (cardsReviewToday > 0) FilledTonalButton(
                            onClick = { onReviewClick(deckWithInfo) },
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0x44CCCCCC)),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Review: $cardsReviewToday",
                                color = courseTextColor,
                            )
                        }

                        FilledTonalButton(
                            onClick = { onQuizClick(deckWithInfo) },
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0x44CCCCCC)),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Quiz",
                                color = courseTextColor,
                            )
                        }
                    }

                }
                Image(
                    painter = painterResource(deckWithInfo.info.courseId.getCourseMascot()),
                    contentDescription = null,
                    alpha = 0.6f,
                    colorFilter = ColorFilter.tint(courseTextColor),
                    modifier = Modifier.heightIn(max = 56.dp).padding(end = 16.dp)
                )
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item { Spacer(Modifier.size(8.dp)) }
                items(deckWithInfo.deck.cards) { card ->
                    HomeScreenCharacterCard(
                        card = card,
                        languageId = deckWithInfo.info.learningLanguageId,
                        onClick = onCardDetailsClick
                    )
                }
                item { Spacer(Modifier.size(8.dp)) }
            }
        }
    }
}