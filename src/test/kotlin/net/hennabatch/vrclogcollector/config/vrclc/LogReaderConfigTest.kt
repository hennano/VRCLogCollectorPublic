package net.hennabatch.vrclogcollector.config.vrclc

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.file.Paths

@OptIn(ExperimentalKotest::class)
class LogReaderConfigTest: FunSpec({

    context("正常系"){
        test("コンフィグファイルなし"){
            val configTest = LogReaderConfig()

            configTest.vrcLogDirectoryPath shouldBe "${System.getProperty("user.home")}\\AppData\\LocalLow\\VRChat\\VRChat"
            configTest.cookieFilePath shouldBe "${System.getProperty("user.dir")}\\vrc-log-collector-cookie.txt"
        }

        test("コンフィグファイルあり"){
            val configJson = """
                {
                    "vrcLogDirectoryPath": "test\/path",
                    "cookieFilePath": "test\/pathpath"
                }
            """.trimIndent()

            val configTest = Json.decodeFromString<LogReaderConfig>(configJson)

            configTest.vrcLogDirectoryPath shouldBe "test/path"
            configTest.cookieFilePath shouldBe "test/pathpath"
        }

        test("コンフィグファイル中身なし"){
            val configJson = "{}"

            val configTest = Json.decodeFromString<LogReaderConfig>(configJson)

            configTest.vrcLogDirectoryPath shouldBe "${System.getProperty("user.home")}\\AppData\\LocalLow\\VRChat\\VRChat"
            configTest.cookieFilePath shouldBe "${System.getProperty("user.dir")}\\vrc-log-collector-cookie.txt"
        }
    }

    context("異常系"){
        test("コンフィグファイルがJsonではない"){
            val configJson = """
                cookieFilePath=test/path
                cookieFilePath=test/pathpath
            """.trimIndent()
            shouldThrow<SerializationException>{
                Json.decodeFromString<LogReaderConfig>(configJson)
            }
        }
    }

    /**
     * VRCを実行したPCでテストをすること
     */
    context("ファイル確認").config(enabled = false){
        test("デフォルトログファイルパスにVRCのログファイルがある"){
            val vrcLogPath = Paths.get(LogReaderConfig().vrcLogDirectoryPath)

            vrcLogPath.toFile().exists().shouldBeTrue()
            vrcLogPath.toFile().isDirectory.shouldBeTrue()

            val files = vrcLogPath.toFile().listFiles()?.map { it.name } ?: listOf()

            files.count { Regex("^output_log_[0-9]{4}-[0-9]{2}-[0-9]{2}_[0-9]{2}-[0-9]{2}-[0-9]{2}.txt$").matches(it) } shouldNotBe 0
        }
    }
})