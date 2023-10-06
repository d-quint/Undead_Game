package undeadgame;

import java.util.ArrayList;

import undeadgame.creatures.*;
import undeadgame.util.MsgType;

/**
 * A class encapsulating an Undead with its type (in String)
 * This is used to easily determine the subtype of an Undead
 */
class UndeadWithType {
  private Undead undead;
  private String type;

  public UndeadWithType(Undead undead, String type) {
    this.undead = undead;
    this.type = type;
  }

  public Undead getUndead() {
    return undead;
  }

  public String getType() {
    return type;
  }
}

public class UndeadGame {
  private boolean gameOver;
  private boolean running;

  /**
   * An arraylist of all the game-specific commands in the game.
   */
  private ArrayList<Command> gameCommands;

  /**
   * An arraylist of all the creatures in the game.
   * Each entry is an array 
   */
  private ArrayList<UndeadWithType> creatures;

  // Constructor:
  public UndeadGame() {
    gameOver = false;
    running = false;

    creatures = new ArrayList<UndeadWithType>();
    gameCommands = new ArrayList<Command>();
  }

  public UndeadGame(boolean isSandboxMode) {
    this();
  }

  // Getters:
  public boolean isGameOver() {
    return gameOver;
  }

  public boolean isRunning() {
    return running;
  }

  // Setters:
  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }

  public void setIsRunning(boolean running) {
    this.running = running;
  }

  private void initializeCommands() {
    gameCommands.add(
      new Command("POPULATE",  

        "Raise an undead from the depths of hell", 

        args -> {
          UndeadGameUI.printMessage("You have raised an undead!", MsgType.GAMEMASTER);
          return true;
        }
    ));
    
    gameCommands.add(
      new Command("ATTACK",  

        new String[]{"ATTACKER_NAME", "TARGET_NAME"}, 

        "Pick an undead to command and attack another undead", 

        args -> {
          UndeadGameUI.printMessage("You have attacked an undead!", MsgType.GAMEMASTER);
          return true;
        }
    ));
  }

  /**
   * Starts the game.
   * This method should be called after the game is initialized.
   */
	public void start() {
    running = true;

    initializeCommands();

    // Append the game-specific commands to the list of commands in UndeadGameUI
    for (Command command : gameCommands) {
      UndeadGameUI.commands.add(command);
    }
	}
}