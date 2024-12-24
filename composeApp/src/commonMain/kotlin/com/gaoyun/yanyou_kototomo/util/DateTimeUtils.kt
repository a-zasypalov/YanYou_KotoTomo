package com.gaoyun.yanyou_kototomo.util

import androidx.compose.runtime.Composable
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.relative_days_ago
import yanyou_kototomo.composeapp.generated.resources.relative_in_days
import yanyou_kototomo.composeapp.generated.resources.relative_in_months
import yanyou_kototomo.composeapp.generated.resources.relative_in_weeks
import yanyou_kototomo.composeapp.generated.resources.relative_in_years
import yanyou_kototomo.composeapp.generated.resources.relative_months_ago
import yanyou_kototomo.composeapp.generated.resources.relative_today
import yanyou_kototomo.composeapp.generated.resources.relative_weeks_ago
import yanyou_kototomo.composeapp.generated.resources.relative_years_ago
import yanyou_kototomo.composeapp.generated.resources.relative_yesterday

fun localDateNow() = localDateTimeNow().date
fun localDateTimeNow() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
fun now() = Clock.System.now()

@Composable
fun LocalDate.toRelativeFormat(): String {
    val daysDifference = localDateNow().daysUntil(this)

    return when {
        daysDifference == 0 -> stringResource(Res.string.relative_today)
        daysDifference == -1 -> stringResource(Res.string.relative_yesterday)
        daysDifference in -6..-2 -> pluralStringResource(Res.plurals.relative_days_ago, -daysDifference, -daysDifference)
        daysDifference in 1..6 -> pluralStringResource(Res.plurals.relative_in_days, daysDifference, daysDifference)
        daysDifference in -30..-7 -> pluralStringResource(Res.plurals.relative_weeks_ago, -daysDifference / 7, -daysDifference / 7)
        daysDifference in 7..30 -> pluralStringResource(Res.plurals.relative_in_weeks, daysDifference / 7, daysDifference / 7)
        daysDifference in -365..-31 -> pluralStringResource(Res.plurals.relative_months_ago, -daysDifference / 30, -daysDifference / 30)
        daysDifference in 31..365 -> pluralStringResource(Res.plurals.relative_in_months, daysDifference / 30, daysDifference / 30)
        daysDifference < -365 -> pluralStringResource(Res.plurals.relative_years_ago, -daysDifference / 365, -daysDifference / 365)
        daysDifference > 365 -> pluralStringResource(Res.plurals.relative_in_years, daysDifference / 365, daysDifference / 365)
        else -> this.toString() // Fallback to ISO-8601 format
    }
}