package com.gaoyun.yanyou_kototomo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import moe.tlaster.precompose.PreComposeApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PreComposeApp {
                YanYouKotoTomoApp()
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    YanYouKotoTomoApp()
}