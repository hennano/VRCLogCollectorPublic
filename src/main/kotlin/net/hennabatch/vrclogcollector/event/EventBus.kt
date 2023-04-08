package net.hennabatch.vrclogcollector.event

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.hennabatch.vrclogcollector.common.util.logger
import net.hennabatch.vrclogcollector.event.common.UpdateInstanceStateEvent
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
class EventBus(private val eventPublishedQueue: PriorityBlockingQueue<Event>, private val eventSubscribers: List<EventSubscriber>): Runnable{
    private var activateShutDown = false
    private var continuePoll = true
    private var currentState: UpdateInstanceStateEvent? = null
    private val scope = CoroutineScope(Job() + Dispatchers.Default)
    override fun run() {
        while (continuePoll) {
            val event = eventPublishedQueue.poll(5, TimeUnit.MINUTES)

            //TODO イベントの優先度に従ってログレベルを変える
            event?.let { logger.info(event.toMap().toString()) }

            when (event) {
                /*
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
                 */
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
                            scope.launch {
                                subscribers.execute(event, state)
                            }
                        }
                    }
            }
        }
        //終了処理
        eventSubscribers.forEach { it.onExit() }
    }
}