package com.gaoyun.yanyou_kototomo

import com.gaoyun.yanyou_kototomo.domain.GetCoursesRoot
import com.gaoyun.yanyou_kototomo.domain.GetDeck
import com.gaoyun.yanyou_kototomo.network.DecksApi
import com.gaoyun.yanyou_kototomo.network.PlatformHttpClient
import com.gaoyun.yanyou_kototomo.repository.CoursesRootComponentRepository
import com.gaoyun.yanyou_kototomo.repository.DeckRepository
import com.gaoyun.yanyou_kototomo.ui.HomeViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(networkModule, repositoryModule, useCaseModule, viewModelModule)
}

val networkModule = module {
    single { PlatformHttpClient.httpClient() }
    single<DecksApi> { DecksApi(get()) }
}

val repositoryModule = module {
    single { CoursesRootComponentRepository(get()) }
    single { DeckRepository(get()) }
}

val useCaseModule = module {
    single { GetCoursesRoot(get()) }
    single { GetDeck(get()) }
}

val viewModelModule = module {
    factory { HomeViewModel(get(), get()) }
}