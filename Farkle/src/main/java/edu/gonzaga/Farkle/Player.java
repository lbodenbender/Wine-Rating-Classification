
package edu.gonzaga.Farkle;

import java.util.ArrayList;
import java.util.List;

/**
 * Has a score card object and hand of dice object, functions
 * for the player to "grab dice" and roll them
 * 
 * CPSC 224
 * Homework #2
 * @author Lindsey Bodenbender
 * @version v1.0 2/8/2024
 */
public class Player // implements Comparable<Player>
{
protected
    ScoreCard scoreCard = new ScoreCard();
    String name;
public 
    HandOfDice dieHand = new HandOfDice();
    List<Meld> melds = new ArrayList<Meld>();
    Integer currentMeldIndex = 0;
    
    public Player()
    {
        
    }

    Integer getTotalScore(Turn turn)
    {
        return scoreCard.totalScore;
    }

    /**
     * Creates a meld object and returns it
     * 
     * @param isCurrent
     * @return meld
     */
    public Meld createMeld(Boolean isCurrent)
    {
        Meld meld = new Meld();
        meld.isCurrentMeld = isCurrent;
        return meld;
    }

    /**
     * Sets the current status of the meld at desired index
     * 
     * @param isCurrent
     * @param index
     */
    public void setCurrentMeldStatus(Boolean isCurrent, Integer index)
    {
        melds.get(index).isCurrentMeld = isCurrent;
    }

    /**
     * Sets the scored status of the meld at desired index
     * 
     * @param isScored
     * @param index
     */
    public void setScoredStatus(Boolean isScored, Integer index)
    {
        melds.get(index).isScored = isScored;
    }

    /**
     * Adds meld object to list of <Meld>
     * 
     * @param meldList
     * @param addThisMeld
     */
    public void addMeldToList(List<Meld> meldList, Meld addThisMeld)
    {
        meldList.add(addThisMeld);
    }

    /**
     * Calls createAndAddDiceToList
     * 
     * @param numDice, numsides, uses these to detemine how many
     * die objects to create and how many sides
     * @return dieHand
     */
    public HandOfDice grabDice(Integer numDice, Integer numSides)
    {
        dieHand.createAndAddDiceToList(numDice, numSides);
        return dieHand;
    }

    /**
     * Calls rollDiceInHand
     * 
     * @param none
     * @return void
     */
    void roll()
    {
        dieHand.rollDiceInHand();
    }
}


