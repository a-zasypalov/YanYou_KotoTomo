package com.gaoyun.yanyou_kototomo.ui.deck_overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
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

@Composable
fun DeckOverviewHeader(
    viewState: DeckOverviewState,
    updateTranslationSettings: (Boolean) -> Unit,
    updateTranscriptionSettings: (Boolean) -> Unit,
    updateReadingSettings: (Boolean) -> Unit,
) {
    val deck = viewState.deck

    Column(modifier = Modifier.fillMaxWidth()) {
        AutoResizeText(
            text = deck.name,
            fontSizeRange = FontSizeRange(
                max = MaterialTheme.typography.displayLarge.fontSize,
                min = 24.sp
            ),
            maxLines = 1,
            style = MaterialTheme.typography.displayLarge,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (deck.isKanaDeck() == false) {
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
            if (deck.isJlptDeck() == true) {
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