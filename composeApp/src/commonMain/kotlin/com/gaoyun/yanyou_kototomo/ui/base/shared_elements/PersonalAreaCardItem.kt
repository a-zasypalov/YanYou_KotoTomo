package com.gaoyun.yanyou_kototomo.ui.base.shared_elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.domain.mapIntervalToColor
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.base.deckColor
import com.gaoyun.yanyou_kototomo.ui.card_details.getDeckMascot
import com.gaoyun.yanyou_kototomo.util.toRelativeFormat
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.current_interval_days

@Composable
fun PersonalAreaCardItem(
    card: CardWithProgress.WithDeckInfo<*>,
    modifier: Modifier = Modifier,
    onCardClick: (CardWithProgress<*>, LanguageId) -> Unit,
) {
    val cardTop = buildAnnotatedString {
        withStyle(MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium).toSpanStyle()) {
            append(card.card.front)
        }
        withStyle(MaterialTheme.typography.titleSmall.toSpanStyle()) {
            append(" (${card.card.answer()})")
        }
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp,
    ) {
        Box(modifier = Modifier.fillMaxSize().platformStyleClickable { onCardClick(card, card.learningLanguageId) }) {
            Column(modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 4.dp, bottom = 4.dp)) {
                Text(
                    text = cardTop,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .padding(bottom = if (card.progress?.interval == null) 32.dp else 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(bottom = 2.dp)
                ) {
                    card.progress?.interval?.let { interval ->
                        Box(
                            modifier = Modifier.size(16.dp).padding(2.dp)
                                .background(color = mapIntervalToColor(interval), shape = CircleShape)
                        )
                        Text(
                            text = pluralStringResource(Res.plurals.current_interval_days, interval, interval),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    card.progress?.nextReview?.let { nextReview ->
                        Icon(
                            imageVector = Icons.Default.EventRepeat,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Review ${card.progress.nextReview.toRelativeFormat()}",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .background(card.deckId.deckColor(), shape = MaterialTheme.shapes.small)
            ) {
                val contentColor = Color(0xFFEDE1D4)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(all = 4.dp)
                ) {
                    Text(
                        text = card.deckName,
                        color = contentColor,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    )
                    Image(
                        painter = painterResource(card.deckId.getDeckMascot()),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(contentColor),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}