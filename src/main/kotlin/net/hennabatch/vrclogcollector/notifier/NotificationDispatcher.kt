package net.hennabatch.vrclogcollector.notifier

import net.hennabatch.vrclogcollector.common.util.logger
import java.util.concurrent.LinkedTransferQueue
import java.util.concurrent.TransferQueue

/**
 * NotificationDispatcher クラス
 * 通知の送信を管理するクラス
 * @param messageQueue メッセージのキュー
 */
class NotificationDispatcher(val messageQueue: TransferQueue<Message> = LinkedTransferQueue()): Runnable{
    private val sendNotifiers = mutableListOf<Notifier>()

    /**
     * 通知先の追加
     * @param notifier 追加するNotifier
     */
    fun addNotifier(notifier: Notifier){
        sendNotifiers.add(notifier)
    }

    /**
     * 通知先の削除
     * @param notifierClass 削除するNotifierのクラス
     */
    fun removeNotifier(notifierClass: Class<out Notifier>){
        sendNotifiers.removeIf {
            it::class == notifierClass
        }
    }

    /**
     * 通知先のリセット
     */
    fun clear(){
        sendNotifiers.clear()
    }

    override fun run() {
        try{
            while (!Thread.interrupted()){
                val message = messageQueue.take()
                sendNotifiers.forEach{
                    it.send(message)
                }
            }
        }catch (_: InterruptedException){}
    }
}