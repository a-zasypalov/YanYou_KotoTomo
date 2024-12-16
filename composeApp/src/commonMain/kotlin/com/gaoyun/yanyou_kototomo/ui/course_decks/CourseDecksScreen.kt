package com.gaoyun.yanyou_kototomo.ui.course_decks

import androidx.compose.foundation.background
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
import com.gaoyun.yanyou_kototomo.data.local.CourseId
import com.gaoyun.yanyou_kototomo.ui.base.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.PreviewBase
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CourseDecksScreen(
    courseId: CourseId,
    navigate: (NavigationSideEffect) -> Unit
) {
    val viewModel = koinViewModel(vmClass = CourseDecksViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.getCourseDecks(courseId)
    }

    CourseDecksContent(viewModel.viewState.collectAsState().value)
}

@Composable
private fun CourseDecksContent(course: Course?) {
    LazyColumn(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        course?.let {
            item {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.displayLarge
                )
            }

            course.decks.forEach { deck ->
                item {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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

@Preview
@Composable
fun CourseChaptersScreenContentPreview() {
    PreviewBase {
        CourseDecksContent(null)
    }
}