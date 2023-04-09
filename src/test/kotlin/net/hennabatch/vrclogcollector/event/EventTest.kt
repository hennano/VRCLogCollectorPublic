package net.hennabatch.vrclogcollector.event

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.util.*

class EventTest: FunSpec({
    context("正常系"){
        test("Attributeなし"){
            val uuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
            val localDateTime = LocalDateTime.of(2023, 4, 7, 12, 34, 56)
            val tags = listOf(TestTag1())
            val priority = Priority.NORMAL

            val testEvent = TestEventNonAttribute(uuid, localDateTime, tags, priority)

            testEvent.getEventName() shouldBe "TestEventNonAttribute"
            testEvent.id shouldBe UUID.fromString("00000000-0000-0000-0000-000000000001")
            testEvent.occurredAt shouldBe LocalDateTime.of(2023, 4, 7, 12, 34, 56)
            testEvent.tags[0]::class.simpleName shouldBe TestTag1()::class.simpleName
            testEvent.priority shouldBe Priority.NORMAL

            val testMap = testEvent.toMap()
            testMap shouldContain ("event_name" to "TestEventNonAttribute")
            testMap shouldContain ("id" to UUID.fromString("00000000-0000-0000-0000-000000000001"))
            testMap shouldContain ("occurred_at" to LocalDateTime.of(2023, 4, 7, 12, 34, 56))
            testMap shouldContainKey "attributes"
            (testMap["attributes"] as Map<*, *>).shouldBeEmpty()
        }

        test("Attribute1つ"){
            val testEvent = TestEventAttribute(UUID.randomUUID(), LocalDateTime.now(), listOf(), Priority.NORMAL)

            val testMap = testEvent.toMap()["attributes"] as Map<String, String>
            testMap shouldContain ("testKey" to "testVal")
        }

        test("Attribute複数"){
            val testEvent = TestEventMultiAttribute(UUID.randomUUID(), LocalDateTime.now(), listOf(), Priority.NORMAL)

            val testMap = testEvent.toMap()["attributes"] as Map<String, String>
            testMap shouldContain ("testKey1" to "testVal1")
            testMap shouldContain ("testKey2" to "testVal2")
        }
    }

    context("優先度"){
        test("優先度違い"){
            val critical = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000000"), LocalDateTime.of(2023, 4, 7, 12, 34, 56), listOf(), Priority.CRITICAl)
            val immediate = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000001"), LocalDateTime.of(2023, 4, 7, 12, 34, 56), listOf(), Priority.IMMEDIATE)
            val normal = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000002"), LocalDateTime.of(2023, 4, 7, 12, 34, 56), listOf(), Priority.NORMAL)
            val minor = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000003"), LocalDateTime.of(2023, 4, 7, 12, 34, 56), listOf(), Priority.MINOR)

            val priorityQueue = PriorityQueue<Event>()
            priorityQueue.add(minor)
            priorityQueue.add(normal)
            priorityQueue.add(immediate)
            priorityQueue.add(critical)

            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000000")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000001")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000002")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000003")
        }

        test("時刻違い"){
            val normal1st = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000000"), LocalDateTime.of(2023, 4, 7, 12, 34, 0), listOf(), Priority.NORMAL)
            val normal2nd = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000001"), LocalDateTime.of(2023, 4, 7, 12, 34, 1), listOf(), Priority.NORMAL)
            val normal3rd = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000002"), LocalDateTime.of(2023, 4, 7, 12, 34, 2), listOf(), Priority.NORMAL)
            val normal4th = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000003"), LocalDateTime.of(2023, 4, 7, 12, 34, 3), listOf(), Priority.NORMAL)

            val priorityQueue = PriorityQueue<Event>()
            priorityQueue.add(normal4th)
            priorityQueue.add(normal1st)
            priorityQueue.add(normal3rd)
            priorityQueue.add(normal2nd)

            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000000")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000001")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000002")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000003")
        }

        test("全違い"){
            val normal_00 = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000004"), LocalDateTime.of(2023, 4, 7, 12, 34, 0), listOf(), Priority.NORMAL)
            val immediate_00 = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000002"), LocalDateTime.of(2023, 4, 7, 12, 34, 0), listOf(), Priority.IMMEDIATE)
            val critical_01 = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000000"), LocalDateTime.of(2023, 4, 7, 12, 34, 1), listOf(), Priority.CRITICAl)
            val minor_01 = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000006"), LocalDateTime.of(2023, 4, 7, 12, 34, 1), listOf(), Priority.MINOR)
            val normal_02 = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000005"), LocalDateTime.of(2023, 4, 7, 12, 34, 2), listOf(), Priority.NORMAL)
            val immediate_02 = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000003"), LocalDateTime.of(2023, 4, 7, 12, 34, 2), listOf(), Priority.IMMEDIATE)
            val minor_03 = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000007"), LocalDateTime.of(2023, 4, 7, 12, 34, 3), listOf(), Priority.MINOR)
            val critical_04 = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000001"), LocalDateTime.of(2023, 4, 7, 12, 34, 4), listOf(), Priority.CRITICAl)

            val priorityQueue = PriorityQueue<Event>()
            priorityQueue.add(normal_00)
            priorityQueue.add(immediate_00)
            priorityQueue.add(critical_01)
            priorityQueue.add(minor_01)
            priorityQueue.add(normal_02)
            priorityQueue.add(immediate_02)
            priorityQueue.add(minor_03)
            priorityQueue.add(critical_04)

            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000000")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000001")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000002")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000003")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000004")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000005")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000006")
            priorityQueue.poll().id shouldBe UUID.fromString("000000-0000-0000-0000-000000000007")
        }
    }
})
    class TestEventNonAttribute(id: UUID, occurredAt: LocalDateTime, tags: List<Tag>, priority: Priority) : Event(id, occurredAt, tags, priority)

    class TestEventAttribute(id: UUID, occurredAt: LocalDateTime, tags: List<Tag>, priority: Priority) : Event(id, occurredAt, tags, priority){
        override fun getAttributes(): Map<String, Any> {
            return mapOf("testKey" to "testVal")
        }
    }

    class TestEventMultiAttribute(id: UUID, occurredAt: LocalDateTime, tags: List<Tag>, priority: Priority) : Event(id, occurredAt, tags, priority){
        override fun getAttributes(): Map<String, Any> {
            return mapOf(
                "testKey1" to "testVal1",
                "testKey2" to "testVal2"
            )
        }
    }

    class TestTag1 : Tag()