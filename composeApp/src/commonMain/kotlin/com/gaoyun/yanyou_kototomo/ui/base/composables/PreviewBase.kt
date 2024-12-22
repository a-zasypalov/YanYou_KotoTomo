package com.gaoyun.yanyou_kototomo.ui.base.composables

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.theme.AppTheme
import moe.tlaster.precompose.PreComposeApp

@Composable
fun PreviewBase(content: @Composable () -> Unit) {
    PreComposeApp {
        AppTheme {
            Surface(tonalElevation = 2.dp) {
            }
        }
    }
}