package net.hennabatch.vrclogcollector.vrclog

import io.kotest.assertions.json.shouldBeValidJson
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.spyk
import java.io.FileNotFoundException
import java.nio.file.NoSuchFileException
import java.nio.file.Path

class CookieTest: FunSpec( {
    context("正常系"){
        test("ファイルあり"){
            val tempDir = tempdir()
            val cookieTestPath = tempDir.resolve("cookieTest.json").toPath()
            cookieTestPath.toFile().writeText("""
                {
                    "readingLogFile": "test",
                    "readRows": 100,
                    "launchIdStr": "testId"
                }
            """.trimIndent(), Charsets.UTF_8)

            //実行
            val cookie = Cookie.read(cookieTestPath)

            //テスト
            cookie.readingLogFile shouldBe "test"
            cookie.readRows shouldBe 100
            cookie.launchIdStr shouldBe "testId"
        }

        test("ファイルなし"){
            val tempDir = tempdir()
            val cookieTestPath = tempDir.resolve("cookieTest.json").toPath()

            //実行
            val cookie = Cookie.read(cookieTestPath)

            //テスト
            cookie.readingLogFile shouldBe ""
            cookie.readRows shouldBe 0
            cookie.launchIdStr shouldBe ""
            cookieTestPath.toFile().exists().shouldBeTrue()
        }

        test("書き込み"){
            //準備
            val tempDir = tempdir()
            val cookieTestPath = tempDir.resolve("cookieTest.json").toPath()
            val testCookie = Cookie("test1", 200, "IdTest")

            //実行
            testCookie.write(cookieTestPath)

            //テスト
            cookieTestPath.toFile().exists().shouldBeTrue()
            val testJson = cookieTestPath.toFile().readText()
            testJson.shouldBeValidJson()
            testJson.shouldEqualJson("""
                {
                    "readingLogFile": "test1",
                    "readRows": 200,
                    "launchIdStr": "IdTest"
                }
            """.trimIndent())
        }
    }

    context("異常系"){
        test("指定パスがディレクトリ"){
            //準備
            val tempDir = tempdir()
            val cookieTestPath = tempDir.toPath()

            //実行
            shouldThrow<NoSuchFileException> {
                Cookie.read(cookieTestPath)
            }
        }

        test("指定パスが存在しないディレクトリ上にある"){
            //準備
            val tempDir = tempdir()
            val cookieTestPath = tempDir.resolve("test/cookieTest.json").toPath()

            //実行
            shouldThrow<FileNotFoundException> {
                Cookie.read(cookieTestPath)
            }
        }

        test("デフォルトファイルの出力に失敗"){
            //準備
            val tempDir = tempdir()
            val cookieTestPath = tempDir.resolve("cookieTest.json").toPath()

            //実行
            mockkObject(objects = arrayOf(Cookie), recordPrivateCalls = true)
            every { Cookie["makeDefaultCookieFile"](any<Path>()) } throws Exception()

            shouldThrowAny{
                Cookie.read(cookieTestPath)
            }
        }

        test("クッキーファイルの読み込みに失敗"){
            //準備
            val tempDir = tempdir()
            val cookieTestPath = tempDir.resolve("cookieTest.json").toPath()

            //実行
            mockkObject(objects = arrayOf(Cookie), recordPrivateCalls = true)
            every { Cookie["readCookieFile"](any<Path>()) } throws Exception()

            shouldThrowAny{
                Cookie.read(cookieTestPath)
            }
        }

        test("クッキーファイルの書き込みに失敗"){
            val tempDir = tempdir()
            val cookieTestPath = tempDir.resolve("configTest.json").toPath()

            //実行
            val cookie = spyk(Cookie())
            every { cookie.write(any()) } throws Exception()

            shouldThrowAny{
                cookie.write(cookieTestPath)
            }
        }
    }
})