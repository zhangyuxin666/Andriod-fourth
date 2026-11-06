package cn.edu.sicnu.stu.zhangyuxin.fourth.model

class CardMatchingGame private constructor(private var cards: MutableList<Card>) : java.io.Serializable {
    constructor(count: Int) : this(deal(count))
    constructor(testCards: Collection<Card>) : this(testCards.toMutableList())

    companion object {
        const val GROUP_SIZE = 3
        private const val MISMATCH_PENALTY = 2
        private const val MATCH_BONUS = 4
        private const val COST_TO_CHOOSE = 1

        private fun deal(count: Int): MutableList<Card> {
            require(count in 3..39 && count % GROUP_SIZE == 0)
            // Deal rank triples before shuffling so every initial board has a solution.
            return Card.rankStrings.toList().shuffled().take(count / GROUP_SIZE)
                .flatMap { rank -> Card.validSuits.toList().shuffled().take(GROUP_SIZE).map { Card(it, rank) } }
                .shuffled().toMutableList()
        }
    }

    val cardCount: Int get() = cards.size
    val matchedCount: Int get() = cards.count { it.isMatched }
    val selectedCount: Int get() = cards.count { it.isChosen && !it.isMatched }
    val isFinished: Boolean get() {
        val remaining = cards.filterNot { it.isMatched }
        for (i in remaining.indices) {
            for (j in i + 1 until remaining.size) {
                for (k in j + 1 until remaining.size) {
                    if (remaining[i].match(arrayOf(remaining[j], remaining[k])) > 0) return false
                }
            }
        }
        return true
    }
    var score = 0
        private set

    fun cardAtIndex(index: Int): Card = cards[index]

    fun reset() {
        cards = deal(cardCount)
        score = 0
    }

    fun chooseCardAtIndex(index: Int) {
        val card = cardAtIndex(index)
        if (card.isMatched) return
        if (card.isChosen) {
            card.isChosen = false
            return
        }

        val selected = cards.filter { it.isChosen && !it.isMatched }
        card.isChosen = true
        score -= COST_TO_CHOOSE
        if (selected.size < GROUP_SIZE - 1) return

        val matchScore = card.match(selected.toTypedArray())
        val group = selected + card
        if (matchScore > 0) {
            score += matchScore * MATCH_BONUS
            group.forEach { it.isMatched = true }
        } else {
            score -= MISMATCH_PENALTY
            group.forEach { it.isChosen = false }
        }
    }
}
