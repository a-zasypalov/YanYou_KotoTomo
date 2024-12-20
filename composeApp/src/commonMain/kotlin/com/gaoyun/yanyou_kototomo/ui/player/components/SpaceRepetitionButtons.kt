package com.gaoyun.yanyou_kototomo.ui.player.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.player.PlayerCardViewState

@Composable
internal fun BoxScope.SpaceRepetitionButtons(
    currentCardState: PlayerCardViewState, onCardOpenClick: () -> Unit,
    onNextCardClick: () -> Unit,
    onFinishClick: () -> Unit,
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
                    PrimaryElevatedButton(
                        text = "Hard",
                        modifier = Modifier.weight(1f),
                        onClick = if (!currentCardState.isLast) onNextCardClick else onFinishClick
                    )

                    PrimaryElevatedButton(
                        text = "Good",
                        modifier = Modifier.weight(1f),
                        onClick = if (!currentCardState.isLast) onNextCardClick else onFinishClick
                    )

                    PrimaryElevatedButton(
                        text = "Easy",
                        modifier = Modifier.weight(1f),
                        onClick = if (!currentCardState.isLast) onNextCardClick else onFinishClick
                    )
                }
            }
        }
    }
}