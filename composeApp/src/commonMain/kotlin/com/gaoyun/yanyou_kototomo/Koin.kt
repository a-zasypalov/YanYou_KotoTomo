package com.gaoyun.yanyou_kototomo

import com.gaoyun.yanyou_kototomo.data.persistence.DriverFactory
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.platformModule
import com.gaoyun.yanyou_kototomo.domain.AllDataReset
import com.gaoyun.yanyou_kototomo.domain.BookmarksInteractor
import com.gaoyun.yanyou_kototomo.domain.CardProgressUpdater
import com.gaoyun.yanyou_kototomo.domain.DeckSettingsInteractor
import com.gaoyun.yanyou_kototomo.domain.GetCardProgress
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.domain.GetDeckFromCache
import com.gaoyun.yanyou_kototomo.domain.GetHomeState
import com.gaoyun.yanyou_kototomo.domain.QuizInteractor
import com.gaoyun.yanyou_kototomo.domain.SpacedRepetitionCalculation
import com.gaoyun.yanyou_kototomo.network.DecksApi
import com.gaoyun.yanyou_kototomo.network.PlatformHttpClient
import com.gaoyun.yanyou_kototomo.repository.CardsAndProgressRepository
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository
import com.gaoyun.yanyou_kototomo.repository.DeckRepository
import com.gaoyun.yanyou_kototomo.repository.DeckSettingsRepository
import com.gaoyun.yanyou_kototomo.repository.DeckUpdatesRepository
import com.gaoyun.yanyou_kototomo.repository.QuizSessionRepository
import com.gaoyun.yanyou_kototomo.ui.AppViewModel
import com.gaoyun.yanyou_kototomo.ui.base.ColorsProvider
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppNavigator
import com.gaoyun.yanyou_kototomo.ui.course_decks.CourseDecksViewModel
import com.gaoyun.yanyou_kototomo.ui.courses.CoursesViewModel
import com.gaoyun.yanyou_kototomo.ui.deck_overview.DeckOverviewViewModel
import com.gaoyun.yanyou_kototomo.ui.home.HomeViewModel
import com.gaoyun.yanyou_kototomo.ui.player.DeckPlayerViewModel
import com.gaoyun.yanyou_kototomo.ui.quiz_session_summary.QuizSessionSummaryViewModel
import com.gaoyun.yanyou_kototomo.ui.settings.sections.SettingsViewModel
import com.gaoyun.yanyou_kototomo.ui.statistics.StatisticsViewModel
import com.gaoyun.yanyou_kototomo.ui.statistics.full_list.StatisticsFullListViewModel
import com.gaoyun.yanyoukototomo.data.persistence.CardsPersisted
import com.gaoyun.yanyoukototomo.data.persistence.CoursesPersisted
import com.gaoyun.yanyoukototomo.data.persistence.QuizSessionsPersisted
import com.squareup.sqldelight.ColumnAdapter
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * iOS Koin initialisation entry point
 */
fun initKoin(appDeclaration: IOSAppDeclaration) = startKoin {
    val iosDependenciesModule = module {
        single { appDeclaration.themeChanger }
    }
    modules(
        platformModule(),
        iosDependenciesModule,
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
        dbModule
    )
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        platformModule(),
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
        dbModule
    )
}

val networkModule = module {
    single { PlatformHttpClient.httpClient() }
    single<DecksApi> { DecksApi(get()) }
}

val repositoryModule = module {
    single { CoursesRootComponentRepository(get(), get(), get(), get()) }
    single { DeckRepository(get(), get(), get()) }
    single { DeckUpdatesRepository(get(), get(), get()) }
    single { CardsAndProgressRepository(get()) }
    single { DeckSettingsRepository(get()) }
    single { QuizSessionRepository(get()) }
}

val useCaseModule = module {
    single { GetCoursesRoot(get()) }
    single { GetDeck(get(), get()) }
    single { GetDeckFromCache(get(), get()) }
    single { SpacedRepetitionCalculation() }
    single { CardProgressUpdater(get()) }
    single { DeckSettingsInteractor(get()) }
    single { QuizInteractor(get(), get(), get(), get()) }
    single { GetCardProgress(get()) }
    single { GetHomeState(get(), get(), get(), get(), get()) }
    single { BookmarksInteractor(get(), get()) }
    single { ColorsProvider(get()) }
    single { AllDataReset(get(), get()) }
}

val viewModelModule = module {
    single { AppNavigator() }
    factory { AppViewModel(get(), get()) }
    factory { HomeViewModel(get()) }
    factory { CoursesViewModel(get()) }
    factory { CourseDecksViewModel(get()) }
    factory { DeckOverviewViewModel(get(), get(), get(), get(), get()) }
    factory { DeckPlayerViewModel(get(), get(), get(), get(), get()) }
    factory { QuizSessionSummaryViewModel(get()) }
    factory { StatisticsViewModel(get(), get()) }
    factory { StatisticsFullListViewModel(get(), get()) }
    factory { SettingsViewModel(get(), get(), get()) }
}

val dbModule = module {
    single { Preferences() }
    single { get<DriverFactory>().createDriver() }
    single {
        YanYouKotoTomoDatabase(
            driver = get(),
            CardsPersistedAdapter = CardsPersisted.Adapter(get(), get(), get()),
            CoursesPersistedAdapter = CoursesPersisted.Adapter(get()),
            QuizSessionsPersistedAdapter = QuizSessionsPersisted.Adapter(get()),
        )
    }
    single<ColumnAdapter<List<String>, String>> {
        object : ColumnAdapter<List<String>, String> {
            override fun decode(databaseValue: String): List<String> =
                if (databaseValue.isEmpty()) emptyList() else databaseValue.split(",")

            override fun encode(value: List<String>): String = value.joinToString(",")
        }
    }
}