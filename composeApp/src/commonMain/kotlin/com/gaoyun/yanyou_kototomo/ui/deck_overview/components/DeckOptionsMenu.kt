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
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.cancel
import yanyou_kototomo.composeapp.generated.resources.confirm
import yanyou_kototomo.composeapp.generated.resources.edit
import yanyou_kototomo.composeapp.generated.resources.options
import yanyou_kototomo.composeapp.generated.resources.reset_deck_dialog_description
import yanyou_kototomo.composeapp.generated.resources.reset_deck_dialog_title

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
                    Icon(Icons.Default.Edit, contentDescription = stringResource(Res.string.edit))
                }
            }
            IconButton(
                modifier = Modifier.padding(horizontal = 4.dp),
                onClick = { expanded.value = true }
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = stringResource(Res.string.options))
            }
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.reset_deck_dialog_title)) },
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
            title = { Text(stringResource(Res.string.reset_deck_dialog_title)) },
            text = { Text(stringResource(Res.string.reset_deck_dialog_description)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog.value = false
                        onResetDeck()
                    }
                ) {
                    Text(stringResource(Res.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        )
    }
}