package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.ui.player.SpacedRepetitionIntervalsInDays
import com.gaoyun.yanyou_kototomo.ui.player.components.RepetitionAnswer
import com.gaoyun.yanyou_kototomo.util.localDateNow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class SpacedRepetitionCalculation(
    private val interactor: SpacialRepetitionSettingsInteractor,
) {
    companion object {
        const val EASY_INTERVAL_BASE = 1.2
        const val GOOD_INTERVAL_BASE = 1.1
        const val HARD_INTERVAL_BASE = 0.9

        const val GOOD_K = 0.05f
        const val HARD_K = 1.5f

    }

    // Default ease factor, adjusted for HSK level
    val baseEaseFactor = 1.8f
    val intervalBase = interactor.intervalBase() * 0.8f

    fun calculateNextIntervals(
        currentReviewDate: LocalDate?,
        easeFactorInput: Float?,
    ): SpacedRepetitionIntervalsInDays {
        val reviewDate = currentReviewDate ?: localDateNow()
        val easy = calculateNextInterval(reviewDate, easeFactorInput, RepetitionAnswer.Easy).third
        val good = calculateNextInterval(reviewDate, easeFactorInput, RepetitionAnswer.Good).third
        val hard = calculateNextInterval(reviewDate, easeFactorInput, RepetitionAnswer.Hard).third
        return SpacedRepetitionIntervalsInDays(easy = easy, good = good, hard = hard)
    }

    fun calculateNextIntervalsFull(
        currentReviewDate: LocalDate?,
        easeFactorInput: Float?,
    ): Triple<Triple<LocalDate, Float, Int>, Triple<LocalDate, Float, Int>, Triple<LocalDate, Float, Int>> {
        val reviewDate = currentReviewDate ?: localDateNow()
        val easy = calculateNextInterval(reviewDate, easeFactorInput, RepetitionAnswer.Easy)
        val good = calculateNextInterval(reviewDate, easeFactorInput, RepetitionAnswer.Good)
        val hard = calculateNextInterval(reviewDate, easeFactorInput, RepetitionAnswer.Hard)
        return Triple(easy, good, hard)
    }

    fun calculateNextInterval(
        currentReviewDate: LocalDate,
        easeFactorInput: Float?,
        reviewQuality: RepetitionAnswer,
    ): Triple<LocalDate, Float, Int> {
        val easeFactor = easeFactorInput ?: baseEaseFactor

        // Adjust ease factor based on review quality
        val newEaseFactor = when (reviewQuality) {
            RepetitionAnswer.Easy -> easeFactor + interactor.easyAnswerWeight()
            RepetitionAnswer.Good -> easeFactor + interactor.goodAnswerWeight() - GOOD_K
            RepetitionAnswer.Hard -> maxOf(easeFactor - interactor.hardAnswerWeight(), HARD_K)
        }

        // Adjusted interval calculation
        val intervalDays = when (reviewQuality) {
            RepetitionAnswer.Easy -> maxOf((intervalBase * (EASY_INTERVAL_BASE + interactor.easyAnswerWeight()) * newEaseFactor).toInt(), 1)
            RepetitionAnswer.Good -> maxOf((intervalBase * (GOOD_INTERVAL_BASE + interactor.goodAnswerWeight()) * newEaseFactor).toInt(), 1)
            RepetitionAnswer.Hard -> maxOf((intervalBase * (HARD_INTERVAL_BASE - interactor.hardAnswerWeight()) * newEaseFactor).toInt(), 1)
        }

        // Calculate the next review date
        val nextReviewDate = currentReviewDate.plus(intervalDays, DateTimeUnit.DAY)

        return Triple(nextReviewDate, newEaseFactor, intervalDays)
    }
}