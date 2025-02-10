package com.gaoyun.yanyou_kototomo.ui.bookmarks

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeck
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookmarksScreen(
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel<BookmarksViewModel>()

    LaunchedEffect(Unit) {
        viewModel.getBookmarksState()
    }

    SurfaceScaffold {
        BookmarksScreenContent(
            viewState = viewModel.viewState.collectAsState().value,
            onRemoveBookmark = viewModel::removeBookmark,
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
) {
    val state = rememberLazyListState()

}