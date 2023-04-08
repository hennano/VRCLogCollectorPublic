package net.hennabatch.vrclogcollector.event

import java.util.concurrent.PriorityBlockingQueue

abstract class EventPublisher(private val eventQueue: PriorityBlockingQueue<Event>) {

    protected fun publishEvent(event: Event){
        eventQueue.add(event)
    }

}