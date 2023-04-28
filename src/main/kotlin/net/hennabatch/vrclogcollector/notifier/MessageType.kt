package net.hennabatch.vrclogcollector.notifier

/**
 * MessageType クラス
 * 通知の重要度を識別する
 * 重要度が高い順に
 * ERROR → WARN → INFO
 */
enum class MessageType {
    /**
     * ERROR
     * 問題があり、他の機能に影響が出るもの
     */
    ERROR,

    /**
     * WARN
     * 問題があるものの、他の機能に影響が出ない
     */
    WARN,

    /**
     * INFO
     * 基本的な通知
     */
    INFO
}