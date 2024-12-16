package com.gaoyun.yanyou_kototomo.ui.home

import com.gaoyun.yanyou_kototomo.data.local.AlphabetType
import com.gaoyun.yanyou_kototomo.data.local.CourseDeck
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class HomeViewModel(
    private val getCoursesRoot: GetCoursesRoot,
    private val getDeck: GetDeck
) : BaseViewModel() {

    override val viewState = MutableStateFlow<RootStructure?>(null)

    fun getRootComponent() = viewModelScope.launch {
        val result = getCoursesRoot.getCourses()
        viewState.value = result
    }

    fun getDeckCn() = viewModelScope.launch {
        val result = getDeck(
            learningLanguage = LanguageId("cn"),
            sourceLanguage = LanguageId("en"),
            deck = CourseDeck.Normal(
                id = DeckId("hsk1_1_en"),
                name = "HSK1 Chapter 1",
                version = 1
            ),
            requiredDecks = listOf()
        )
//        viewState.value = result.toString()
    }

    fun getDeckKana() = viewModelScope.launch {
        val result = getDeck(
            learningLanguage = LanguageId("jp"),
            sourceLanguage = LanguageId("en"),
            deck = CourseDeck.Alphabet(
                id = DeckId("kana_en"),
                name = "Hiragana",
                alphabet = AlphabetType.Hiragana,
                version = 1,
            ),
            requiredDecks = listOf()
        )
//        viewState.value = result.toString()
    }

    fun getDeckJlpt() = viewModelScope.launch {
        val result = getDeck(
            learningLanguage = LanguageId("jp"),
            sourceLanguage = LanguageId("en"),
            deck = CourseDeck.Normal(
                id = DeckId("jlpt5_1_en"),
                name = "JLPT 5 Chapter 1",
                version = 1,
            ),
            requiredDecks = listOf(DeckId("kana_en"))
        )
//        viewState.value = result.toString()
    }

}