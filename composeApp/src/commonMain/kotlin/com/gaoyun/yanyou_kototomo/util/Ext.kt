package com.gaoyun.yanyou_kototomo.util

import androidx.compose.runtime.MutableState

fun MutableState<Boolean>.toggle() {
    value = !value
}