package com.gaoyun.yanyou_kototomo.ui.base.shared_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
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
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.cards_number

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
                text = bookmark.deck.formatDeckChaptersWithLineSeparator(bookmark.info.courseName),
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
                text = stringResource(Res.string.cards_number, bookmark.deck.cards.size),
                color = courseTextColor,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}