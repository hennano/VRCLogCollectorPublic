package net.hennabatch.vrclogcollector.config.vrclc

import kotlinx.serialization.Serializable

/**
 * OSCListenerの関する設定
 * @param vrcIp VRCを実行しているIPアドレス
 * @param vrcInPort VRCが受け付けるポート
 * @param vrcOutPort VRCから送信されるOSCのポート
 */
@Serializable
data class OSCListenerConfig (
    var vrcOutPort: Int = 9000,
    var vrcInPort: Int = 9001,
    var vrcIp: String = "localhost"
)
