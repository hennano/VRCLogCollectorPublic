package net.hennabatch.vrclogcollector.notifier.xsoverlay

import kotlinx.serialization.Serializable

/**
 * XSOMessage クラス
 * XSOverlayの通知パラメータ
 * @param messageType 通知の種類 1:ポップアップ 2:メディアプレーヤー情報
 * @param index messageTypeが2の時に利用　手首のアイコンを変更する
 * @param timeOut 通知インジケータが表示される時間
 * @param height 通知インジケータの高さ位置
 * @param opacity 通知インジケータの透明度
 * @param volume 通知音の大きさ
 * @param audioPath 通知ファイルのパス
 * @param title 通知のタイトル
 * @param content 通知の内容
 * @param useBase64Icon base64エンコードされたアイコンを利用するか
 * @param icon base64エンコードされたアイコン
 * @param sourceApp 通知元アプリ名(XSOverlay側デバッグ用)
 *
 * 詳細はhttps://xiexe.github.io/XSOverlayDocumentation/#/NotificationsAPI?id=xsoverlay-message-object
 */
@Serializable
data class XSOMessage (
    val messageType: Int = 1,
    val index: Int = 0,
    val timeOut: Float = 1.0f,
    val height: Float = 120.0f,
    val opacity: Float = 1.0f,
    val volume: Float = 0.5f,
    val audioPath: String = "default",
    val title: String = "",
    val content: String = "",
    val useBase64Icon: Boolean = false,
    val icon: String = "",
    val sourceApp: String = "VRCLC"
)