package com.gaoyun.yanyou_kototomo.ui.personal_space.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.domain.mapIntervalToColor
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.util.toRelativeFormat
import org.jetbrains.compose.resources.pluralStringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.current_interval_days

@Composable
fun PersonalAreaCardItem(card: CardWithProgress.WithDeckInfo<*>, onCardClick: (CardWithProgress<*>) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().platformStyleClickable { onCardClick(card) },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = card.card.front,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                )
                Text(
                    text = "(${card.card.answer()})",
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Spacer(Modifier.weight(1f))

                card.progress?.nextReview?.let { nextReview ->
                    Icon(
                        imageVector = Icons.Default.EventRepeat,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = card.progress.nextReview.toRelativeFormat(),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                //Deck item

                Spacer(Modifier.weight(1f))

                card.progress?.interval?.let { interval ->
                    Text(
                        text = pluralStringResource(Res.plurals.current_interval_days, interval, interval),
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Box(modifier = Modifier.size(12.dp).background(color = mapIntervalToColor(interval), shape = CircleShape))
                }
            }
        }
    }
}