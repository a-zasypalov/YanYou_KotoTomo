package com.gaoyun.yanyou_kototomo.ui.card_details

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.card.Card
import com.gaoyun.yanyou_kototomo.ui.base.composables.Divider

@Composable
internal fun ColumnScope.CardDetailsWord(card: Card.WordCard) {
    CardDetailsFront(card.front, modifier = Modifier.fillMaxWidth())
    CardDetailsTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardDetailsTranslation(card.translation, modifier = Modifier.fillMaxWidth())
    card.additionalInfo?.let { CardDetailsAdditionalInfo(it) }
}

@Composable
internal fun ColumnScope.CardDetailsKanaCard(card: Card.KanaCard) {
    CardDetailsFront(front = card.front)
    CardDetailsTranscription(
        transcription = "[${card.transcription}] ${card.mirror.front}",
        preformatted = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
internal fun ColumnScope.CardDetailsKanjiCard(card: Card.KanjiCard) {
    CardDetailsFront(
        front = card.front,
        modifier = Modifier.weight(1f).padding(horizontal = 24.dp),
        leftAttachment = { CardDetailsReading(card.reading.on, Modifier.align(Alignment.CenterStart)) },
        rightAttachment = { CardDetailsReading(card.reading.kun, Modifier.align(Alignment.CenterEnd)) }
    )
    CardDetailsTranscription(card.transcription(), modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardDetailsTranslation(card.translation, modifier = Modifier.fillMaxWidth())
    card.additionalInfo?.let { CardDetailsAdditionalInfo(it) }
}

@Composable
internal fun ColumnScope.CardDetailsPhraseCard(card: Card.PhraseCard) {
    CardDetailsFront(card.front)
    CardDetailsTranscription(card.transcription, modifier = Modifier.fillMaxWidth())
    Divider(2.dp, Modifier.padding(vertical = 4.dp))
    CardDetailsTranslation(card.translation, modifier = Modifier.fillMaxWidth())
    card.additionalInfo?.let { CardDetailsAdditionalInfo(it) }
}