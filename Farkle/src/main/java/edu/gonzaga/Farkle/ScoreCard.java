
package edu.gonzaga.Farkle;

import java.util.Arrays;

/**
 * Class to score different combos
 * 
 * CPSC 224
 * Homework #2
 * @author Lindsey Bodenbender
 * @version v1.0 2/8/2024
 */
public class ScoreCard
{
protected
    Integer totalScore = 0;
    Integer pointsForRound = 0;
    Integer pointsToPlay = 10000;
public
    Combo combo = new Combo();

    /**
     * Calculates score of die of value one
     * 
     * @param combo, meldValues, uses this to calculate the score
     * @return score
     */
    Integer scoreOne(Integer[] meldValues, Player player)
    {
        Integer score = 0;
        score = 100;
        if(meldValues[0] == 2)
        {
            score = 200;
        }
        player.melds.get(player.currentMeldIndex).meldValues[0] = 0;
        return score;
    }

    /**
     * Calculates score of die of value five
     * 
     * @param combo, meldValues, uses this to calculate the score
     * @return score
     */
    Integer scoreFive(Integer[] meldValues, Player player)
    {
        Integer score = 0;
        score = 50;
        if(meldValues[4] == 2)
        {
            score = 100;

        }
        player.melds.get(player.currentMeldIndex).meldValues[4] = 0;
        return score;
    }

    /**
     * Calculates score of a triple
     * 
     * @param combo, meldValues, uses this to calculate the score
     * @return score
     */
    Integer scoreTriple(Integer[] meldValues, Player player)
    {
        Integer score = 0;
        // if ones
        if(meldValues[0] >= 3)
        {
            score = 1000;
            // if more than three of a kind
            if(meldValues[0] > 3) 
            { 
                score += (meldValues[0] - 3) * 100;
            }
        // // all 1s have been used in scoring
        player.melds.get(player.currentMeldIndex).meldValues[0] = 0;
        }
        // all other die values
        for(int i = 1; i < meldValues.length; i++)
        {
            Boolean isUsed = false;
            if(meldValues[i] >= 3)
            {
                score += (i + 1) * 100;
                isUsed = true;
            }
            if(meldValues[i] > 3)
            {
                score += (meldValues[i] - 3) * 100 * (i + 1); 
            } 
            if(isUsed)
            {
                player.melds.get(player.currentMeldIndex).meldValues[i] = 0;
            }
        }
        return score;
    }

    /**
     * Calculates score of pairs of dice
     * 
     * @param combo, meldValues, uses this to calculate the score
     * @return score
     */
    Integer scorePairs(Integer[] meldValues, Player player)
    {
        Integer score = 750;
        // set meld values to 0
        Arrays.fill(player.melds.get(player.currentMeldIndex).meldValues, 0);
        return score;
    }

    /**
     * Calculates the score of a full house and subtracts from meld values accordingly
     * 
     * @param meldValues
     * @param player
     * @return score
     */
    Integer scoreFullHouse(Integer[] meldValues, Player player)
    {
        Integer score = 1500;
        for(int i = 0; i < meldValues.length; i++)
        {
            if(meldValues[i] == 3)
            {
                player.melds.get(player.currentMeldIndex).meldValues[i] = 0;
            }
            if(meldValues[i] == 2)
            {
                player.melds.get(player.currentMeldIndex).meldValues[i] = 0;
            } 
        }
        return score;
    }

    /**
     * Calculates score of die of straight
     * 
     * @param combo, meldValues, uses this to calculate the score
     * @return score
     */
    Integer scoreStraight(Integer[] meldValues, Player player)
    {
        Integer score = 750;
        // if(combo.triple(meldValues))
        // {
        //     for(int i = 0; i < meldValues.length; i++)
        //     {
        //         if(meldValues[i] >= 3)
        //         {
        //             score = i * 100;
        //             if(i == 1)
        //             {
        //                 score = 1000;
        //             }
        //         }
        //     }
        //     // set current meld values to 0
        //     Arrays.fill(player.melds.get(player.currentMeldIndex).meldValues, 0);
        // }
        Arrays.fill(player.melds.get(player.currentMeldIndex).meldValues, 0);
        return score;
    }
}


