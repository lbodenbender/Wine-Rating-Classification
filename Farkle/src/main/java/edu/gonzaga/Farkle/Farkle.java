
package edu.gonzaga.Farkle;

import java.util.Scanner;

/**
*  Program to simulate the dice based game Farkle, allowing players
*  to choose dice to be included in the "meld" and then scores 
*  them based on different combination
*  
*  CPSC 224
*  Homework #2
*  @author Lindsey Bodenbender
*  @version v1.0 2/8/2024 
*/
/** Main program class for launching Farkle program. */
public class Farkle 
{
    public static void main(String[] args) 
    {
        // create game object
        Game game = new Game();
        game.gameBanner();
        Scanner scanner = game.createScanner();
        // create a player, get game inputs for points and number of players, name player(s)
        game.setUp(scanner);
        // grab dice for all players and create melds
        game.setUpForAllPlayers();
        // print out current state of game
        game.game(game.turn.currentPlayer().dieHand.dice);
        
        game.gameLoop(scanner);
        game.printWinner(game.determineWinner());
        
        game.closeScanner(scanner);
    }
}


