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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.data.local.HomeState
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.data.local.deck.Deck
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.deck_overview.details.getCourseMascot
import com.gaoyun.yanyou_kototomo.util.localDateNow
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.border_design
import yanyou_kototomo.composeapp.generated.resources.ic_home_title_cn

@Composable
fun HomeScreen(
    navigate: (NavigationSideEffect) -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel(vmClass = HomeViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.getHomeState()
    }

    HomeScreenContent(
        content = viewModel.viewState.collectAsState().value,
        modifier = modifier
    )
}

@Composable
private fun HomeScreenContent(
    content: HomeState?,
    modifier: Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        item { HomeScreenTitle() }

        item {
            Text(
                text = "Home",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .padding(horizontal = 16.dp)            )
        }

        content?.currentlyLearn?.let {
            item { HomeScreenSectionTitle("Currently learning") }

            it.second?.let { deck ->
                item { HomeScreenCurrentlyLearningDeck(it.first, deck) }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item { Spacer(Modifier.size(8.dp)) }
                        items(deck.cards) { card ->
                            HomeScreenCharacterCard(card = card)
                        }
                        item { Spacer(Modifier.size(8.dp)) }
                    }
                }
            }
        }

        content?.bookmarks?.let {
            item { HomeScreenSectionTitle("Bookmarks") }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Spacer(Modifier.size(8.dp)) }
                    items(it) { pair ->
                        val courseId = pair.first
                        val deck = pair.second
                        val courseCardColor = courseId.courseCardColor()
                        val courseTextColor = Color(0xFFEDE1D4)
                        val preview = deck.cards.joinToString(" ") { it.card.front }

                        ElevatedCard(
                            modifier = Modifier.platformStyleClickable { }.height(150.dp).widthIn(max = 150.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxSize().background(Color(0x33000000)).padding(8.dp)
                            ) {
                                AutoResizeText(
                                    text = deck.name,
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
                    item { Spacer(Modifier.size(8.dp)) }
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
fun HomeScreenCharacterCard(card: CardWithProgress<*>) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().platformStyleClickable { },
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
fun HomeScreenCurrentlyLearningDeck(courseId: CourseId, deck: Deck) {
    val courseCardColor = courseId.courseCardColor()
    val courseTextColor = Color(0xFFEDE1D4)
    val cardsReviewToday = deck.cards.count { it.progress?.nextReview == localDateNow() }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).platformStyleClickable { },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().background(Color(0x33000000))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
                Text(
                    text = deck.name,
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
                painter = painterResource(courseId.getCourseMascot()),
                contentDescription = null,
                colorFilter = ColorFilter.tint(courseTextColor),
                modifier = Modifier.size(24.dp).padding(top = 8.dp)
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
            modifier = Modifier.rotate(90f).size(48.dp)
        )
        Image(
            painter = painterResource(Res.drawable.ic_home_title_cn),
            contentDescription = "",
            alpha = 0.4f,
            modifier = Modifier.size(48.dp)
        )
        Image(
            painter = painterResource(Res.drawable.border_design),
            contentDescription = "",
            alpha = 0.4f,
            modifier = Modifier.rotate(180f).size(48.dp)
        )
    }
}
