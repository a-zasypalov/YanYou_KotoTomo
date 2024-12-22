package com.gaoyun.yanyou_kototomo

import com.gaoyun.yanyou_kototomo.data.persistence.DriverFactory
import com.gaoyun.yanyou_kototomo.data.persistence.Preferences
import com.gaoyun.yanyou_kototomo.data.persistence.YanYouKotoTomoDatabase
import com.gaoyun.yanyou_kototomo.data.persistence.platformModule
import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.network.DecksApi
import com.gaoyun.yanyou_kototomo.network.PlatformHttpClient
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository
import com.gaoyun.yanyou_kototomo.repository.DeckRepository
import com.gaoyun.yanyou_kototomo.repository.DeckUpdateRepository
import com.gaoyun.yanyou_kototomo.ui.AppViewModel
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppNavigator
import com.gaoyun.yanyou_kototomo.ui.course_decks.CourseDecksViewModel
import com.gaoyun.yanyou_kototomo.ui.courses.CoursesViewModel
import com.gaoyun.yanyou_kototomo.ui.deck_overview.DeckOverviewViewModel
import com.gaoyun.yanyou_kototomo.ui.home.HomeViewModel
import com.gaoyun.yanyou_kototomo.ui.player.DeckPlayerViewModel
import com.gaoyun.yanyoukototomo.data.persistence.CardsPersisted
import com.gaoyun.yanyoukototomo.data.persistence.CoursesPersisted
import com.squareup.sqldelight.ColumnAdapter
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

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
    single { DeckUpdateRepository(get(), get(), get()) }
}

val useCaseModule = module {
    single { GetCoursesRoot(get()) }
    single { GetDeck(get()) }
}

val viewModelModule = module {
    single { AppNavigator() }
    factory { AppViewModel(get()) }
    factory { HomeViewModel(get()) }
    factory { CoursesViewModel(get()) }
    factory { CourseDecksViewModel(get()) }
    factory { DeckOverviewViewModel(get(), get()) }
    factory { DeckPlayerViewModel(get(), get()) }
}

val dbModule = module {
    single { Preferences() }
    single { get<DriverFactory>().createDriver() }
    single<ColumnAdapter<List<String>, String>> {
        object : ColumnAdapter<List<String>, String> {
            override fun decode(databaseValue: String): List<String> =
                if (databaseValue.isEmpty()) emptyList() else databaseValue.split(",")

            override fun encode(value: List<String>): String = value.joinToString(",")
        }
    }
    single {
        YanYouKotoTomoDatabase(
            driver = get(),
            CardsPersistedAdapter = CardsPersisted.Adapter(
                wordsAdapter = get(),
                reading_onAdapter = get(),
                reading_kunAdapter = get()
            ),
            CoursesPersistedAdapter = CoursesPersisted.Adapter(
                required_decksAdapter = get()
            )
        )
    }
}