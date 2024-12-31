package edu.gonzaga.Farkle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

// ** Currently, this is a copy of Die test.
// You can create more test files for your classes in this directoy.
// Copy DieTest and start from there
class HandTest 
{
    @Test
    void testDefaultValue() {
        Integer expectedValue = 1;
        Die die = new Die(6);
        System.out.println("Default value should be 1");
        assertEquals(expectedValue, die.getSideUp());
    }

    @Test
    void testInitializedValue() {
        Integer expectedValue = 4;
        Die die = new Die(6, expectedValue);
        System.out.println("Initialized value should be 4");
        assertEquals(expectedValue, die.getSideUp());
    }

    @Test
    void testGreaterThan() {
        Integer die1Value = 6;
        Integer die2Value = 4;
        Die die1 = new Die(6, die1Value);
        Die die2 = new Die(6, die2Value);
        assertTrue(die1.compareTo(die2) > 0 );
    }

    @Test
    void testLessThan() {
        Integer die1Value = 1;
        Integer die2Value = 2;
        Die die1 = new Die(6, die1Value);
        Die die2 = new Die(6, die2Value);
        assertTrue(die1.compareTo(die2) < 0 );
    }

    @Test
    void testEquals() {
        Integer die1Value = 3;
        Integer die2Value = 3;
        Die die1 = new Die(6, die1Value);
        Die die2 = new Die(6, die2Value);
        assertTrue(die1.compareTo(die2) == 0);
    }

    @Test
    void testStraight()
    {
        Combo combo = new Combo();
        Integer meldValues[] = {1, 1, 1, 1, 1, 1};
        assertTrue(combo.isStraight(meldValues));
    }

    @Test void testScoreStraight()
    {
        Turn turn = new Turn();
        turn.addPlayerToList(turn.createPlayer());

        Meld meld = turn.currentPlayer().createMeld(true);
        turn.currentPlayer().addMeldToList(turn.currentPlayer().melds, meld);
        
        // add to meld's meldValues
        Integer meldValues[] = {1, 1, 1, 1, 1, 1};
        turn.currentPlayer().melds.get(0).meldValues = meldValues;
        
        Integer expected = 750;
        Integer actual = turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).scoreMeld(turn.currentPlayer(), turn.currentPlayer().currentMeldIndex);
        assertEquals(expected, actual);
    }

    @Test void testScoreNotStraight()
    {
        Turn turn = new Turn();
        turn.addPlayerToList(turn.createPlayer());

        Meld meld = turn.currentPlayer().createMeld(true);
        turn.currentPlayer().addMeldToList(turn.currentPlayer().melds, meld);
        
        // add to meld's meldValues
        Integer meldValues[] = {1, 1, 1, 2, 1, 1};
        turn.currentPlayer().melds.get(0).meldValues = meldValues;
        
        Integer expected = 750;
        Integer actual = turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).scoreMeld(turn.currentPlayer(), turn.currentPlayer().currentMeldIndex);
        assertNotEquals(expected, actual);
    }

    @Test void scoreFullHouse()
    {
        Game game = new Game();
        // Turn turn = new Turn();
        game.turn.addPlayerToList(game.turn.createPlayer());

        Meld meld = game.turn.currentPlayer().createMeld(true);
        game.turn.currentPlayer().addMeldToList(game.turn.currentPlayer().melds, meld);
        
        // add to meld's meldValues
        Integer meldValues[] = {3, 0, 0, 2, 0, 0};
        game.turn.currentPlayer().melds.get(0).meldValues = meldValues;
        
        Integer expected = 1500;
        Integer actual = game.turn.currentPlayer().melds.get(game.turn.currentPlayer().currentMeldIndex).scoreMeld(game.turn.currentPlayer(), game.turn.currentPlayer().currentMeldIndex);
        assertEquals(expected, actual);
    }

    @Test
    void testTriple()
    {
        Combo combo = new Combo();
        Integer meldValues[] = {0, 0, 0, 3, 0 ,0};
        assertTrue(combo.triple(meldValues));
    }

    @Test
    void scoreTripleFours()
    {
        Turn turn = new Turn();
        turn.addPlayerToList(turn.createPlayer());

        Meld meld = turn.currentPlayer().createMeld(true);
        turn.currentPlayer().addMeldToList(turn.currentPlayer().melds, meld);

        Integer meldValues[] = {0, 0, 0, 3, 0 ,0};
        turn.currentPlayer().melds.get(0).meldValues = meldValues;
        
        Integer expected = 400;
        Integer actual = turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).scoreMeld(turn.currentPlayer(), turn.currentPlayer().currentMeldIndex);
        assertEquals(expected, actual);
    }

    @Test
    void scoreTripleOnes()
    {
        Player player = new Player();
        Integer meldValues[] = {4, 0, 0, 0, 0 ,0};
        Integer actual = player.scoreCard.scoreTriple(meldValues, player);
        Integer expected = 1100;
        assertEquals(expected, actual);
    }

    @Test
    void scoreTwoTriples()
    {
        Player player = new Player();
        Integer meldValues[] = {0, 3, 3, 0, 0, 0};
        Integer actual = player.scoreCard.scoreTriple(meldValues, player);
        Integer expected = 500;
        assertEquals(expected, actual);
    }

    @Test
    void testHotHand()
    {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        game.turn.addPlayerToList(game.turn.createPlayer());
        game.setUpGame();
        Integer meldValues[] = {1, 1, 1, 1, 1, 1};
        game.turn.currentPlayer().melds.get(game.turn.currentPlayer().currentMeldIndex).meldValues = meldValues;
        char choice = game.getChoiceInput(scanner);
        game.handleHotHand(game.isHotHand(), scanner);
    }

    // @Test
    // void scoreTwoTriplesWithOne()
    // {
    //     Player player = new Player();
    //     Integer meldValues[] = {3, 0, 0, 0, 3, 0};
    //     Integer actual = player.scoreCard.scoreTriple(meldValues, player);
    //     Integer expected = 1500;
    //     assertEquals(expected, actual);
    // }

    @Test
    void testPairs()
    {
        Combo combo = new Combo();
        Integer meldValues[] = {2, 0, 2, 2, 0, 0};
        Integer expected = 3;
        Integer actual = combo.pairCount(meldValues);
        assertEquals(expected, actual);
    }

    @Test
    void testPairsNotEqual()
    {
        Combo combo = new Combo();
        Integer meldValues[] = {2, 0, 2, 1, 0, 0};
        Integer expected = 3;
        Integer actual = combo.pairCount(meldValues);
        assertNotEquals(expected, actual);
    }

    @Test
    void testFarkle()
    {
        Combo combo = new Combo();
        Integer meldValues[] = {0, 1, 2, 0, 0, 1};
        assertTrue(combo.isFarkle(meldValues));
    }

    @Test
    void testNotFarkle()
    {
        Combo combo = new Combo();
        Integer meldValues[] = {0, 3, 2, 0, 0, 1};
        assertFalse(combo.isFarkle(meldValues));
    }

    @Test
    void testPlayerName()
    {
        Game game = new Game();
        game.turn.numPlayers = 1;
        game.turn.createPlayersAddToList();
        Scanner scanner = new Scanner(System.in);
        game.setName("Lindsey", scanner, 0);
        String actual = game.turn.currentPlayer().name;
        String expected = "Lindsey";
        assertEquals(expected, actual);
    }

    @Test
    void testFullHouse()
    {
        Combo combo = new Combo();
        Integer meldValues[] = {0, 3, 2, 0, 0, 0};
        assertTrue(combo.isFullHouse(meldValues));
    }
}


