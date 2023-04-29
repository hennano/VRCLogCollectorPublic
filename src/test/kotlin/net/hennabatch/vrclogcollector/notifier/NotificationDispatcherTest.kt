package net.hennabatch.vrclogcollector.notifier

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify

class NotificationDispatcherTest: FunSpec({
    context("正常系"){
        test("通知１つ"){
            //準備
            val testNotifier = mockkClass(Notifier::class)
            every { testNotifier.send(any()) } returns Unit

            val testMessage = Message(MessageType.INFO, "testTitle", "testContent")

            //実行
            val dispatcher = NotificationDispatcher()
            dispatcher.addNotifier(testNotifier)
            val thread = Thread(dispatcher)
            val queue = dispatcher.messageQueue

            queue.add(testMessage)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 1) {
                testNotifier.send(testMessage)
            }
            queue.shouldBeEmpty()

            thread.interrupt()
        }

        test("通知複数"){
            //準備
            val testNotifier1 = mockkClass(Notifier::class)
            val testNotifier2 = mockkClass(Notifier::class)
            every { testNotifier1.send(any()) } returns Unit
            every { testNotifier2.send(any()) } returns Unit

            val testMessage = Message(MessageType.INFO, "testTitle", "testContent")

            //実行
            val dispatcher = NotificationDispatcher()
            dispatcher.addNotifier(testNotifier1)
            dispatcher.addNotifier(testNotifier2)
            val thread = Thread(dispatcher)
            val queue = dispatcher.messageQueue

            queue.add(testMessage)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 1) {
                testNotifier1.send(testMessage)
                testNotifier2.send(testMessage)
            }
            queue.shouldBeEmpty()

            thread.interrupt()
        }

        test("通知先複数") {
            //準備
            val testNotifier = mockkClass(Notifier::class)
            every { testNotifier.send(any()) } returns Unit

            val testMessage1 = Message(MessageType.INFO, "testTitle1", "testContent1")
            val testMessage2 = Message(MessageType.INFO, "testTitle2", "testContent2")


            //実行
            val dispatcher = NotificationDispatcher()
            dispatcher.addNotifier(testNotifier)
            val thread = Thread(dispatcher)
            val queue = dispatcher.messageQueue

            queue.add(testMessage1)
            queue.add(testMessage2)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 1) {
                testNotifier.send(testMessage1)
                testNotifier.send(testMessage2)
            }
            queue.shouldBeEmpty()

            thread.interrupt()
        }

        test("通知の無効化"){
            //準備
            val testNotifier = mockkClass(Notifier::class)
            every { testNotifier.send(any()) } returns Unit

            val testMessage = Message(MessageType.INFO, "testTitle", "testContent")

            //実行
            val dispatcher = NotificationDispatcher()
            dispatcher.addNotifier(testNotifier)
            dispatcher.isActivated = false
            val thread = Thread(dispatcher)
            val queue = dispatcher.messageQueue

            queue.add(testMessage)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 0) {
                testNotifier.send(testMessage)
            }
            queue.shouldBeEmpty()

            thread.interrupt()
        }

        test("強制通知"){
            //準備
            val testNotifier = mockkClass(Notifier::class)
            every { testNotifier.send(any()) } returns Unit

            val testMessage = Message(MessageType.INFO, "testTitle", "testContent")
            val testMessageForce = Message(MessageType.INFO, "testTitle", "testContent", forcedNotify = true)

            //実行
            val dispatcher = NotificationDispatcher()
            dispatcher.addNotifier(testNotifier)
            dispatcher.isActivated = false
            val thread = Thread(dispatcher)
            val queue = dispatcher.messageQueue

            queue.add(testMessage)
            queue.add(testMessageForce)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 0) {
                testNotifier.send(testMessage)
            }
            verify(exactly = 1) {
                testNotifier.send(testMessageForce)
            }
            queue.shouldBeEmpty()

            thread.interrupt()
        }
    }

    context("異常系"){
        test("Notifierでエラー"){
            //準備
            val testNotifier = mockkClass(Notifier::class)
            every { testNotifier.send(any()) } throws Exception()

            val testMessage = Message(MessageType.INFO, "testTitle", "testContent")

            //実行
            val dispatcher = NotificationDispatcher()
            dispatcher.addNotifier(testNotifier)
            val thread = Thread(dispatcher)
            val queue = dispatcher.messageQueue

            queue.add(testMessage)
            thread.start()
            Thread.sleep(1000)

            //テスト
            thread.isAlive.shouldBeFalse()
            thread.interrupt()
        }
    }
})