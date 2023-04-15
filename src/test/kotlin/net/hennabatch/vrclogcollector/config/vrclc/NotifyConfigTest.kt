package net.hennabatch.vrclogcollector.config.vrclc

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.hennabatch.vrclogcollector.config.vrclc.notify.ToastConfig
import net.hennabatch.vrclogcollector.config.vrclc.notify.XSOverlayConfig

class NotifyConfigTest: FunSpec({
    context("正常系"){
        test("コンフィグファイルなし"){
            val configTest = NotifyConfig()

            configTest.xsOverlayConfig::class shouldBe XSOverlayConfig::class
            configTest.toastConfig::class shouldBe ToastConfig::class
        }

        test("コンフィグファイルあり"){
            val configJson = """
                {
                    "xsOverlayConfig": {}
                    "toastConfig": {}
                }
            """.trimIndent()

            val configTest = Json.decodeFromString<NotifyConfig>(configJson)

            configTest.xsOverlayConfig::class shouldBe XSOverlayConfig::class
            configTest.toastConfig::class shouldBe ToastConfig::class
        }

        test("コンフィグファイル中身なし"){
            val configJson = "{}"

            val configTest = Json.decodeFromString<NotifyConfig>(configJson)

            configTest.xsOverlayConfig::class shouldBe XSOverlayConfig::class
            configTest.toastConfig::class shouldBe ToastConfig::class
        }
    }

    context("異常系"){
        test("コンフィグファイルがJsonではない"){
            val configJson = """
                xsOverlayConfig={}
                toastConfig={}
            """.trimIndent()
            shouldThrow<SerializationException>{
                Json.decodeFromString<LogReaderConfig>(configJson)
            }
        }
    }
})