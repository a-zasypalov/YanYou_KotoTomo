package com.gaoyun.yanyou_kototomo.ui.settings.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.DialogProperties

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
                    Text("Reload")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Reload") },
            text = { Text("Are you sure you want to reload all courses and decks? This might take some time") },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}