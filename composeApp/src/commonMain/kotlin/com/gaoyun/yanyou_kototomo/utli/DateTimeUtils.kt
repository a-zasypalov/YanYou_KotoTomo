package com.gaoyun.yanyou_kototomo.utli

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun localDateTimeNow() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
fun now() = Clock.System.now()