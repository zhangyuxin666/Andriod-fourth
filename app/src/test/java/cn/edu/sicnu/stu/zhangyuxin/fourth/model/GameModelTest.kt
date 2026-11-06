package cn.edu.sicnu.stu.zhangyuxin.fourth.model

import java.io.*
import org.junit.Assert.*
import org.junit.Test

class GameModelTest {
    @Test fun deckContains52UniqueCardsAndExhausts() {
        val deck = Deck()
        val drawn = (1..52).map { requireNotNull(deck.drawRandomCard()).toString() }
        assertEquals(52, drawn.toSet().size)
        assertNull(deck.drawRandomCard())
    }

    @Test fun matchingRanksLockAndScoreInBothModes() {
        val suits = listOf("♥", "♣", "♦")
        for (size in 2..3) {
            val game = CardMatchingGame(suits.take(size).map { Card(it, "A") }, size)
            repeat(size) { game.chooseCardAtIndex(it) }
            assertEquals(16 - size, game.score)
            assertEquals(size, game.matchedCount)
            assertTrue(game.isFinished)
            game.chooseCardAtIndex(0)
            assertEquals(16 - size, game.score)
        }
    }

    @Test fun matchingSuitsScoreInBothModes() {
        for (size in 2..3) {
            val game = CardMatchingGame((1..size).map { Card("♥", it.toString()) }, size)
            repeat(size) { game.chooseCardAtIndex(it) }
            assertEquals(4 - size, game.score)
            assertEquals(size, game.matchedCount)
        }
    }

    @Test fun mismatchClearsSelectionAndDeductsPenalty() {
        val cards = listOf(Card("♥", "A"), Card("♣", "K"), Card("♦", "Q"), Card("♠", "Q"))
        val game = CardMatchingGame(cards, 2)
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(1)
        assertEquals(-4, game.score)
        assertEquals(0, game.selectedCount)
        assertEquals(0, game.matchedCount)
        assertFalse(game.isFinished)
    }

    @Test fun resetClearsScoreSelectionAndMatchedCards() {
        for (size in 2..3) {
            val game = CardMatchingGame(24, size)
            game.chooseCardAtIndex(0)
            game.reset()
            assertEquals(24, game.cardCount)
            assertEquals(0, game.score)
            assertEquals(0, game.selectedCount)
            assertEquals(0, game.matchedCount)
            assertEquals(24, (0 until 24).map { game.cardAtIndex(it).toString() }.toSet().size)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun invalidCountRejected() { CardMatchingGame(25, 3) }

    @Test fun serializationPreservesModeBoardAndScore() {
        for (size in 2..3) {
            val game = CardMatchingGame(24, size)
            game.chooseCardAtIndex(0)
            val bytes = ByteArrayOutputStream()
            ObjectOutputStream(bytes).use { it.writeObject(game) }
            val restored = ObjectInputStream(ByteArrayInputStream(bytes.toByteArray())).use {
                it.readObject() as CardMatchingGame
            }
            assertEquals(size, restored.groupSize)
            assertEquals(-1, restored.score)
            assertTrue(restored.cardAtIndex(0).isChosen)
            assertEquals((0 until 24).map { game.cardAtIndex(it).toString() },
                (0 until 24).map { restored.cardAtIndex(it).toString() })
        }
    }
}
