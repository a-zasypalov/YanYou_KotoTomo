package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.ui.player.components.RepetitionAnswer
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class SpacedRepetitionCalculation {

    fun calculateNextInterval(
        currentReviewDate: LocalDate,
        easeFactorInput: Double?,
        reviewQuality: RepetitionAnswer,
    ): Triple<LocalDate, Double, Int> {
        // Default ease factor if not provided
        val easeFactor = easeFactorInput ?: 2.5

        // Calculate the new ease factor based on the review quality
        val newEaseFactor = when (reviewQuality) {
            RepetitionAnswer.Easy -> easeFactor + 0.2  // Increase ease factor for "Easy"
            RepetitionAnswer.Good -> easeFactor        // Keep it the same for "Good"
            RepetitionAnswer.Hard -> maxOf(easeFactor - 0.2, 1.3)  // Decrease ease factor for "Hard"
        }

        // Base interval starting at 1 day, increasing with review quality
        val intervalBase = 2

        // Calculate the interval in days based on review quality
        val intervalDays = when (reviewQuality) {
            RepetitionAnswer.Easy -> (intervalBase * 1.4 * newEaseFactor).toInt() // Gradually increase interval for "Easy"
            RepetitionAnswer.Good -> (intervalBase * 1.2 * newEaseFactor).toInt() // Gradually increase interval for "Good"
            RepetitionAnswer.Hard -> maxOf((intervalBase * 0.8).toInt(), 1)      // Slightly reduce interval for "Hard"
        }

        // Calculate the next review date
        val nextReviewDate = currentReviewDate.plus(intervalDays, DateTimeUnit.DAY)

        return Triple(nextReviewDate, newEaseFactor, intervalDays)
    }
}