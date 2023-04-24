package net.hennabatch.vrclogcollector.event.common

import net.hennabatch.vrclogcollector.event.Event
import net.hennabatch.vrclogcollector.event.Priority
import net.hennabatch.vrclogcollector.event.Tag
import java.time.LocalDateTime
import java.util.UUID

/**
 * UpdateInstanceStateEvent クラス
 * インスタンス状況が更新された際に発行されるイベント
 * @param id イベントの固有ID
 * @param occurredAt イベントの発生時刻
 * @param tags イベントに付与されたTagのリスト
 * @param priority イベントの処理優先度
 * @param launchId VRCLogCollector起動時に割り当てられるID
 * @param instanceId 参加したインスタンスのID
 * @param joinId インスタンス参加時に割り当てられるID 同一インスタンスであっても参加毎に変更される
 * @param updatedById UpdateInstanceStateEventが発行される原因になったイベントのID
 * @param world 参加したインスタンスのワールド名
 * @param players 参加したインスタンスにいるプレイヤー名のリスト
 */
class UpdateInstanceStateEvent(
    id: UUID = UUID.randomUUID(),
    occurredAt: LocalDateTime = LocalDateTime.now(),
    tags: List<Tag> = listOf(),
    priority: Priority = Priority.NORMAL,
    val launchId: UUID,
    val instanceId: UUID,
    val joinId: UUID,
    val updatedById: UUID,
    val world: String,
    val players: List<String>
) : Event(id, occurredAt, tags, priority) {

    override fun getAttributes(): Map<String, Any> {
        return mapOf(
            "launch_uuid" to launchId,
            "instance_uuid" to instanceId,
            "join_uuid" to joinId,
            "updated_by_uuid" to updatedById,
            "world" to world,
            "players" to players
        )
    }
}