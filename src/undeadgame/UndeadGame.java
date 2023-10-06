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
   * An arraylist of all the creatures in the game.
   * Each entry is an array 
   */
  private ArrayList<UndeadWithType> creatures;

  // Constructor:
  public UndeadGame() {
    gameOver = false;
    running = false;

    creatures = new ArrayList<UndeadWithType>();
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

  /**
   * Starts the game.
   * This method should be called after the game is initialized.
   */
	public void start() {
    running = true;

    
	}
}