package com.gaoyun.yanyou_kototomo.ui.player.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.theme.YanYouColors
import com.gaoyun.yanyou_kototomo.ui.player.PlayerCardViewState

@Composable
internal fun BoxScope.SpaceRepetitionButtons(
    currentCardState: PlayerCardViewState,
    onCardOpenClick: () -> Unit,
    onRepetitionClick: (RepetitionAnswer) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
    ) {
        AnimatedContent(
            targetState = currentCardState.answerOpened,
            transitionSpec = {
                if (targetState) {
                    slideInVertically(animationSpec = tween(300)) { it } +
                            fadeIn(animationSpec = tween(300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                } else {
                    fadeIn(
                        animationSpec = tween(200, delayMillis = 300)
                    ) togetherWith fadeOut(animationSpec = tween(300))
                }
            },
            label = "Answer Opened Transition",
        ) { isAnswerOpened ->
            if (!isAnswerOpened) {
                PrimaryElevatedButton(
                    text = "Open card",
                    modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp).padding(horizontal = 24.dp),
                    onClick = onCardOpenClick
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp).padding(horizontal = 24.dp)
                ) {
                    val easyButtonLabel = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) { append("Easy") }
                        currentCardState.intervalsInDays?.let {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) { append("\n${it.easy}d") }
                        }
                    }

                    val goodButtonLabel = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) { append("Good") }
                        currentCardState.intervalsInDays?.let {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) { append("\n${it.good}d") }
                        }
                    }

                    val hardButtonLabel = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) { append("Hard") }
                        currentCardState.intervalsInDays?.let {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) { append("\n${it.hard}d") }
                        }
                    }

                    PrimaryElevatedButton(
                        text = easyButtonLabel,
                        contentPadding = PaddingValues(8.dp),
                        maxLines = 2,
                        modifier = Modifier.weight(1f),
                        onClick = { onRepetitionClick(RepetitionAnswer.Easy) },
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
                        onClick = { onRepetitionClick(RepetitionAnswer.Good) },
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
                        onClick = { onRepetitionClick(RepetitionAnswer.Hard) },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = YanYouColors.current.redButtonContainer,
                            contentColor = YanYouColors.current.onButtonContainer,
                        )
                    )
                }
            }
        }
    }
}