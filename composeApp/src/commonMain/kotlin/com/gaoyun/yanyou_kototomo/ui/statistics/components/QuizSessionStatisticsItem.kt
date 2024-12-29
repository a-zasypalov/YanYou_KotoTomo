package com.gaoyun.yanyou_kototomo.ui.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionForStatistic
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.theme.YanYouColors
import com.gaoyun.yanyou_kototomo.util.formatDateTimeStatistics

@Composable
fun QuizSessionStatisticsItem(session: QuizSessionForStatistic, addDivider: Boolean, modifier: Modifier = Modifier) {
    val correctAnswers = session.results.count { it.isCorrect }
    val wrongAnswers = session.results.size - correctAnswers
    val correctAnswersPercentage = (correctAnswers.toDouble() / session.results.size.toDouble() * 100).toInt()
    val sessionCharacters = buildAnnotatedString {
        session.results.forEachIndexed { index, result ->
            withStyle(
                style = SpanStyle(
                    color = if (result.isCorrect) YanYouColors.current.greenCorrect else YanYouColors.current.redWrong,
                )
            ) {
                append(result.card.character)
                if (index != session.results.lastIndex) append(", ")
            }
        }
    }
    val summaryString = buildAnnotatedString {
        append("$correctAnswersPercentage%: ")
        withStyle(SpanStyle(color = YanYouColors.current.greenCorrect)) { append("$correctAnswers") }
        append(" / ")
        withStyle(SpanStyle(color = YanYouColors.current.redWrong)) { append("$wrongAnswers") }
    }


    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.DataUsage, "", Modifier.size(16.dp))
            Text(
                text = summaryString,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = session.startTime.formatDateTimeStatistics(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Text(
            text = session.deckNames.joinToString(", "),
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            text = sessionCharacters,
            style = MaterialTheme.typography.bodyLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )

        if (addDivider) Divider(height = 1.dp)
    }
}