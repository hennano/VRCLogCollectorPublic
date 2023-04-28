package net.hennabatch.vrclogcollector.notifier

/**
 * Notifier インターフェース
 * 通知を行うクラスは継承する
 */
interface Notifier {
    /**
     * 通知を送信する
     * @param message 送信するメッセージ
     */
    fun send(message: Message)
}