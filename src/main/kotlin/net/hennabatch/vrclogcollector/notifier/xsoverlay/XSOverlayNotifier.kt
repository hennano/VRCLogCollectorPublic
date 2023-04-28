package net.hennabatch.vrclogcollector.notifier.xsoverlay

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.hennabatch.vrclogcollector.common.util.logger
import net.hennabatch.vrclogcollector.notifier.Message
import net.hennabatch.vrclogcollector.notifier.MessageType
import net.hennabatch.vrclogcollector.notifier.Notifier
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.Charset

/**
 * XSOverlayNotifier クラス
 * XSOverlayで通知を行う
 * @param ip XSOverlayを実行しているIPアドレス
 * @param port XSOverlayが受け付けるポート
 */
class XSOverlayNotifier(private val ip: InetAddress, private val port: Int): Notifier {
    override fun send(message: Message) {
        sendMessage(message.toXSOMessage())
    }

    /**
     * 通知の種類に合わせた設定のXOSMessageに変換する
     */
    private fun Message.toXSOMessage(): XSOMessage {
        return when (this.type) {
            MessageType.INFO -> {
                XSOMessage(title = title, content = content)
            }
            MessageType.WARN -> {
                XSOMessage(title = title, content = content)
            }
            MessageType.ERROR -> {
                XSOMessage(title = title, content = content)
            }
        }
    }

    /**
     * XSOverlayの通知を送信する
     * @param xsoMessage 送信するメッセージ
     */
    private fun sendMessage(xsoMessage: XSOMessage){
        var socket: DatagramSocket? = null
        try {
            socket = DatagramSocket()
            logger.debug(Json.encodeToString(xsoMessage))
            val data = Json{ encodeDefaults = true }.encodeToString(xsoMessage).toByteArray(Charset.forName("utf-8"))
            socket.soTimeout = 100
            socket.send(DatagramPacket(data, data.size, ip, port))
        } catch (e: Exception) {
            logger.warn("send failed.")
        } finally {
            socket?.close()
        }
    }
}