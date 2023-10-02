package undeadgame;

import undeadgame.creatures.*;
import undeadgame.util.MsgType;

public class UndeadGame {
  public enum GameMode {
    PVP,
    SANDBOX
  }

  private boolean gameOver;
  private boolean running;

  private Undead player;
  private Undead enemy;

  private GameMode mode;

  // Constructor:
  public UndeadGame() {
    gameOver = false;
    running = false;

    this.mode = GameMode.PVP;
  }

  public UndeadGame(GameMode mode) {
    this();
    this.mode = mode;
  }

  // Getters:
  public boolean isGameOver() {
    return gameOver;
  }

  public GameMode getGameMode() {
    return mode;
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

  public void setGameMode(GameMode mode) {
    this.mode = mode;
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
  
  /**
   * Executes a command in the game. This must be overridden by the gamemode subclasses.
   * 
   * @param args The arguments to the command.
   */
  public boolean executeGameCommand(String[] args) { return false; }

  /**
   * Starts the game. This must be overridden by the gamemode subclasses.
   */
  public void start() {}

  public static void displayAttack(String skill, Undead source, Undead target) {
    int deltaHP = 0;

    switch (skill) {
      case "1":
        if (!source.canAttack()) {
          UndeadGameUI.printMessage(new String[] {
            source.getName() + " tried to attack, but it's incapacitated!",
            source.getName() + "'s current HP: " + source.getHPstring(),
          }, MsgType.GAMEMASTER);
          return;
        }

        deltaHP = source.attack(target);

        UndeadGameUI.printMessage(new String[] {
          source.getName() + " attacks " + target.getName() + "!",
          "Damage dealt: " + deltaHP + " HP",
          target.getName() + "'s HP is now " + target.getHPstring() + "!"
        }, MsgType.GAMEMASTER);

        break;
      case "2":
        if (source.getType() == Undead.Type.VAMPIRE) {
          deltaHP = ((Vampire) source).bite(target);

          UndeadGameUI.printMessage(new String[] {
            source.getName() + " bites " + target.getName() + " for 80% of its HP! Sluuurp!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else if (source.getType() == Undead.Type.ZOMBIE && ((Zombie) source).getZombieType() == Zombie.ZombieType.MUMMY) {
          deltaHP = ((Mummy) source).eat(target);
          
          if (deltaHP == -444) {
            UndeadGameUI.printMessage(new String[] {
              source.getName() + " tried to eat " + target.getName() + ", but it's a zombie!",
              source.getName() + "'s current HP: " + source.getHPstring(),
            }, MsgType.GAMEMASTER);
            return;
          }

          UndeadGameUI.printMessage(new String[] {
            source.getName() + " eats " + target.getName() + " whole for half its HP! Yummy!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else if (source.getType() == Undead.Type.ZOMBIE) {
          deltaHP = ((Zombie) source).eat(target);
          
          UndeadGameUI.printMessage(new String[] {
            source.getName() + " eats " + target.getName() + " whole for half its HP! Yummy!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else if (source.getType() == Undead.Type.GHOST) {
          deltaHP = ((Ghost) source).haunt(target);
          
          UndeadGameUI.printMessage(new String[] {
            source.getName() + " haunts " + target.getName() + " for 10% of its HP! Spooooky!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else if (source.getType() == Undead.Type.SKELETON && ((Skeleton) source).getSkeletonType() == Skeleton.SkeletonType.LICH) {
          deltaHP = ((Lich) source).castSpell(target);

          UndeadGameUI.printMessage(new String[] {
            source.getName() + " casts a malevolent spell on " + target.getName() + "! It takes 10% of its HP!",
            "HP taken from enemy: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!",
            target.getName() + "'s HP is now " + target.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else {
          UndeadGameUI.printMessage("You can't use this skill!", MsgType.ERROR);
          return;
        }
        break;
      case "3":
        if (((Zombie) source).getZombieType() == Zombie.ZombieType.MUMMY) {
          deltaHP = ((Mummy) source).revive();

          if (deltaHP == -444) {
            UndeadGameUI.printMessage(new String[] {
              source.getName() + " tried to revive itself, but it's already alive!",
              source.getName() + "'s current HP: " + source.getHPstring(),
            }, MsgType.GAMEMASTER);
            return;
          }

          UndeadGameUI.printMessage(new String[] {
            source.getName() + " revives itself with 100% HP!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else {
          UndeadGameUI.printMessage("You can't use this skill!", MsgType.ERROR);
          return;
        }
        break;
      default:
        UndeadGameUI.printMessage("You picked an invalid skill. Perhaps you failed to input a number?", MsgType.ERROR);
        return;
    }
  }
}
