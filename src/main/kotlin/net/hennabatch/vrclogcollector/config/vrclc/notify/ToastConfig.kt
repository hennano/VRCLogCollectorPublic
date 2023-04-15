package net.hennabatch.vrclogcollector.config.vrclc.notify

import kotlinx.serialization.Serializable

/**
 * Toastに関する設定
 * @param psPath Toast通知で利用するpsファイルのパス
 */
@Serializable
data class ToastConfig(
    var psPath: String = "${System.getProperty("user.dir")}\\toast.ps",
)