package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerMode

@Composable
fun BoxScope.DeckOverviewActionButtons(dueCards: Int, onPlayDeckClick: (PlayerMode) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(bottom = 8.dp)
            .padding(horizontal = 24.dp)
    ) {
        if (dueCards > 0) {
            PrimaryElevatedButton(
                text = "Review: $dueCards",
                leadingIcon = Icons.Outlined.LocalLibrary,
                onClick = { onPlayDeckClick(PlayerMode.SpacialRepetition) },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier.weight(1f),
            )
        }

        PrimaryElevatedButton(
            text = "Quiz",
            leadingIcon = Icons.Outlined.Quiz,
            onClick = { onPlayDeckClick(PlayerMode.Quiz) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        )
    }
}