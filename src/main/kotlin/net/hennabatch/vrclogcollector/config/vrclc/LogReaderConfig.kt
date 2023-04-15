package net.hennabatch.vrclogcollector.config.vrclc

import kotlinx.serialization.Serializable

/**
 * LogReaderに関する設定
 * @param vrcLogDirectoryPath VRCのログファイルが生成されるディレクトリのパス
 * @param cookieFilePath LogReaderが生成するクッキーファイルのパス
 */
@Serializable
data class LogReaderConfig (
    var vrcLogDirectoryPath: String = "${System.getProperty("user.home")}\\AppData\\LocalLow\\VRChat\\VRChat",
    var cookieFilePath: String = "${System.getProperty("user.dir")}\\vrc-log-collector-cookie.txt"
)
