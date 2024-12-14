package com.gaoyun.yanyou_kototomo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gaoyun.yanyou_kototomo.ui.HomeViewModel
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = koinViewModel(vmClass = HomeViewModel::class)

        val state = viewModel.viewState.collectAsState()

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = viewModel::getRootComponent) {
                Text("Click me!")
            }
            Text("Root: $state")
        }
    }
}