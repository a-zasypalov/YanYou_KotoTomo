package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gaoyun.yanyou_kototomo.ui.base.PreviewBase
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel(vmClass = HomeViewModel::class)

    val state = viewModel.viewState.collectAsState()
    HomeScreenContent(
        content = state.value,
        getRootComponent = viewModel::getRootComponent,
        getDeckCn = viewModel::getDeckCn,
        getDeckKana = viewModel::getDeckKana,
        getDeckJlpt = viewModel::getDeckJlpt
    )
}

@Composable
private fun HomeScreenContent(
    content: String,
    getRootComponent: () -> Unit,
    getDeckCn: () -> Unit,
    getDeckKana: () -> Unit,
    getDeckJlpt: () -> Unit
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = getRootComponent) {
            Text("Root")
        }
        Button(onClick = getDeckCn) {
            Text("Deck Cn")
        }
        Button(onClick = getDeckKana) {
            Text("Deck Kana")
        }
        Button(onClick = getDeckJlpt) {
            Text("Deck JLPT")
        }
        Text("Root: $content")
    }
}

@Preview
@Composable
fun HomeScreenContentPreview() {
    PreviewBase {
        HomeScreenContent("test", {}, {}, {}, {})
    }
}