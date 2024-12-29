package com.gaoyun.yanyou_kototomo.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gaoyun.yanyou_kototomo.data.local.deck.DeckWithCourseInfo
import com.gaoyun.yanyou_kototomo.ui.base.composables.AutoResizeText
import com.gaoyun.yanyou_kototomo.ui.base.composables.FontSizeRange
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.courseCardColor

@Composable
fun HomeScreenBookmarkedDeck(bookmark: DeckWithCourseInfo, onCourseClick: (DeckWithCourseInfo) -> Unit) {
    val courseCardColor = bookmark.info.courseId.courseCardColor()
    val courseTextColor = Color(0xFFEDE1D4)

    ElevatedCard(
        modifier = Modifier.platformStyleClickable { onCourseClick(bookmark) }.height(150.dp).widthIn(max = 150.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = courseCardColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize().background(Color(0x33000000)).padding(vertical = 8.dp)
        ) {
            AutoResizeText(
                text = bookmark.deck.name,
                fontSizeRange = FontSizeRange(min = 16.sp, max = MaterialTheme.typography.titleLarge.fontSize),
                color = courseTextColor,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(courseTextColor).padding(horizontal = 8.dp))
            Text(
                text = bookmark.info.preview,
                color = courseTextColor.copy(alpha = 0.35f),
                maxLines = 3,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
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