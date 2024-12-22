package com.gaoyun.yanyou_kototomo.ui.base.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryElevatedButton(
    text: String,
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ),
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    ElevatedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = colors,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        ),
        modifier = modifier
    ) {
        leadingIcon?.let { Icon(it, null, modifier = Modifier.align(Alignment.CenterVertically)) }
        Text(text = text, modifier = Modifier.padding(8.dp).align(Alignment.CenterVertically))
        trailingIcon?.let { Icon(it, null, modifier = Modifier.align(Alignment.CenterVertically)) }
    }
}