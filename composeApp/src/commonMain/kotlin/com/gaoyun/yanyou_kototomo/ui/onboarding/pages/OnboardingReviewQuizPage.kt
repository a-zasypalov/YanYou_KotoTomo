package com.gaoyun.yanyou_kototomo.ui.onboarding.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton

@Composable
fun OnboardingReviewQuizPage(onNextPage: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Review & Quiz",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryElevatedButton(
                text = "Review: 3",
                leadingIcon = Icons.Outlined.LocalLibrary,
                onClick = { },
            )

            Text(
                text = "Review cards when it is time for it. Spacial repetition algorithm will take care of repeating cards when it's needed",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.size(32.dp))
            Divider(height = 1.dp)
            Spacer(modifier = Modifier.size(32.dp))

            PrimaryElevatedButton(
                text = "Quiz",
                leadingIcon = Icons.Outlined.Quiz,
                onClick = { },
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            )

            Text(
                text = "You can play quiz with the deck even if you don't have any cards to review now.\nYou can find all quizzes results on Statics page",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )


            Spacer(modifier = Modifier.weight(1.5f))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            PrimaryElevatedButton(
                text = "Next",
                onClick = onNextPage,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.size(16.dp))
        }
    }
}