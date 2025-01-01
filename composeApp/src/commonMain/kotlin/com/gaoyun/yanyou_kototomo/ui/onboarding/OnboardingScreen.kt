package com.gaoyun.yanyou_kototomo.ui.onboarding

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToHomeScreen
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun OnboardingScreen(navigate: (NavigationSideEffect) -> Unit) {
    val viewModel = koinViewModel(vmClass = OnboardingViewModel::class)

    SurfaceScaffold {
        OnboardingScreenContent {
            viewModel.finishOnboarding()
            navigate(ToHomeScreen)
        }
    }
}

@Composable
private fun OnboardingScreenContent(onFinish: () -> Unit) {
    Button(onFinish) {
        Text("Finish")
    }
}