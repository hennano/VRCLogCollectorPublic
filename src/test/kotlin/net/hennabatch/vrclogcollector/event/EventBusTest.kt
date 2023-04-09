package net.hennabatch.vrclogcollector.event

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
        /*
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

            //テスト
            verify{
                //testSubscriber.execute(any(), any())
                //testSubscriber.execute(any(), updateInstanceState)
                //コルーチンの中で呼ぶと認識してくれない
                testSubscriber.execute(testEvent, updateInstanceState)
            }
            Thread.sleep(1000)
            thread.interrupt()
        }

         */

        test("サブスクライバー1つイベント複数")

        test("VRC起動時")

        test("VRC終了時")

        test("VRC再起動時")

        test("インスタンス情報更新時")
    }
    context("異常系"){
        test("インスタンス情報更新が１回もなし")

        test("サブスクライバーで例外")
    }
})
