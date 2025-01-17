package com.gaoyun.yanyou_kototomo.ui.statistics

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
    override val viewState = MutableStateFlow<StatisticsViewState?>(null)

    fun getStatistics() = viewModelScope.launch {
        val sessions = quizInteractor.getSessionsPage(0).sortedByDescending { it.startTime }.toSet()
        val cardsProgress = getCardProgress.getCardProgressPage(0).sortedBy { it.progress.nextReview }.toSet()
        viewState.value = StatisticsViewState(sessions, cardsProgress)
    }
}