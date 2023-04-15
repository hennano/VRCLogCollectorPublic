package net.hennabatch.vrclogcollector.config.vrclc.notify

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
class XSOverlayConfigTest: FunSpec({
    context("正常系"){
        test("コンフィグファイルなし"){
            val configTest = XSOverlayConfig()

            configTest.ip shouldBe "localhost"
            configTest.port shouldBe 42069
        }

        test("コンフィグファイルあり"){
            val configJson = """
                {
                    "ip": "172.0.0.1",
                    "port": 9999
                }
            """.trimIndent()

            val configTest = Json.decodeFromString<XSOverlayConfig>(configJson)

            configTest.ip shouldBe "172.0.0.1"
            configTest.port shouldBe 9999
        }
        test("コンフィグファイル中身なし"){
            val configJson = "{}"

            val configTest = Json.decodeFromString<XSOverlayConfig>(configJson)

            configTest.ip shouldBe "localhost"
            configTest.port shouldBe 42069
        }
    }

    context("異常系"){
        test("コンフィグファイルがJsonではない"){
            val configJson = """
                    ip=172.0.0.1
                    port=9999
            """.trimIndent()
            shouldThrow<SerializationException>{
                Json.decodeFromString<XSOverlayConfig>(configJson)
            }
        }
    }
})