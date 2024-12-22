package com.gaoyun.yanyou_kototomo.ui.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.domain.toStringRes
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.CourseScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToCourse
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun CoursesScreen(
    navigate: (NavigationSideEffect) -> Unit,
) {
    val viewModel = koinViewModel(vmClass = CoursesViewModel::class)

    LaunchedEffect(Unit) {
        viewModel.getRootComponent()
    }

    SurfaceScaffold {
        CoursesScreenContent(
            content = viewModel.viewState.collectAsState().value,
            toCourse = { args -> navigate(ToCourse(args)) }
        )
    }
}

@Composable
private fun CoursesScreenContent(
    content: RootStructure?,
    toCourse: (CourseScreenArgs) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
    ) {
        item {
            Text(
                text = "Courses",
                style = MaterialTheme.typography.displayLarge,
            )
        }
        content?.languages?.forEach { language ->
            item {
                Text(
                    text = stringResource(language.id.toStringRes()),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            language.sourceLanguages.forEach { sourceLanguage ->

//                TODO: Unblock when new source languages will come
//                item {
//                    Text(
//                        text = stringResource(sourceLanguage.id.toStringRes()),
//                        style = MaterialTheme.typography.titleLarge
//                    )
//                }

                sourceLanguage.courses.forEach { course ->
                    item {
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth().platformStyleClickable {
                                toCourse(
                                    CourseScreenArgs(
                                        learningLanguageId = language.id,
                                        sourceLanguageId = sourceLanguage.id,
                                        courseId = course.id
                                    )
                                )
                            },
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
                        ) {
                            Text(
                                text = course.id.identifier
                            )
                            Text(
                                text = course.courseName
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
            }
        } ?: item {
            CircularProgressIndicator()
        }
    }
}