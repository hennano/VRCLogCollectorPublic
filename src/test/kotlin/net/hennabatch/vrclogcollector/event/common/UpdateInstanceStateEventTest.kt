package net.hennabatch.vrclogcollector.event.common

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.shouldBe
import net.hennabatch.vrclogcollector.event.Event
import net.hennabatch.vrclogcollector.event.Priority
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class UpdateInstanceStateEventTest: FunSpec({
    context("正常系"){

        val strLaunchId = "000000-0000-0000-0001-000000000000"
        val strInstanceId = "000000-0000-0000-0002-000000000000"
        val strJoinId = "000000-0000-0000-0003-000000000000"
        val strUpdateById = "000000-0000-0000-0004-000000000000"
        val world = "TestWorld"
        val player = "TestPlayer"

        test("最小"){
            val players = listOf(player)

            val testUpdateInstanceStateEvent = UpdateInstanceStateEvent(
                launchId = UUID.fromString(strLaunchId),
                instanceId = UUID.fromString(strInstanceId),
                joinId = UUID.fromString(strJoinId),
                updatedById = UUID.fromString(strUpdateById),
                world = world,
                players = players
            )

            val testAttributes = testUpdateInstanceStateEvent.toMap()["attributes"] as Map<String, Any>
            testAttributes shouldContain ("launch_uuid" to UUID.fromString(strLaunchId))
            testAttributes shouldContain ("instance_uuid" to UUID.fromString(strInstanceId))
            testAttributes shouldContain ("join_uuid" to UUID.fromString(strJoinId))
            testAttributes shouldContain ("updated_by_uuid" to UUID.fromString(strUpdateById))
            testAttributes shouldContain ("world" to world)
            testAttributes shouldContainKey ("players")
            (testAttributes["players"] as List<String>).shouldContainExactly(player)
        }

        test("プレイヤー空"){
            val players = listOf<String>()

            val testUpdateInstanceStateEvent = UpdateInstanceStateEvent(
                launchId = UUID.fromString(strLaunchId),
                instanceId = UUID.fromString(strInstanceId),
                joinId = UUID.fromString(strJoinId),
                updatedById = UUID.fromString(strUpdateById),
                world = world,
                players = players
            )

            val testAttributes = testUpdateInstanceStateEvent.toMap()["attributes"] as Map<String, Any>
            testAttributes shouldContain ("launch_uuid" to UUID.fromString(strLaunchId))
            testAttributes shouldContain ("instance_uuid" to UUID.fromString(strInstanceId))
            testAttributes shouldContain ("join_uuid" to UUID.fromString(strJoinId))
            testAttributes shouldContain ("updated_by_uuid" to UUID.fromString(strUpdateById))
            testAttributes shouldContain ("world" to world)
            testAttributes shouldContainKey ("players")
            (testAttributes["players"] as List<String>).shouldBeEmpty()
        }

        test("プレイヤー複数"){
            val player2 = "TestPlayer2"
            val players = listOf<String>(player, player2)

            val testUpdateInstanceStateEvent = UpdateInstanceStateEvent(
                launchId = UUID.fromString(strLaunchId),
                instanceId = UUID.fromString(strInstanceId),
                joinId = UUID.fromString(strJoinId),
                updatedById = UUID.fromString(strUpdateById),
                world = world,
                players = players
            )

            val testAttributes = testUpdateInstanceStateEvent.toMap()["attributes"] as Map<String, Any>
            testAttributes shouldContain ("launch_uuid" to UUID.fromString(strLaunchId))
            testAttributes shouldContain ("instance_uuid" to UUID.fromString(strInstanceId))
            testAttributes shouldContain ("join_uuid" to UUID.fromString(strJoinId))
            testAttributes shouldContain ("updated_by_uuid" to UUID.fromString(strUpdateById))
            testAttributes shouldContain ("world" to world)
            testAttributes shouldContainKey ("players")
            (testAttributes["players"] as List<String>).shouldContainExactly(player, player2)

        }
    }
})