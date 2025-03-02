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

@Composable
internal fun CardPauseButton(paused: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    if (paused) {
        PrimaryElevatedButton(
            text = "Bring back to deck",
            contentPadding = PaddingValues(8.dp),
            onClick = onClick,
            leadingIcon = Icons.AutoMirrored.Default.Undo,
            modifier = modifier
        )
    } else {
        PrimaryElevatedButton(
            text = "Pause card",
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
            text = "Reset card",
            contentPadding = PaddingValues(8.dp),
            onClick = onClick,
            leadingIcon = Icons.AutoMirrored.Default.Undo,
            modifier = modifier
        )
    } else {
        PrimaryElevatedButton(
            text = "Complete card",
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