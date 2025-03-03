package com.gaoyun.yanyou_kototomo.ui.bookmarks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeck
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.bookmarks

@Composable
fun BookmarksScreen(
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel<BookmarksViewModel>()

    LaunchedEffect(Unit) {
        viewModel.getBookmarksState()
    }

    SurfaceScaffold(backHandler = { navigate(BackNavigationEffect) }) {
        BookmarksScreenContent(
            viewState = viewModel.viewState.collectAsState().value,
            onRemoveBookmark = viewModel::removeBookmark,
            onReorder = viewModel::onReorder,
            onBookmarkClick = {
                navigate(
                    ToDeck(
                        DeckScreenArgs(
                            learningLanguageId = it.info.learningLanguageId,
                            sourceLanguageId = it.info.sourceLanguageId,
                            courseId = it.info.courseId,
                            deckId = it.deck.id
                        )
                    )
                )
            }
        )
    }
}

@Composable
fun BookmarksScreenContent(
    viewState: List<DeckWithCourseInfo>,
    onRemoveBookmark: (DeckId) -> Unit,
    onBookmarkClick: (DeckWithCourseInfo) -> Unit,
    onReorder: (Int, Int) -> Unit,
) {
    val titleItems = 1
    val listState = rememberLazyListState()
    val animatedBookmarks = remember { mutableStateListOf<DeckWithCourseInfo>() }

    val reorderableLazyListState = rememberReorderableLazyListState(listState) { from, to ->
        animatedBookmarks.apply {
            add(to.index - titleItems, removeAt(from.index - titleItems))
            onReorder(from.index - titleItems, to.index - titleItems)
        }
    }

    LaunchedEffect(viewState) {
        val toAdd = viewState.filterNot { animatedBookmarks.contains(it) }
        val toRemove = animatedBookmarks.filterNot { viewState.contains(it) }
        animatedBookmarks.addAll(toAdd)
        animatedBookmarks.removeAll(toRemove)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(Res.string.bookmarks),
                style = MaterialTheme.typography.displayLarge,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 16.dp).padding(horizontal = 16.dp)
            )
        }

        itemsIndexed(animatedBookmarks, key = { index, bookmark -> bookmark.deck.id.identifier }) { index, bookmark ->
            ReorderableItem(reorderableLazyListState, key = bookmark.deck.id.identifier) { isDragging ->
                AnimatedVisibility(
                    visible = true, // Handles fade-out when removed
                    exit = fadeOut() + shrinkVertically(),
                    modifier = Modifier.animateItem() // Smooth reordering
                ) {
                    SwipeToDismissBookmarkItem(
                        bookmark = bookmark,
                        onClick = { onBookmarkClick(it) },
                        onDelete = { onRemoveBookmark(bookmark.deck.id) }
                    )
                }
            }
        }


        item { Spacer(Modifier.navigationBarsPadding().size(32.dp)) }
    }
}