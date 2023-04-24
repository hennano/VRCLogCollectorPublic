package net.hennabatch.vrclogcollector.event.common

import net.hennabatch.vrclogcollector.event.Event
import net.hennabatch.vrclogcollector.event.Priority
import net.hennabatch.vrclogcollector.event.Tag
import java.time.LocalDateTime
import java.util.*

/**
 * LaunchVRCEvent クラス
 * VRC起動時に発行されるイベント
 * @param id イベントの固有ID
 * @param occurredAt イベントの発生時刻
 * @param tags イベントに付与されたTagのリスト
 * @param priority イベントの処理優先度
 * @param launchId VRCLogCollector起動時に割り当てられるID
 */
class LaunchVRCEvent(
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