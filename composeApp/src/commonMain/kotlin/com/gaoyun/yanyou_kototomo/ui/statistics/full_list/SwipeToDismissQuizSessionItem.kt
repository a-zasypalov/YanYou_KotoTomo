package com.gaoyun.yanyou_kototomo.ui.statistics.full_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionForStatistic
import com.gaoyun.yanyou_kototomo.ui.statistics.components.QuizSessionStatisticsItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissQuizSessionItem(session: QuizSessionForStatistic, onDelete: () -> Unit, addDivider: Boolean) {
    val scope = rememberCoroutineScope()
    var showDialog = remember { mutableStateOf(false) } // Track if dialog is showing
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                // Show the confirmation dialog before proceeding with the delete
                showDialog.value = true
                false // Do not delete immediately, wait for confirmation
            } else {
                false
            }
        }
    )

    // Show confirmation dialog
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                scope.launch {
                    showDialog.value = false
                    dismissState.reset() // Reset swipe state when dialog is dismissed
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            onDelete()
                            showDialog.value = false
                        }
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            showDialog.value = false
                            dismissState.reset() // Reset swipe state on cancel
                        }
                    }
                ) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this session?") }
        )
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        backgroundContent = { // Swipe background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        },
        content = { // Original content
            QuizSessionStatisticsItem(
                session = session,
                addDivider = addDivider,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )
        }
    )
}