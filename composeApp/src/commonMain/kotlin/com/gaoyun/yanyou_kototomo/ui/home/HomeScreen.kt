package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.course.CourseWithInfo
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.data.ui_state.CardCategoryType
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewPart
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState
import com.gaoyun.yanyou_kototomo.ui.base.composables.FullScreenLoader
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.PlayerBackRoute
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToBookmarks
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeck
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeckQuizPlayer
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToMixedDeckReviewPlayer
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenDeckQuizArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenMixedDeckReviewArgs
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckOverviewCategories
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckProgressStatus
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.HorizontalCourseCard
import com.gaoyun.yanyou_kototomo.ui.card_details.CardDetailsView
import com.gaoyun.yanyou_kototomo.ui.home.components.CurrentlyLearningCourse
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenEmptyState
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenSectionTitle
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenTitle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    navigate: (NavigationSideEffect) -> Unit,
    onCoursesClick: () -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel<HomeViewModel>()
    val cardDetailState = remember { mutableStateOf<CardWithProgress<*>?>(null) }
    val cardDetailLanguageState = remember { mutableStateOf<LanguageId>(LanguageId("cn")) }
    val cardDetailDeckIdState = remember { mutableStateOf<DeckId?>(null) }
    val cardDetailPausedState = remember { mutableStateOf<Boolean>(false) }
    val cardDetailCompletedState = remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(Unit) {
        viewModel.getSpaceState()
    }

    HomeScreenContent(
        content = viewModel.viewState.collectAsState().value,
        modifier = modifier,
        onCardDetailsClick = { deckId, cardToShow, languageId ->
            cardDetailLanguageState.value = languageId
            cardDetailDeckIdState.value = deckId
            cardDetailState.value = cardToShow
        },
        onCourseClick = { deckId, courseId, learningLanguageId, sourceLanguageId ->
            navigate(
                ToDeck(
                    DeckScreenArgs(
                        learningLanguageId = learningLanguageId,
                        sourceLanguageId = sourceLanguageId,
                        courseId = courseId,
                        deckId = deckId
                    )
                )
            )
        },
        onReviewClick = { course ->
            navigate(
                ToMixedDeckReviewPlayer(
                    PlayerScreenMixedDeckReviewArgs(
                        learningLanguageId = course.learningLanguageId,
                        sourceLanguageId = course.sourceLanguageId,
                        courseId = course.id,
                        deckIds = course.decks.map { it.id },
                        backToRoute = PlayerBackRoute.Home,
                    )
                )
            )
        },
        onQuizClick = { deckWithInfo ->
            navigate(
                ToDeckQuizPlayer(
                    PlayerScreenDeckQuizArgs(
                        learningLanguageId = deckWithInfo.info.learningLanguageId,
                        sourceLanguageId = deckWithInfo.info.sourceLanguageId,
                        courseId = deckWithInfo.info.courseId,
                        deckIds = listOf(deckWithInfo.deck.id),
                        backToRoute = PlayerBackRoute.Home,
                    )
                )
            )
        },
        onBookmarksEdit = { navigate(ToBookmarks) },
        onCoursesClick = onCoursesClick,
        updateShowToReviewCards = viewModel::updateShowToReviewCards,
        updateShowNewCards = viewModel::updateShowNewCards,
        updateShowPausedCards = viewModel::updateShowPausedCards,
        updateShowCompletedCards = viewModel::updateShowCompletedCards
    )
    CardDetailsView(
        cardState = cardDetailState,
        languageId = cardDetailLanguageState.value,
        paused = cardDetailPausedState,
        completed = cardDetailCompletedState,
        onCardPause = { card, paused -> cardDetailDeckIdState.value?.let { viewModel.pauseCard(it, card, paused) } },
        onCardComplete = { card, completed -> cardDetailDeckIdState.value?.let { viewModel.completeCard(it, card, completed) } },
        onDismiss = { cardDetailState.value = null }
    )
}

@Composable
private fun HomeScreenContent(
    content: PersonalSpaceState?,
    modifier: Modifier,
    onCardDetailsClick: (DeckId, CardWithProgress<*>, LanguageId) -> Unit,
    onCourseClick: (DeckId, CourseId, LanguageId, LanguageId) -> Unit,
    onReviewClick: (CourseWithInfo) -> Unit,
    onQuizClick: (DeckWithCourseInfo) -> Unit,
    onBookmarksEdit: () -> Unit,
    onCoursesClick: () -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowNewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
) {
    content?.let { viewState ->
        if (viewState.bookmarks.isEmpty() && viewState.learningCourse == null) {
            HomeScreenEmptyState(onCoursesClick)
        } else {
            val state = rememberLazyListState()
            val listState = rememberLazyListState()
            val lastIndex = viewState.learningCourse?.decks?.indexOfLast { it.id in viewState.learningDecks } ?: -1
            var previousIndex = rememberSaveable { mutableStateOf(-1) }

            LaunchedEffect(lastIndex) {
                if (lastIndex != -1 && lastIndex != previousIndex.value) {
                    previousIndex.value = lastIndex
                    listState.animateScrollToItem(lastIndex)
                }
            }

            LazyColumn(
                state = state,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.fillMaxSize(),
            ) {
                item { HomeScreenTitle() }

                viewState.learningCourse?.let { course ->
                    item {
                        CurrentlyLearningCourse(
                            course = course,
                            learningDecks = viewState.learningDecks,
                            decksState = listState,
                            onDeckClick = { courseDeck ->
                                onCourseClick(courseDeck.id, course.id, course.learningLanguageId, course.sourceLanguageId)
                            }
                        )
                    }
                }

                val cardsDueToReviewNotEmpty = viewState.cardsDueToReview.isNotEmpty()
                val newCardsNotEmpty = viewState.newCards.isNotEmpty()
                if (cardsDueToReviewNotEmpty || newCardsNotEmpty) item {
                    val reviewLabel = listOfNotNull(
                        viewState.cardsDueToReview.takeIf { cardsDueToReviewNotEmpty }?.size?.let { "$it cards to review" },
                        viewState.newCards.takeIf { newCardsNotEmpty }?.size?.let { if (cardsDueToReviewNotEmpty) "$it new cards" else "$it new" }
                    ).joinToString(", ")

                    PrimaryElevatedButton(
                        text = reviewLabel,
                        leadingIcon = Icons.Outlined.LocalLibrary,
                        onClick = { viewState.learningCourse?.let { onReviewClick(it) } },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }


                if (viewState.bookmarks.isNotEmpty()) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            HomeScreenSectionTitle("Bookmarks")
                            IconButton(onClick = onBookmarksEdit) { Icon(Icons.Default.Edit, "edit") }
                        }
                    }

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                            item { Spacer(Modifier.size(8.dp)) }
                            items(viewState.bookmarks) { bookmark ->
                                HorizontalCourseCard(bookmark) { deckWithInfo ->
                                    onCourseClick(
                                        deckWithInfo.deck.id,
                                        deckWithInfo.info.courseId,
                                        deckWithInfo.info.learningLanguageId,
                                        deckWithInfo.info.sourceLanguageId
                                    )
                                }
                            }
                            item { Spacer(Modifier.size(8.dp)) }
                        }
                    }
                }

                item {
                    DeckProgressStatus(
                        toReviewCardsCount = viewState.newCards.size + viewState.cardsToReview.size,
                        completedCardsCount = viewState.completedCards.size,
                        pausedCardsCount = viewState.pausedCards.size
                    )
                }

                PersonalSegmentedArea(
                    viewState = viewState,
                    onCardClick = onCardDetailsClick,
                    updateShowToReviewCards = updateShowToReviewCards,
                    updateShowNewCards = updateShowNewCards,
                    updateShowPausedCards = updateShowPausedCards,
                    updateShowCompletedCards = updateShowCompletedCards,
                    showToReviewCards = viewState.showToReviewCards,
                    showNewCards = viewState.showNewCards,
                    showPausedCards = viewState.showPausedCards,
                    showCompletedCards = viewState.showCompletedCards,
                )

                item {
                    Spacer(modifier = Modifier.size(64.dp))
                }
            }
        }
    } ?: FullScreenLoader()
}

fun LazyListScope.PersonalSegmentedArea(
    viewState: PersonalSpaceState,
    onCardClick: (DeckId, CardWithProgress<*>, LanguageId) -> Unit,
    updateShowNewCards: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
    showToReviewCards: Boolean,
    showNewCards: Boolean,
    showPausedCards: Boolean,
    showCompletedCards: Boolean,
) {
    val categories = listOf(
        CardOverviewPart.List(
            name = "To Review",
            cards = viewState.cardsToReview,
            type = CardCategoryType.ToReview,
            isShown = showToReviewCards,
            visibilityToggle = updateShowToReviewCards
        ),
        CardOverviewPart.List(
            name = "New",
            cards = viewState.newCards,
            type = CardCategoryType.New,
            isShown = showNewCards,
            visibilityToggle = updateShowNewCards
        ),
        CardOverviewPart.List(
            name = "Paused",
            cards = viewState.pausedCards,
            type = CardCategoryType.Paused,
            isShown = showPausedCards,
            visibilityToggle = updateShowPausedCards
        ),
        CardOverviewPart.List(
            name = "✓ Completed",
            cards = viewState.completedCards,
            type = CardCategoryType.Completed,
            isShown = showCompletedCards,
            visibilityToggle = updateShowCompletedCards
        )
    )

    DeckOverviewCategories(categories, Modifier.padding(horizontal = 16.dp), onCardClick)
}
