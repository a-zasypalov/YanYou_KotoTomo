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
import com.gaoyun.yanyou_kototomo.data.local.CardId
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.theme.YanYouColors
import com.gaoyun.yanyou_kototomo.ui.player.PlayerCardViewState
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.easy
import yanyou_kototomo.composeapp.generated.resources.good
import yanyou_kototomo.composeapp.generated.resources.hard
import yanyou_kototomo.composeapp.generated.resources.n_days_short
import yanyou_kototomo.composeapp.generated.resources.open_card

@Composable
internal fun BoxScope.SpaceRepetitionButtons(
    currentCardState: PlayerCardViewState,
    onCardOpenClick: () -> Unit,
    onRepetitionClick: (RepetitionAnswer, CardId) -> Unit,
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
                    text = stringResource(Res.string.open_card),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp).padding(horizontal = 24.dp),
                    onClick = onCardOpenClick
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp).padding(horizontal = 24.dp)
                ) {
                    val easyButtonLabel = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) { append(stringResource(Res.string.easy)) }
                        currentCardState.intervalsInDays?.let {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                append("\n")
                                append(stringResource(Res.string.n_days_short, it.easy))
                            }
                        }
                    }

                    val goodButtonLabel = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) { append(stringResource(Res.string.good)) }
                        currentCardState.intervalsInDays?.let {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                append("\n")
                                append(stringResource(Res.string.n_days_short, it.good))
                            }
                        }
                    }

                    val hardButtonLabel = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) { append(stringResource(Res.string.hard)) }
                        currentCardState.intervalsInDays?.let {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                                append("\n")
                                append(stringResource(Res.string.n_days_short, it.hard))
                            }
                        }
                    }

                    PrimaryElevatedButton(
                        text = easyButtonLabel,
                        contentPadding = PaddingValues(8.dp),
                        maxLines = 2,
                        modifier = Modifier.weight(1f),
                        onClick = { currentCardState.card?.card?.id?.let { onRepetitionClick(RepetitionAnswer.Easy, it) } },
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
                        onClick = { currentCardState.card?.card?.id?.let { onRepetitionClick(RepetitionAnswer.Good, it) } },
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
                        onClick = { currentCardState.card?.card?.id?.let { onRepetitionClick(RepetitionAnswer.Hard, it) } },
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