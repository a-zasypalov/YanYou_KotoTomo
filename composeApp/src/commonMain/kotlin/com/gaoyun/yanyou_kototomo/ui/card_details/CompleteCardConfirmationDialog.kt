package com.gaoyun.yanyou_kototomo.ui.card_details

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.DialogProperties

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
                    Text("Complete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm card completion") },
            text = { Text("After completing the card, review progress will be reset, but you can always start learning it again.") },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        )
    }
}