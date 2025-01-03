package com.gaoyun.yanyou_kototomo.ui.settings.sections

import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.PreferencesKeys
import com.gaoyun.yanyou_kototomo.domain.SpacedRepetitionCalculation
import com.gaoyun.yanyou_kototomo.domain.SpacialRepetitionSettingsInteractor
import com.gaoyun.yanyou_kototomo.domain.SpacialRepetitionSettingsInteractor.Companion.DEFAULT_EASE_FACTOR
import com.gaoyun.yanyou_kototomo.ui.base.BaseViewModel
import com.gaoyun.yanyou_kototomo.ui.player.components.RepetitionAnswer
import com.gaoyun.yanyou_kototomo.util.AppIcon
import com.gaoyun.yanyou_kototomo.util.AppTheme
import com.gaoyun.yanyou_kototomo.util.ThemeChanger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDate

class SettingsSectionsViewModel(
    private val themeChanger: ThemeChanger,
    private val preferences: Preferences,
    private val spacialRepetitionSettingsInteractor: SpacialRepetitionSettingsInteractor,
    private val spacedRepetitionCalculation: SpacedRepetitionCalculation,
) : BaseViewModel() {
    override val viewState = MutableStateFlow<SettingsSectionsViewState?>(null)

    // Define the ranges for each slider
    private val easeFactorRange = listOf(1.6f, 1.8f, 2.2f, 2.6f, 3.0f)
    private val intervalBaseRange = listOf(
        0.8f,
        1.2f,
        1.5f,
        1.8f,
        2.0f
    ) //for levels 1-2, 3-4:listOf(1.0f, 1.5f, 2.0f, 2.5f, 3.0f), more: listOf(1.2f, 1.8f, 2.5f, 3.0f, 3.5f)
    private val easyAnswerWeightRange = listOf(0.05f, 0.1f, 0.2f, 0.4f, 0.6f)
    private val goodAnswerWeightRange = listOf(0.05f, 0.1f, 0.15f, 0.25f, 0.4f)
    private val hardAnswerWeightRange = listOf(0.1f, 0.2f, 0.3f, 0.5f, 0.7f)

    fun getSettings() {
        val easeFactor = spacialRepetitionSettingsInteractor.easeFactor()
        val intervalBase = spacialRepetitionSettingsInteractor.intervalBase()
        val easyAnswerWeight = spacialRepetitionSettingsInteractor.easyAnswerWeight()
        val goodAnswerWeight = spacialRepetitionSettingsInteractor.goodAnswerWeight()
        val hardAnswerWeight = spacialRepetitionSettingsInteractor.hardAnswerWeight()

        fun findSliderPosition(value: Float, range: List<Float>): Int {
            return range.indexOfFirst { it == value } + 1
        }

        val slidersPosition = listOf(
            findSliderPosition(easeFactor, easeFactorRange),
            findSliderPosition(intervalBase, intervalBaseRange),
            findSliderPosition(easyAnswerWeight, easyAnswerWeightRange),
            findSliderPosition(goodAnswerWeight, goodAnswerWeightRange),
            findSliderPosition(hardAnswerWeight, hardAnswerWeightRange)
        )

        val intervals = spacedRepetitionCalculation.calculateNextIntervalsFull(null, null)

        viewState.value = SettingsSectionsViewState(
            slidersPosition = slidersPosition,
            currentIntervals = intervals
        )
    }


    fun setAppIcon(icon: AppIcon) {
        themeChanger.activateIcon(icon)
    }

    fun setAppTheme(theme: AppTheme) {
        preferences.setString(PreferencesKeys.COLOR_THEME, theme.name)
        themeChanger.applyTheme()
    }

    fun setRepetitionSettings(values: List<Int>) {
        fun mapValue(index: Int, mapping: List<Float>): Float {
            return mapping[values[index] - 1]
        }

        // Adjust slider ranges based on HSK level
        val easeFactor = mapValue(0, easeFactorRange)
        val intervalBase = mapValue(1, intervalBaseRange)
        val easyAnswerWeight = mapValue(2, easyAnswerWeightRange)
        val goodAnswerWeight = mapValue(3, goodAnswerWeightRange)
        val hardAnswerWeight = mapValue(4, hardAnswerWeightRange)

        spacialRepetitionSettingsInteractor.setEaseFactor(easeFactor)
        spacialRepetitionSettingsInteractor.setIntervalBase(intervalBase)
        spacialRepetitionSettingsInteractor.setEasyAnswerWeight(easyAnswerWeight)
        spacialRepetitionSettingsInteractor.setGoodAnswerWeight(goodAnswerWeight)
        spacialRepetitionSettingsInteractor.setHardAnswerWeight(hardAnswerWeight)

        viewState.value = viewState.value?.copy(
            currentIntervals = spacedRepetitionCalculation.calculateNextIntervalsFull(null, null),
            slidersPosition = values
        )
    }

    fun onSampleRepetitionClick(answer: RepetitionAnswer) {
        val easeFactor = when (answer) {
            RepetitionAnswer.Easy -> viewState.value?.currentIntervals?.first?.second ?: DEFAULT_EASE_FACTOR
            RepetitionAnswer.Good -> viewState.value?.currentIntervals?.second?.second ?: DEFAULT_EASE_FACTOR
            RepetitionAnswer.Hard -> viewState.value?.currentIntervals?.third?.second ?: DEFAULT_EASE_FACTOR
        }

        viewState.value = viewState.value?.copy(
            currentIntervals = spacedRepetitionCalculation.calculateNextIntervalsFull(null, easeFactor),
        )
    }
}

data class SettingsSectionsViewState(
    val slidersPosition: List<Int>,
    val currentIntervals: Triple<Triple<LocalDate, Float, Int>, Triple<LocalDate, Float, Int>, Triple<LocalDate, Float, Int>>,
)