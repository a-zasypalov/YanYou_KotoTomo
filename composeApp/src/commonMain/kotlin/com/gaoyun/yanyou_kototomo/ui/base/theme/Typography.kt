package com.gaoyun.yanyou_kototomo.ui.base.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.noto_sans_black
import yanyou_kototomo.composeapp.generated.resources.noto_sans_bold
import yanyou_kototomo.composeapp.generated.resources.noto_sans_extra_bold
import yanyou_kototomo.composeapp.generated.resources.noto_sans_extra_light
import yanyou_kototomo.composeapp.generated.resources.noto_sans_light
import yanyou_kototomo.composeapp.generated.resources.noto_sans_medium
import yanyou_kototomo.composeapp.generated.resources.noto_sans_regular
import yanyou_kototomo.composeapp.generated.resources.noto_sans_semi_bold
import yanyou_kototomo.composeapp.generated.resources.noto_sans_thin

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NotoSansFontFamily() = FontFamily(
    Font(Res.font.noto_sans_black, weight = FontWeight.Black),
    Font(Res.font.noto_sans_extra_bold, weight = FontWeight.ExtraBold),
    Font(Res.font.noto_sans_bold, weight = FontWeight.Bold),
    Font(Res.font.noto_sans_semi_bold, weight = FontWeight.SemiBold),
    Font(Res.font.noto_sans_medium, weight = FontWeight.Medium),
    Font(Res.font.noto_sans_regular, weight = FontWeight.Normal),
    Font(Res.font.noto_sans_light, weight = FontWeight.Light),
    Font(Res.font.noto_sans_extra_light, weight = FontWeight.ExtraLight),
    Font(Res.font.noto_sans_thin, weight = FontWeight.Thin),
)

@Composable
fun AppTypography() = Typography().run {
    val notoSans = NotoSansFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = notoSans),
        displayMedium = displayMedium.copy(fontFamily = notoSans),
        displaySmall = displaySmall.copy(fontFamily = notoSans),
        headlineLarge = headlineLarge.copy(fontFamily = notoSans),
        headlineMedium = headlineMedium.copy(fontFamily = notoSans),
        headlineSmall = headlineSmall.copy(fontFamily = notoSans),
        titleLarge = titleLarge.copy(fontFamily = notoSans),
        titleMedium = titleMedium.copy(fontFamily = notoSans),
        titleSmall = titleSmall.copy(fontFamily = notoSans),
        bodyLarge = bodyLarge.copy(fontFamily = notoSans),
        bodyMedium = bodyMedium.copy(fontFamily = notoSans),
        bodySmall = bodySmall.copy(fontFamily = notoSans),
        labelLarge = labelLarge.copy(fontFamily = notoSans),
        labelMedium = labelMedium.copy(fontFamily = notoSans),
        labelSmall = labelSmall.copy(fontFamily = notoSans),
    )
}

