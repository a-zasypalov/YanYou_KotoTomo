package com.gaoyun.yanyou_kototomo.ui.base.shared_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor

@Composable
fun HorizontalCourseCardsList(
    decks: List<DeckWithCourseInfo>,
    onCourseClick: (DeckWithCourseInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        item { Spacer(Modifier.size(8.dp)) }
        items(decks) { deck -> HorizontalCourseCard(deck, onCourseClick) }
        item { Spacer(Modifier.size(8.dp)) }
    }
}

@Composable
fun HorizontalCourseCard(bookmark: DeckWithCourseInfo, onCourseClick: (DeckWithCourseInfo) -> Unit) {
    val courseCardColor = bookmark.info.courseId.courseCardColor()
    val courseTextColor = Color(0xFFEDE1D4)

    ElevatedCard(
        modifier = Modifier.platformStyleClickable { onCourseClick(bookmark) }.height(120.dp).widthIn(max = 150.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Column(modifier = Modifier.fillMaxSize().background(Color(0x33000000)).padding(vertical = 4.dp)) {
            AutoResizeText(
                text = bookmark.deck.name.formatBookmarkName(bookmark.info.courseName),
                fontSizeRange = FontSizeRange(min = 14.sp, max = MaterialTheme.typography.titleLarge.fontSize),
                color = courseTextColor,
                maxLines = 2,
                minLines = 2,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 4.dp),
            )
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(courseTextColor).padding(horizontal = 8.dp, vertical = 4.dp))
            Text(
                text = bookmark.info.preview,
                color = courseTextColor.copy(alpha = 0.6f),
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .weight(1f)
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding(horizontal = 4.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Cards: ${bookmark.deck.cards.size}",
                color = courseTextColor,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun String.formatBookmarkName(courseName: String): String {
    return if (startsWith(courseName)) {
        val rest = removePrefix(courseName).trim()
        if (rest.isNotEmpty()) "$courseName\n$rest" else courseName
    } else {
        val firstDelimiterIndex = indexOfAny(charArrayOf(' ', ','))
        if (firstDelimiterIndex != -1) {
            val firstWord = substring(0, firstDelimiterIndex)
            val rest = substring(firstDelimiterIndex).trim()
            "$firstWord\n$rest"
        } else {
            this
        }
    }
}