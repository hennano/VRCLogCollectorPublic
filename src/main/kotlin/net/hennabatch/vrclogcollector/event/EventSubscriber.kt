package net.hennabatch.vrclogcollector.event

import net.hennabatch.vrclogcollector.event.common.UpdateInstanceStateEvent
import java.util.concurrent.PriorityBlockingQueue

/**
 * EventSubscriber クラス
 * EventBusからイベントを受け取って処理を行う
 *
 * @param eventQueue イベント発行先キュー
 */
abstract class EventSubscriber (private val eventQueue: PriorityBlockingQueue<Event>){

    /**
     * VRCが起動した際に呼ばれる
     */
    open fun onLaunchVRC(){}

    /**
     * VRCが終了した際に呼ばれる
     */
    open fun onQuitVRC(){}

    /**
     * VRCLogCollectorが終了する際に呼ばれる
     */
    open fun onExit(){}

    /**
     * 発行されたイベントが実行対象か判断する
     *
     * @param event 判断対象のイベント
     * @return 処理を行う場合はtrue
     */
    abstract fun isTargetEvent(event: Event): Boolean

    /**
     * 処理を行う
     *
     * @param event 処理対象のイベント
     * @param instanceState イベント発行時点のインスタンス状況
     */
    abstract fun execute(event: Event, instanceState: UpdateInstanceStateEvent)

    /**
     * 通知を送信する
     * @param sendNotifyEvent 送信する通知イベント
     * TODO SendNotifyEventに差し替える
     */
    protected fun sendNotify(sendNotifyEvent: Event){
        eventQueue.add(sendNotifyEvent)
    }
}