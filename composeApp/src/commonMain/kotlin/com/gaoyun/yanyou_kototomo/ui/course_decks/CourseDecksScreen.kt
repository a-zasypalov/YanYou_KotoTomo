package com.gaoyun.yanyou_kototomo.ui.course_decks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.Course
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.ui.CourseScreenArgs
import com.gaoyun.yanyou_kototomo.ui.DeckOverviewScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.ToDeck
import moe.tlaster.precompose.koin.koinViewModel

private fun CourseScreenArgs.toDeckOverviewArgs(deckId: DeckId) = DeckOverviewScreenArgs(
    learningLanguageId = learningLanguageId,
    sourceLanguageId = sourceLanguageId,
    courseId = courseId,
    deckId = deckId
)

@Composable
fun CourseDecksScreen(
    args: CourseScreenArgs,
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = CourseDecksViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.getCourseDecks(args.courseId)
    }

    SurfaceScaffold(backHandler = { navigate(BackNavigationEffect) }) {
        CourseDecksContent(
            course = viewModel.viewState.collectAsState().value,
            toDeck = { deckId -> navigate(ToDeck(args.toDeckOverviewArgs(deckId))) }
        )
    }
}

@Composable
private fun CourseDecksContent(course: Course?, toDeck: (DeckId) -> Unit) {
    LazyColumn(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        course?.let {
            item {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            course.decks.forEach { deck ->
                item {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable { toDeck(deck.id) },
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
                    ) {
                        Text(
                            text = deck.id.identifier
                        )
                        Text(
                            text = deck.name
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(2.dp).background(
                        MaterialTheme.colorScheme.outline
                    )
                )
            }

        } ?: item {
            CircularProgressIndicator()
        }
    }
}