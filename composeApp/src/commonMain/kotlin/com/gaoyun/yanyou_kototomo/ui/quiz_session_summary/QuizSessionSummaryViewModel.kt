package com.gaoyun.yanyou_kototomo.ui.quiz_session_summary

import com.gaoyun.yanyou_kototomo.data.local.QuizSessionWithCards
import com.gaoyun.yanyou_kototomo.domain.QuizInteractor
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.ui.base.navigation.QuizSessionSummaryArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class QuizSessionSummaryViewModel(
    private val quizInteractor: QuizInteractor,
) : BaseViewModel() {

    override val viewState = MutableStateFlow<QuizSessionSummaryViewState?>(null)

    fun getSession(args: QuizSessionSummaryArgs) = viewModelScope.launch {
        val session = quizInteractor.getQuizSession(args)
        viewState.value = QuizSessionSummaryViewState(session)
    }
}

data class QuizSessionSummaryViewState(val session: QuizSessionWithCards?)