package net.hennabatch.vrclogcollector.event

import java.util.concurrent.PriorityBlockingQueue

/**
 * EventPublisher クラス
 * Eventの発行を行う
 *
 * @param eventQueue イベント発行先キュー
 */
abstract class EventPublisher(private val eventQueue: PriorityBlockingQueue<Event>) {

    /**
     * 実行時に呼ばれる
     */
    open fun onStart(){}

    /**
     * VRCLogCollector終了時に呼ばれる
     */
    open fun onExit(){}

    /**
     * イベントを発行する
     * @param event 発行するイベント
     */
    protected fun publishEvent(event: Event){
        eventQueue.add(event)
    }
}