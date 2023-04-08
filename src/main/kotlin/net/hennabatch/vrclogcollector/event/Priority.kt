package net.hennabatch.vrclogcollector.event

/**
 * Priority クラス
 * eventQueueの優先度を定義する
 * 優先度が高い順に
 * CRITICAl →　IMMEDIATE → NORMAL → MINOR
 */
enum class Priority(val priority: Int) {
    /**
     * Critical
     * 基本は指定せず、致命的なエラー等のみ指定する
     */
    CRITICAl(0),

    /**
     * Immediate
     * 即時性が必要なときに指定する
     */
    IMMEDIATE(1),

    /**
     * Normal
     * 通常はこれを指定する
     */
    NORMAL(2),

    /**
     * Minor
     * 優先度の低いイベントのときに指定する
     */
    MINOR(3)
}