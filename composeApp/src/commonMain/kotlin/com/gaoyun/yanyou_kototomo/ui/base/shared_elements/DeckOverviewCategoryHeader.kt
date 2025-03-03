package com.gaoyun.yanyou_kototomo.ui.base.shared_elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.expand

@Composable
fun DeckOverviewCategoryHeader(name: String, isOpen: Boolean, modifier: Modifier = Modifier, onOpenToggle: ((Boolean) -> Unit)?) {
    val expandIconAngle = animateFloatAsState(targetValue = if (isOpen) 180f else 0f)
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, style = MaterialTheme.typography.headlineMedium)
            onOpenToggle?.let {
                IconButton(onClick = { onOpenToggle(!isOpen) }) {
                    Icon(Icons.Default.ExpandMore, stringResource(Res.string.expand), modifier = Modifier.rotate(expandIconAngle.value))
                }
            }
        }
        Divider(1.dp, modifier = Modifier.fillMaxWidth())
    }
}