package com.gaoyun.yanyou_kototomo.data.persistence

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.dsl.module

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(YanYouKotoTomoDatabase.Schema, "yanyou_kototomo.db")
    }
}

actual fun platformModule() = module {
    single { DriverFactory() }
}