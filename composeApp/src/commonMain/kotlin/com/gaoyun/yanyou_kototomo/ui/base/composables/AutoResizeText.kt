package com.gaoyun.yanyou_kototomo.ui.base.composables

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoResizeText(
    text: String,
    fontSizeRange: FontSizeRange,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    style: TextStyle = LocalTextStyle.current,
) {
    var calculatedFontSize by remember { mutableStateOf(fontSizeRange.max) }
    var ready by remember { mutableStateOf(false) }

    BoxWithConstraints(modifier) {
        val constraints = this.constraints
        val textMeasurer = rememberTextMeasurer()

        // Perform font size calculation only when constraints or text changes
        LaunchedEffect(text, constraints.maxWidth, constraints.maxHeight) {
            calculatedFontSize = calculateFontSize(
                text = text,
                fontSizeRange = fontSizeRange,
                maxLines = maxLines,
                textStyle = style,
                maxWidth = constraints.maxWidth,
                maxHeight = constraints.maxHeight,
                textMeasurer = textMeasurer
            )
            ready = true
        }

        if (ready) {
            Text(
                text = text,
                textAlign = textAlign,
                style = style.copy(
                    color = color,
                    fontSize = calculatedFontSize,
                    fontStyle = fontStyle,
                    textDecoration = textDecoration,
                ),
                minLines = minLines,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun calculateFontSize(
    text: String,
    fontSizeRange: FontSizeRange,
    maxLines: Int,
    textStyle: TextStyle,
    maxWidth: Int,
    maxHeight: Int,
    textMeasurer: TextMeasurer,
): TextUnit {
    var low = fontSizeRange.min.value
    var high = fontSizeRange.max.value
    var bestSize = low

    // Use binary search for fast font size resolution
    while (low <= high) {
        val mid = (low + high) / 2
        val result = textMeasurer.measure(
            text = AnnotatedString(text),
            style = textStyle.copy(fontSize = mid.sp),
            constraints = Constraints(maxWidth = maxWidth)
        )

        if (result.size.height <= maxHeight && result.lineCount <= maxLines) {
            bestSize = mid
            low = mid + fontSizeRange.step.value
        } else {
            high = mid - fontSizeRange.step.value
        }
    }
    return bestSize.sp
}

data class FontSizeRange(
    val min: TextUnit,
    val max: TextUnit,
    val step: TextUnit = 1.sp,
) {
    init {
        require(min < max) { "FontSizeRange: min should be less than max" }
        require(step.value > 0) { "FontSizeRange: step should be greater than 0" }
    }
}