package com.gaoyun.yanyou_kototomo.ui.onboarding

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToHomeScreen
import com.gaoyun.yanyou_kototomo.ui.onboarding.pages.OnboardingCardProgressPage
import com.gaoyun.yanyou_kototomo.ui.onboarding.pages.OnboardingCardShowcasePage
import com.gaoyun.yanyou_kototomo.ui.onboarding.pages.OnboardingLanguageChooserPage
import com.gaoyun.yanyou_kototomo.ui.onboarding.pages.OnboardingLearningBookmarksPage
import com.gaoyun.yanyou_kototomo.ui.onboarding.pages.OnboardingReviewQuizPage
import com.gaoyun.yanyou_kototomo.ui.onboarding.pages.OnboardingWelcomePage
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen(navigate: (NavigationSideEffect) -> Unit) {
    val viewModel = koinViewModel<OnboardingViewModel>()

    SurfaceScaffold {
        OnboardingScreenContent(
            onPrimaryLanguageChosen = viewModel::onPrimaryLanguageChosen,
            onFinish = {
                viewModel.finishOnboarding()
                navigate(ToHomeScreen)
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardingScreenContent(
    onPrimaryLanguageChosen: (LanguageId) -> Unit,
    onFinish: () -> Unit,
) {
    val state = rememberPagerState(pageCount = { return@rememberPagerState 6 })
    val scope = rememberCoroutineScope()

    fun nextPage() {
        scope.launch {
            state.animateScrollToPage(
                page = state.currentPage + 1,
                animationSpec = tween(durationMillis = 500)
            )
        }
    }

    HorizontalPager(
        state = state,
        modifier = Modifier.navigationBarsPadding().statusBarsPadding(),
        userScrollEnabled = false
    ) { page ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (page) {
                0 -> OnboardingWelcomePage { nextPage() }
                1 -> OnboardingLanguageChooserPage { onPrimaryLanguageChosen(it); nextPage() }
                2 -> OnboardingLearningBookmarksPage { nextPage() }
                3 -> OnboardingReviewQuizPage { nextPage() }
                4 -> OnboardingCardShowcasePage { nextPage() }
                5 -> OnboardingCardProgressPage(onFinish)
            }
        }
    }
}