/**
 * Class that represents a meld with meldValues and booleans to determine the current or scored status of the meld
 * 
 * 
 * CPSC 224
 * Homework #2
 * @author Lindsey Bodenbender
 * @version v1.0 2/8/2024
 */

package edu.gonzaga.Farkle;

import java.util.Arrays;
import java.util.List;

public class Meld
{
    ScoreCard scoreCard = new ScoreCard();
    Integer meldValues[] = new Integer[6];
    Boolean isCurrentMeld = false;
    Boolean isScored = false;
    protected Integer meldScore = 0;

    /**
     * Returns the meld score
     * 
     * @return meldScore
     * @param none
     */
    public Integer getMeldScore()
    {
        return meldScore;
    }

    /**
     * Adds to the meldValues array, counting how many
     * of each die value is in the current meld
     * 
     * @param dice
     * @return void
     */
    public Integer[] addToMeldValues(List<Die> dice)
    {
        // fill meldValues with 0, otherwise will have errors
        Arrays.fill(meldValues, 0);
        // if die is in meld, add its value to that index of the meldValues array
        for(int i = 0; i < dice.size(); i++)
        {
            if(dice.get(i).dieStatus == DieStatusEnum.ISINCURRENTMELD)
            {
                meldValues[dice.get(i).getSideUp() - 1] += 1;
            }
        }
        return meldValues;
    }

    /**
     * Adds to the score variable depending on which combo was made
     * in the meld
     * 
     * @param none
     * @return score
     */
    // pass the current meld index, use that meld's meldValues
    public Integer scoreMeld(Player player, Integer currentIndex)
    {
        Integer score = 0;

        // if the current meld has not been scored
        if(player.scoreCard.combo.isFullHouse(player.melds.get(currentIndex).meldValues))
        {
            score = player.scoreCard.scoreFullHouse(player.melds.get(currentIndex).meldValues, player);
        }
        if(player.scoreCard.combo.isStraight(player.melds.get(currentIndex).meldValues))
        {
            score = player.scoreCard.scoreStraight(player.melds.get(currentIndex).meldValues, player);
        }
        if(player.scoreCard.combo.pairCount(player.melds.get(currentIndex).meldValues) == 3)
        {   
            score = player.scoreCard.scorePairs(player.melds.get(currentIndex).meldValues, player);
        }

        if(player.scoreCard.combo.triple(player.melds.get(currentIndex).meldValues) )
        {
            score += player.scoreCard.scoreTriple(player.melds.get(currentIndex).meldValues, player);
        }

        if(player.scoreCard.combo.one(player.melds.get(currentIndex).meldValues))
        {
            score += player.scoreCard.scoreOne(player.melds.get(currentIndex).meldValues, player);
        }

        if(player.scoreCard.combo.five(player.melds.get(currentIndex).meldValues))
        {
            score += player.scoreCard.scoreFive(player.melds.get(currentIndex).meldValues, player);
        }

        player.melds.get(currentIndex).meldScore = score;

        return score;
    }
}


