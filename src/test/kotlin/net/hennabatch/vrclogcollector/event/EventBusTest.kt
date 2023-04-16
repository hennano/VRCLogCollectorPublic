package net.hennabatch.vrclogcollector.event

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.*
import io.mockk.verify
import net.hennabatch.vrclogcollector.event.common.LaunchVRCEvent
import net.hennabatch.vrclogcollector.event.common.QuitVRCEvent
import net.hennabatch.vrclogcollector.event.common.UpdateInstanceStateEvent
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

class EventBusTest: FunSpec ({
    context("正常系"){
        test("サブスクライバー1つイベント１つ"){
            //準備
            val updateInstanceState = UpdateInstanceStateEvent(
                launchId = UUID.randomUUID(),
                instanceId = UUID.randomUUID(),
                joinId = UUID.randomUUID(),
                updatedById = UUID.randomUUID(),
                world = "testWorld",
                players = listOf("testPlayer")
            )
            val testEvent = mockkClass(Event::class)
            every { testEvent.toMap() } returns mapOf()
            every { testEvent.compareTo(updateInstanceState) } returns 1
            val testSubscriber = mockkClass(EventSubscriber::class)
            every { testSubscriber.isTargetEvent(any()) } returns false
            every { testSubscriber.isTargetEvent(testEvent) } returns true
            every { testSubscriber.execute(any(), updateInstanceState) } returns Unit
            val eventQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(eventQueue, listOf(testSubscriber), 1)
            val thread = Thread(eventBus)

            //実行
            eventQueue.add(updateInstanceState)
            eventQueue.add(testEvent)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 1){
                testSubscriber.execute(testEvent, updateInstanceState)
            }
            thread.interrupt()
        }

        test("サブスクライバー1つイベント複数"){
            //実行順序についてはEventTestで実施

            //準備
            val updateInstanceState = UpdateInstanceStateEvent(
                launchId = UUID.randomUUID(),
                instanceId = UUID.randomUUID(),
                joinId = UUID.randomUUID(),
                updatedById = UUID.randomUUID(),
                world = "testWorld",
                players = listOf("testPlayer")
            )
            val testEvent1 = mockkClass(Event::class)
            every { testEvent1.toMap() } returns mapOf()
            every { testEvent1.compareTo(any()) } returns 1
            val testEvent2 = mockkClass(Event::class)
            every { testEvent2.toMap() } returns mapOf()
            every { testEvent2.compareTo(any()) } returns 1
            val testSubscriber = mockkClass(EventSubscriber::class)
            every { testSubscriber.isTargetEvent(any()) } returns false
            every { testSubscriber.isTargetEvent(testEvent1) } returns true
            every { testSubscriber.isTargetEvent(testEvent2) } returns true
            every { testSubscriber.execute(any(), updateInstanceState) } returns Unit
            val eventQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(eventQueue, listOf(testSubscriber), 1)
            val thread = Thread(eventBus)

            //実行
            eventQueue.add(updateInstanceState)
            eventQueue.add(testEvent1)
            eventQueue.add(testEvent2)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 2){
                testSubscriber.execute(any(), updateInstanceState)
            }
            thread.interrupt()
        }

        test("インスタンス情報更新が１回もなし"){
            //準備
            val testEvent = mockkClass(Event::class)
            every { testEvent.toMap() } returns mapOf()
            val testSubscriber = mockkClass(EventSubscriber::class)
            every { testSubscriber.isTargetEvent(any()) } returns false
            every { testSubscriber.isTargetEvent(testEvent) } returns true
            val eventQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(eventQueue, listOf(testSubscriber), 1)
            val thread = Thread(eventBus)

            //実行
            eventQueue.add(testEvent)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 0){
                testSubscriber.execute(testEvent, any())
            }
            thread.interrupt()
        }

        test("VRC起動時"){
            //準備
            val launchVRC = LaunchVRCEvent(launchId = UUID.randomUUID())
            val testSubscriber = mockkClass(EventSubscriber::class)
            every { testSubscriber.onLaunchVRC() } returns Unit
            every { testSubscriber.isTargetEvent(any())} returns false
            val eventQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(eventQueue, listOf(testSubscriber), 1)
            val thread = Thread(eventBus)

            //実行
            eventQueue.add(launchVRC)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 1){
                testSubscriber.onLaunchVRC()
            }
            thread.interrupt()
        }

        test("VRC終了時"){
            //準備
            val quitVRC = QuitVRCEvent(launchId = UUID.randomUUID())
            val testSubscriber = mockkClass(EventSubscriber::class)
            every { testSubscriber.isTargetEvent(any())} returns false
            every { testSubscriber.onQuitVRC() } returns Unit
            every { testSubscriber.onExit() } returns Unit
            val eventQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(eventQueue, listOf(testSubscriber), 1)
            val thread = Thread(eventBus)

            //実行
            eventQueue.add(quitVRC)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 1){
                testSubscriber.onQuitVRC()
            }
            Thread.sleep(2000)
            verify(exactly = 1){
                testSubscriber.onExit()
            }
            Thread.sleep(1000)
            thread.isAlive.shouldBeFalse()
        }

        test("VRC終了後にイベント発生"){
            //準備
            val quitVRC = QuitVRCEvent(launchId = UUID.randomUUID())
            val updateInstanceState = UpdateInstanceStateEvent(
                launchId = UUID.randomUUID(),
                instanceId = UUID.randomUUID(),
                joinId = UUID.randomUUID(),
                updatedById = UUID.randomUUID(),
                world = "testWorld",
                players = listOf("testPlayer")
            )
            val testEvent = mockkClass(Event::class)
            every { testEvent.toMap() } returns mapOf()
            every { testEvent.compareTo(updateInstanceState) } returns 1
            val testSubscriber = mockkClass(EventSubscriber::class)
            every { testSubscriber.isTargetEvent(any()) } returns false
            every { testSubscriber.isTargetEvent(testEvent) } returns true
            every { testSubscriber.execute(any(), updateInstanceState) } returns Unit
            every { testSubscriber.onQuitVRC() } returns Unit
            every { testSubscriber.onExit() } returns Unit
            val eventQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(eventQueue, listOf(testSubscriber), 10)
            val thread = Thread(eventBus)

            //実行
            eventQueue.add(updateInstanceState)
            eventQueue.add(quitVRC)
            thread.start()
            Thread.sleep(1000)
            //テスト
            verify(exactly = 1){
                testSubscriber.onQuitVRC()
            }
            //VRC終了後のイベント
            Thread.sleep(5000)
            eventQueue.add(testEvent)
            //VRC終了後から12.5s, 最後のイベントから7.5s
            Thread.sleep(7500)
            verify(exactly = 0){
                testSubscriber.onExit()
            }
            thread.isAlive.shouldBeTrue()
            //更に5s待機
            Thread.sleep(5000)
            verify(exactly = 1){
                testSubscriber.onExit()
            }
            thread.isAlive.shouldBeFalse()
        }

        test("VRC再起動時"){
            //準備
            val quitVRC = QuitVRCEvent(launchId = UUID.randomUUID())
            val launchVRC = LaunchVRCEvent(launchId = UUID.randomUUID())
            val testSubscriber = mockkClass(EventSubscriber::class)
            every { testSubscriber.isTargetEvent(any())} returns false
            every { testSubscriber.onLaunchVRC() } returns Unit
            every { testSubscriber.onQuitVRC() } returns Unit
            every { testSubscriber.onExit() } returns Unit
            val eventQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(eventQueue, listOf(testSubscriber), 1)
            val thread = Thread(eventBus)

            //実行
            eventQueue.add(quitVRC)
            eventQueue.add(launchVRC)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 1){
                testSubscriber.onQuitVRC()
                testSubscriber.onLaunchVRC()
            }
            Thread.sleep(2000)
            verify(exactly = 0){
                testSubscriber.onExit()
            }
            thread.isAlive.shouldBeTrue()
            thread.interrupt()
        }
    }
    context("異常系"){
        test("サブスクライバーで例外"){
            //準備
            val testEvent = mockkClass(Event::class)
            val updateInstanceState = UpdateInstanceStateEvent(
                launchId = UUID.randomUUID(),
                instanceId = UUID.randomUUID(),
                joinId = UUID.randomUUID(),
                updatedById = UUID.randomUUID(),
                world = "testWorld",
                players = listOf("testPlayer")
            )
            every { testEvent.toMap() } returns mapOf()
            every { testEvent.compareTo(updateInstanceState) } returns 1
            val testSubscriber = mockkClass(EventSubscriber::class)
            every { testSubscriber.isTargetEvent(any()) } returns false
            every { testSubscriber.isTargetEvent(testEvent) } returns true
            every { testSubscriber.execute(any(), updateInstanceState) } throws Exception()
            val eventQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(eventQueue, listOf(testSubscriber), 10)
            val thread = Thread(eventBus)

            //実行
            eventQueue.add(updateInstanceState)
            eventQueue.add(testEvent)
            thread.start()

            shouldThrowAny {
                thread.start()
                Thread.sleep(15000)
            }
        }
    }
})
