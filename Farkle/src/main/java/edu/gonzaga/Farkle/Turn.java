/**
 * Class to keep track of current player index, list of players, and index to mod in order to loop through the players to simulate their turns
 * 
 * 
 * CPSC 224
 * Homework #3
 * @author Lindsey Bodenbender
 * @version 1.1 3/3/2024   
 */

package edu.gonzaga.Farkle;

import java.util.ArrayList;
import java.util.List;

public class Turn 
{
    public
    Integer currentPlayerIndex = 0;
    Integer indexToMod = 0;
    Integer numPlayers = 0;
    List<Player> players = new ArrayList<Player>();

    /**
     * Creates a player object
     * 
     * @return player
     */
    public Player createPlayer()
    {
        Player player = new Player();
        return player;
    }

    /**
     * Adds a player to the list
     * 
     * @param addThisPlayer
     */
    public void addPlayerToList(Player addThisPlayer)
    {
        players.add(addThisPlayer);
    }

    /**
     * Creates and adds desired number of players to list
     */
    public void createPlayersAddToList()
    {
        for(int i = 0; i < numPlayers - 1; i++)
        {
            addPlayerToList(createPlayer());
        }
    }

    /**
     * Returns the current player
     * 
     * @return Player
     */
    // returns the current player
    public Player currentPlayer()
    {
        return players.get(currentPlayerIndex);
    }
}


