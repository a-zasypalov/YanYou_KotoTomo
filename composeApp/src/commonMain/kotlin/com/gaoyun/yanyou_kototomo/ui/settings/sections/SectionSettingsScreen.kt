package com.gaoyun.yanyou_kototomo.ui.settings.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.SettingsSections
import com.gaoyun.yanyou_kototomo.ui.base.navigation.SettingsSectionsArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.StatisticsModeArgs
import com.gaoyun.yanyou_kototomo.ui.player.SpacedRepetitionIntervalsInDays
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SectionSettingsScreen(
    args: SettingsSectionsArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val section = args.section
    val viewModel = koinViewModel<SettingsSectionsViewModel>()

    LaunchedEffect(Unit) {
        viewModel.getSettings()
    }

    SurfaceScaffold(
        backHandler = { navigate(BackNavigationEffect) },
    ) {
        when (section) {
            SettingsSections.AppIcon -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    AutoResizeText(
                        text = section.title(),
                        fontSizeRange = FontSizeRange(min = 16.sp, max = MaterialTheme.typography.displayLarge.fontSize),
                        style = MaterialTheme.typography.displayLarge,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(align = Alignment.CenterVertically)
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                    AppIconSettingScreenContent(viewModel::setAppIcon)
                }
            }

            SettingsSections.ColorTheme -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    AutoResizeText(
                        text = section.title(),
                        fontSizeRange = FontSizeRange(min = 16.sp, max = MaterialTheme.typography.displayLarge.fontSize),
                        style = MaterialTheme.typography.displayLarge,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(align = Alignment.CenterVertically)
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                    ColorThemeSettingScreenContent(viewModel::setAppTheme)
                }
            }

            SettingsSections.AboutApp -> AboutAppSettingScreenContent()
            SettingsSections.SpacialRepetition -> {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp),
                ) {
                    AutoResizeText(
                        text = section.title(),
                        fontSizeRange = FontSizeRange(min = 16.sp, max = MaterialTheme.typography.displayLarge.fontSize),
                        style = MaterialTheme.typography.displayLarge,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(align = Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                    viewModel.viewState.collectAsState().value?.let {
                        SpacedRepetitionSettingScreenContent(
                            currentRepetitionValues = it.currentIntervals.let {
                                SpacedRepetitionIntervalsInDays(
                                    easy = it.first.third,
                                    good = it.second.third,
                                    hard = it.third.third
                                )
                            },
                            onSliderValuesChanged = viewModel::setRepetitionSettings,
                            onSampleButtonClick = viewModel::onSampleRepetitionClick,
                            sliderPositions = it.slidersPosition
                        )
                    }
                    Spacer(modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

private fun SettingsSections.title() = when (this) {
    SettingsSections.AppIcon -> "App icon"
    SettingsSections.ColorTheme -> "Color theme"
    SettingsSections.AboutApp -> "About app"
    SettingsSections.SpacialRepetition -> "Spacial repetition"
}