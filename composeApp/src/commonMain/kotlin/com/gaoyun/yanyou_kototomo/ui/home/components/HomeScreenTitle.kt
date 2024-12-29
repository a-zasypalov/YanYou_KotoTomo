package com.gaoyun.yanyou_kototomo.ui.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.border_design
import yanyou_kototomo.composeapp.generated.resources.ic_home_title_cn

@Composable
fun HomeScreenTitle() {
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painterResource(Res.drawable.border_design),
            contentDescription = "",
            alpha = 0.4f,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.rotate(90f).size(48.dp)
        )
        Image(
            painter = painterResource(Res.drawable.ic_home_title_cn),
            contentDescription = "",
            alpha = 0.4f,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.size(48.dp)
        )
        Image(
            painter = painterResource(Res.drawable.border_design),
            contentDescription = "",
            alpha = 0.4f,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.rotate(180f).size(48.dp)
        )
    }
}