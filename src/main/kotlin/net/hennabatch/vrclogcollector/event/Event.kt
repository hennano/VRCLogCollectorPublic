package net.hennabatch.vrclogcollector.event

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

/**
 * Event クラス
 * イベント情報を保持する
 *
 * @param id イベントの固有ID
 * @param occurredAt イベントの発生時刻
 * @param tags イベントに付与されたTagのリスト
 * @param priority イベントの処理優先度
 */
abstract class Event(val id: UUID, val occurredAt: LocalDateTime, val tags: List<Tag>, val priority: Priority): Comparable<Event>{

    /**
     * イベント名を返す
     * @return イベント名
     */
    open fun getEventName(): String{
        return this::class.simpleName ?: ""
    }

    /**
     * イベントの属性を返す
     * @return イベント属性をまとめたMap
     */
    protected open fun getAttributes(): Map<String, Any> = mapOf()

    /**
     * EventをMapに変換する
     * @return イベントをまとめたMap
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "event_name" to getEventName(),
            "occurred_at" to occurredAt.toEpochSecond(ZoneOffset.ofHours(9)),
            "attributes" to getAttributes()
        )
    }

    override fun compareTo(other: Event): Int {
        var priority = this.priority.compareTo(other.priority)
        if(priority == 0){
            priority = this.occurredAt.compareTo(other.occurredAt)
        }
        return priority
    }
}