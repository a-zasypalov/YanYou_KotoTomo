package com.gaoyun.yanyou_kototomo.ui.card_details

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.cancel
import yanyou_kototomo.composeapp.generated.resources.card_completion_dialog_description
import yanyou_kototomo.composeapp.generated.resources.card_completion_dialog_title
import yanyou_kototomo.composeapp.generated.resources.complete

@Composable
fun CompleteCardConfirmationDialog(showDialog: MutableState<Boolean>, onCompleteConfirmed: () -> Unit) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    onCompleteConfirmed()
                }) {
                    Text(stringResource(Res.string.complete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(stringResource(Res.string.cancel))
                }
            },
            title = { Text(stringResource(Res.string.card_completion_dialog_title)) },
            text = { Text(stringResource(Res.string.card_completion_dialog_description)) },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}