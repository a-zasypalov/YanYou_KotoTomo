package com.gaoyun.yanyou_kototomo.ui

import androidx.compose.runtime.Composable
import com.gaoyun.yanyou_kototomo.ui.base.ColorsProvider
import com.gaoyun.yanyou_kototomo.ui.base.navigation.AppNavigator
import com.gaoyun.yanyou_kototomo.ui.base.navigation.BackNavigationEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigationSideEffect
import com.gaoyun.yanyou_kototomo.ui.base.navigation.NavigatorAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

const val LAUNCH_LISTEN_FOR_EFFECTS = "app-launch-listen-to-effects"

class AppViewModel(
    private val appNavigator: AppNavigator,
    private val colorsProvider: ColorsProvider,
) : ViewModel() {
    private val _event: MutableSharedFlow<NavigationSideEffect> = MutableSharedFlow()
    private val _effect: Channel<NavigatorAction> = Channel()
    val navigationEffect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            _event.collect { event ->
                when (event) {
                    is BackNavigationEffect -> setEffect { NavigatorAction.NavigateBack }
                    else -> appNavigator.navigate(event)?.let { setEffect { it } }
                }
            }
        }
    }

    @Composable
    fun colorScheme() = colorsProvider.getCurrentScheme()

    private fun setEffect(builder: () -> NavigatorAction) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    fun navigate(event: NavigationSideEffect) {
        viewModelScope.launch { _event.emit(event) }
    }
}