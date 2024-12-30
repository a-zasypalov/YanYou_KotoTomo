package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.HomeState
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.FullScreenLoader
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerBackRoute
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerMode
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeck
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeckPlayer
import com.gaoyun.yanyou_kototomo.ui.card_details.CardDetailsView
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenBookmarkedDeck
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenCharacterCard
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenCurrentlyLearningDeck
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenEmptyState
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenSectionTitle
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenTitle
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.maneki_neko

@Composable
fun HomeScreen(
    navigate: (NavigationSideEffect) -> Unit,
    onCoursesClick: () -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel(vmClass = HomeViewModel::class)
    val cardDetailState = remember { mutableStateOf<CardWithProgress<*>?>(null) }
    val cardDetailLanguageState = remember { mutableStateOf<LanguageId>(LanguageId("cn")) }

    LaunchedEffect(Unit) {
        viewModel.getHomeState()
    }

    HomeScreenContent(
        content = viewModel.viewState.collectAsState().value,
        modifier = modifier,
        onCardDetailsClick = { cardToShow, languageId ->
            cardDetailLanguageState.value = languageId
            cardDetailState.value = cardToShow
        },
        onCourseClick = { deckWithInfo ->
            navigate(
                ToDeck(
                    DeckScreenArgs(
                        learningLanguageId = deckWithInfo.info.learningLanguageId,
                        sourceLanguageId = deckWithInfo.info.sourceLanguageId,
                        courseId = deckWithInfo.info.courseId,
                        deckId = deckWithInfo.deck.id
                    )
                )
            )
        },
        onReviewClick = { deckWithInfo ->
            navigate(
                ToDeckPlayer(
                    PlayerScreenArgs(
                        learningLanguageId = deckWithInfo.info.learningLanguageId,
                        sourceLanguageId = deckWithInfo.info.sourceLanguageId,
                        courseId = deckWithInfo.info.courseId,
                        deckId = deckWithInfo.deck.id,
                        playerMode = PlayerMode.SpacialRepetition,
                        backToRoute = PlayerBackRoute.Home
                    )
                )
            )
        },
        onQuizClick = { deckWithInfo ->
            navigate(
                ToDeckPlayer(
                    PlayerScreenArgs(
                        learningLanguageId = deckWithInfo.info.learningLanguageId,
                        sourceLanguageId = deckWithInfo.info.sourceLanguageId,
                        courseId = deckWithInfo.info.courseId,
                        deckId = deckWithInfo.deck.id,
                        playerMode = PlayerMode.Quiz,
                        backToRoute = PlayerBackRoute.Home
                    )
                )
            )
        },
        onCoursesClick = onCoursesClick
    )
    CardDetailsView(cardDetailState, cardDetailLanguageState.value) { cardDetailState.value = null }
}

@Composable
private fun HomeScreenContent(
    content: HomeState?,
    modifier: Modifier,
    onCardDetailsClick: (CardWithProgress<*>, LanguageId) -> Unit,
    onCourseClick: (DeckWithCourseInfo) -> Unit,
    onReviewClick: (DeckWithCourseInfo) -> Unit,
    onQuizClick: (DeckWithCourseInfo) -> Unit,
    onCoursesClick: () -> Unit,
) {
    content?.let {
        if (it.currentlyLearn == null && it.bookmarks.isEmpty() && it.recentlyReviewed.isEmpty()) {
            HomeScreenEmptyState(onCoursesClick)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier.fillMaxWidth(),
            ) {
                item { HomeScreenTitle() }

                it.currentlyLearn?.let { deckWithInfo ->
                    item {
                        HomeScreenCurrentlyLearningDeck(
                            deckWithInfo = deckWithInfo,
                            onCourseClick = onCourseClick,
                            onCardDetailsClick = onCardDetailsClick,
                            onReviewClick = onReviewClick,
                            onQuizClick = onQuizClick
                        )
                    }
                }

                it.bookmarks.let { bookmarks ->
                    if (bookmarks.isNotEmpty()) item { HomeScreenSectionTitle("Bookmarks") }

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item { Spacer(Modifier.size(8.dp)) }
                            items(bookmarks) { bookmark -> HomeScreenBookmarkedDeck(bookmark, onCourseClick) }
                            item { Spacer(Modifier.size(8.dp)) }
                        }
                    }
                }

                it.recentlyReviewed.let {
                    if (it.isNotEmpty()) item { HomeScreenSectionTitle("Recently reviewed") }

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item { Spacer(Modifier.size(8.dp)) }
                            items(it.sortedByDescending { pair -> pair.second.progress?.lastReviewed }) { pair ->
                                HomeScreenCharacterCard(card = pair.second, languageId = pair.first, onClick = onCardDetailsClick)
                            }
                            item { Spacer(Modifier.size(8.dp)) }
                        }
                    }
                }

                item { Spacer(Modifier.size(32.dp)) }

                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(Res.drawable.maneki_neko),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            modifier = Modifier.size(48.dp).alpha(0.4f)
                        )
                    }
                }

                item { Spacer(Modifier.size(32.dp)) }
            }
        }
    } ?: FullScreenLoader()
}
