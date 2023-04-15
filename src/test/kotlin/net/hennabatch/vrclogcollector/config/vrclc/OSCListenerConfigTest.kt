package net.hennabatch.vrclogcollector.config.vrclc

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class OSCListenerConfigTest: FunSpec({

    context("正常系"){
        test("コンフィグファイルなし"){
            val configTest = OSCListenerConfig()

            configTest.vrcInPort shouldBe 9001
            configTest.vrcOutPort shouldBe 9000
            configTest.vrcIp shouldBe "localhost"
        }

        test("コンフィグファイルあり"){
            val configJson = """
                {
                    "vrcInPort": 8000,
                    "vrcOutPort": 7000,
                    "vrcIp": "172.0.0.1"
                }
            """.trimIndent()

            val configTest = Json.decodeFromString<OSCListenerConfig>(configJson)

            configTest.vrcInPort shouldBe 8000
            configTest.vrcOutPort shouldBe 7000
            configTest.vrcIp shouldBe "172.0.0.1"
        }

        test("コンフィグファイル中身なし"){
            val configJson = "{}"
            val configTest = Json.decodeFromString<OSCListenerConfig>(configJson)

            configTest.vrcInPort shouldBe 9001
            configTest.vrcOutPort shouldBe 9000
            configTest.vrcIp shouldBe "localhost"
        }
    }

    context("異常系"){
        test("コンフィグファイルがJsonではない"){
            val configJson = """
                vrcInPort=8000
                vrcOutPort=7000
                vrcIp=172.0.0.1
            """.trimIndent()
            shouldThrow<SerializationException>{
                Json.decodeFromString<OSCListenerConfig>(configJson)
            }
        }
    }
})