package com.gaoyun.yanyou_kototomo.ui.onboarding.pages

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.AlphabetType
import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton

@Composable
fun OnboardingCardShowcasePage(onNextPage: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                text = "Flexible cards",
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


            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedCard(
                    modifier = Modifier.weight(0.75f),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(4.dp)) {
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
                    text = "You can see reading, transcription and translation on the card",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1.25f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedCard(
                    modifier = Modifier.weight(0.75f),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(card.front, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold))
                        Text(
                            text = "[${card.transcription()}]",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(4.dp),
                        )
                    }
                }
                Text(
                    text = "Or any combination of them you want",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1.25f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedCard(
                    modifier = Modifier.weight(0.75f),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(card.front, style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold))
                    }
                }
                Text(
                    text = "Good for everyday practice",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1.25f)
                )
            }

            Divider(height = 1.dp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedIconToggleButton(
                    checked = true,
                    onCheckedChange = {},
                ) {
                    Icon(Icons.Default.Translate, null)
                }

                OutlinedIconToggleButton(
                    checked = true,
                    onCheckedChange = {},
                ) {
                    Icon(Icons.Default.Subtitles, null)
                }

                OutlinedIconToggleButton(
                    checked = true,
                    onCheckedChange = {},
                ) {
                    Icon(Icons.Default.ViewColumn, null)
                }

                Text(
                    text = "Control deck settings using these buttons.\nSettings saved per deck.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                )
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