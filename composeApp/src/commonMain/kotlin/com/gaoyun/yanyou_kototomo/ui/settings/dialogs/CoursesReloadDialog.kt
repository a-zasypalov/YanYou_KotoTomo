package com.gaoyun.yanyou_kototomo.ui.settings.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.cancel
import yanyou_kototomo.composeapp.generated.resources.reload
import yanyou_kototomo.composeapp.generated.resources.reload_dialog_description
import yanyou_kototomo.composeapp.generated.resources.reload_dialog_title

@Composable
fun CoursesReloadDialog(showDialog: MutableState<Boolean>, onConfirmed: () -> Unit) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    onConfirmed()
                }) {
                    Text(stringResource(Res.string.reload))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(stringResource(Res.string.cancel))
                }
            },
            title = { Text(stringResource(Res.string.reload_dialog_title)) },
            text = { Text(stringResource(Res.string.reload_dialog_description)) },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}