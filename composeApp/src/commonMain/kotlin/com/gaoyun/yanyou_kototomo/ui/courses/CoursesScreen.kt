package com.gaoyun.yanyou_kototomo.ui.courses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.RootStructure
import com.gaoyun.yanyou_kototomo.domain.toStringRes
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.base.composables.SurfaceScaffold
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
        content?.languages?.forEachIndexed { languageIndex, language ->
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

                items(sourceLanguage.courses) { course ->
                    CourseCard(course = course) {
                        toCourse(
                            CourseScreenArgs(
                                learningLanguageId = language.id,
                                sourceLanguageId = sourceLanguage.id,
                                courseId = course.id
                            )
                        )
                    }
                }

                if (languageIndex != content.languages.lastIndex) item {
                    Divider(height = 2.dp, modifier = Modifier.fillMaxWidth())
                } else item {
                    Spacer(modifier = Modifier.height(64.dp))
                }
            }
        } ?: item {
            CircularProgressIndicator()
        }
    }
}