package com.gaoyun.yanyou_kototomo.domain

import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import org.jetbrains.compose.resources.StringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.chinese
import yanyou_kototomo.composeapp.generated.resources.english
import yanyou_kototomo.composeapp.generated.resources.japanese

fun LanguageId.toStringRes(): StringResource = when (identifier) {
    "en" -> Res.string.english
    "cn" -> Res.string.chinese
    "jp" -> Res.string.japanese
    else -> Res.string.english
}