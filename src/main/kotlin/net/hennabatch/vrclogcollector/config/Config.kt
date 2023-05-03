package net.hennabatch.vrclogcollector.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.hennabatch.vrclogcollector.common.util.logger
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import kotlin.io.path.exists

/**
 * Config クラス
 * 設定の管理を行う
 */
object Config {
    lateinit var VRCLCConfig: VRCLCConfig

    /**
     * コンフィグをロードするときに呼ぶ
     * @param vrclcConfigPath 本体のコンフィグファイルパス
     * @exception NoSuchFileException 指定されたパスがディレクトリ
     */
    fun load(vrclcConfigPath: Path){
        if(vrclcConfigPath.toFile().isDirectory){
            //指定パスがディレクトリ
            logger.error("${vrclcConfigPath.toAbsolutePath()} is directory")
            throw NoSuchFileException(vrclcConfigPath.toAbsolutePath().toString())
        }

        if(!vrclcConfigPath.exists()){
            //指定パスに何もない
            logger.info("config file was not found: ${vrclcConfigPath.toAbsolutePath()}")
            makeDefaultConfigFile(vrclcConfigPath)
            logger.info("create default config file: ${vrclcConfigPath.toAbsolutePath()}")
        }

        //ファイル読み込み
        logger.info("load config file: ${vrclcConfigPath.toAbsolutePath()}")
        VRCLCConfig = readConfigFile(vrclcConfigPath)
    }

    /**
     * 指定されたパスにコンフィグファイルを生成する
     * @param vrclcConfigPath 本体のコンフィグファイルパス
     */
    private fun makeDefaultConfigFile(vrclcConfigPath: Path){
        val jsonText = Json{ encodeDefaults = true }.encodeToString(VRCLCConfig())
        vrclcConfigPath.toFile().writeText(jsonText, Charsets.UTF_8)
    }

    /**
     * 指定されたパスのコンフィグファイルを読み込む
     * @param vrclcConfigPath 本体のコンフィグファイルパス
     */
    @OptIn(ExperimentalSerializationApi::class)
    private fun readConfigFile(vrclcConfigPath: Path):VRCLCConfig{
        val stream = vrclcConfigPath.toFile().inputStream()
        val vrclcConfig =  Json.decodeFromStream<VRCLCConfig>(stream)
        stream.close()
        return vrclcConfig
    }
}