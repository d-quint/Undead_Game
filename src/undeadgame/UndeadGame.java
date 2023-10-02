package undeadgame;

import undeadgame.creatures.Undead;

public class UndeadGame {
  private boolean gameOver;
  private boolean running;

  private Undead player;
  private Undead enemy;

  // Constructor:
  public UndeadGame() {
    gameOver = false;
    running = false;
  }

  // Getters:
  public boolean isGameOver() {
    return gameOver;
  }

  public boolean isRunning() {
    return running;
  }

  public Undead getPlayer() {
    if (player == null) {
      return new Undead("Player", 100);
    }

    return player;
  }

  public Undead getEnemy() {
    if (enemy == null) {
      return new Undead("Enemy", 100);
    }

    return enemy;
  }

  // Setters:
  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }

  public void setPlayer(Undead player) {
    this.player = player;
  }

  public void setIsRunning(boolean running) {
    this.running = running;
  }

  public void setEnemy(Undead enemy) {
    this.enemy = enemy;
  }

  public int checkWhoDied() {
    if (player.isDead() || enemy.isDead()) {
      this.setGameOver(true);
      this.setIsRunning(false);
    }
    
    if (player.isDead()) {
      return 1;
    } else if (enemy.isDead()) {
      return 2;
    }

    return 0;
  }
}
