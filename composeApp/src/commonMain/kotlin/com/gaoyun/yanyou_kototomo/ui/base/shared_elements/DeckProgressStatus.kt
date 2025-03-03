package com.gaoyun.yanyou_kototomo.ui.base.shared_elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.cards_completed
import yanyou_kototomo.composeapp.generated.resources.cards_paused
import yanyou_kototomo.composeapp.generated.resources.cards_to_review

@Composable
fun DeckProgressStatus(
    toReviewCardsCount: Int,
    completedCardsCount: Int,
    pausedCardsCount: Int,
) {
    Surface(
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            if (toReviewCardsCount > 0) Text(
                text = stringResource(Res.string.cards_to_review, toReviewCardsCount),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            if (completedCardsCount > 0) Text(
                text = stringResource(Res.string.cards_completed, completedCardsCount),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            if (pausedCardsCount > 0) Text(
                text = stringResource(Res.string.cards_paused, pausedCardsCount),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}