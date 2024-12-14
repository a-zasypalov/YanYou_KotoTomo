package com.gaoyun.yanyou_kototomo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform