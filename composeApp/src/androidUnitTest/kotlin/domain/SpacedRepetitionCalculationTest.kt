package domain

import com.gaoyun.yanyou_kototomo.domain.SpacedRepetitionCalculation
import com.gaoyun.yanyou_kototomo.domain.SpacialRepetitionSettingsInteractor
import com.gaoyun.yanyou_kototomo.ui.player.components.RepetitionAnswer
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.junit.jupiter.api.Test

class SpacedRepetitionCalculationTest {

    private val mockInteractor = mockk<SpacialRepetitionSettingsInteractor>(relaxed = true) {
        every { intervalBase() } returns SpacialRepetitionSettingsInteractor.DEFAULT_INTERVAL_BASE // 1.5f
        every { easyAnswerWeight() } returns SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT
        every { goodAnswerWeight() } returns SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT
        every { hardAnswerWeight() } returns SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT
    }
    private val spacedRepetitionCalculation = SpacedRepetitionCalculation(mockInteractor)
    val intervalBase = spacedRepetitionCalculation.intervalBase

    @Test
    fun `calculateNextInterval should return correct values for Easy answer`() {
        // Arrange
        val currentReviewDate = LocalDate(2023, 10, 1)
        val easeFactorInput = SpacialRepetitionSettingsInteractor.DEFAULT_EASE_FACTOR // 2.0f
        every { mockInteractor.easyAnswerWeight() } returns SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT // 0.2f

        // Act
        val result = spacedRepetitionCalculation.calculateNextInterval(
            currentReviewDate,
            easeFactorInput,
            RepetitionAnswer.Easy
        )

        // Assert
        val expectedEaseFactor = easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT
        val expectedIntervalDays = (intervalBase *
                (SpacedRepetitionCalculation.EASY_INTERVAL_BASE + SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT) *
                expectedEaseFactor).toInt()
        val expectedDate = currentReviewDate.plus(expectedIntervalDays, DateTimeUnit.DAY)

        result.first shouldBe expectedDate
        result.second shouldBe expectedEaseFactor
        result.third shouldBe expectedIntervalDays
    }

    @Test
    fun `calculateNextInterval should return correct values for Good answer`() {
        // Arrange
        val currentReviewDate = LocalDate(2023, 10, 1)
        val easeFactorInput = SpacialRepetitionSettingsInteractor.DEFAULT_EASE_FACTOR
        every { mockInteractor.goodAnswerWeight() } returns SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT

        // Act
        val result = spacedRepetitionCalculation.calculateNextInterval(
            currentReviewDate,
            easeFactorInput,
            RepetitionAnswer.Good
        )

        // Assert
        val expectedDate = currentReviewDate.plus(
            (intervalBase * (SpacedRepetitionCalculation.GOOD_INTERVAL_BASE + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT) * (easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT - SpacedRepetitionCalculation.GOOD_K)).toInt(),
            DateTimeUnit.DAY
        )
        val expectedEaseFactor =
            easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT - SpacedRepetitionCalculation.GOOD_K
        val expectedIntervalDays = maxOf(
            (intervalBase * (SpacedRepetitionCalculation.GOOD_INTERVAL_BASE + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT) * (easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT - SpacedRepetitionCalculation.GOOD_K)).toInt(),
            1
        )

        result.first shouldBe expectedDate
        result.second shouldBe expectedEaseFactor
        result.third shouldBe expectedIntervalDays
    }

    @Test
    fun `calculateNextInterval should return correct values for Hard answer`() {
        // Arrange
        val currentReviewDate = LocalDate(2023, 10, 1)
        val easeFactorInput = SpacialRepetitionSettingsInteractor.DEFAULT_EASE_FACTOR
        every { mockInteractor.hardAnswerWeight() } returns SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT

        // Act
        val result = spacedRepetitionCalculation.calculateNextInterval(
            currentReviewDate,
            easeFactorInput,
            RepetitionAnswer.Hard
        )

        // Assert
        val expectedDate = currentReviewDate.plus(
            (intervalBase * (SpacedRepetitionCalculation.HARD_INTERVAL_BASE - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT) * maxOf(
                easeFactorInput - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT,
                SpacedRepetitionCalculation.HARD_K
            )).toInt(), DateTimeUnit.DAY
        )
        val expectedEaseFactor =
            maxOf(easeFactorInput - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT, SpacedRepetitionCalculation.HARD_K)
        val expectedIntervalDays = maxOf(
            (intervalBase * (SpacedRepetitionCalculation.HARD_INTERVAL_BASE - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT) * maxOf(
                easeFactorInput - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT,
                SpacedRepetitionCalculation.HARD_K
            )).toInt(), 1
        )

        result.first shouldBe expectedDate
        result.second shouldBe expectedEaseFactor
        result.third shouldBe expectedIntervalDays
    }

    @Test
    fun `calculateNextIntervals should return correct intervals for all answer types`() {
        // Arrange
        val currentReviewDate = LocalDate(2023, 10, 1)
        val easeFactorInput = SpacialRepetitionSettingsInteractor.DEFAULT_EASE_FACTOR

        // Act
        val result = spacedRepetitionCalculation.calculateNextIntervals(currentReviewDate, easeFactorInput)

        // Assert
        val easyInterval = maxOf(
            (intervalBase * (SpacedRepetitionCalculation.EASY_INTERVAL_BASE + SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT) * (easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT)).toInt(),
            1
        )
        val goodInterval = maxOf(
            (intervalBase * (SpacedRepetitionCalculation.GOOD_INTERVAL_BASE + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT) * (easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT - SpacedRepetitionCalculation.GOOD_K)).toInt(),
            1
        )
        val hardInterval = maxOf(
            (intervalBase * (SpacedRepetitionCalculation.HARD_INTERVAL_BASE - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT) * maxOf(
                easeFactorInput - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT,
                SpacedRepetitionCalculation.HARD_K
            )).toInt(), 1
        )

        result.easy shouldBe easyInterval
        result.good shouldBe goodInterval
        result.hard shouldBe hardInterval
    }

    @Test
    fun `calculateNextIntervalsFull should return correct full intervals for all answer types`() {
        // Arrange
        val currentReviewDate = LocalDate(2023, 10, 1)
        val easeFactorInput = SpacialRepetitionSettingsInteractor.DEFAULT_EASE_FACTOR

        // Act
        val result = spacedRepetitionCalculation.calculateNextIntervalsFull(currentReviewDate, easeFactorInput)

        // Assert
        val easyDate = currentReviewDate.plus(
            (intervalBase * (SpacedRepetitionCalculation.EASY_INTERVAL_BASE + SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT) * (easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT)).toInt(),
            DateTimeUnit.DAY
        )
        val easyEaseFactor = easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT
        val easyIntervalDays = maxOf(
            (intervalBase * (SpacedRepetitionCalculation.EASY_INTERVAL_BASE + SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT) * (easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_EASY_ANSWER_WEIGHT)).toInt(),
            1
        )

        val goodDate = currentReviewDate.plus(
            (intervalBase * (SpacedRepetitionCalculation.GOOD_INTERVAL_BASE + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT) * (easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT - SpacedRepetitionCalculation.GOOD_K)).toInt(),
            DateTimeUnit.DAY
        )
        val goodEaseFactor =
            easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT - SpacedRepetitionCalculation.GOOD_K
        val goodIntervalDays = maxOf(
            (intervalBase * (SpacedRepetitionCalculation.GOOD_INTERVAL_BASE + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT) * (easeFactorInput + SpacialRepetitionSettingsInteractor.DEFAULT_GOOD_ANSWER_WEIGHT - SpacedRepetitionCalculation.GOOD_K)).toInt(),
            1
        )

        val hardDate = currentReviewDate.plus(
            (intervalBase * (SpacedRepetitionCalculation.HARD_INTERVAL_BASE - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT) * maxOf(
                easeFactorInput - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT,
                SpacedRepetitionCalculation.HARD_K
            )).toInt(), DateTimeUnit.DAY
        )
        val hardEaseFactor =
            maxOf(easeFactorInput - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT, SpacedRepetitionCalculation.HARD_K)
        val hardIntervalDays = maxOf(
            (intervalBase * (SpacedRepetitionCalculation.HARD_INTERVAL_BASE - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT) * maxOf(
                easeFactorInput - SpacialRepetitionSettingsInteractor.DEFAULT_HARD_ANSWER_WEIGHT,
                SpacedRepetitionCalculation.HARD_K
            )).toInt(), 1
        )

        result.first shouldBe Triple(easyDate, easyEaseFactor, easyIntervalDays)
        result.second shouldBe Triple(goodDate, goodEaseFactor, goodIntervalDays)
        result.third shouldBe Triple(hardDate, hardEaseFactor, hardIntervalDays)
    }
}