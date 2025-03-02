package com.gaoyun.yanyou_kototomo.ui.base.composables

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.theme.AppTheme
import com.gaoyun.yanyou_kototomo.ui.base.theme.colors.OriginalColors

@Composable
fun PreviewBase(content: @Composable () -> Unit) {
    AppTheme(OriginalColors.lightScheme) {
        Surface(tonalElevation = 2.dp) {
            content()
        }
    }
}