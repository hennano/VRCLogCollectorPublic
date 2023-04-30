package net.hennabatch.vrclogcollector.notifier.xsoverlay

import io.kotest.assertions.json.shouldBeValidJson
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.FunSpec
import kotlinx.coroutines.async
import net.hennabatch.vrclogcollector.notifier.Message
import net.hennabatch.vrclogcollector.notifier.MessageType
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

@OptIn(io.kotest.common.ExperimentalKotest::class)
class XSOverlayNotifierTest: FunSpec( {
    context("正常系"){

        val localTestIp = InetAddress.getLoopbackAddress()
        val localTestPort = 55555
        var receiveSocket: DatagramSocket? = null

        beforeTest {
            //通知受け取り側ソケットを生成する
            try{
                receiveSocket = DatagramSocket(localTestPort)
            }catch (_: Exception){
                receiveSocket?.close()
            }
        }

        afterTest {
            //通知受け取り側ソケットを削除する
            receiveSocket?.close()
        }

        test("info"){
            //準備
            val receiveMessage = async{
                val buf = ByteArray(10240)
                val packet = DatagramPacket(buf, buf.size)
                receiveSocket?.receive(packet)
                return@async String(buf, 0 , packet.length)
            }
            val testNotifier = XSOverlayNotifier(localTestIp, localTestPort)
            val testMessage = Message(MessageType.INFO, "testTitle", "testContent")

            //テスト
            testNotifier.send(testMessage)

            //検証
            val actualMessage = receiveMessage.await()
            actualMessage.shouldBeValidJson()
            actualMessage.shouldEqualJson("""{"messageType":1,"index":0,"timeOut":1.0,"height":120.0,"opacity":1.0,"volume":0.5,"audioPath":"default","title":"testTitle","content":"testContent","useBase64Icon":false,"icon":"","sourceApp":"VRCLC"}""")
        }

        test("warn"){
            //準備
            val receiveMessage = async{
                val buf = ByteArray(10240)
                val packet = DatagramPacket(buf, buf.size)
                receiveSocket?.receive(packet)
                return@async String(buf, 0 , packet.length)
            }
            val testNotifier = XSOverlayNotifier(localTestIp, localTestPort)
            val testMessage = Message(MessageType.WARN, "testTitle", "testContent")

            //テスト
            testNotifier.send(testMessage)

            //検証
            val actualMessage = receiveMessage.await()
            actualMessage.shouldBeValidJson()
            actualMessage.shouldEqualJson("""{"messageType":1,"index":0,"timeOut":1.0,"height":120.0,"opacity":1.0,"volume":0.5,"audioPath":"default","title":"testTitle","content":"testContent","useBase64Icon":false,"icon":"","sourceApp":"VRCLC"}""")
        }

        test("error"){
            //準備
            val receiveMessage = async{
                val buf = ByteArray(10240)
                val packet = DatagramPacket(buf, buf.size)
                receiveSocket?.receive(packet)
                return@async String(buf, 0 , packet.length)
            }
            val testNotifier = XSOverlayNotifier(localTestIp, localTestPort)
            val testMessage = Message(MessageType.ERROR, "testTitle", "testContent")

            //テスト
            testNotifier.send(testMessage)

            //検証
            val actualMessage = receiveMessage.await()
            actualMessage.shouldBeValidJson()
            actualMessage.shouldEqualJson("""{"messageType":1,"index":0,"timeOut":1.0,"height":120.0,"opacity":1.0,"volume":0.5,"audioPath":"default","title":"testTitle","content":"testContent","useBase64Icon":false,"icon":"","sourceApp":"VRCLC"}""")
        }
    }
    context("異常系"){

        val localTestIp = InetAddress.getLoopbackAddress()
        val localTestPort = 55555

        test("送信タイムアウト"){
            //準備
            val testNotifier = XSOverlayNotifier(localTestIp, localTestPort)
            val testMessage = Message(MessageType.INFO, "testTitle", "testContent")

            //テスト
            shouldNotThrowAny {
                testNotifier.send(testMessage)
            }
        }
    }

    context("XSOverlay送信テスト").config(enabled = false){

        val localTestIp = InetAddress.getLoopbackAddress()
        val localTestPort = 42069

        test("info"){
            val testNotifier = XSOverlayNotifier(localTestIp, localTestPort)
            val testMessage = Message(MessageType.INFO, "testTitle", "testContent")

            //テスト
            testNotifier.send(testMessage)
        }
    }
})