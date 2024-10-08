package com.unongmilk

val hashMap = hashMapOf<Pair<Finger, Finger>, Result>()

fun main() {
    val p1 = Finger()
    val p2 = Finger()

    println(evaluate(p1, p2, 0).name)
}

fun evaluate(me : Finger, enemy: Finger, depth : Int) : Result {
    var s = ""
    repeat(depth) {
        s += "-"
    }
    if (depth == 40) return Result.D
    val temp1 = hashMap.keys.find {it.first.l == me.l && it.first.r == me.r && it.second.l == enemy.l && it.second.r == enemy.r}
    if (temp1 != null && hashMap[temp1] != null) {
        return hashMap[temp1]!!
    }
    var isDraw = false
    var result = Result.L
    val p = me.getValidMove(enemy)
    if (enemy.l == enemy.r) {
        p.remove(1)
        p.remove(3)
    }

    if (me.l == me.r) {
        p.remove(2)
        p.remove(3)
    }

    if (me.l == me.r && enemy.l == enemy.r) {
        p.remove(4)
    }
    println(s + "Enable Move " + p.joinToString(" "))
    p.forEach {
        //println(s + it.toString())
        when (it) {
            0 -> {
                enemy.l += me.l
                if (enemy.l >= 5) {
                    enemy.l = 0
                    if (enemy.r == 0) { println("${s}win for 0,0 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                }
                println("${s}LL \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }

            1 -> {
                enemy.r += me.l
                if (enemy.r >= 5) {
                    enemy.r = 0
                    if (enemy.l == 0) { println("${s}win for 0,0 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                }
                println("${s}LR \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }

            2 -> {
                enemy.l += me.r
                if (enemy.l >= 5) {
                    enemy.l = 0
                    if (enemy.r == 0) { println("${s}win for 0,0 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                }
                println("${s}RL \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }

            3 -> {
                enemy.r += me.r
                if (enemy.r >= 5) {
                    enemy.r = 0
                    if (enemy.l == 0) { println("${s}win for 0,0 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                }
                println("${s}RR \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }

            4 -> {
                me.l -= 1
                me.r += 1

                println("${s}L1TR1 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }

            5 -> {
                me.l -= 2
                me.r += 2

                println("${s}L2TR2 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }

            6 -> {
                me.l -= 3
                me.r += 3

                println("${s}L3TR3 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }

            7 -> {
                me.r -= 1
                me.l += 1

                println("${s}R1TL1 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }

            8 -> {
                me.r -= 2
                me.l += 2

                println("${s}R2TL2 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }

            9 -> {
                me.r -= 3
                me.l += 3

                println("${s}R3TL3 \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}")
                val a = evaluate(enemy.copy(), me.copy(), depth + 1)
                if (a == Result.L) { println("${s}win for win_only_move \n${s}${me.l} ${me.r}\n${s}${enemy.l} ${enemy.r}"); result = Result.W }
                if (a == Result.D) isDraw = true
            }
        }
    }
    result = if (result == Result.W) return result
    else if (isDraw) Result.D
    else Result.L
    if (hashMap.keys.none { it.first.l == me.l && it.first.r == me.r && it.second.l == enemy.l && it.second.r == enemy.r }) {
        hashMap[Pair(me, enemy)] = result
    }
    return result
}

enum class Result {
    W, D, L
}

class Finger {
    var l = 0
    var r = 0

    constructor() {
        l = 1
        r = 1
    }

    constructor(a : Int, b : Int) {
        l = a
        r = b
    }
    
    fun copy() : Finger {
        return Finger(l, r)
    }

    fun getValidMove(enemy : Finger) : ArrayList<Int> {
        val ar = arrayListOf<Int>()
        if (enemy.l != 0) {
            if (l != 0) ar.add(0)
            if (r != 0) ar.add(2)
        }
        if (enemy.r != 0) {
            if (l != 0) ar.add(1)
            if (r != 0) ar.add(3)
        }
        if (l > r) {
            when (l - r) {
                1 -> {
                    if (l == 2 || l == 3) {ar.add(7); ar.add(5)}
                }
                2 -> {
                    ar.add(4)
                    if (l == 3) ar.add(7)
                }
                3 -> {
                    ar.add(4)
                    ar.add(5)
                }
                4 -> {
                    ar.add(6)
                }
            }
        } else if (r > l) {
            when (r - l) {
                1 -> {
                    if (r == 2 || r == 3) { ar.add(4); ar.add(8) }
                }
                2 -> {
                    ar.add(7)
                    if (r == 3) ar.add(4)
                }
                3 -> {
                    ar.add(7)
                    ar.add(8)
                }
                4 -> {
                    ar.add(9)
                }
            }
        } else {
            if (l == 1 || l == 2 || l == 3) {
                ar.add(4)
                ar.add(7)
                if (l == 2) { ar.add(5); ar.add(8) }
            }
        }
        return ar
    }
}