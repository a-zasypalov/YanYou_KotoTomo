package com.gaoyun.yanyou_kototomo.ui.settings.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.data.local.LanguageId
import com.gaoyun.yanyou_kototomo.ui.base.composables.platformStyleClickable
import com.gaoyun.yanyou_kototomo.ui.card_details.getRandomMascotImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import yanyou_kototomo.composeapp.generated.resources.Res
import yanyou_kototomo.composeapp.generated.resources.cancel
import yanyou_kototomo.composeapp.generated.resources.select_primary_language

@Composable
fun PrimaryLanguageChooser(
    showDialog: MutableState<Boolean>,
    availableLanguages: List<Pair<LanguageId, String>>,
    selectedLanguage: MutableState<String>,
    onPrimaryLanguageChange: (LanguageId) -> Unit,
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(stringResource(Res.string.select_primary_language)) },
            text = {
                Column {
                    availableLanguages.forEach { (id, languageName) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id.getRandomMascotImage()),
                                null,
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            )
                            Text(
                                text = languageName,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .platformStyleClickable {
                                        selectedLanguage.value = languageName
                                        showDialog.value = false
                                        onPrimaryLanguageChange(id)
                                    }
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        )
    }
}