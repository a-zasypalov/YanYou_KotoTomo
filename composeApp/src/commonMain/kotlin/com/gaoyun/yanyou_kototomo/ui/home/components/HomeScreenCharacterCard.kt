package com.gaoyun.yanyou_kototomo.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.data.local.card.CardWithProgress
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable

@Composable
fun HomeScreenCharacterCard(card: CardWithProgress<*>, languageId: LanguageId, onClick: (CardWithProgress<*>, LanguageId) -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().platformStyleClickable(onClick = { onClick(card, languageId) }),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = card.card.front,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}