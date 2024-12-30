package com.gaoyun.yanyou_kototomo.util

import android.app.Activity
import android.content.ComponentName
import android.content.pm.PackageManager
import org.koin.core.component.KoinComponent

class ThemeChangerAndroid(private val activityProvider: ActivityProvider) : KoinComponent, ThemeChanger {

    private val icons = mapOf(
        AppIcon.Original to "com.gaoyun.yanyou_kototomo.ORIGINAL",
        AppIcon.Clip to "com.gaoyun.yanyou_kototomo.CLIP",
        AppIcon.VerticalHalf to "com.gaoyun.yanyou_kototomo.VERTICALHALF"
    )

    override fun applyTheme() {
        activityProvider.currentActivity?.recreate()
        activityProvider.mainActivity?.recreate()
    }

    override fun activateIcon(icon: AppIcon) {
        activityProvider.currentActivity?.let { tryActivateIcon(it, icon) }
        activityProvider.mainActivity?.let { tryActivateIcon(it, icon) }
    }

    private fun tryActivateIcon(activity: Activity, icon: AppIcon) {
        val enable = icons[icon] ?: return
        val disable = icons.filterNot { it.key == icon }

        disable.values.forEach {
            activity.packageManager.setComponentEnabledSetting(
                ComponentName(activity, it),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
            )
        }
        activity.packageManager.setComponentEnabledSetting(
            ComponentName(activity, enable),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }
}