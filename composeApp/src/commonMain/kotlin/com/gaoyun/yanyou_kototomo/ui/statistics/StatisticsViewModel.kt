package com.gaoyun.yanyou_kototomo.ui.statistics

import com.gaoyun.yanyou_kototomo.data.local.CardSimpleDataEntryWithProgress
import com.gaoyun.yanyou_kototomo.data.local.QuizSessionWithSimpleDataEntryCards
import com.gaoyun.yanyou_kototomo.domain.GetCardProgress
import com.gaoyun.yanyou_kototomo.domain.QuizInteractor
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class StatisticsViewModel(
    private val quizInteractor: QuizInteractor,
    private val getCardProgress: GetCardProgress,
) : BaseViewModel() {
    private val quizPage = MutableStateFlow(0)
    private val cardsProgressPage = MutableStateFlow(0)

    override val viewState = MutableStateFlow<StatisticsViewState?>(null)

    fun getStatistics() = viewModelScope.launch {
        val sessions = quizInteractor.getSessionsPage(quizPage.value)
        val cardsProgress = getCardProgress.getCardProgressPage(cardsProgressPage.value)
        viewState.value = StatisticsViewState(sessions, cardsProgress)
    }
}

data class StatisticsViewState(
    val sessions: List<QuizSessionWithSimpleDataEntryCards>,
    val cardsProgress: List<CardSimpleDataEntryWithProgress>,
)