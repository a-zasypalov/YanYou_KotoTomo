package com.gaoyun.yanyou_kototomo.ui.statistics.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LazyItemScope.SectionDividerShowMore(onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth()) {
        TextButton(onClick = onClick, modifier = Modifier.align(Alignment.CenterEnd)) {
            Text("Show more...")
        }
        Spacer(Modifier.size(8.dp))
    }
}