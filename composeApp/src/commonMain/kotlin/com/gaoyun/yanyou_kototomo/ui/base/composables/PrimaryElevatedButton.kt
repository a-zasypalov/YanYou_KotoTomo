package com.gaoyun.yanyou_kototomo.ui.base.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrimaryElevatedButton(
    text: String,
    maxLines: Int = 1,
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit,
) {
    PrimaryElevatedButton(
        text = AnnotatedString(text = text),
        maxLines = maxLines,
        colors = colors,
        contentPadding = contentPadding,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        onClick = onClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrimaryElevatedButton(
    text: AnnotatedString,
    maxLines: Int = 1,
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
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
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        leadingIcon?.let { Icon(it, null, modifier = Modifier.align(Alignment.CenterVertically)) }
        Text(
            text = text,
            textAlign = TextAlign.Center,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(8.dp).align(Alignment.CenterVertically)
        )
        trailingIcon?.let { Icon(it, null, modifier = Modifier.align(Alignment.CenterVertically)) }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrimaryElevatedMarqueeButton(
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
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier
    ) {
        leadingIcon?.let { Icon(it, null, modifier = Modifier.align(Alignment.CenterVertically)) }
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(8.dp).align(Alignment.CenterVertically).basicMarquee()
        )
        trailingIcon?.let { Icon(it, null, modifier = Modifier.align(Alignment.CenterVertically)) }
    }
}