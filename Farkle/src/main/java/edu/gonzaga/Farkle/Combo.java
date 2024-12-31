
package edu.gonzaga.Farkle;

import java.util.Arrays;
import java.util.List;

/**
 * Has an array to store the value counts in the meld
 * and functions to detemine if the player rolled a farkle
 * and what combo they rolled if not
 * 
 * CPSC 224
 * Homework #2
 * @author Lindsey Bodenbender
 * @version v1.0 2/8/2024
 */
public class Combo 
{
    // dice that are not in meld
    Integer remainingDice[] = new Integer[6];

    /**
     * Detemines if the player rolled a farkle
     * 
     * @param values
     * @return isFarkle
     */
    Boolean isFarkle(Integer[] values)
    {
        Boolean isFarkle = true;

        if(values[0] != 0 || values[4] != 0) 
        {
            isFarkle = false;
        }
        if(pairCount(values) == 3)
        {
            isFarkle = false;
        }
        if(triple(values))
        {
            isFarkle = false;
        }
        return isFarkle;
    }

    /**
     * Determines if there is a die of value one in the meld
     * 
     * @param values
     * @return one
     */
    Boolean one(Integer[] values)
    {
        Boolean one = false;
        if(values[0] >= 1)
        {
            one = true;
        }
        return one;
    }

    /**
     * Determines if there is a die of value 5 in the meld
     * 
     * @param values
     * @return five
     */
    Boolean five(Integer[] values)
    {
        Boolean five = false;
        if(values[4] >= 1)
        {
            five = true;
        }
        return five;
    }

    /**
     * Determines if the values are a full house
     * 
     * @param values
     * @return isFullHouse
     */
    Boolean isFullHouse(Integer[] values)
    {
        Boolean isFullHouse = false;
        Boolean isTriple = false;
        Boolean isDouble = false;
        for(int i = 0; i < values.length; i++)
        {
            if(values[i] == 3)
            {
                // index to keep track of which die value has 3 of it
                isTriple = true;
            }
            if(values[i] == 2)
            {
                isDouble = true;
            }
            if(isTriple && isDouble)
            {
                isFullHouse = true;
            }
        }
        return isFullHouse;
    }

    
    /**
     * Determines if there is a straight in the meld
     * 
     * @param values
     * @return isStraight
     */
    Boolean isStraight(Integer[] values)
    {
        Boolean isStraight = false;
        Integer count = 0;
        for(int i = 0; i < values.length; i++)
        {
            if(values[i] == 1)
            {
                count++;
            }
        }
        if(count == values.length)
        {
            isStraight = true;
        }
        return isStraight;
    }

    /**
     * Determines if there is a triple in the meld
     * 
     * @param values
     * @return triple
     */
    Boolean triple(Integer[] values)
    {
        Boolean triple = false;
        for(int i = 0; i < values.length; i++)
        {
            if(values[i] >= 3)
            {
                triple = true;
            }
        }
        return triple;
    }

    /**
     * Counts how many pairs of dice are in the meld
     * 
     * @param values
     * @return pairCount
     */
    int pairCount(Integer[] values)
    {
        int pairCount = 0;
        for(int i = 0; i < values.length; i++) 
        {
            if(values[i] == 2 ) 
            {
                pairCount++;
            }
        }
        return pairCount;
    }

    /**
     * Count the die values that are in the hand
     * 
     * @return void
     * @param dice
     */
    void addToRemainingDice(List<Die> dice)
    {
        // fill remainingDice with 0 (must do this)
        Arrays.fill(remainingDice, 0);
        // if die is not in meld, add its value to that index of the remainingDice array
        for(int i = 0; i < dice.size(); i++)
        {
            if(dice.get(i).dieStatus == DieStatusEnum.ISINHAND)
            {
                remainingDice[dice.get(i).getSideUp() - 1] += 1;
            }
        }
    }
}


