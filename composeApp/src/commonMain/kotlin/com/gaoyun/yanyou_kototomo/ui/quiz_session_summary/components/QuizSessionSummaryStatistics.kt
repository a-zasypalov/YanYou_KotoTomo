package com.gaoyun.yanyou_kototomo.ui.quiz_session_summary.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.quiz.QuizSessionWithCards
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.theme.YanYouColors
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.number_correct
import yanyou_kototomo.composeapp.generated.resources.number_wrong

@Composable
fun QuizSessionSummaryStatistics(session: QuizSessionWithCards) {
    val correctAnswers = session.results.count { it.isCorrect }
    val wrongAnswers = session.results.size - correctAnswers
    val correctAnswersPercentage = (correctAnswers.toDouble() / session.results.size.toDouble() * 100).toInt()

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Icon(Icons.Default.DataUsage, null, Modifier.size(40.dp).padding(top = 3.dp))

        Text(
            text = "$correctAnswersPercentage%",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.padding(top = 3.dp)) {
            Text(
                text = stringResource(Res.string.number_correct, correctAnswers),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = YanYouColors.current.greenCorrect,
                    fontWeight = FontWeight.Medium
                )
            )

            Divider(
                height = 1.dp,
                modifier = Modifier.width(56.dp).alpha(0.5f).padding(top = 2.dp).align(Alignment.CenterHorizontally)
            )

            Text(
                text = stringResource(Res.string.number_wrong, wrongAnswers),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = YanYouColors.current.redWrong,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}