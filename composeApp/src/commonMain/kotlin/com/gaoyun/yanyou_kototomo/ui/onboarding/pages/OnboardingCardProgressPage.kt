package com.gaoyun.yanyou_kototomo.ui.onboarding.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.AlphabetType
import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.domain.mapIntervalToColor
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import org.jetbrains.compose.resources.pluralStringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.current_interval_days

@Composable
fun OnboardingCardProgressPage(onNextPage: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Progress indicators",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.weight(1f))

            val card = Card.KanjiCard(
                id = CardId.WordCardId("jlpt1_1_en_5"),
                front = "本",
                translation = "Book, Origin",
                reading = Card.KanjiCard.Reading(
                    on = listOf(
                        Card.KanaCard(
                            id = CardId.AlphabetCardId("hiragana16"),
                            front = "た",
                            transcription = "ta",
                            alphabet = AlphabetType.Hiragana,
                            set = Card.KanaCard.determineKanaSet("た"),
                            mirror = Card.KanaCard.Mirror(
                                id = CardId.AlphabetCardId("katakana16"),
                                front = "タ"
                            )
                        )
                    ),
                    kun = listOf(
                        Card.KanaCard(
                            id = CardId.AlphabetCardId("hiragana22"),
                            front = "に",
                            transcription = "ni",
                            set = Card.KanaCard.determineKanaSet("に"),
                            alphabet = AlphabetType.Hiragana,
                            mirror = Card.KanaCard.Mirror(
                                id = CardId.AlphabetCardId("katakana22"),
                                front = "ニ"
                            )
                        )
                    )
                ),
                additionalInfo = "Common Kanji meaning 'book' or 'origin'.",
                speechPart = "noun"
            )
            val interval = 1
            val nextReviewDate = "Today"

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedCard(
                    modifier = Modifier.weight(0.75f),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(4.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(end = 6.dp, top = 6.dp)
                        ) {
                            val color = mapIntervalToColor(interval)
                            Text(
                                text = nextReviewDate,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.EventRepeat,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.size(10.dp)
                            )
                            Box(modifier = Modifier.size(6.dp).background(color = color, shape = CircleShape))
                        }
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = card.reading.on.map { it.front }.joinToString("\n"),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp)
                            )
                            Text(card.front, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold))
                            Text(
                                text = card.reading.kun.map { it.front }.joinToString("\n"),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp)
                            )
                        }
                        Text(
                            text = "[${card.transcription()}]",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(4.dp),
                        )

                        Divider(1.dp, Modifier.padding(vertical = 2.dp))
                        Text(
                            text = card.translation,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(2.dp),
                        )
                    }
                }
                Text(
                    text = "Indicators help to understand your progress",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1.25f)
                )
            }

            Divider(height = 1.dp, modifier = Modifier.padding(vertical = 16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp)) {
                Icon(
                    imageVector = Icons.Default.EventRepeat,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Text(
                    text = "Review $nextReviewDate",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Next review date",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(4f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp)) {
                Box(modifier = Modifier.size(20.dp).background(color = mapIntervalToColor(interval), shape = CircleShape))

                Text(
                    text = pluralStringResource(Res.plurals.current_interval_days, interval, interval),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Color indicator of current progress based on interval",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(4f)
                )
            }


            Surface(tonalElevation = 8.dp, modifier = Modifier.fillMaxWidth().padding(top = 16.dp), shape = MaterialTheme.shapes.large) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(
                        text = "Interval indicators (in days)",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    ProgressColorLegend()
                }
            }

            Spacer(modifier = Modifier.weight(1.75f))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            PrimaryElevatedButton(
                text = "Start learning",
                onClick = onNextPage,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}