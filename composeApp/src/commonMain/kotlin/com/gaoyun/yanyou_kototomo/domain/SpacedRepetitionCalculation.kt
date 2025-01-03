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
        val proficiencyLevel = 1 //TODO: change for presets
        // Default ease factor, adjusted for HSK level
        val baseEaseFactor = when (proficiencyLevel) {
            in 1..2 -> 1.8f // Frequent reviews for beginners
            in 3..4 -> 2.0f // Balanced for intermediate learners
            else -> 2.2f    // Advanced learners can handle longer intervals
        }
        val easeFactor = easeFactorInput ?: baseEaseFactor

        // Adjust ease factor based on review quality
        val newEaseFactor = when (reviewQuality) {
            RepetitionAnswer.Easy -> easeFactor + interactor.easyAnswerWeight()
            RepetitionAnswer.Good -> easeFactor + interactor.goodAnswerWeight() - 0.05f
            RepetitionAnswer.Hard -> maxOf(easeFactor - interactor.hardAnswerWeight(), 1.5f)
        }

        // Base interval scaling by HSK level
        val intervalBase = when (proficiencyLevel) {
            in 1..2 -> interactor.intervalBase() * 0.8f // Shorter intervals for frequent reviews
            in 3..4 -> interactor.intervalBase()       // Standard intervals
            else -> interactor.intervalBase() * 1.2f   // Longer intervals for advanced learners
        }

        // Adjusted interval calculation
        val intervalDays = when (reviewQuality) {
            RepetitionAnswer.Easy -> maxOf((intervalBase * (1.2 + interactor.easyAnswerWeight()) * newEaseFactor).toInt(), 1)
            RepetitionAnswer.Good -> maxOf((intervalBase * (1.1 + interactor.goodAnswerWeight()) * newEaseFactor).toInt(), 1)
            RepetitionAnswer.Hard -> maxOf((intervalBase * (0.9 - interactor.hardAnswerWeight()) * newEaseFactor).toInt(), 1)
        }

        // Calculate the next review date
        val nextReviewDate = currentReviewDate.plus(intervalDays, DateTimeUnit.DAY)

        return Triple(nextReviewDate, newEaseFactor, intervalDays)
    }
}