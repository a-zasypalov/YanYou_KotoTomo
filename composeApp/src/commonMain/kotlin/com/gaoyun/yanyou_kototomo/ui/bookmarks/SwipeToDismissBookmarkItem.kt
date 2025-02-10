package com.gaoyun.yanyou_kototomo.ui.bookmarks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor
import com.gaoyun.yanyou_kototomo.ui.card_details.getCourseMascot
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import sh.calvin.reorderable.ReorderableCollectionItemScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReorderableCollectionItemScope.SwipeToDismissBookmarkItem(
    bookmark: DeckWithCourseInfo,
    onClick: (DeckWithCourseInfo) -> Unit,
    onDelete: () -> Unit,
) {
    val courseTextColor = Color(0xFFEDE1D4)
    val courseCardColor = bookmark.info.courseId.courseCardColor()

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
            text = { Text("Are you sure you want to delete this bookmark?") }
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
            Surface(
                modifier = Modifier.fillMaxWidth().platformStyleClickable { onClick(bookmark) },
                color = courseCardColor
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().background(Color(0x33000000)).padding(horizontal = 8.dp)
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.draggableHandle().weight(1f).align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Rounded.DragHandle, contentDescription = "Reorder")
                    }

                    Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp).weight(8f)) {
                        AutoResizeText(
                            text = bookmark.deck.name,
                            color = courseTextColor,
                            maxLines = 1,
                            fontSizeRange = FontSizeRange(min = 24.sp, max = MaterialTheme.typography.headlineMedium.fontSize),
                            style = MaterialTheme.typography.headlineMedium,
                        )

                        Text(
                            text = bookmark.info.preview,
                            color = courseTextColor.copy(alpha = 0.4f),
                            textAlign = TextAlign.Left,
                            maxLines = 2,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = "Cards: ${bookmark.deck.cards.count()}",
                            color = courseTextColor,
                            style = MaterialTheme.typography.bodyLarge,
                        )

                        Spacer(Modifier.size(6.dp))
                    }

                    Image(
                        painter = painterResource(bookmark.info.courseId.getCourseMascot()),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(courseTextColor),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(1f)
                    )
                }
            }
        }
    )
}