package net.hennabatch.vrclogcollector.notifier

/**
 * Message クラス
 * 通知情報を保持する
 * @param type 通知のタイプ
 * @param title 通知のタイトル
 * @param content 通知の内容
 */
class Message (
    val type: MessageType,
    val title: String,
    val content: String
)