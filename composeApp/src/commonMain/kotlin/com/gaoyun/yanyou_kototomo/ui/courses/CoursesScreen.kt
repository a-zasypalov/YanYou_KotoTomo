package com.gaoyun.yanyou_kototomo.ui.courses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gaoyun.yanyou_kototomo.ui.base.PreviewBase
import com.gaoyun.yanyou_kototomo.ui.home.HomeViewModel
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CoursesScreen() {
    val viewModel = koinViewModel(vmClass = HomeViewModel::class)
    val state = viewModel.viewState.collectAsState()

    CoursesScreenContent()
}

@Composable
private fun CoursesScreenContent() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Courses")
    }
}

@Preview
@Composable
fun CoursesScreenContentPreview() {
    PreviewBase {
        CoursesScreenContent()
    }
}