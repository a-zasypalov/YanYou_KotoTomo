package com.gaoyun.yanyou_kototomo.ui.deck_overview.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DeckOptionsMenu(
    isKanaDeck: Boolean,
    onResetDeck: () -> Unit,
    onEditDeck: () -> Unit,
) {
    var expanded = remember { mutableStateOf(false) }
    var showDialog = remember { mutableStateOf(false) }

    Box {
        Row {
            if (!isKanaDeck) {
                IconButton(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = onEditDeck
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "edit")
                }
            }
            IconButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { expanded.value = true }
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "options")
            }
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            DropdownMenuItem(
                text = { Text("Reset Deck") },
                onClick = {
                    expanded.value = false
                    showDialog.value = true
                }
            )
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Reset Deck") },
            text = { Text("Are you sure you want to reset the deck? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        onResetDeck()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}