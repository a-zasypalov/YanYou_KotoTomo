package com.gaoyun.yanyou_kototomo.ui.settings.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.theme.YanYouColors
import com.gaoyun.yanyou_kototomo.ui.player.SpacedRepetitionIntervalsInDays
import com.gaoyun.yanyou_kototomo.ui.player.components.RepetitionAnswer
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.ease_factor
import yanyou_kototomo.composeapp.generated.resources.easy
import yanyou_kototomo.composeapp.generated.resources.easy_answer_weight
import yanyou_kototomo.composeapp.generated.resources.good
import yanyou_kototomo.composeapp.generated.resources.good_answer_weight
import yanyou_kototomo.composeapp.generated.resources.hard
import yanyou_kototomo.composeapp.generated.resources.hard_answer_weight
import yanyou_kototomo.composeapp.generated.resources.interval_base
import yanyou_kototomo.composeapp.generated.resources.n_days_short
import yanyou_kototomo.composeapp.generated.resources.reset

@Composable
fun ColumnScope.SpacedRepetitionSettingScreenContent(
    sliderPositions: List<Int>,
    onSliderValuesChanged: (List<Int>) -> Unit,
    onSampleButtonClick: (RepetitionAnswer) -> Unit,
    currentRepetitionValues: SpacedRepetitionIntervalsInDays,
) {

    SampleButtons(currentRepetitionValues, onSampleButtonClick)

    Spacer(modifier = Modifier.size(32.dp))

    // Render sliders
    for (i in sliderPositions.indices) {
        SliderWithSteps(
            currentValue = sliderPositions[i],
            onValueChange = { newValue ->
                onSliderValuesChanged(sliderPositions.toMutableList().also { it[i] = newValue })
            },

            title = when (i) {
                0 -> stringResource(Res.string.ease_factor)
                1 -> stringResource(Res.string.interval_base)
                2 -> stringResource(Res.string.easy_answer_weight)
                3 -> stringResource(Res.string.good_answer_weight)
                4 -> stringResource(Res.string.hard_answer_weight)
                else -> ""
            }
        )
    }

    Spacer(modifier = Modifier.size(32.dp))

    // Reset button
    Button(
        onClick = {
            onSliderValuesChanged(listOf(3, 3, 3, 3, 3))
        },
        modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
        Text(text = stringResource(Res.string.reset))
    }
}

@Composable
private fun SliderWithSteps(
    currentValue: Int,
    onValueChange: (Int) -> Unit,
    title: String,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        Slider(
            value = currentValue.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 1f..5f,
            steps = 3, // 5 steps mean 3 intermediate steps between 1 and 5
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        Divider(1.dp, modifier = Modifier.padding(bottom = 16.dp))
    }
}

@Composable
internal fun SampleButtons(
    intervalsInDays: SpacedRepetitionIntervalsInDays,
    onSampleButtonClick: (RepetitionAnswer) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shadowElevation = 8.dp,
        tonalElevation = 16.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxWidth().padding(16.dp)
        ) {
            val easyButtonLabel = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(stringResource(Res.string.easy))
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append("\n")
                    append(stringResource(Res.string.n_days_short, intervalsInDays.easy))
                }
            }

            val goodButtonLabel = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(stringResource(Res.string.good))
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append("\n")
                    append(stringResource(Res.string.n_days_short, intervalsInDays.good))
                }
            }

            val hardButtonLabel = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                    append(stringResource(Res.string.hard))
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append("\n")
                    append(stringResource(Res.string.n_days_short, intervalsInDays.hard))
                }
            }

            PrimaryElevatedButton(
                text = easyButtonLabel,
                contentPadding = PaddingValues(8.dp),
                maxLines = 2,
                modifier = Modifier.weight(1f),
                onClick = { onSampleButtonClick(RepetitionAnswer.Easy) },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = YanYouColors.current.greenButtonContainer,
                    contentColor = YanYouColors.current.onButtonContainer,
                )
            )

            PrimaryElevatedButton(
                text = goodButtonLabel,
                contentPadding = PaddingValues(8.dp),
                maxLines = 2,
                modifier = Modifier.weight(1f),
                onClick = { onSampleButtonClick(RepetitionAnswer.Good) },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = YanYouColors.current.blueButtonContainer,
                    contentColor = YanYouColors.current.onButtonContainer,
                )
            )

            PrimaryElevatedButton(
                text = hardButtonLabel,
                contentPadding = PaddingValues(8.dp),
                maxLines = 2,
                modifier = Modifier.weight(1f),
                onClick = { onSampleButtonClick(RepetitionAnswer.Hard) },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = YanYouColors.current.redButtonContainer,
                    contentColor = YanYouColors.current.onButtonContainer,
                )
            )
        }
    }
}