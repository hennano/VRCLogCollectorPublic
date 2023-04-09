package net.hennabatch.vrclogcollector.event.common

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldContain
import java.util.*

class LaunchVRCEventTest: FunSpec({
    context("正常系"){
        val strLaunchId = "000000-0000-0000-0000-000000000000"

        test("最小"){
            val testQuitVRCEvent = LaunchVRCEvent(launchId = UUID.fromString(strLaunchId))

            val testAttributes = testQuitVRCEvent.toMap()["attributes"] as Map<String, Any>
            testAttributes shouldContain ("launch_uuid" to UUID.fromString(strLaunchId))
        }
    }
})