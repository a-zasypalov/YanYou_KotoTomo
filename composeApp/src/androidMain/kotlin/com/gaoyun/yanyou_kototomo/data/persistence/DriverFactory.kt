package com.gaoyun.yanyou_kototomo.data.persistence

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.dsl.module

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(YanYouKotoTomoDatabase.Schema, context, "yanyou_kototomo.db")
    }
}

actual fun platformModule() = module {
    single { DriverFactory(get()) }
}