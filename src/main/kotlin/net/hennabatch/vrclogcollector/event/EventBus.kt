package net.hennabatch.vrclogcollector.event

import kotlinx.coroutines.*
import net.hennabatch.vrclogcollector.common.util.logger
import net.hennabatch.vrclogcollector.event.common.LaunchVRCEvent
import net.hennabatch.vrclogcollector.event.common.QuitVRCEvent
import net.hennabatch.vrclogcollector.event.common.UpdateInstanceStateEvent
import java.lang.Runnable
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TimeUnit

/**
 * EventBus クラス
 * EventPublisherから受け取ったEventをEventSubscriberに転送する
 * またVRCLogCollectorの終了を検知する
 *
 * @param eventPublishedQueue EventPublisherで発行されたEventを受け取るqueue
 * @param eventSubscribers EventBusをサブスクライブしているEventSubscriberのリスト
 */
class EventBus(private val eventPublishedQueue: PriorityBlockingQueue<Event>, private val eventSubscribers: List<EventSubscriber>, private val timeout: Long = 5): Runnable{
    private var activateShutDown = false
    private var continuePoll = true
    private var currentState: UpdateInstanceStateEvent? = null
    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        //更に外側へ伝播させる
        throw throwable
    }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    override fun run() {
        while (continuePoll) {
            val event = eventPublishedQueue.poll(timeout, TimeUnit.MINUTES)

            showLog(event)

            when (event) {
                is QuitVRCEvent -> {
                    //シャットダウンを有効化
                    activateShutDown = true
                    logger.info("activate shutdown...")
                    eventSubscribers.forEach { it.onQuitVRC() }
                }
                is LaunchVRCEvent -> {
                    if (activateShutDown) {
                        //シャットダウンを無効化
                        activateShutDown = false
                        logger.info("inactivate shutdown")
                    }
                    eventSubscribers.forEach { it.onLaunchVRC() }
                }
                is UpdateInstanceStateEvent -> {
                    currentState = event
                }
                null -> {
                    if (activateShutDown) {
                        //イベントのポーリング終了
                        continuePoll = false
                        logger.info("shutting down")
                    }
                }
            }
            //イベント配給
            if (event != null) {
                eventSubscribers.filter { it.isTargetEvent(event) }
                    .forEach { subscribers ->
                        currentState?.let { state ->
                            scope.launch(exceptionHandler) {
                                subscribers.execute(event, state)
                            }
                        }
                    }
            }
        }
        //終了処理
        eventSubscribers.forEach { it.onExit() }
    }

    private fun showLog(event: Event?){
        //TODO イベントの優先度に従ってログレベルを変える
        event?.let { logger.info(event.toMap().toString()) }
    }
}