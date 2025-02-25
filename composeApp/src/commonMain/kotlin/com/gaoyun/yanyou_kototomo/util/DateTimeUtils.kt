package com.gaoyun.yanyou_kototomo.util

import androidx.compose.runtime.Composable
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.month_apr
import yanyou_kototomo.composeapp.generated.resources.month_aug
import yanyou_kototomo.composeapp.generated.resources.month_dec
import yanyou_kototomo.composeapp.generated.resources.month_feb
import yanyou_kototomo.composeapp.generated.resources.month_jan
import yanyou_kototomo.composeapp.generated.resources.month_jul
import yanyou_kototomo.composeapp.generated.resources.month_jun
import yanyou_kototomo.composeapp.generated.resources.month_mar
import yanyou_kototomo.composeapp.generated.resources.month_may
import yanyou_kototomo.composeapp.generated.resources.month_nov
import yanyou_kototomo.composeapp.generated.resources.month_oct
import yanyou_kototomo.composeapp.generated.resources.month_sep
import yanyou_kototomo.composeapp.generated.resources.relative_days_ago
import yanyou_kototomo.composeapp.generated.resources.relative_in_days
import yanyou_kototomo.composeapp.generated.resources.relative_in_days_short
import yanyou_kototomo.composeapp.generated.resources.relative_in_months
import yanyou_kototomo.composeapp.generated.resources.relative_in_months_short
import yanyou_kototomo.composeapp.generated.resources.relative_in_weeks
import yanyou_kototomo.composeapp.generated.resources.relative_in_weeks_short
import yanyou_kototomo.composeapp.generated.resources.relative_in_years
import yanyou_kototomo.composeapp.generated.resources.relative_in_years_short
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
        daysDifference <= 0 -> stringResource(Res.string.relative_today)
        daysDifference in 1..6 -> pluralStringResource(Res.plurals.relative_in_days, daysDifference, daysDifference)
        daysDifference in 7..30 -> pluralStringResource(Res.plurals.relative_in_weeks, daysDifference / 7, daysDifference / 7)
        daysDifference in 31..365 -> pluralStringResource(Res.plurals.relative_in_months, daysDifference / 30, daysDifference / 30)
        daysDifference > 365 -> pluralStringResource(Res.plurals.relative_in_years, daysDifference / 365, daysDifference / 365)
        else -> this.toString() // Fallback to ISO-8601 format
    }
}

@Composable
fun LocalDate.toReviewRelativeShortFormat(): String {
    val daysDifference = localDateNow().daysUntil(this)

    return when {
        daysDifference <= 0 -> stringResource(Res.string.relative_today)
        daysDifference in 1..6 -> stringResource(Res.string.relative_in_days_short, daysDifference, daysDifference)
        daysDifference in 7..30 -> stringResource(Res.string.relative_in_weeks_short, daysDifference / 7, daysDifference / 7)
        daysDifference in 31..365 -> stringResource(Res.string.relative_in_months_short, daysDifference / 30, daysDifference / 30)
        daysDifference > 365 -> stringResource(Res.string.relative_in_years_short, daysDifference / 365, daysDifference / 365)
        else -> this.toString() // Fallback to ISO-8601 format
    }
}

@Composable
fun LocalDateTime.formatDateTimeStatistics(): String {
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    val localizedMonth = getLocalizedMonthName(month)

    return if (year == currentYear) {
        // Format: DD MMM HH:mm
        "${dayOfMonth.toString().padStart(2, '0')} $localizedMonth ${hour.toString().padStart(2, '0')}:${
            minute.toString().padStart(2, '0')
        }"
    } else {
        // Format: DD MMM YYYY
        "${dayOfMonth.toString().padStart(2, '0')} $localizedMonth $year"
    }
}

@Composable
fun getLocalizedMonthName(month: Month): String {
    val monthResId = when (month) {
        Month.JANUARY -> Res.string.month_jan
        Month.FEBRUARY -> Res.string.month_feb
        Month.MARCH -> Res.string.month_mar
        Month.APRIL -> Res.string.month_apr
        Month.MAY -> Res.string.month_may
        Month.JUNE -> Res.string.month_jun
        Month.JULY -> Res.string.month_jul
        Month.AUGUST -> Res.string.month_aug
        Month.SEPTEMBER -> Res.string.month_sep
        Month.OCTOBER -> Res.string.month_oct
        Month.NOVEMBER -> Res.string.month_nov
        Month.DECEMBER -> Res.string.month_dec
        else -> Res.string.month_dec
    }
    return stringResource(resource = monthResId)
}

@Composable
fun LocalDate.toRelativeFormatWithPastSupport(): String {
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
