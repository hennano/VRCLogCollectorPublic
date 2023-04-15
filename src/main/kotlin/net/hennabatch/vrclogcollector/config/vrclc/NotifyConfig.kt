package net.hennabatch.vrclogcollector.config.vrclc

import kotlinx.serialization.Serializable
import net.hennabatch.vrclogcollector.config.vrclc.notify.ToastConfig
import net.hennabatch.vrclogcollector.config.vrclc.notify.XSOverlayConfig

/**
 * 通知に関する設定
 * @param xsOverlayConfig XSOverlayに関する設定
 * @param toastConfig Toastに関する設定
 */
@Serializable
data class NotifyConfig(
    var xsOverlayConfig: XSOverlayConfig = XSOverlayConfig(),
    var toastConfig: ToastConfig = ToastConfig()
)