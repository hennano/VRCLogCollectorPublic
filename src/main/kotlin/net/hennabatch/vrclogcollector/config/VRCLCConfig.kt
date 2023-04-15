package net.hennabatch.vrclogcollector.config

import kotlinx.serialization.Serializable
import net.hennabatch.vrclogcollector.config.vrclc.LogReaderConfig
import net.hennabatch.vrclogcollector.config.vrclc.NotifyConfig
import net.hennabatch.vrclogcollector.config.vrclc.OSCListenerConfig

/**
 * VRCLCに関する設定
 * @param activateLogReader ログ解析機能の有効化
 * @param activateOSCListener OSC機能の有効化
 * @param activateNotify 通知機能の有効化
 * @param eventPollingTimeout VRC終了後のイベント待機時間
 * @param lockFilePath ロックファイルのパス
 * @param pluginDirectoryPath プラグインディレクトリのパス
 * @param logReader LogReaderに関する設定
 * @param oscListener OSCListenerの関する設定
 * @param notify 通知に関する設定
 */
@Serializable
data class VRCLCConfig(
    var activateLogReader: Boolean = true,
    var activateOSCListener: Boolean = true,
    var activateNotify: Boolean = true,
    var eventPollingTimeout: Long = 300,
    var lockFilePath: String = "${System.getProperty("user.dir")}\\vrc-log-collector-lock.lock",
    var pluginDirectoryPath: String = "${System.getProperty("user.dir")}\\plugins",

    var logReader: LogReaderConfig = LogReaderConfig(),
    var oscListener: OSCListenerConfig = OSCListenerConfig(),
    val notify: NotifyConfig = NotifyConfig()
)
