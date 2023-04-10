package net.hennabatch.vrclogcollector.event

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.mockk.*
import io.mockk.impl.annotations.SpyK
import io.mockk.verify
import net.hennabatch.vrclogcollector.common.util.logger
import net.hennabatch.vrclogcollector.event.common.UpdateInstanceStateEvent
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

class EventBusTest: FunSpec ({
    context("正常系"){
        test("サブスクライバー1つイベント１つ"){
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
            every { testSubscriber.execute(any(), updateInstanceState) } returns Unit
            val evenQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(evenQueue, listOf(testSubscriber), 1)
            val thread = Thread(eventBus)

            //実行
            evenQueue.add(updateInstanceState)
            evenQueue.add(testEvent)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify{
                testSubscriber.execute(testEvent, updateInstanceState)
            }
            thread.interrupt()
        }

        test("サブスクライバー1つイベント複数"){}

        test("インスタンス情報更新が１回もなし"){
            //準備
            val testEvent = mockkClass(Event::class)
            every { testEvent.toMap() } returns mapOf()
            val testSubscriber = mockkClass(EventSubscriber::class)
            every { testSubscriber.isTargetEvent(any()) } returns false
            every { testSubscriber.isTargetEvent(testEvent) } returns true
            val evenQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(evenQueue, listOf(testSubscriber), 1)
            val thread = Thread(eventBus)

            //実行
            evenQueue.add(testEvent)
            thread.start()
            Thread.sleep(1000)

            //テスト
            verify(exactly = 0){
                testSubscriber.execute(testEvent, any())
            }
            thread.interrupt()
        }

        test("VRC起動時"){}

        test("VRC終了時"){}

        test("VRC再起動時"){}

        test("インスタンス情報更新時"){}
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
            val evenQueue = PriorityBlockingQueue<Event>()
            val eventBus = EventBus(evenQueue, listOf(testSubscriber), 1)
            val thread = Thread(eventBus)

            //実行
            evenQueue.add(updateInstanceState)
            evenQueue.add(testEvent)
            thread.start()

            shouldThrowAny {
                thread.start()
                Thread.sleep(10000)
            }
        }
    }
})
