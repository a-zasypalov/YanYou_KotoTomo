package com.gaoyun.yanyou_kototomo.util

expect object Platform {
    val name: PlatformNames
    val supportsDynamicColor: Boolean
}

enum class PlatformNames {
    Android, IOS
}