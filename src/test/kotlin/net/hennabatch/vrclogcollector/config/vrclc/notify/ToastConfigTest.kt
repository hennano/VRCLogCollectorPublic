package net.hennabatch.vrclogcollector.config.vrclc.notify

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalKotest::class)
class ToastConfigTest: FunSpec({
    context("正常系"){
        test("コンフィグファイルなし"){
            val configTest = ToastConfig()

            configTest.psPath shouldBe "${System.getProperty("user.dir")}\\toast.ps"
        }

        test("コンフィグファイルあり"){
            val configJson = """
                {
                    "psPath": "test\/path"
                }
            """.trimIndent()

            val configTest = Json.decodeFromString<ToastConfig>(configJson)

            configTest.psPath shouldBe "test/path"
        }

        test("コンフィグファイル中身なし"){
            val configJson = "{}"

            val configTest = Json.decodeFromString<ToastConfig>(configJson)

            configTest.psPath shouldBe "${System.getProperty("user.dir")}\\toast.ps"
        }
    }

    context("異常系"){
        test("コンフィグファイルがJsonではない"){
            val configJson = """
                psPath=test/path,
            """.trimIndent()
            shouldThrow<SerializationException>{
                Json.decodeFromString<ToastConfig>(configJson)
            }
        }
    }

    /**
     * 未実装
     */
    context("ファイル確認").config(enabled = false){
        test("デフォルトパスにpsファイルが存在する"){}
    }
})