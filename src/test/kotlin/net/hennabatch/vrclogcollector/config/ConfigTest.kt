package net.hennabatch.vrclogcollector.config

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockkObject
import java.io.FileNotFoundException
import java.nio.file.NoSuchFileException
import java.nio.file.Path

class ConfigTest: FunSpec({
    context("正常系"){
        test("ファイルあり"){
            //準備
            val tempDir = tempdir()
            val configTestPath = tempDir.resolve("configTest.json").toPath()
            configTestPath.toFile().writeText("{}", Charsets.UTF_8)

            //実行
            Config.load(configTestPath)

            //テスト
            Config.VRCLCConfig.shouldNotBeNull()
            configTestPath.toFile().exists().shouldBeTrue()
        }

        test("ファイルなし") {
            //準備
            val tempDir = tempdir()
            val configTestPath = tempDir.resolve("configTest.json").toPath()

            //実行
            Config.load(configTestPath)

            //テスト
            Config.VRCLCConfig.shouldNotBeNull()
            configTestPath.toFile().exists().shouldBeTrue()
        }
    }

    context("異常系"){
        test("指定パスがディレクトリ"){
            //準備
            val tempDir = tempdir()
            val configTestPath = tempDir.toPath()

            //実行
            shouldThrow<NoSuchFileException> {
                Config.load(configTestPath)
            }
        }

        test("指定パスが存在しないディレクトリ上にある"){
            val tempDir = tempdir()
            val configTestPath = tempDir.resolve("test/configTest.json").toPath()

            shouldThrow<FileNotFoundException> {
                Config.load(configTestPath)
            }
        }

        test("デフォルトファイルの出力に失敗"){
            //準備
            val tempDir = tempdir()
            val configTestPath = tempDir.resolve("configTest.json").toPath()

            //実行
            mockkObject(objects = arrayOf(Config), recordPrivateCalls = true)
            every { Config["makeDefaultConfigFile"](any<Path>()) } throws Exception()

            shouldThrowAny{
                Config.load(configTestPath)
            }
        }

        test("コンフィグファイルの読み込みに失敗"){
            val tempDir = tempdir()
            val configTestPath = tempDir.resolve("configTest.json").toPath()
            configTestPath.toFile().writeText("{}", Charsets.UTF_8)

            //実行
            mockkObject(objects = arrayOf(Config), recordPrivateCalls = true)
            every { Config["readConfigFile"](any<Path>()) } throws Exception()

            shouldThrowAny{
                Config.load(configTestPath)
            }
        }

        test("load()を呼ばずにコンフィグへアクセス").config(enabled = false){
            shouldThrow<UninitializedPropertyAccessException> {
                Config.VRCLCConfig
            }
        }
    }
})