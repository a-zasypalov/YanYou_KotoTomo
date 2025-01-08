package com.gaoyun.yanyou_kototomo.ui.course_decks

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.DeckId
import com.gaoyun.yanyou_kototomo.data.local.course.Course
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.CourseScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.DeckScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToDeck
import com.gaoyun.yanyou_kototomo.ui.card_details.getCourseMascot
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.painterResource

private fun CourseScreenArgs.toDeckOverviewArgs(deckId: DeckId) = DeckScreenArgs(
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
    val state = rememberLazyListState()
    Box(Modifier.fillMaxSize()) {
        if (course != null && course.decks.size < 5) {
            Image(
                painter = painterResource(course.id.getCourseMascot()),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp).size(48.dp).alpha(0.2f)
            )
        }

        LazyColumn(state = state, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            course?.let {
                item {
                    Text(
                        text = course.courseName,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                items(course.decks) { deck ->
                    CourseDeckCard(
                        course = course,
                        deck = deck,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) { toDeck(deck.id) }
                }

                if (course.decks.size >= 5) {
                    item {
                        Box(Modifier.fillMaxWidth().navigationBarsPadding().padding(vertical = 32.dp)) {
                            Image(
                                painter = painterResource(course.id.getCourseMascot()),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                modifier = Modifier.align(Alignment.BottomCenter).size(48.dp).alpha(0.2f)
                            )
                        }
                    }
                } else {
                    item {
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }

            } ?: item {
                CircularProgressIndicator()
            }
        }
    }
}