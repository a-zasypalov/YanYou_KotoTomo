package com.gaoyun.yanyou_kototomo.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.HomeState
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.deck_overview.details.getCourseMascot
import com.gaoyun.yanyou_kototomo.util.localDateNow
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource

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
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        content?.currentlyLearn?.let {
            item {
                Text(
                    text = "Currently learn",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            it.second?.let { deck ->
                val courseId = it.first
                val courseTextColor = Color(0xFFEDE1D4)
                val cardsReviewToday = deck.cards.count { it.progress?.nextReview == localDateNow() }

                item {
                    val courseCardColor = courseId.courseCardColor()

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().platformStyleClickable { },
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

                item {
                    LazyRow {
                        items(deck.cards) { card ->
                            ElevatedCard(
                                modifier = modifier.fillMaxWidth(),
                                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .platformStyleClickable { },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    AutoResizeText(
                                        text = card.card.front,
                                        fontSizeRange = FontSizeRange(min = 16.sp, max = 64.sp),
                                        style = MaterialTheme.typography.displayMedium.copy(
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 64.sp
                                        ),
                                        textAlign = TextAlign.Center,
                                        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.size(32.dp)) }
    }
}