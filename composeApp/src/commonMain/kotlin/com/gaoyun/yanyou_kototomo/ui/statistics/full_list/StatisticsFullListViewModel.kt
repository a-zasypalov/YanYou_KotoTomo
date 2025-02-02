package com.gaoyun.yanyou_kototomo.ui.statistics.full_list

import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionId
import com.gaoyun.yanyou_kototomo.domain.GetCardProgress
import com.gaoyun.yanyou_kototomo.domain.QuizInteractor
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import com.gaoyun.yanyou_kototomo.repository.QuizSessionRepository
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.ui.statistics.StatisticsViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class StatisticsFullListViewModel(
    private val quizInteractor: QuizInteractor,
    private val getCardProgress: GetCardProgress,
) : BaseViewModel() {
    private var page = 0

    override val viewState = MutableStateFlow<StatisticsViewState>(StatisticsViewState(setOf(), setOf()))
    val canRequestNewPage = MutableStateFlow(true)

    fun nextPage(mode: StatisticsListMode) = viewModelScope.launch {
        when (mode) {
            StatisticsListMode.Cards -> {
                val cardsProgress = getCardProgress.getCardProgressPage(page).sortedBy { it.progress.nextReview }.take(10)
                if (cardsProgress.size < CardsAndProgressRepository.PAGE_SIZE) {
                    canRequestNewPage.value = false
                }
                viewState.value = viewState.value.copy(cardsProgress = viewState.value.cardsProgress + cardsProgress)
            }

            StatisticsListMode.Quizzes -> {
                val sessions = quizInteractor.getSessionsPage(page).sortedByDescending { it.startTime }.take(5)
                if (sessions.size < QuizSessionRepository.PAGE_SIZE) {
                    canRequestNewPage.value = false
                }
                viewState.value = viewState.value.copy(sessions = viewState.value.sessions + sessions)
            }
        }
        page++
    }

    fun onSessionDelete(id: QuizSessionId) = viewModelScope.launch {
        quizInteractor.deleteSession(id)
        viewState.value = viewState.value.copy(
            sessions = viewState.value.sessions.filterNot { it.sessionId == id }.toSet()
        )
    }
}