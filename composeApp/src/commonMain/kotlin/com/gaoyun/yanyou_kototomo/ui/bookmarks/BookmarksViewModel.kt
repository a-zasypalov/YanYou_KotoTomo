package com.gaoyun.yanyou_kototomo.ui.bookmarks

import androidx.lifecycle.viewModelScope
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.course.CourseDeck
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.domain.BookmarksInteractor
import com.gaoyun.yanyou_kototomo.domain.GetBookmarksState
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BookmarksViewModel(
    private val getBookmarksState: GetBookmarksState,
    private val bookmarksInteractor: BookmarksInteractor,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<List<DeckWithCourseInfo>>(listOf())
    val bookmarksState = MutableStateFlow(listOf<CourseDeck>())

    fun getBookmarksState() = viewModelScope.launch {
        bookmarksState.value = bookmarksInteractor.getBookmarkedDecks()
        viewState.value = getBookmarksState.get()
    }

    fun removeBookmark(deckId: DeckId) {
        bookmarksState.value = bookmarksState.value.filter { it.id != deckId }
        bookmarksInteractor.saveBookmarkedDecks(bookmarksState.value)
        viewState.value = viewState.value.filter { it.deck.id != deckId }
    }

    fun onReorder(from: Int, to: Int) = viewModelScope.launch {
        bookmarksState.value = bookmarksState.value.toMutableList().apply { add(to, removeAt(from)) }
        bookmarksInteractor.saveBookmarkedDecks(bookmarksState.value)
        viewState.value = viewState.value.toMutableList().apply { add(to, removeAt(from)) }
    }
}