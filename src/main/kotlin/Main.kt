package com.unongmilk

private const val MAX_FINGERS = 4
private const val RESET_THRESHOLD = 5

data class PlayerState(val hands: IntArray = intArrayOf(1, 1)) {
    init {
        require(hands.size == 2) { "현재 버전은 플레이어당 손 2개만 지원합니다." }
        require(hands.all { it in 0..MAX_FINGERS }) { "손가락 수는 0~$MAX_FINGERS 여야 합니다." }
    }

    fun copyHands(): IntArray = hands.copyOf()

    fun isEliminated(): Boolean = hands.all { it == 0 }

    override fun toString(): String = "[L:${hands[0]}, R:${hands[1]}]"
}

data class GameState(
    val players: List<PlayerState> = listOf(PlayerState(), PlayerState()),
    val currentPlayer: Int = 0,
) {
    fun opponentOf(player: Int): Int = if (player == 0) 1 else 0

    fun isGameOver(): Boolean = players.count { !it.isEliminated() } <= 1

    fun winner(): Int? {
        if (!isGameOver()) return null
        return players.indexOfFirst { !it.isEliminated() }.takeIf { it >= 0 }
    }
}

sealed interface Move {
    data class Attack(val fromHand: Int, val targetHand: Int) : Move
    data class Redistribute(val newLeft: Int, val newRight: Int) : Move
}

class ChopsticksEngine(private var state: GameState = GameState()) {

    fun state(): GameState = state

    fun validMoves(): List<Move> {
        if (state.isGameOver()) return emptyList()

        val meIdx = state.currentPlayer
        val enemyIdx = state.opponentOf(meIdx)
        val me = state.players[meIdx]
        val enemy = state.players[enemyIdx]
        val moves = mutableListOf<Move>()

        // 공격
        for (myHand in 0..1) {
            if (me.hands[myHand] == 0) continue
            for (enemyHand in 0..1) {
                if (enemy.hands[enemyHand] == 0) continue
                moves += Move.Attack(myHand, enemyHand)
            }
        }

        // 분배(총합 보존 + 같은 형상 금지)
        val total = me.hands.sum()
        val currentShape = normalizedShape(me.hands[0], me.hands[1])
        for (left in 0..MAX_FINGERS) {
            val right = total - left
            if (right !in 0..MAX_FINGERS) continue
            val candidate = normalizedShape(left, right)
            if (candidate == currentShape) continue
            moves += Move.Redistribute(left, right)
        }

        return moves
    }

    fun applyMove(move: Move) {
        val available = validMoves()
        require(move in available) { "유효하지 않은 수입니다: $move" }

        val meIdx = state.currentPlayer
        val enemyIdx = state.opponentOf(meIdx)
        val players = state.players.map { PlayerState(it.copyHands()) }.toMutableList()

        when (move) {
            is Move.Attack -> {
                val damage = players[meIdx].hands[move.fromHand]
                val updated = players[enemyIdx].hands[move.targetHand] + damage
                players[enemyIdx].hands[move.targetHand] = if (updated >= RESET_THRESHOLD) 0 else updated
            }

            is Move.Redistribute -> {
                players[meIdx].hands[0] = move.newLeft
                players[meIdx].hands[1] = move.newRight
            }
        }

        state = GameState(players = players, currentPlayer = enemyIdx)
    }

    private fun normalizedShape(a: Int, b: Int): Pair<Int, Int> = if (a <= b) a to b else b to a
}

fun main() {
    val engine = ChopsticksEngine()

    println("=== Chopsticks Game (2 Players) ===")
    println("규칙: 손가락이 5 이상이 되면 해당 손은 0으로 리셋됩니다.")
    println("명령: attack <내손(0/1)> <상대손(0/1)> | split <왼손> <오른손> | quit")

    while (!engine.state().isGameOver()) {
        val s = engine.state()
        println("\n현재 턴: Player ${s.currentPlayer + 1}")
        println("Player 1 ${s.players[0]}")
        println("Player 2 ${s.players[1]}")

        val moves = engine.validMoves()
        println("가능한 수:")
        moves.forEachIndexed { idx, move -> println("  [$idx] $move") }

        print("> ")
        val input = readlnOrNull()?.trim().orEmpty()
        if (input.equals("quit", ignoreCase = true)) {
            println("게임을 종료합니다.")
            return
        }

        val tokens = input.split(" ").filter { it.isNotBlank() }
        val move = parseMove(tokens)
        if (move == null) {
            println("입력이 잘못되었습니다. 예: attack 0 1 또는 split 3 1")
            continue
        }

        try {
            engine.applyMove(move)
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    val winner = engine.state().winner()
    if (winner == null) {
        println("무승부")
    } else {
        println("\n게임 종료! 승자: Player ${winner + 1}")
    }
}

private fun parseMove(tokens: List<String>): Move? {
    if (tokens.isEmpty()) return null
    return when (tokens[0].lowercase()) {
        "attack" -> {
            if (tokens.size != 3) return null
            val from = tokens[1].toIntOrNull() ?: return null
            val to = tokens[2].toIntOrNull() ?: return null
            Move.Attack(from, to)
        }

        "split" -> {
            if (tokens.size != 3) return null
            val left = tokens[1].toIntOrNull() ?: return null
            val right = tokens[2].toIntOrNull() ?: return null
            Move.Redistribute(left, right)
        }

        else -> null
    }
}
