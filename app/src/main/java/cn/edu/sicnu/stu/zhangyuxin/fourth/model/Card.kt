package cn.edu.sicnu.stu.zhangyuxin.fourth.model

class Card(private var suit: String, private var rank: String, var isChosen:Boolean = false, var isMatched:Boolean = false) : java.io.Serializable {
    companion object {
        private const val serialVersionUID = 1L
        val rankStrings= arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val validSuits = arrayOf("♥", "♦", "♠", "♣")
    }

    override fun toString(): String {
        return suit + rank
    }

    fun match(otherCards: Array<Card>): Int {
        if (otherCards.size !in 1..2) return 0
        if (otherCards.all { it.rank == rank }) return 4
        if (otherCards.all { it.suit == suit }) return 1
        return 0
    }
}
