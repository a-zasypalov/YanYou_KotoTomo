package com.gaoyun.yanyou_kototomo.ui.player.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.player.PlayerCardViewState

@Composable
internal fun BoxScope.QuizButtons(
    currentCardState: PlayerCardViewState,
    onAnswerClick: (String) -> Unit,
    onNextCardClick: () -> Unit,
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
                    fadeIn(animationSpec = tween(200, delayMillis = 300)) togetherWith
                            fadeOut(animationSpec = tween(300))
                }
            },
            label = "Answer Opened Transition",
        ) { isAnswerOpened ->
            if (!isAnswerOpened) {
                val cellsNumber = 2
                LazyVerticalGrid(
                    columns = GridCells.Fixed(cellsNumber),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    items(currentCardState.possibleAnswers) { answer ->
                        PrimaryElevatedButton(
                            text = answer,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onAnswerClick(answer) }
                        )
                    }
                    item(span = { GridItemSpan(cellsNumber) }) {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            } else {
                Column {
                    Spacer(modifier = Modifier.height(56.dp))
                    PrimaryElevatedButton(
                        text = "Next card",
                        modifier = Modifier.fillMaxWidth().padding(bottom = 64.dp).padding(horizontal = 24.dp),
                        onClick = onNextCardClick
                    )
                }
            }
        }
    }
}