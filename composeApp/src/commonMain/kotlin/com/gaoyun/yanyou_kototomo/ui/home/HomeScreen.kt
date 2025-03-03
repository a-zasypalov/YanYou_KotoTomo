package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.course.CourseWithInfo
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState
import com.gaoyun.yanyou_kototomo.ui.base.composables.FullScreenLoader
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.navigation.CourseScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToBookmarks
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToCourse
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeck
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToMixedDeckReviewPlayer
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerBackRoute
import com.gaoyun.yanyou_kototomo.ui.base.navigation.args.PlayerScreenMixedDeckReviewArgs
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckProgressStatus
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.HorizontalCourseCard
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.StartLearningState
import com.gaoyun.yanyou_kototomo.ui.card_details.CardDetailsView
import com.gaoyun.yanyou_kototomo.ui.home.components.CurrentlyLearningCourse
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenTitle
import com.gaoyun.yanyou_kototomo.ui.home.components.PersonalSegmentedArea
import com.gaoyun.yanyou_kototomo.ui.home.components.personalSegmentedCategories
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.bookmarks
import yanyou_kototomo.composeapp.generated.resources.edit
import yanyou_kototomo.composeapp.generated.resources.n_cards_to_review
import yanyou_kototomo.composeapp.generated.resources.n_new
import yanyou_kototomo.composeapp.generated.resources.n_to_learn
import yanyou_kototomo.composeapp.generated.resources.start_learning_deck_button
import yanyou_kototomo.composeapp.generated.resources.start_learning_deck_text

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
        onCardDetailsClick = { deckId, cardToShow, languageId, paused ->
            cardDetailLanguageState.value = languageId
            cardDetailDeckIdState.value = deckId
            cardDetailState.value = cardToShow
            cardDetailCompletedState.value = cardToShow.progress?.completed == true
            cardDetailPausedState.value = paused
        },
        onCourseClick = { courseId, learningLanguageId, sourceLanguageId ->
            navigate(
                ToCourse(
                    CourseScreenArgs(
                        learningLanguageId = learningLanguageId,
                        sourceLanguageId = sourceLanguageId,
                        courseId = courseId,
                    )
                )
            )
        },
        onDeckClick = { deckId, courseId, learningLanguageId, sourceLanguageId ->
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
    onCardDetailsClick: (DeckId, CardWithProgress<*>, LanguageId, Boolean) -> Unit,
    onCourseClick: (CourseId, LanguageId, LanguageId) -> Unit,
    onDeckClick: (DeckId, CourseId, LanguageId, LanguageId) -> Unit,
    onReviewClick: (CourseWithInfo) -> Unit,
    onBookmarksEdit: () -> Unit,
    onCoursesClick: () -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowNewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
) {
    content?.let { viewState ->
        if (viewState.bookmarks.isEmpty() && viewState.learningCourse == null) {
            StartLearningState(onCoursesClick)
        } else {
            val state = rememberLazyListState()
            val listState = rememberLazyListState()
            val lastIndex = viewState.learningCourse?.decks?.indexOfLast { it.id in viewState.learningDecks } ?: -1
            var previousIndex = rememberSaveable { mutableStateOf(-1) }
            val categories = personalSegmentedCategories(
                viewState = viewState,
                updateShowToReviewCards = updateShowToReviewCards,
                updateShowNewCards = updateShowNewCards,
                updateShowPausedCards = updateShowPausedCards,
                updateShowCompletedCards = updateShowCompletedCards,
                showToReviewCards = viewState.showToReviewCards,
                showNewCards = viewState.showNewCards,
                showPausedCards = viewState.showPausedCards,
                showCompletedCards = viewState.showCompletedCards,
            )

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
                                onDeckClick(courseDeck.id, course.id, course.learningLanguageId, course.sourceLanguageId)
                            },
                            onCourseClick = {
                                onCourseClick(course.id, course.learningLanguageId, course.sourceLanguageId)
                            }
                        )
                    }
                }

                val cardsDueToReviewNotEmpty = viewState.cardsDueToReview.isNotEmpty()
                val newCardsNotEmpty = viewState.newCards.isNotEmpty()
                if (cardsDueToReviewNotEmpty || newCardsNotEmpty) item {
                    val reviewLabel = listOfNotNull(
                        viewState.cardsDueToReview.takeIf { cardsDueToReviewNotEmpty }?.size?.let {
                            stringResource(Res.string.n_cards_to_review, it)
                        },
                        viewState.newCards.takeIf { newCardsNotEmpty }?.size?.let {
                            if (cardsDueToReviewNotEmpty) {
                                stringResource(Res.string.n_new, it)
                            } else {
                                stringResource(Res.string.n_to_learn, it)
                            }
                        }
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
                            Text(
                                text = stringResource(Res.string.bookmarks),
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.padding(bottom = 8.dp).padding(horizontal = 16.dp)
                            )
                            IconButton(onClick = onBookmarksEdit) {
                                Icon(
                                    Icons.Default.Edit,
                                    stringResource(Res.string.edit)
                                )
                            }
                        }
                    }

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                            item { Spacer(Modifier.size(8.dp)) }
                            items(viewState.bookmarks) { bookmark ->
                                HorizontalCourseCard(bookmark) { deckWithInfo ->
                                    onDeckClick(
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

                if (viewState.hasCardsForProgressStatus()) {
                    item {
                        DeckProgressStatus(
                            toReviewCardsCount = viewState.newOrReviewCards(),
                            completedCardsCount = viewState.completedCards.size,
                            pausedCardsCount = viewState.pausedCards.size,
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                        )
                    }
                } else {
                    item {
                        StartLearnDeck {
                            viewState.learningCourse?.let { course ->
                                onCourseClick(course.id, course.learningLanguageId, course.sourceLanguageId)
                            }
                        }
                    }
                }

                PersonalSegmentedArea(
                    categories = categories,
                    onCardClick = onCardDetailsClick,
                )

                item {
                    Spacer(modifier = Modifier.size(64.dp))
                }
            }
        }
    } ?: FullScreenLoader()
}

@Composable
fun StartLearnDeck(onCourseClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(Res.string.start_learning_deck_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 64.dp, bottom = 16.dp, start = 32.dp, end = 32.dp)
        )
        FilledTonalButton(onClick = onCourseClick) {
            Text(
                text = stringResource(Res.string.start_learning_deck_button),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}