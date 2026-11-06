package cn.edu.sicnu.stu.zhangyuxin.fourth.model

import org.junit.Assert.*
import org.junit.Test
import java.io.*

class GameModelTest {
    @Test fun threeCardsMustShareRankOrSuit() {
        val ace = Card("♥", "A")
        assertEquals(4, ace.match(arrayOf(Card("♣", "A"), Card("♦", "A"))))
        assertEquals(1, ace.match(arrayOf(Card("♥", "K"), Card("♥", "Q"))))
        assertEquals(0, ace.match(arrayOf(Card("♥", "K"), Card("♣", "Q"))))
        assertEquals(0, ace.match(arrayOf(Card("♥", "K"))))
    }
    @Test fun deckContains52UniqueCardsAndExhausts() {
        val deck = Deck()
        val drawn = (1..52).map { requireNotNull(deck.drawRandomCard()).toString() }
        assertEquals(52, drawn.toSet().size)
        assertNull(deck.drawRandomCard())
    }
    @Test fun rankTripleScores13AndLocksCards() {
        val game = CardMatchingGame(listOf(Card("♥", "A"), Card("♣", "A"), Card("♦", "A")))
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(1)
        assertEquals(-2, game.score)
        assertEquals(2, game.selectedCount)
        game.chooseCardAtIndex(2)
        assertEquals(13, game.score)
        assertEquals(3, game.matchedCount)
        game.chooseCardAtIndex(0)
        assertEquals(13, game.score)
        assertTrue(game.isFinished)
    }
    @Test fun suitTripleScores1() {
        val game = CardMatchingGame(listOf(Card("♥", "A"), Card("♥", "K"), Card("♥", "Q")))
        (0..2).forEach(game::chooseCardAtIndex)
        assertEquals(1, game.score)
    }
    @Test fun mismatchScoresMinus5AndTurnsAllThreeBack() {
        val game = CardMatchingGame(listOf(Card("♥", "A"), Card("♣", "K"), Card("♠", "Q")))
        (0..2).forEach(game::chooseCardAtIndex)
        assertEquals(-5, game.score)
        assertEquals(0, game.selectedCount)
        assertEquals(0, game.matchedCount)
        assertTrue(game.isFinished)
    }
    @Test fun unchooseDoesNotChargeAgain() {
        val game = CardMatchingGame(24)
        game.chooseCardAtIndex(0); game.chooseCardAtIndex(0)
        assertEquals(-1, game.score)
        assertFalse(game.cardAtIndex(0).isChosen)
    }
    @Test fun freshGameResetsScoreAndCards() {
        val game = CardMatchingGame(24)
        game.chooseCardAtIndex(0)
        game.reset()
        assertEquals(0, game.selectedCount)
        assertEquals(24, game.cardCount); assertEquals(0, game.score)
        assertEquals(0, game.matchedCount)
        assertEquals(24, (0..23).map { game.cardAtIndex(it).toString() }.toSet().size)
    }
    @Test(expected = IllegalArgumentException::class) fun invalidCountRejected() { CardMatchingGame(53) }
    @Test fun serializationPreservesTurnAndScore() {
        val game = CardMatchingGame(24); game.chooseCardAtIndex(0)
        val bytes = ByteArrayOutputStream()
        ObjectOutputStream(bytes).use { it.writeObject(game) }
        val restored = ObjectInputStream(ByteArrayInputStream(bytes.toByteArray())).use { it.readObject() as CardMatchingGame }
        assertEquals(-1, restored.score)
        assertTrue(restored.cardAtIndex(0).isChosen)
        assertEquals(game.cardAtIndex(0).toString(), restored.cardAtIndex(0).toString())
    }
}
