package com.gaoyun.yanyou_kototomo.ui.personal_space

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.ui_state.CardCategoryType
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewPart
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckOverviewCategories
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.DeckProgressStatus
import com.gaoyun.yanyou_kototomo.ui.base.shared_elements.HorizontalCourseCardsList
import com.gaoyun.yanyou_kototomo.ui.personal_space.elements.FirstLearningDeck
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PersonalSpaceScreen(
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel<PersonalSpaceViewModel>()

    LaunchedEffect(Unit) { viewModel.getSpaceState() }

    SurfaceScaffold(backHandler = { navigate(BackNavigationEffect) }) {
        viewModel.viewState.collectAsState().value?.let {
            PersonalSpaceScreenContent(it)
        }
    }
}

@Composable
private fun PersonalSpaceScreenContent(viewState: PersonalSpaceState) {
    val state = rememberLazyListState()
    LazyColumn(
        state = state,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Text(
                text = "Learning",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 8.dp).padding(horizontal = 16.dp)
            )
        }

        viewState.learningDecks.firstOrNull()?.let {
            item {
                FirstLearningDeck(
                    deckWithInfo = it,
                    onCourseClick = {},
                    onCardDetailsClick = { _, _ -> }
                )
            }
        }

        if (viewState.learningDecks.size > 1) item {
            HorizontalCourseCardsList(
                decks = viewState.learningDecks.drop(1),
                onCourseClick = {}
            )
        }

        if (viewState.cardsDueToReview.isNotEmpty()) item {
            PrimaryElevatedButton(
                text = "Today's review: ${viewState.cardsDueToReview.size} cards",
                leadingIcon = Icons.Outlined.LocalLibrary,
                onClick = { }, //onPlayDeckClick(PlayerMode.SpacialRepetition)
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            )
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
            onCardClick = {},
            updateShowToReviewCards = {},
            updateShowNewCards = {},
            updateShowPausedCards = {},
            updateShowCompletedCards = {}
        )
    }
}

fun LazyListScope.PersonalSegmentedArea(
    viewState: PersonalSpaceState,
    onCardClick: (CardWithProgress<*>) -> Unit,
    updateShowNewCards: (Boolean) -> Unit,
    updateShowToReviewCards: (Boolean) -> Unit,
    updateShowPausedCards: (Boolean) -> Unit,
    updateShowCompletedCards: (Boolean) -> Unit,
) {
    val showNewCards = true
    val showToReviewCards = true
    val showPausedCards = true
    val showCompletedCards = true

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

    DeckOverviewCategories(categories, onCardClick)
}