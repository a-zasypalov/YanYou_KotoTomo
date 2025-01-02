package com.gaoyun.yanyou_kototomo.ui.settings.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.DialogProperties

@Composable
fun ResetDialog(showDialog: MutableState<Boolean>, onResetConfirmed: () -> Unit) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    onResetConfirmed()
                }) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Reset") },
            text = { Text("Are you sure you want to reset all data in the app? This action cannot be undone.") },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}