package com.gaoyun.yanyou_kototomo.network

import io.ktor.client.HttpClient

expect object PlatformHttpClient {
    fun httpClient(): HttpClient
}