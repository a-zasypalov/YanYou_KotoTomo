package com.gaoyun.yanyou_kototomo.ui.personal_space

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.data.ui_state.CardOverviewCategory
import com.gaoyun.yanyou_kototomo.data.ui_state.PersonalSpaceState
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.PrimaryElevatedButton
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.deck_overview.components.CardCategoryType
import com.gaoyun.yanyou_kototomo.ui.home.components.HomeScreenBookmarkedDeck
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
            HorizontalCourseCards(
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

        item {
            val expandIconAngle = animateFloatAsState(targetValue = if (true) 180f else 0f)
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("To Review", style = MaterialTheme.typography.headlineMedium)
//                    onOpenToggle?.let {
                    IconButton(onClick = { /*onOpenToggle(!isOpen)*/ }) {
                        Icon(Icons.Default.ExpandMore, "", modifier = Modifier.rotate(expandIconAngle.value))
                    }
//                    }
                }
                Divider(1.dp, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun HorizontalCourseCards(
    decks: List<DeckWithCourseInfo>,
    onCourseClick: (DeckWithCourseInfo) -> Unit,
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Spacer(Modifier.size(8.dp)) }
        items(decks) { deck -> HomeScreenBookmarkedDeck(deck, onCourseClick) }
        item { Spacer(Modifier.size(8.dp)) }
    }
}

@Composable
fun DeckProgressStatus(
    toReviewCardsCount: Int,
    completedCardsCount: Int,
    pausedCardsCount: Int,
) {
    Surface(
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            if (toReviewCardsCount > 0) Text(
                text = "$toReviewCardsCount to review",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            if (completedCardsCount > 0) Text(
                text = " $completedCardsCount completed",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            if (pausedCardsCount > 0) Text(
                text = "$pausedCardsCount paused",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun PersonalSegmentedArea(
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
        CardOverviewCategory.ListOverview(
            "To Review",
            viewState.cardsToReview,
            CardCategoryType.ToReview,
            showToReviewCards,
            updateShowToReviewCards
        ),
        CardOverviewCategory.ListOverview("New", viewState.newCards, CardCategoryType.New, showNewCards, updateShowNewCards),
        CardOverviewCategory.ListOverview("Paused", viewState.pausedCards, CardCategoryType.Paused, showPausedCards, updateShowPausedCards),
        CardOverviewCategory.ListOverview(
            "✓ Completed",
            viewState.completedCards,
            CardCategoryType.Completed,
            showCompletedCards,
            updateShowCompletedCards
        )
    )
}