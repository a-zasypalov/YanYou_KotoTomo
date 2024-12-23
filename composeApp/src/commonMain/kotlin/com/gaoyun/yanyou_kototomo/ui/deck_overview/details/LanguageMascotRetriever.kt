package com.gaoyun.yanyou_kototomo.ui.deck_overview.details

import com.gaoyun.yanyou_kototomo.data.local.Course
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import org.jetbrains.compose.resources.DrawableResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.bamboo
import yanyou_kototomo.composeapp.generated.resources.bamboo_2
import yanyou_kototomo.composeapp.generated.resources.chinese_knot
import yanyou_kototomo.composeapp.generated.resources.dragon
import yanyou_kototomo.composeapp.generated.resources.fan
import yanyou_kototomo.composeapp.generated.resources.fuji_mountain
import yanyou_kototomo.composeapp.generated.resources.great_wall
import yanyou_kototomo.composeapp.generated.resources.hat
import yanyou_kototomo.composeapp.generated.resources.itsukushima_shrine
import yanyou_kototomo.composeapp.generated.resources.knot
import yanyou_kototomo.composeapp.generated.resources.koinobori
import yanyou_kototomo.composeapp.generated.resources.lantern
import yanyou_kototomo.composeapp.generated.resources.lotus
import yanyou_kototomo.composeapp.generated.resources.maneki_neko
import yanyou_kototomo.composeapp.generated.resources.maneki_neko_2
import yanyou_kototomo.composeapp.generated.resources.noodles
import yanyou_kototomo.composeapp.generated.resources.osaka_castle
import yanyou_kototomo.composeapp.generated.resources.panda
import yanyou_kototomo.composeapp.generated.resources.ramen
import yanyou_kototomo.composeapp.generated.resources.resource_break
import yanyou_kototomo.composeapp.generated.resources.rice
import yanyou_kototomo.composeapp.generated.resources.sakura
import yanyou_kototomo.composeapp.generated.resources.shrine
import yanyou_kototomo.composeapp.generated.resources.skyscrapers
import yanyou_kototomo.composeapp.generated.resources.tea
import yanyou_kototomo.composeapp.generated.resources.tea_ceremony
import yanyou_kototomo.composeapp.generated.resources.temple
import yanyou_kototomo.composeapp.generated.resources.tokyo
import yanyou_kototomo.composeapp.generated.resources.wave
import yanyou_kototomo.composeapp.generated.resources.wonton

private val jpImages = listOf(
    Res.drawable.maneki_neko,
    Res.drawable.bamboo,
    Res.drawable.bamboo_2,
    Res.drawable.fan,
    Res.drawable.fuji_mountain,
    Res.drawable.itsukushima_shrine,
    Res.drawable.koinobori,
    Res.drawable.maneki_neko_2,
    Res.drawable.osaka_castle,
    Res.drawable.ramen,
    Res.drawable.resource_break,
    Res.drawable.sakura,
    Res.drawable.shrine,
    Res.drawable.tokyo,
    Res.drawable.wave,
)

private val cnImages = listOf(
    Res.drawable.chinese_knot,
    Res.drawable.dragon,
    Res.drawable.great_wall,
    Res.drawable.hat,
    Res.drawable.knot,
    Res.drawable.lantern,
    Res.drawable.lotus,
    Res.drawable.noodles,
    Res.drawable.panda,
    Res.drawable.rice,
    Res.drawable.skyscrapers,
    Res.drawable.tea,
    Res.drawable.tea_ceremony,
    Res.drawable.temple,
    Res.drawable.wonton,
)

fun LanguageId.getRandomMascotImage(): DrawableResource {
    return when(this.identifier) {
        "jp" -> jpImages.random()
        "cn" -> cnImages.random()
        else -> Res.drawable.sakura
    }
}

fun Course.getCourseMascot(): DrawableResource {
    val id = this.id.identifier
    return when {
        id.contains("hsk1") -> Res.drawable.tea
        id.contains("hsk2") -> Res.drawable.rice
        id.contains("hsk3") -> Res.drawable.lantern
        id.contains("hsk4") -> Res.drawable.knot
        id.contains("hsk5") -> Res.drawable.wonton
        id.contains("hsk6") -> Res.drawable.temple
        id.contains("kana") -> Res.drawable.sakura
        id.contains("jlpt5") -> Res.drawable.bamboo
        id.contains("jlpt4") -> Res.drawable.koinobori
        id.contains("jlpt3") -> Res.drawable.fuji_mountain
        id.contains("jlpt2") -> Res.drawable.maneki_neko
        id.contains("jlpt1") -> Res.drawable.shrine
        else -> Res.drawable.sakura
    }
}
