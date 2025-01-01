package com.gaoyun.yanyou_kototomo.ui.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToHomeScreen
import kotlinx.coroutines.launch
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardingScreenContent(onFinish: () -> Unit) {
    val state = rememberPagerState(pageCount = { return@rememberPagerState 3 })
    val scope = rememberCoroutineScope()

    Box {
        HorizontalPager(
            state = state,
            modifier = Modifier
                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(bottom = 72.dp)
        ) { page ->
            when (page) {
                0 -> Box(modifier = Modifier.fillMaxSize())
                1 -> Box(modifier = Modifier.fillMaxSize())
                2 -> Box(modifier = Modifier.fillMaxSize())
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        ) {
            DotsIndicator(
                totalDots = state.pageCount,
                selectedIndex = state.currentPage,
                selectedColor = MaterialTheme.colorScheme.inverseSurface,
                unSelectedColor = MaterialTheme.colorScheme.outline,
            )
            Spacer(modifier = Modifier.size(16.dp))
            if (state.currentPage < state.pageCount - 1) {
                PrimaryElevatedButton(
                    text = "Next",
                    onClick = { scope.launch { state.animateScrollToPage(state.currentPage + 1) } },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            } else {
                PrimaryElevatedButton(
                    text = "Start",
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }
            Spacer(
                modifier = Modifier.navigationBarsPadding().size(16.dp)
            )
        }
    }
}