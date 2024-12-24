package com.gaoyun.yanyou_kototomo.ui.base.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gaoyun.yanyou_kototomo.util.Platform
import com.gaoyun.yanyou_kototomo.util.PlatformNames

enum class BackButtonType {
    Back, Close
}

@Composable
fun SurfaceScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.Companion.End,
    containerColor: Color = MaterialTheme.colorScheme.surfaceBright,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    backButtonType: BackButtonType = BackButtonType.Back,
    backHandler: (() -> Unit)? = null,
    actionButtons: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        contentColor = contentColor,
        containerColor = containerColor,
        contentWindowInsets = contentWindowInsets,
    ) {
        Box(
            modifier = modifier.imePadding().padding(WindowInsets.statusBars.asPaddingValues())
        ) {
            Column {
                if (backHandler != null) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            modifier = Modifier.padding(start = 4.dp),
                            onClick = backHandler,
                            colors = IconButtonDefaults.iconButtonColors(contentColor = contentColor)
                        ) {
                            when (backButtonType) {
                                BackButtonType.Back -> {
                                    if (Platform.name == PlatformNames.Android) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "" //TODO: content description
                                        )
                                    } else {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBackIos,
                                            contentDescription = "" //TODO: content description
                                        )
                                    }
                                }

                                BackButtonType.Close -> Icon(
                                    Icons.Default.Close,
                                    contentDescription = "" //TODO: content description
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        actionButtons()
                    }
                }
                content()
            }
        }
    }
}