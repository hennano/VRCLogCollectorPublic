package net.hennabatch.vrclogcollector.config

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.hennabatch.vrclogcollector.config.vrclc.LogReaderConfig
import net.hennabatch.vrclogcollector.config.vrclc.NotifyConfig
import net.hennabatch.vrclogcollector.config.vrclc.OSCListenerConfig

class VRCLCConfigTest: FunSpec({
    context("正常系"){
        test("コンフィグファイルなし"){
            val configTest = VRCLCConfig()
            
            configTest.activateLogReader.shouldBeTrue()
            configTest.activateOSCListener.shouldBeTrue()
            configTest.activateNotify.shouldBeTrue()
            configTest.eventPollingTimeoutSecond shouldBe 300
            configTest.lockFilePath shouldBe "${System.getProperty("user.dir")}\\vrc-log-collector-lock.lock"
            configTest.pluginDirectoryPath shouldBe "${System.getProperty("user.dir")}\\plugins"

            configTest.logReader::class shouldBe LogReaderConfig::class
            configTest.oscListener::class shouldBe OSCListenerConfig::class
            configTest.notify::class shouldBe NotifyConfig::class
        }

        test("コンフィグファイルあり"){
            val configJson = """
                {
                    "activateLogReader": false,
                    "activateOSCListener": false,
                    "activateNotify": false,
                    "eventPollingTimeoutSecond": 60,
                    "lockFilePath": "test\/path",
                    "pluginDirectoryPath": "test\/path\/tete",
                    "logReader": {},
                    "oscListener": {},
                    "notify": {}
                }
            """.trimIndent()

            val configTest = Json.decodeFromString<VRCLCConfig>(configJson)

            configTest.activateLogReader.shouldBeFalse()
            configTest.activateOSCListener.shouldBeFalse()
            configTest.activateNotify.shouldBeFalse()
            configTest.eventPollingTimeoutSecond shouldBe 60
            configTest.lockFilePath shouldBe "test/path"
            configTest.pluginDirectoryPath shouldBe "test/path/tete"

            configTest.logReader::class shouldBe LogReaderConfig::class
            configTest.oscListener::class shouldBe OSCListenerConfig::class
            configTest.notify::class shouldBe NotifyConfig::class

        }

        test("コンフィグファイル中身なし"){
            val configJson = "{}"

            val configTest = Json.decodeFromString<VRCLCConfig>(configJson)

            configTest.activateLogReader.shouldBeTrue()
            configTest.activateOSCListener.shouldBeTrue()
            configTest.activateNotify.shouldBeTrue()
            configTest.eventPollingTimeoutSecond shouldBe 300
            configTest.lockFilePath shouldBe "${System.getProperty("user.dir")}\\vrc-log-collector-lock.lock"
            configTest.pluginDirectoryPath shouldBe "${System.getProperty("user.dir")}\\plugins"

            configTest.logReader::class shouldBe LogReaderConfig::class
            configTest.oscListener::class shouldBe OSCListenerConfig::class
            configTest.notify::class shouldBe NotifyConfig::class
        }
    }

    context("異常系"){
        test("コンフィグファイルがJsonではない"){
            val configJson = """
                activateLogReader=false
                activateOSCListener=false
                activateNotify=false
                lockFilePath=test\/path
                pluginDirectoryPath=test\/path\/tete
                logReader={}
                oscListener={}
                notify={}
            """.trimIndent()
            shouldThrow<SerializationException>{
                Json.decodeFromString<VRCLCConfig>(configJson)
            }
        }
    }
})