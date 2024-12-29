package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.HomeState
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeck
import com.gaoyun.yanyou_kototomo.ui.card_details.CardDetailsView
import com.gaoyun.yanyou_kototomo.ui.card_details.getCourseMascot
import com.gaoyun.yanyou_kototomo.util.localDateNow
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.border_design
import yanyou_kototomo.composeapp.generated.resources.ic_home_title_cn
import yanyou_kototomo.composeapp.generated.resources.maneki_neko

@Composable
fun HomeScreen(
    navigate: (NavigationSideEffect) -> Unit,
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
                        deckWithInfo.deck.id
                    )
                )
            )
        }
    )
    CardDetailsView(cardDetailState, cardDetailLanguageState.value) { cardDetailState.value = null }
}

@Composable
private fun HomeScreenContent(
    content: HomeState?,
    modifier: Modifier,
    onCardDetailsClick: (CardWithProgress<*>, LanguageId) -> Unit,
    onCourseClick: (DeckWithCourseInfo) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        item { HomeScreenTitle() }

        content?.currentlyLearn?.let { deckWithInfo ->
            item { HomeScreenCurrentlyLearningDeck(deckWithInfo, onCourseClick) }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Spacer(Modifier.size(8.dp)) }
                    items(deckWithInfo.deck.cards) { card ->
                        HomeScreenCharacterCard(
                            card = card,
                            languageId = deckWithInfo.info.learningLanguageId,
                            onClick = onCardDetailsClick
                        )
                    }
                    item { Spacer(Modifier.size(8.dp)) }
                }
            }
        }

        content?.bookmarks?.let { bookmarks ->
            if (bookmarks.isNotEmpty()) item { HomeScreenSectionTitle("Bookmarks") }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Spacer(Modifier.size(8.dp)) }
                    items(bookmarks) { bookmark -> HomeScreenBookmarkedDeck(bookmark, onCourseClick) }
                    item { Spacer(Modifier.size(8.dp)) }
                }
            }
        }

        content?.recentlyReviewed?.let {
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

        content?.let {
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
        }


        item { Spacer(Modifier.size(32.dp)) }
    }
}

@Composable
fun HomeScreenSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(bottom = 8.dp).padding(horizontal = 16.dp)
    )
}

@Composable
fun HomeScreenCharacterCard(card: CardWithProgress<*>, languageId: LanguageId, onClick: (CardWithProgress<*>, LanguageId) -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().platformStyleClickable(onClick = { onClick(card, languageId) }),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = card.card.front,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun HomeScreenCurrentlyLearningDeck(deckWithInfo: DeckWithCourseInfo, onCourseClick: (DeckWithCourseInfo) -> Unit) {
    val courseCardColor = deckWithInfo.info.courseId.courseCardColor()
    val courseTextColor = Color(0xFFEDE1D4)
    val cardsReviewToday = deckWithInfo.deck.cards.count { it.progress?.nextReview == localDateNow() }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).platformStyleClickable { onCourseClick(deckWithInfo) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().background(Color(0x33000000)).padding(vertical = 16.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
                Text(
                    text = deckWithInfo.deck.name,
                    color = courseTextColor,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Review: $cardsReviewToday",
                        color = courseTextColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = "Quiz",
                        color = courseTextColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

            }
            Image(
                painter = painterResource(deckWithInfo.info.courseId.getCourseMascot()),
                contentDescription = null,
                alpha = 0.6f,
                colorFilter = ColorFilter.tint(courseTextColor),
                modifier = Modifier.heightIn(max = 48.dp).padding(end = 16.dp)
            )
        }
    }
}

@Composable
fun HomeScreenTitle() {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painterResource(Res.drawable.border_design),
            contentDescription = "",
            alpha = 0.4f,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.rotate(90f).size(48.dp)
        )
        Image(
            painter = painterResource(Res.drawable.ic_home_title_cn),
            contentDescription = "",
            alpha = 0.4f,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.size(48.dp)
        )
        Image(
            painter = painterResource(Res.drawable.border_design),
            contentDescription = "",
            alpha = 0.4f,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.rotate(180f).size(48.dp)
        )
    }
}

@Composable
fun HomeScreenBookmarkedDeck(bookmark: DeckWithCourseInfo, onCourseClick: (DeckWithCourseInfo) -> Unit) {
    val courseCardColor = bookmark.info.courseId.courseCardColor()
    val courseTextColor = Color(0xFFEDE1D4)
    val preview = bookmark.deck.cards.joinToString(" ") { it.card.front }

    ElevatedCard(
        modifier = Modifier.platformStyleClickable { onCourseClick(bookmark) }.height(150.dp).widthIn(max = 150.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize().background(Color(0x33000000)).padding(8.dp)
        ) {
            AutoResizeText(
                text = bookmark.deck.name,
                fontSizeRange = FontSizeRange(min = 16.sp, max = MaterialTheme.typography.titleLarge.fontSize),
                color = courseTextColor,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
            )
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(courseTextColor))
            Text(
                text = preview,
                color = courseTextColor.copy(alpha = 0.8f),
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
