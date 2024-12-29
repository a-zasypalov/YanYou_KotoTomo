package com.gaoyun.yanyou_kototomo.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun SettingsScreen(
    navigate: (NavigationSideEffect) -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel(vmClass = SettingsViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.getSettings()
    }

    SettingsScreenContent(
        content = viewModel.viewState.collectAsState().value,
        modifier = modifier,
    )
}

@Composable
private fun SettingsScreenContent(
    content: SettingsViewState?,
    modifier: Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )
        }
    }
}