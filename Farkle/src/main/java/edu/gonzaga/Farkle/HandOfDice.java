
package edu.gonzaga.Farkle;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to store multiple dice in a "hand". Han array of die objects with functions to 
 * add/remove/create dice as well as add and remove them from the meld
 * 
 * CPSC 224
 * Homework #2
 * @author Lindsey Bodenbender
 * @version v1.0 2/8/2024
 */
public class HandOfDice 
{
public
    // create list to store dice (in "hand")
    List<Die> dice = new ArrayList<Die>();
    // array to store value count of dice
    Integer values[] = new Integer[6];

    /**
     * Creates a die object
     * 
     * @param numSides, desired number of sides this method uses to 
     * create the die
     * @return Die object created
     */
    public Die createDie(Integer numSides)
    {
        Die die = new Die(numSides);
        return die;
    }

    /**
     * Adds a die object to the list of Die
     * 
     * @param addThisDie, this method uses it to add the desired die to
     * the dice list
     * @return void
     */
    public void addDieToList(Die addThisDie)
    {
        dice.add(addThisDie);
    }

    /**
     * Creates n number of dice
     * 
     * @param numDice, numSides this object uses to determine how many die
     * objects to create and how many sides to give them
     * @return dice
     */
    public List<Die> createAndAddDiceToList(Integer numDice, Integer numSides)
    {
        for(Integer i = 0; i < numDice; i++)
        {
            addDieToList(createDie(numSides));
        }
        return dice;
    }

    /**
     * Rolls all die objects in dice list
     * 
     * @param none
     * @return void
     */
    public void rollDiceInHand()
    {
        for(Integer i = 0; i < dice.size(); i++)
        {
            // if(dice.get(i).isInHand)
            if(dice.get(i).dieStatus == DieStatusEnum.ISINHAND)
            {
                dice.get(i).roll();
            }
            else
            {
                // if there aren't any dice in the meld
                if(!dieInMeld())
                {
                    System.out.println("Add dice to meld before rolling!");
                    return;
                }
            }
        }
    }

     /**
     * Detemines if there is a die in the meld
     * 
     * @param none
     * @return true if there is a die in the meld
     */
    public Boolean dieInMeld()
    {
        for(int i = 0; i < dice.size(); i++)
        {
            // if(!dice.get(i).isInHand)
            if(dice.get(i).dieStatus != DieStatusEnum.ISINHAND)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Sorts dice, stores the results in an array sortedDiceSideUp
     * 
     * @param none
     * @return sortedDiceSideUp
     */
    public Integer[] sortDice()
    {
        Integer sortedDiceSideUp[] = new Integer[dice.size()];
        for(int i = 0; i < dice.size(); i++)
        {
            sortedDiceSideUp[i] = dice.get(i).getSideUp();
        }
        Arrays.sort(sortedDiceSideUp);
        return sortedDiceSideUp;
    }

    /**
     * Displays the sortedDice array
     * 
     * @param sortedDice array, uses this to print it
     * @return void
     */
    public void showDice(Integer[] sortedDice)
    {
        System.out.print("Hand: ");
        for(Integer i = 0; i < dice.size(); i++)
        {
            System.out.print(sortedDice[i] + " ");
        }
        System.out.println("\n");
    }

    /**
     * Prints the dice array and adds to the values array which holds
     * the number of each die value
     * 
     * @param none
     * @return void
     */
    void countDice()
    {
        Arrays.fill(values, 0);
        for(Integer i = 0; i < dice.size(); i++)
        {
            values[dice.get(i).getSideUp() - 1] += 1;
        }
        System.out.print("Quantity of each die value: ");
        for(Integer i = 0; i < dice.size(); i++)
        {
            System.out.print(values[i] + " ");
        }
    }

    /**
     * Converts a char input to an integer
     * 
     * @param input, the char to be converted
     * @return index
     */
    int choiceToIndex(char input)
    {
        int charToInt1 = input;
        int index = charToInt1 - 65;
        return index;
    }

    // todo
    /**
     * Sets the isInCurrentMeld value of a single die to true
     * 
     * @param dice, index uses these to detemine which die's 
     * isInMeld value to change to true
     * @return void
     */
    void addDieToCurrentMeld(List<Die> dice, int index)
    {
        // dice.get(index).isInHand = false;
        dice.get(index).dieStatus = DieStatusEnum.ISINCURRENTMELD;
    }

    /**
     * If the die has not been scored, change its status to in hand
     * 
     * @param dice, index uses these to detemine which die's 
     * isInMeld value to change to false
     * @return void
     */
    void removeFromCurrentMeld(List<Die> dice, int index)
    {
        if(dice.get(index).dieStatus != DieStatusEnum.ISINSCOREDMELD)
        {
            dice.get(index).dieStatus = DieStatusEnum.ISINHAND;
        }
    }
}


