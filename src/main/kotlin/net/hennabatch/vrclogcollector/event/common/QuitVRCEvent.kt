package net.hennabatch.vrclogcollector.event.common

import net.hennabatch.vrclogcollector.event.Event
import net.hennabatch.vrclogcollector.event.Priority
import net.hennabatch.vrclogcollector.event.Tag
import java.time.LocalDateTime
import java.util.UUID

class QuitVRCEvent(
    id: UUID = UUID.randomUUID(),
    occurredAt: LocalDateTime = LocalDateTime.now(),
    tags: List<Tag> = listOf(),
    priority: Priority = Priority.NORMAL,
    val launchId: UUID,
    ): Event(id, occurredAt, tags, priority) {
    override fun getAttributes(): Map<String, Any> {
        return mapOf(
            "launch_uuid" to launchId
        )
    }
}