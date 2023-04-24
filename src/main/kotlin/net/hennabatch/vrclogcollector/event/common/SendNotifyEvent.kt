package net.hennabatch.vrclogcollector.event.common

import net.hennabatch.vrclogcollector.event.Event
import net.hennabatch.vrclogcollector.event.Priority
import net.hennabatch.vrclogcollector.event.Tag
import net.hennabatch.vrclogcollector.vrclog.Message
import java.time.LocalDateTime
import java.util.*

class SendNotifyEvent(
    id: UUID = UUID.randomUUID(),
    occurredAt: LocalDateTime = LocalDateTime.now(),
    tags: List<Tag> = listOf(),
    priority: Priority = Priority.NORMAL,
    val message: Message,
    val force: Boolean = false
    ): Event(id, occurredAt, tags, priority) {
    override fun getAttributes(): Map<String, Any> {
        return mapOf(
            "type" to message.type,
            "title" to message.title,
            "content" to message.content,
            "force" to force
        )
    }
}