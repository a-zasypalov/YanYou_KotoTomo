package com.gaoyun.yanyou_kototomo.ui.quiz_session_summary.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizCardResult
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import org.jetbrains.compose.resources.painterResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.correct
import yanyou_kototomo.composeapp.generated.resources.wrong

@Composable
fun QuizSessionSummaryQuestionResult(index: Int, lastIndex: Int, result: QuizCardResult) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = result.card.front,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Medium),
                    maxLines = 1,
                )
                Text(
                    text = result.card.translationOrTranscription("– "),
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f).padding(end = 36.dp)
                )
            }

            if (result.isCorrect) {
                Image(
                    painter = painterResource(Res.drawable.correct),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp).align(Alignment.CenterEnd)
                )
            } else {
                Image(
                    painter = painterResource(Res.drawable.wrong),
                    contentDescription = "",
                    modifier = Modifier.size(32.dp).padding(4.dp).align(Alignment.CenterEnd)
                )
            }
        }

        if (index != lastIndex) {
            Divider(height = 1.dp)
        } else {
            Spacer(Modifier.size(32.dp))
        }

    }
}
