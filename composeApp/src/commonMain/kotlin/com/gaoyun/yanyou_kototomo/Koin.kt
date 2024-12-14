package com.gaoyun.yanyou_kototomo

import com.gaoyun.yanyou_kototomo.network.DecksApi
import com.gaoyun.yanyou_kototomo.network.PlatformHttpClient
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(networkModule)
}

val networkModule = module {
    single { PlatformHttpClient.httpClient() }
    single<DecksApi> { DecksApi(get()) }
}