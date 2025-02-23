package com.gaoyun.yanyou_kototomo.ui.onboarding.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.ui_state.ProgressColor
import com.gaoyun.yanyou_kototomo.domain.toComposeColor

@Composable
fun ProgressColorLegend() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProgressColor.entries.forEach { progressColor ->
            ProgressColorItem(progressColor = progressColor)
        }
    }
}

@Composable
fun ProgressColorItem(progressColor: ProgressColor) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color = progressColor.toComposeColor(), shape = CircleShape)
        )

        Text(
            text = when (progressColor) {
                ProgressColor.Red -> "0-1"
                ProgressColor.Orange -> "2-3"
                ProgressColor.Yellow -> "4-7"
                ProgressColor.Green -> "8-14"
                ProgressColor.Blue -> "15+"
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}