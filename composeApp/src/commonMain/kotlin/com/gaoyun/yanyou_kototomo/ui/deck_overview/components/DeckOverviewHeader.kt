package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.deck_overview.DeckOverviewState

@Composable
fun DeckOverviewHeader(
    viewState: DeckOverviewState,
    updateTranslationSettings: (Boolean) -> Unit,
    updateTranscriptionSettings: (Boolean) -> Unit,
    updateReadingSettings: (Boolean) -> Unit,
    updateBookmarkedState: (Boolean) -> Unit,
    updateLearnedState: (Boolean) -> Unit,
) {

    Column(modifier = Modifier.fillMaxWidth()) {
        AutoResizeText(
            text = viewState.deckName,
            fontSizeRange = FontSizeRange(
                max = MaterialTheme.typography.displayLarge.fontSize,
                min = 24.sp
            ),
            maxLines = 1,
            style = MaterialTheme.typography.displayLarge,
        )

        if (viewState.newCards.size() != viewState.allCards.size) {
            Surface(
                tonalElevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    val toReview = viewState.newCards.words.size + viewState.newCards.phrases.size + viewState.cardsToReview.size
                    if (toReview > 0) Text(
                        text = "$toReview to review",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                    if (viewState.completedCards.isNotEmpty()) Text(
                        text = " ${viewState.completedCards.size} completed",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                    if (viewState.pausedCards.isNotEmpty()) Text(
                        text = "${viewState.pausedCards.size} paused",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            LearnButton(isLearned = viewState.isCurrentlyLearned, onClick = { updateLearnedState(!viewState.isCurrentlyLearned) })

            OutlinedIconToggleButton(
                checked = viewState.isBookmarked,
                onCheckedChange = updateBookmarkedState,
            ) {
                if (viewState.isBookmarked) {
                    Icon(Icons.Default.Bookmark, null)
                } else {
                    Icon(Icons.Default.BookmarkBorder, null)
                }
            }


            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.height(32.dp).width(1.dp).background(MaterialTheme.colorScheme.onSurface))
            Spacer(modifier = Modifier.weight(1f))

            if (viewState.deckId.isKanaDeck() == false) {

                OutlinedIconToggleButton(
                    checked = viewState.settings.showTranslation,
                    onCheckedChange = updateTranslationSettings,
                ) {
                    Icon(Icons.Default.Translate, null)
                }
            }

            OutlinedIconToggleButton(
                checked = viewState.settings.showTranscription,
                onCheckedChange = updateTranscriptionSettings,
            ) {
                Icon(Icons.Default.Subtitles, null)
            }
            if (viewState.deckId.isJlptDeck() == true) {
                OutlinedIconToggleButton(
                    checked = viewState.settings.showReading,
                    onCheckedChange = updateReadingSettings,
                ) {
                    Icon(Icons.Default.ViewColumn, null)
                }
            }
        }
    }
}

@Composable
fun LearnButton(
    isLearned: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isLearned) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
            contentColor = if (isLearned) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isLearned) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if (isLearned) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null
            )
            Text(
                text = if (isLearned) "Learning" else "Learn"
            )
        }
    }
}