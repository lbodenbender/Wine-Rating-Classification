
package edu.gonzaga.Farkle;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
// import java.util.Collections;

/**
 * Class to handle different game functions like 
 * printing out the state of the game
 * 
 * CPSC 224
 * Homework #2
 * @author Lindsey Bodenbender
 * @version v1.0 2/8/2024
 */
public class Game
{
    Turn turn = new Turn();

    /**
     * Calls functions to display the current state of the dice and meld
     * 
     * @param dice, list of die objects which this function prints out
     * and uses to add to the meld values array
     * @return void
     */
    public void game(List<Die> dice)
    {
        turn.currentPlayer().dieHand.showDice(turn.currentPlayer().dieHand.sortDice());
        turn.currentPlayer().dieHand.countDice();

        // print out the current state of the game
        gameLayout(dice);
        System.out.println("\n");
        System.out.println(" (K) BanK Meld & End Round");
        System.out.println(" (R) Reroll");
        System.out.println(" (Q) Quit game" + "\n");
        System.out.println("Enter letters for your choice(s): ");
    }

    /**
     * 
     * Creates a player, get inputs for points and number of players and name player(s)
     * 
     * @param scanner
     * @return void
     */
    public void setUp(Scanner scanner)
    {
        // game must have at least one player
        turn.addPlayerToList(turn.createPlayer());
        // get points to play and number of players
        getSetUpGameInputs(scanner);
        turn.createPlayersAddToList();
        // player names
        setPlayerNames(scanner);
    }

    /**
     * Adds to the current meld values
     * 
     * @param index
     * @return void
     */
    public void addToCurrentMeldValues(Integer index)
    {
        if(index >= 0 && index < turn.currentPlayer().melds.size())
        {
            // reset meld values to 0 and count up all the die values in the meld
            turn.currentPlayer().melds.get(index).addToMeldValues(turn.currentPlayer().dieHand.dice);
        }
    }

    /**
     * Prints the current meld score
     * 
     * @param melds
     * @return void
     */
    public void printMeldScore(List<Meld> melds)
    {
        System.out.println(turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).getMeldScore());
    }

    /**
     * Returns true or false and calls functions depending on 
     * choice made by the user
     * 
     * @param choice, user input which this function uses to set play value
     * to true/false and call functions depending on input
     * @return bool play
     */
    public Boolean play(char choice, Scanner scanner)
    {
        Boolean play = false;

        if(choice == 'A' || choice == 'B' || choice == 'C' || choice == 'D' || choice == 'E' || choice == 'F')
        {
            play = true;
        }
        if(choice == 'K')
        {
            play = handleBank();
            handleHotHand(isHotHand(), scanner);
        }

        if(choice == 'Q')
        {
            quit();
            play = false;
        }
        if(choice == 'R')
        {
            play = true;
        }
        return play;
    }   

    /**
     * Continues to get user input and print out the state of the game
     * 
     * @param none
     * @return void
     */
    public void playGame(Scanner myScanner)
    {
        // if farkle print out message and end round
        if(handleFarkleHand())
        {
            return;
        }
        else
        {
            char choice = getChoiceInput(myScanner);
            // while the input is a die option, banking, or rerolling, play is true and continue getting input
            while(play(choice, myScanner))
            {   
                if(choice == 'R')
                {
                    // roll dice if the meld score > 0, count how many of each die value is not in the meld
                    if(handleRoll(turn.currentPlayer().dieHand.dice, turn.currentPlayer().melds))
                    {
                        addToCurrentMeldValues(turn.currentPlayer().currentMeldIndex);
                        // add current meld score to pointsForRound here
                        addToPointsForRound(turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).getMeldScore());
                        // score the current meld and change meld and dice
                        scoreMeldSetStatus();
                        // // print out current state of game
                        // game(turn.currentPlayer().dieHand.dice);
                        // detect farkle in non melded dice
                        if(handleFarkleMeld())
                        {
                            break;
                        }
                        updateMeldsAfterRoll();
                    }
                }
                // if choosing a die
                if(choice != 'R' && choice != 'K')
                {
                    handleDieChoice(choice);
                }
                // TODo: need to not print out game on illegal reroll
                if(choice != 'K')
                {
                    game(turn.currentPlayer().dieHand.dice);
                }
                choice = getChoiceInput(myScanner);
            }
        }
    }

    /**
     * Checks to see if any player has met or exceeded the points to play
     * 
     * @return pointsMet
     */
    public Boolean pointsMet()
    {
        Boolean pointsMet = false;
        for(int i = 0; i < turn.players.size(); i++)
        {
            if(turn.players.get(i).scoreCard.totalScore >= turn.players.get(i).scoreCard.pointsToPlay)
            {
                pointsMet = true;
            }
        }
        return pointsMet;
    }

    /**
     * Increments currentPlayerIndex
     * 
     * @return void
     * @param none
     */
    // increments currentPlayerIndex
    public void switchPlayer()
    {
        turn.indexToMod++;
        turn.currentPlayerIndex = turn.indexToMod % turn.players.size();
    }

    /**
     * Prints the current player's name
     * 
     * @return void
     * @param none
     */
    public void printCurrentPlayerName()
    {
        System.out.println(turn.currentPlayer().name + ", it's your turn!");
    }

    /**
     * Creates a new meld, sets the current dice to scored, scores the current meld, and rerolls all dice
     * 
     * @return void
     * @param none
     */
    public void setUpMeldAfterTurn()
    {
        Meld meld = turn.currentPlayer().createMeld(true);
        turn.currentPlayer().addMeldToList(turn.currentPlayer().melds, meld);

        // set current dice to scored
        addToCurrentMeldValues(turn.currentPlayer().currentMeldIndex);
        // score the current meld
        turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).scoreMeld(turn.currentPlayer(), turn.currentPlayer().currentMeldIndex);
        // set current dice to scored
        setDiceToScored(turn.currentPlayer().dieHand.dice);

        rerollAllDice();
        // player needs to have a meld to score before this
        addMeldValuesAndScore();
    }

    /**
     * Sets all dice to in hand and rolls them
     * 
     * @param none
     * @return void
     */
    public void rerollAllDice()
    {
        setAllDiceToInHand();
        turn.currentPlayer().roll();
    }

    /**
     * Count up the die values in the current meld and score it
     * 
     * @param none
     * @return void
     */
    public void addMeldValuesAndScore()
    {
        // add to current meld values
        addToCurrentMeldValues(turn.currentPlayer().currentMeldIndex);

        // score current player's meld
        turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).scoreMeld(turn.currentPlayer(), turn.currentPlayer().currentMeldIndex);
    }

    /**
     * Create 6 die objects and a meld, add it to melds list
     * 
     * @return void
     * @param none
     */
    public void setUpGame()
    {
        // create 6 die objects
        turn.currentPlayer().grabDice(6, 6);
        turn.currentPlayer().roll();

        // start with one meld
        Meld meld = turn.currentPlayer().createMeld(true);
        turn.currentPlayer().addMeldToList(turn.currentPlayer().melds, meld);
        addToCurrentMeldValues(turn.currentPlayer().currentMeldIndex);
    }

    /**
     * Create all the players' die hands and melds
     * 
     * @param none
     * @return void
     */
    public void setUpForAllPlayers()
    {
        for(int i = 0; i < turn.players.size(); i++)
        {
            setUpGame();
            switchPlayer();
        }
    }

    /**
     * Sort a copy of the players list on total score and print them
     * 
     * @param none
     * @return void
     */
    public void printScoresInOrder()
    {
        List<Player> playersCopy = new ArrayList<>(turn.players);
        playersCopy.sort(Comparator.comparingInt(player->-player.scoreCard.totalScore));

        for(int i = 0; i < playersCopy.size(); i++)
        {
            System.out.println(playersCopy.get(i).name + "'s total score: " + playersCopy.get(i).scoreCard.totalScore);
        }
    }

    /**
     * Continues to get input and switch players until points are met
     * 
     * @return void
     * @param scanner
     */
    public void gameLoop(Scanner scanner)
    {
        while(!pointsMet())
        {   
            printCurrentPlayerName();
            playGame(scanner);
            // sort and print out all total scores
            printScoresInOrder();
            if(pointsMet())
            {
                break;
            }
            if(turn.players.size() > 1)
            {
                switchPlayer();
            }
            else
            {
                printCurrentPlayerName();
            }
            // set up new meld after switch
            setUpMeldAfterTurn();
            game(turn.currentPlayer().dieHand.dice);
            
        }
        if(turn.players.size() > 1)
        {
            endgameTurn(scanner);
        }
    }

    /**
     * Allows each player to have one more turn
     * 
     * @return void
     * @param scanner
     */
    public void endgameTurn(Scanner scanner)
    {
        System.out.println(turn.currentPlayer().name + " has met the point total! All other players get one more turn");
        // need to increment current player, then iterate through players until reaching that player
        switchPlayer();
        printCurrentPlayerName();
        for(int i = 0; i < turn.players.size() - 1; i++)
        {
            setUpMeldAfterTurn();
            // print out current state of game
            game(turn.currentPlayer().dieHand.dice);
            playGame(scanner);
            switchPlayer();
            printScoresInOrder();
        }
    }

    /**
     * Determines who has the largest score and returns that player's index
     * 
     * @param none
     * @return winnerIndex
     */
    public Integer determineWinner()
    {
        Integer largestScore = 0;
        Integer winnerIndex = 0;
        for(int i = 0; i < turn.players.size(); i++)
        {
            if(turn.players.get(i).scoreCard.totalScore >= largestScore)
            {
                largestScore = turn.players.get(i).scoreCard.totalScore;
                winnerIndex = i;
            }
        }
        for(int i = 0; i < turn.players.size(); i++)
        {
            // if there is another player with the same score
            if(turn.players.get(i).scoreCard.totalScore == largestScore && i != winnerIndex)
            {
                System.out.println(turn.players.get(i).name + " is also a winner! Total score: " + turn.players.get(i).scoreCard.totalScore);
            }
        }
        return winnerIndex;
    }

    /**
     * Prints out the winner's name and total score
     * 
     * @return void
     * @param winnerIndex
     */
    public void printWinner(Integer winnerIndex)
    {
        System.out.println(turn.players.get(winnerIndex).name + " is the winner! Total score: " + turn.players.get(winnerIndex).scoreCard.totalScore);
    }

    /**
     * Sets points for round
     * 
     * @param points
     */
    public void setPointsForRound(Integer points)
    {
        turn.currentPlayer().scoreCard.pointsForRound = points;
    }

    /**
     * Adds/removes player's choice from meld and scores meld
     * 
     * @param choice
     * @return void
     */
    public void handleDieChoice(char choice)
    {
        // add/remove from meld (current)
        addRemoveFromCurrentMeld(choice);
        addToCurrentMeldValues(turn.currentPlayer().currentMeldIndex);
        // score the current meld
        turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).scoreMeld(turn.currentPlayer(), turn.currentPlayer().currentMeldIndex);
    }

    /**
     * Create a new meld and add it to list, increment current meld index, count up the die values in the meld, reset meld score to 0
     * 
     * @param none
     * @return void
     */
    public void updateMeldsAfterRoll()
    {
        // create new meld, set its status to true
        Meld meld = turn.currentPlayer().createMeld(true);
        turn.currentPlayer().addMeldToList(turn.currentPlayer().melds, meld);
        turn.currentPlayer().currentMeldIndex++;
        addToCurrentMeldValues(turn.currentPlayer().currentMeldIndex);
        turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).meldScore = 0;
    }

    /**
     * Prints the layout of the game with current dice and meld values
     * 
     * @param dice, list of die objects which this function displays
     * @return void
     */
    public void gameLayout(List<Die> dice)
    {
        System.out.println("\n");
        System.out.println("*************************** Current hand and meld *******************");
        System.out.println(" Die   Hand |   Meld");
        System.out.println("------------+---------------");

        for(int i = 0; i < dice.size(); i++ ) 
        {
            if(dice.get(i).dieStatus != DieStatusEnum.ISINHAND)
            {
                char option = (char)('A' + i);
                System.out.print("(" + option + ")" + "         |   ");
                if(dice.get(i).dieStatus == DieStatusEnum.ISINCURRENTMELD)
                {
                    System.out.println(dice.get(i).getSideUp());
                }
                else
                {
                    System.out.println("");
                }
            }
            else
            {
                // print dice in hand
                char option = (char)('A' + i);
                System.out.println("(" + option + ")" + "     " + dice.get(i).getSideUp() + "   |");
            }
        }
        System.out.println("------------+---------------");
        System.out.print("                Meld Score: ");
        printMeldScore(turn.currentPlayer().melds);
        System.out.print("                Total Score: ");
        System.out.println(turn.currentPlayer().scoreCard.totalScore + turn.currentPlayer().scoreCard.pointsForRound);
    }

    /**
     * Calls functions to score the current meld and set the current status to false and scored status to true
     * 
     * @param none
     * @return void
     */
    public void scoreMeldSetStatus()
    {
        // set current dice to scored
        setDiceToScored(turn.currentPlayer().dieHand.dice);
        turn.currentPlayer().setCurrentMeldStatus(false, turn.currentPlayer().currentMeldIndex);
        turn.currentPlayer().setScoredStatus(true, turn.currentPlayer().currentMeldIndex);
    }

    /**
     * Adds to points for round
     * 
     * @return void
     * @param points
     */
    public void addToPointsForRound(Integer points)
    {
        turn.currentPlayer().scoreCard.pointsForRound += points;
    }

    /**
     * Adds die to the current meld if its in the meld and not the current meld
     * 
     * @param dice
     * @return void
     */
    public void addToCurrentMeld(List<Die> dice)
    {
        for(int i = 0; i < dice.size(); i++)
        {
            // if the die is in the meld add it to the current meld
            // if(!dice.get(i).isInHand)
            if(dice.get(i).dieStatus != DieStatusEnum.ISINHAND)
            {
                dice.get(i).dieStatus = DieStatusEnum.ISINCURRENTMELD;
                // dice.get(i).isInCurrentMeld = true;
            }
        }
    }

    /**
     * Adds and removes from current meld as applicable
     * 
     * @param choice
     * @return void
     */
    public void addRemoveFromCurrentMeld(char choice)
    {
        // change choice to int value
        int index = turn.currentPlayer().dieHand.choiceToIndex(choice);

        // if chosen die is already in meld, remove it
        if(turn.currentPlayer().dieHand.dice.get(index).dieStatus != DieStatusEnum.ISINHAND)
        {
            turn.currentPlayer().dieHand.removeFromCurrentMeld(turn.currentPlayer().dieHand.dice, index);
        }
        // otherwise add chosen die to meld
        else
        {
            turn.currentPlayer().dieHand.addDieToCurrentMeld(turn.currentPlayer().dieHand.dice, index);
        }
    }

    /**
     * Sets dice in the current meld to scored meld
     * 
     * @param dice
     * @return void
     */
    public void setDiceToScored(List<Die> dice)
    {
        for(int i = 0; i < dice.size(); i++)
        {
            if(dice.get(i).dieStatus == DieStatusEnum.ISINCURRENTMELD)
            {
                dice.get(i).dieStatus = DieStatusEnum.ISINSCOREDMELD;
            }
        }
    }

    /**
     * Returns the current meld's score
     * 
     * @param meld
     * @param currMeldIndex
     * @return Current meld's score
     */
    public Integer getMeldScore(Meld meld, Integer currMeldIndex)
    {
        return turn.currentPlayer().melds.get(currMeldIndex).getMeldScore();
    }

    /**
     * Sets the meld score
     * 
     * @param score
     * @param meld
     */
    public void setMeldScore(Integer score, Meld meld)
    {
        
        meld.meldScore = score;
    }

    /**
     * Returns the total score
     * 
     * @param none
     * @return Total score
     */
    public Integer getTotalScore()
    {
        return turn.currentPlayer().scoreCard.totalScore;
    }

    /**
     * Sets the totals core
     * 
     * @param score
     * @return void
     */
    public void setTotalScore(Integer score)
    {
        turn.currentPlayer().scoreCard.totalScore += score;
    }

    /**
     * Gets user input for choice of die/quit/bank/roll
     * 
     * @param scanner this object uses to store input
     * @return input
     */
    public char getChoiceInput(Scanner scanner)
    {
        // get input of a char
        char input = scanner.next().charAt(0);
        return Character.toUpperCase(input);
    }

    /**
     * Gets user input for name
     * 
     * @param scanner
     * @return name chosen
     */
    public String getNameInput(Scanner scanner)
    {   
        String name = scanner.nextLine();
        return name;
    }

    /**
     * Sets player names
     * 
     * @param name
     * @return void
     */
    public void setPlayerNames(Scanner scanner)
    {
        Integer numUnknownPlayers = 0;
        for(int i = 0; i < turn.players.size(); i++)
        {
            System.out.println("What is player # " + (i + 1) + "'s name");
            String name = getNameInput(scanner);
            if(name != "")
            {
                turn.players.get(i).name = name;
            }
            else
            {
                numUnknownPlayers ++;
                turn.players.get(i).name = "Unknown Player" + " #" + numUnknownPlayers;
            }
        }
    }

    /**
     * Gets input for number of players and sets number of players to that input
     * 
     * @return void
     * @param scanner
     */
    public void getAndSetNumPlayers(Scanner scanner)
    {
        setNumPlayers(getNumPlayersInput(scanner));
        
    }

    /**
     * Get input for number of players
     * 
     * @param scanner
     * @return numPlayers
     */
    public Integer getNumPlayersInput(Scanner scanner)
    {
        
        System.out.println("How many players are going to be playing?");
        
        Integer numPlayers = scanner.nextInt();
        // consume newline left over after nextInt
        scanner.nextLine();
        return numPlayers;
    }

    /**
     * Set number of players
     * 
     * @param numPlayers
     */
    public void setNumPlayers(Integer numPlayers)
    {
        turn.numPlayers = numPlayers;
    }

    /**
     * Gets input for points to play and sets points to play to that input
     * 
     * @return void
     * @param scanner
     */
    public void getAndSetPointsToPlay(Scanner scanner)
    {
        setPointsToPlay(getPointsInput(scanner));
    }

    /**
     * Gets input for how many points to play
     * 
     * @param scanner
     * @return points
     */
    public String getPointsInput(Scanner scanner)
    {
        System.out.println("How many points would you like to play this time?");
        String points = scanner.nextLine();
        return points;
    }

    /**
     * Set points to play
     * 
     * @return void
     * @param points
     */
    public void setPointsToPlay(String points)
    {
        if(points != "")
        {
            turn.currentPlayer().scoreCard.pointsToPlay = Integer.valueOf(points);
        }
    }

    /**
     * Get and set points to play and num players
     * 
     * @param scanner
     */
    public void getSetUpGameInputs(Scanner scanner)
    {
        getAndSetPointsToPlay(scanner);
        getAndSetNumPlayers(scanner);
    }

    /**
     * Creates a scanner
     * 
     * @param none
     * @return scanner
     */
    public Scanner createScanner()
    {
        Scanner myScanner = new Scanner(System.in);
        return myScanner;
    }

    /**
     * Closes scanner
     * 
     * @param scanner
     * @return void
     */
    public void closeScanner(Scanner scanner)
    {
        scanner.close();
    }

    /**
     * Sets the total score and sets dice to scored if bank is legal
     * 
     * @param scanner
     * @return Play status
     */
    public Boolean handleBank()
    {
        Boolean play = true;
        if(canBank())
        {
            // add points per round to total score
            setTotalScore(turn.currentPlayer().scoreCard.pointsForRound + turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).meldScore);
            setDiceToScored(turn.currentPlayer().dieHand.dice);
            System.out.println("Round over. Total score is now: " + getTotalScore());
            play = false;
        }
        // if bank is not legal
        else
        {
            addToCurrentMeldValues(turn.currentPlayer().currentMeldIndex);
            // // print out current state of the game
            // game(turn.currentPlayer().dieHand.dice);
            System.out.println("Add valid combo to meld before banking");
            play = true;
        }
        return play;
    }

    /**
     * Returns true if all the dice in current meld have been used in combos and if the meld score is > 0
     * 
     * @return canBank
     */
    public Boolean canBank()
    {
        Boolean canBank = true;

        if(turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).getMeldScore() == 0)
        {
            return false;
        }
        for(int i = 0; i < turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).meldValues.length; i++)
        {
            if(turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).meldValues[i] != 0)
            {
                canBank = false;
            }
        }
        return canBank;
    }

    /**
     * If a meld has been created and the meld score is > 0, rolls the dice and counts the die values that are not in the meld
     * 
     * @param dice
     * @param melds
     * @return rollAllowed
     */
    public Boolean handleRoll(List<Die> dice, List <Meld> melds)
    {
        Boolean rollAllowed = true;
        if(melds.size() > 0 && melds.get(turn.currentPlayer().currentMeldIndex).getMeldScore() > 0)
        {
            if(melds.get(turn.currentPlayer().currentMeldIndex).isCurrentMeld)
            {
                turn.currentPlayer().roll();
                // count how many of each die value is not in the meld
                turn.currentPlayer().scoreCard.combo.addToRemainingDice(turn.currentPlayer().dieHand.dice);
                // // print out current state of game
                // game(turn.currentPlayer().dieHand.dice);
            }
        }
        else
        {
            rollAllowed = false;
            System.out.println("Add valid combo to meld before rerolling");
        }
        return rollAllowed;
    }

    /**
     * Returns true if it is a hot hand
     * 
     * @param none
     * @return isHotHand
     */
    public Boolean isHotHand()
    {
        Boolean isHotHand = false;
        Integer scoredDiceCount = 0;
        for(int i = 0; i < turn.currentPlayer().dieHand.dice.size(); i++)
        {
            if(turn.currentPlayer().dieHand.dice.get(i).dieStatus == DieStatusEnum.ISINSCOREDMELD)
            {
                scoredDiceCount++;
            }
        }
        if(scoredDiceCount == turn.currentPlayer().dieHand.dice.size())
        {
            isHotHand = true;
        }
        return isHotHand;
    }

    /**
     * If a hot hand, prints a message and lets player choose to contine playing or bank
     * 
     * @param hotHand
     * @param scanner
     */
    public void handleHotHand(Boolean hotHand, Scanner scanner)
    {
        char choice = 'Z';
        if(hotHand)
        {
            printHotHandMessage();
            choice = getChoiceInput(scanner);
        }
        if(choice == 'K')
        {
            return;
        }
        if(choice == 'R')
        {
            setAllDiceToInHand();
            handleRoll(turn.currentPlayer().dieHand.dice, turn.currentPlayer().melds);
            // continue to get user input until play is false
            playGame(scanner);
        }
    }

    /**
     * Prints message
     * 
     * @param none
     * @return void
     */
    public void printHotHandMessage()
    {
        System.out.println("***************");
        System.out.println("  HOT HAND!!!  ");
        System.out.println("***************");
        System.out.println("Would you like to roll six new dice, or bank and end your turn?");
    }

    /**
     * Sets the value of all current player's dice to in hand
     * 
     * @param non
     * @return void
     */
    public void setAllDiceToInHand()
    {
        for(int i = 0; i < turn.currentPlayer().dieHand.dice.size(); i++)
        {
            turn.currentPlayer().dieHand.dice.get(i).dieStatus = DieStatusEnum.ISINHAND;
        }
    }

    /**
     * Prints out a message
     * 
     * @param none
     * @return void
     */
    public void quit()
    {
        if(turn.currentPlayer().scoreCard.totalScore > 0)
        {
            // System.out.println("Round over. Total score is now: " + getTotalScore());
            printScoresInOrder();
        }
        else
        {
            System.out.println("Round over. Total score is now: 0");
        }
        // exit program
        System.exit(0);
    }

    

    /**
     * If the player rolled a farkle, print out a message and end the round
     * 
     * @param none
     * @return void
     */
    public Boolean handleFarkleHand()
    {
        Boolean farkle = false;
        if(turn.currentPlayer().scoreCard.combo.isFarkle(turn.currentPlayer().dieHand.values))
        {
            System.out.println("Farkle!!");
            // set the current meld score to 0
            setMeldScore(0, turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex));
            // set pointsForRound to 0
            setPointsForRound(0);
            // add meld score to total score
            setTotalScore(turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).getMeldScore());
            System.out.println("Round over. Total score is now: " + getTotalScore());
            farkle = true;
        }
        return farkle;
    }

    /**
     * Prints out a message and sets score to 0 if player rolled a farkle
     * 
     * @return farkle status
     * @param none
     */
    public Boolean handleFarkleMeld()
    {
        Boolean farkle = false;
        // only check if the remaining dice are a farkle if there are dice in the meld (if there are remaining dice)
        if(turn.currentPlayer().dieHand.dieInMeld())
        {
            if(turn.currentPlayer().scoreCard.combo.isFarkle(turn.currentPlayer().scoreCard.combo.remainingDice))
            {
                System.out.println("Farkle!!");
                // set the current meld score to 0
                setMeldScore(0, turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex));
                // set points for round to 0
                setPointsForRound(0);
                // add meld score to total score
                setTotalScore(turn.currentPlayer().melds.get(turn.currentPlayer().currentMeldIndex).getMeldScore());
                System.out.println("Round over. Total score is now: " + getTotalScore());
                farkle = true;
            }
        }
        return farkle;
    }

    /**
     * Print out a banner message
     * 
     * @param none
     * @return none
     */
    public void gameBanner()
    {
        System.out.println("*******************************");
        System.out.println("|  Zag Farkle by Lindsey!     |");
        System.out.println("|        Copyright: 2024      |");
        System.out.println("*******************************");
    }
}


