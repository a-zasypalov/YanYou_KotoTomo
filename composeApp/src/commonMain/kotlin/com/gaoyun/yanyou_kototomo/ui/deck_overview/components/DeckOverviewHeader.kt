package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.ViewColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckProgressStatus
import com.gaoyun.yanyou_kototomo.ui.deck_overview.DeckOverviewState

@Composable
fun DeckOverviewHeader(
    viewState: DeckOverviewState,
    updateTranslationSettings: (Boolean) -> Unit,
    updateTranscriptionSettings: (Boolean) -> Unit,
    updateReadingSettings: (Boolean) -> Unit,
    updateBookmarkedState: (Boolean) -> Unit,
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
            DeckProgressStatus(
                toReviewCardsCount = viewState.newCards.words.size + viewState.newCards.phrases.size + viewState.cardsToReview.size,
                completedCardsCount = viewState.completedCards.size,
                pausedCardsCount = viewState.pausedCards.size
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
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