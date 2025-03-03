package com.gaoyun.yanyou_kototomo.ui.card_details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.bring_back_to_deck
import yanyou_kototomo.composeapp.generated.resources.complete_card
import yanyou_kototomo.composeapp.generated.resources.pause_card
import yanyou_kototomo.composeapp.generated.resources.reset_card

@Composable
internal fun CardPauseButton(paused: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    if (paused) {
        PrimaryElevatedButton(
            text = stringResource(Res.string.bring_back_to_deck),
            contentPadding = PaddingValues(8.dp),
            onClick = onClick,
            leadingIcon = Icons.AutoMirrored.Default.Undo,
            modifier = modifier
        )
    } else {
        PrimaryElevatedButton(
            text = stringResource(Res.string.pause_card),
            contentPadding = PaddingValues(8.dp),
            onClick = onClick,
            leadingIcon = Icons.Default.Pause,
            modifier = modifier
        )
    }
}

@Composable
internal fun CardCompleteButton(completed: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    if (completed) {
        PrimaryElevatedButton(
            text = stringResource(Res.string.reset_card),
            contentPadding = PaddingValues(8.dp),
            onClick = onClick,
            leadingIcon = Icons.AutoMirrored.Default.Undo,
            modifier = modifier
        )
    } else {
        PrimaryElevatedButton(
            text = stringResource(Res.string.complete_card),
            onClick = onClick,
            contentPadding = PaddingValues(8.dp),
            leadingIcon = Icons.Default.Check,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
            modifier = modifier
        )
    }
}