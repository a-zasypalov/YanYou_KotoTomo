package com.gaoyun.yanyou_kototomo.ui.player

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.Card
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerAdditionalInfo
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerReading
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerTranscription
import com.gaoyun.yanyou_kototomo.ui.player.components.CardPlayerTranslation

@Composable
fun ColumnScope.CardPlayerDetailsWord(card: Card.WordCard) {
    CardPlayerTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardPlayerTranslation(card.translation)
    card.additionalInfo?.let { CardPlayerAdditionalInfo(it) }
}

@Composable
fun ColumnScope.CardPlayerDetailsKanaCard(card: Card.KanaCard) {
    CardPlayerTranscription(
        transcription = "[${card.transcription}] ${card.mirror.front}",
        preformatted = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ColumnScope.CardPlayerDetailsKanjiCard(card: Card.KanjiCard) {
    CardPlayerReading(card.reading, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardPlayerTranslation(card.translation)
    card.additionalInfo?.let { CardPlayerAdditionalInfo(it) }
}

@Composable
fun ColumnScope.CardPlayerDetailsPhraseCard(card: Card.PhraseCard) {
    CardPlayerTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardPlayerTranslation(card.translation)
    card.additionalInfo?.let { CardPlayerAdditionalInfo(it) }
}