package com.gaoyun.yanyou_kototomo.ui.courses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.gaoyun.yanyou_kototomo.ui.base.composables.FullScreenLoader
import com.gaoyun.yanyou_kototomo.ui.base.navigation.CourseScreenArgs
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.ToCourse
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.courses

@Composable
fun CoursesScreen(
    navigate: (NavigationSideEffect) -> Unit,
    modifier: Modifier,
) {
    val viewModel = koinViewModel<CoursesViewModel>()

    LaunchedEffect(Unit) {
        viewModel.getRootComponent()
    }

    CoursesScreenContent(
        content = viewModel.viewState.collectAsState().value,
        modifier = modifier,
        toCourse = { args -> navigate(ToCourse(args)) }
    )
}

@Composable
private fun CoursesScreenContent(
    content: RootStructure?,
    toCourse: (CourseScreenArgs) -> Unit,
    modifier: Modifier,
) {
    val state = rememberLazyListState()
    content?.let {
        LazyColumn(
            state = state,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            item {
                Text(
                    text = stringResource(Res.string.courses),
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
            it.languages.forEachIndexed { languageIndex, language ->
                item {
                    Text(
                        text = stringResource(language.id.toStringRes()),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
                language.sourceLanguages.forEach { sourceLanguage ->
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
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    } ?: FullScreenLoader()
}