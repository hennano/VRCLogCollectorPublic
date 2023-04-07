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

        val testEvent = TestEventNonAttribute(uuid, localDateTime, tags)

        assertEquals("TestEventNonAttribute", testEvent.getEventName())
        assertEquals(UUID.fromString("000000-0000-0000-0000-000000000001"), testEvent.id)
        assertEquals(LocalDateTime.of(2023, 4, 7, 12, 34, 56), testEvent.occurredAt)
        assertEquals(TestTag1()::class.simpleName, testEvent.tags[0]::class.simpleName)

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
        val testEvent = TestEventAttribute(UUID.randomUUID(), LocalDateTime.now(), listOf())

        val testMap = testEvent.toMap()
        assertContains(testMap, "attributes")
        assertEquals("testVal", (testMap["attributes"] as Map<*, *>)["testKey"])
    }

    @Test
    fun test_正常系_Attribute複数(){
        val testEvent = TestEventMultiAttribute(UUID.randomUUID(), LocalDateTime.now(), listOf())

        val testMap = testEvent.toMap()
        assertContains(testMap, "attributes")
        assertEquals("testVal1", (testMap["attributes"] as Map<*, *>)["testKey1"])
        assertEquals("testVal2", (testMap["attributes"] as Map<*, *>)["testKey2"])
    }

    class TestEventNonAttribute(id: UUID, occurredAt: LocalDateTime, tags: List<Tag>) : Event(id, occurredAt, tags)

    class TestEventAttribute(id: UUID, occurredAt: LocalDateTime, tags: List<Tag>) : Event(id, occurredAt, tags){
        override fun getAttributes(): Map<String, Any> {
            return mapOf("testKey" to "testVal")
        }
    }

    class TestEventMultiAttribute(id: UUID, occurredAt: LocalDateTime, tags: List<Tag>) : Event(id, occurredAt, tags){
        override fun getAttributes(): Map<String, Any> {
            return mapOf(
                "testKey1" to "testVal1",
                "testKey2" to "testVal2"
            )
        }
    }

    class TestTag1 : Tag()
}