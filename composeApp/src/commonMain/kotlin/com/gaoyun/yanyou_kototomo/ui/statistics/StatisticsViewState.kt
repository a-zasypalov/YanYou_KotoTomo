package com.gaoyun.yanyou_kototomo.ui.statistics

import com.gaoyun.yanyou_kototomo.data.local.card.CardSimpleDataEntryWithProgress
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionForStatistic

data class StatisticsViewState(
    val sessions: Set<QuizSessionForStatistic>,
    val cardsProgress: Set<CardSimpleDataEntryWithProgress>,
)