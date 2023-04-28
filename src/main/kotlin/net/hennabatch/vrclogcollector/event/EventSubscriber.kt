package net.hennabatch.vrclogcollector.event

import net.hennabatch.vrclogcollector.event.common.SendNotifyEvent
import net.hennabatch.vrclogcollector.event.common.UpdateInstanceStateEvent
import net.hennabatch.vrclogcollector.notifier.Message
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
     * @param message 送信するメッセージ
     * @param force 通知がOSCで無効化されていても通知を行うか
     */
    protected fun sendNotify(message: Message, force: Boolean = false){
        eventQueue.add(
            SendNotifyEvent(
                message = message,
                force = force
            )
        )
    }

    /**
     * イベントを発行する
     * @param event 発行するイベント
     */
    @Deprecated(message = "イベントの発行用途のみで利用する場合はEventPublisherの利用を推奨", level = DeprecationLevel.WARNING)
    protected fun publishEvent(event: Event){
        eventQueue.add(event)
    }
}