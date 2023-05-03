package net.hennabatch.vrclogcollector.vrclog

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.hennabatch.vrclogcollector.common.util.logger
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import kotlin.io.path.exists

/**
 * Cookie クラス
 * キャッシュとして残したい情報を入れる
 * @param readingLogFile 読み込み対象のログファイル名
 * @param readRows 読み込み済みログ行数
 * @param launchIdStr launchIdの文字列
 */

@Serializable
data class Cookie (
    val readingLogFile: String = "",
    val readRows: Int = 0,
    val launchIdStr: String = "",
){

    companion object{
        fun read(cookiePath: Path): Cookie {
            if(cookiePath.toFile().isDirectory){
                //指定パスがディレクトリ
                logger.error("${cookiePath.toAbsolutePath()} is directory")
                throw NoSuchFileException(cookiePath.toAbsolutePath().toString())
            }

            if(!cookiePath.exists()){
                //指定パスに何もない
                logger.info("cookie file was not found: ${cookiePath.toAbsolutePath()}")
                makeDefaultCookieFile(cookiePath)
                logger.info("create default cookie file: ${cookiePath.toAbsolutePath()}")
            }

            //ファイル読み込み
            logger.info("read cookie file: ${cookiePath.toAbsolutePath()}")
            return readCookieFile(cookiePath)
        }

        /**
         * 指定されたパスにクッキーファイルを生成する
         * @param cookiePath 本体のコンフィグファイルパス
         */
        private fun makeDefaultCookieFile(cookiePath: Path){
            Cookie(
                readingLogFile = "",
                readRows = 0,
                launchIdStr = ""
            ).write(cookiePath)
        }

        /**
         * 指定されたパスのコンフィグファイルを読み込む
         * @param cookiePath 本体のコンフィグファイルパス
         */
        @OptIn(ExperimentalSerializationApi::class)
        private fun readCookieFile(cookiePath: Path): Cookie {
            val stream = cookiePath.toFile().inputStream()
            val cookie =  Json.decodeFromStream<Cookie>(stream)
            stream.close()
            return cookie
        }
    }

    fun write(cookiePath: Path){
        val jsonText = Json{ encodeDefaults = true }.encodeToString(this)
        cookiePath.toFile().writeText(jsonText, Charsets.UTF_8)
    }
}