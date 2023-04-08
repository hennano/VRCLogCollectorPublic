package net.hennabatch.vrclogcollector.event

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class EventTest {

    @Test
    fun test_正常系_Attributeなし(){
        val uuid = UUID.fromString("000000-0000-0000-0000-000000000001")
        val localDateTime = LocalDateTime.of(2023, 4, 7, 12, 34, 56)
        val tags = listOf(TestTag1())
        val priority = Priority.NORMAL

        val testEvent = TestEventNonAttribute(uuid, localDateTime, tags, priority)

        assertEquals("TestEventNonAttribute", testEvent.getEventName())
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000001"), testEvent.id)
        assertEquals(LocalDateTime.of(2023, 4, 7, 12, 34, 56), testEvent.occurredAt)
        assertEquals(TestTag1()::class.simpleName, testEvent.tags[0]::class.simpleName)
        assertEquals(Priority.NORMAL, testEvent.priority)

        val testMap = testEvent.toMap()
        assertContains(testMap, "event_name")
        assertEquals("TestEventNonAttribute", testMap["event_name"])
        assertContains(testMap, "id")
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000001"), testMap["id"])
        assertContains(testMap, "occurred_at")
        assertEquals(LocalDateTime.of(2023, 4, 7, 12, 34, 56), testEvent.occurredAt)
        assertContains(testMap, "attributes")
    }

    @Test
    fun test_正常系_Attribute1つ(){
        val testEvent = TestEventAttribute(UUID.randomUUID(), LocalDateTime.now(), listOf(), Priority.NORMAL)

        val testMap = testEvent.toMap()
        assertContains(testMap, "attributes")
        assertEquals("testVal", (testMap["attributes"] as Map<*, *>)["testKey"])
    }

    @Test
    fun test_正常系_Attribute複数(){
        val testEvent = TestEventMultiAttribute(UUID.randomUUID(), LocalDateTime.now(), listOf(), Priority.NORMAL)

        val testMap = testEvent.toMap()
        assertContains(testMap, "attributes")
        assertEquals("testVal1", (testMap["attributes"] as Map<*, *>)["testKey1"])
        assertEquals("testVal2", (testMap["attributes"] as Map<*, *>)["testKey2"])
    }

    @Test
    fun test_優先度_優先度違い(){
        val critical = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000000"), LocalDateTime.of(2023, 4, 7, 12, 34, 56), listOf(), Priority.CRITICAl)
        val immediate = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000001"), LocalDateTime.of(2023, 4, 7, 12, 34, 56), listOf(), Priority.IMMEDIATE)
        val normal = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000002"), LocalDateTime.of(2023, 4, 7, 12, 34, 56), listOf(), Priority.NORMAL)
        val minor = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000003"), LocalDateTime.of(2023, 4, 7, 12, 34, 56), listOf(), Priority.MINOR)

        val priorityQueue = PriorityQueue<Event>()
        priorityQueue.add(minor)
        priorityQueue.add(normal)
        priorityQueue.add(immediate)
        priorityQueue.add(critical)

        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000000"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000001"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000002"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000003"), priorityQueue.poll().id)
    }

    @Test
    fun Test_優先度_時刻違い(){
        val normal_1st = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000000"), LocalDateTime.of(2023, 4, 7, 12, 34, 0), listOf(), Priority.NORMAL)
        val normal_2nd = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000001"), LocalDateTime.of(2023, 4, 7, 12, 34, 1), listOf(), Priority.NORMAL)
        val normal_3rd = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000002"), LocalDateTime.of(2023, 4, 7, 12, 34, 2), listOf(), Priority.NORMAL)
        val normal_4th = TestEventNonAttribute(UUID.fromString("000000-0000-0000-0000-000000000003"), LocalDateTime.of(2023, 4, 7, 12, 34, 3), listOf(), Priority.NORMAL)

        val priorityQueue = PriorityQueue<Event>()
        priorityQueue.add(normal_4th)
        priorityQueue.add(normal_1st)
        priorityQueue.add(normal_3rd)
        priorityQueue.add(normal_2nd)

        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000000"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000001"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000002"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000003"), priorityQueue.poll().id)
    }

    @Test
    fun Test_優先度_全違い(){
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

        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000000"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000001"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000002"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000003"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000004"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000005"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000006"), priorityQueue.poll().id)
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000007"), priorityQueue.poll().id)
    }

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
}