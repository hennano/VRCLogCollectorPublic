package net.hennabatch.vrclogcollector.config.vrclc.notify

import kotlinx.serialization.Serializable

/**
 * XSOverlayに関する設定
 * @param ip XSOverlayを実行しているIPアドレス
 * @param port XSOverlayが受け付けるポート
 */
@Serializable
data class XSOverlayConfig(
    var ip: String = "localhost",
    var port: Int = 42069
)