package undeadgame;

import java.lang.Thread;
import java.lang.Math;
import java.util.Scanner;

import undeadgame.creatures.*;
import undeadgame.util.*;

/**
 * A class encapsulating all the UI-related methods of the game
 */
public class UndeadGameUI {
  private static final Scanner READ = new Scanner(System.in);
  private static final int DELAY = 300; // in ms

  private static char prefix = '!';
  private static String currentLine = "";

  private static String playerName = "";
  private static UndeadGame gameInstance;

  private static boolean shouldExit = false;

  public static boolean run() {
    gameInstance = new UndeadGame();
    
    if (playerName.isEmpty()) {
      introduction();
    } else {
      printMessage(new String[] {
        "Alright, " + playerName + "!",
        "Just a quick reminder of the commands:",
        "  Type \"" + prefix + "quit\" to exit.",
        "  Type \"" + prefix + "help\" for a quick rundown of general commands.",
        "  Type \"" + prefix + "start\" to start the game.",
      }, MsgType.GAMEMASTER);
    }

    while (!gameInstance.isGameOver()) {
      loop();

      if (shouldExit) {
        return false;
      }
    }

    printMessage("Thanks for playing!", MsgType.GAMEMASTER);

    printMessage(new String[] {
      "Do you want to play again?",
      "  [1] YES",
      "  [2] No",
      "  [3] Oh hell no",
    }, MsgType.GAMEMASTER);

    do {
      readLine();

      switch (currentLine) {
      case "1":
        return true;
      case "2":
        printMessage("Goodbye!", MsgType.GAMEMASTER);
        break;
      case "3":
        printMessage("Fine.", MsgType.GAMEMASTER);
        break;
      default:
        printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
        break;
      }
    } while (!currentLine.equals("1") && !currentLine.equals("2") && !currentLine.equals("3"));

    return false;
  }

  private static void introduction() {
    System.out.println();
    printMessage("Welcome to Undead Game! What's your name?", MsgType.GAMEMASTER);
    
    readLine();
    playerName = currentLine.toUpperCase();

    printMessage("Hello, " + playerName + "!", MsgType.GAMEMASTER);
    printMessage("Type \"" + prefix + "quit\" to exit.", MsgType.GAMEMASTER);
    printMessage("Type \"" + prefix + "help\" for a quick rundown of general commands.", MsgType.GAMEMASTER);  
    printMessage("Type \"" + prefix + "start\" to start the game.", MsgType.GAMEMASTER);
  }

  private static void loop() {
    printWarning();

    readLine();
    processLine();
  }

  private static void readLine() {
    System.out.println("    | ");
    printMessage("  " + (playerName.isEmpty() ? "STRANGER:" : playerName + ":"), MsgType.INPUT);

    currentLine = READ.nextLine();
    System.out.println("    | ");
  }

  private static void processLine() {
    if (currentLine.startsWith("" + prefix)) {
      String command = currentLine.substring(1);
      String[] args = command.split(" ");

      executeCommand(args);
    } else {
      printMessage("Unknown command \"" + currentLine + "\".", MsgType.ERROR);
      printMessage("Make sure to start your commands with \"" + prefix + "\".", MsgType.WARNING);
    }
  }

  private static void executeCommand(String[] args) {
    if (executeGameCommand(args)) return;    

    switch (args[0].toLowerCase()) {
      case "quit":
        printMessage("Quitting game...", MsgType.GAMEMASTER);
        shouldExit = true;
        break;
      case "start":
        executeStartCommand();
        break;
      case "help":
        printMessage(new String[] {
          "List of general-purpose commands:",
          prefix + "quit - Quits the game.",
          prefix + "help - Displays this list of general commands.",
          prefix + "gamehelp - Displays a list of game-related commands.",
          prefix + "setprefix <character> - Sets <character> as the new command prefix.",
          prefix + "setname <string> - Sets <string> as the new display name.",
          prefix + "start - Starts the game."
        }, MsgType.GAMEMASTER);
        break;
      case "gamehelp":  
        printMessage(new String[] {
          "List of game-related commands:",
          prefix + "listskills - Lists all the skills of your undead.",
          prefix + "useskill <skill_number> | useskill - Puts into action a chosen skill. You may include arguments or not.",
          prefix + "forfeit - Forfeits the game.",
          prefix + "stats - Displays the stats of your undead and the enemy's."
        }, MsgType.GAMEMASTER);
        break;
      case "setprefix":
        if (args.length > 1) {
          prefix = args[1].charAt(0);
          printMessage("Prefix successfully set to \"" + prefix + "\".", MsgType.GAMEMASTER);
        } else {
          printMessage("No prefix specified.", MsgType.ERROR);
        }
        break;
      case "setname":
        if (args.length > 1) {
          playerName = args[1].toUpperCase();
          printMessage("Name successfully set to \"" + playerName + "\".", MsgType.GAMEMASTER);
        } else {
          printMessage("No name specified.", MsgType.ERROR);
        }
        break;
      default:
        printMessage("Unknown command \"" + args[0] + "\".", MsgType.ERROR);
        break;
    }
  }

  public static boolean executeGameCommand(String[] args) {
    String command = args[0];
    String[] commandArgs = new String[args.length - 1];

    for (int i = 1; i < args.length; i++) {
      commandArgs[i - 1] = args[i];
    }

    switch (command.toLowerCase()) {
      case "listskills":
        executeListSkillsCommand();
        break;
      case "useskill":
        executeActionCommand(commandArgs);
        break;
      case "forfeit":
        executeForfeitCommand();
        break;
      case "stats":
        executeStatsCommand();
        break;
      default:
        return false;
    }

    return true;
  }

  private static void printMessage(String message, MsgType type) {
    System.out.print("    | ");
    
    switch (type) {
      case DEFAULT:
        System.out.println(message);
        break;
      case INPUT:
        System.out.print(message + " ");
        break;
      case GAMEMASTER:
        System.out.println("GAMEMASTER: " + message);
        break;
      case ERROR:
        System.out.println("ERROR: " + message);
        break;
      case WARNING:
        System.out.println("WARNING: " + message);
        break;
    }

    // Sleep for 1 second
    try {
      Thread.sleep(DELAY);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void printMessage(String[] messageLines, MsgType type) {
    printMessage("", MsgType.DEFAULT);
    
    printMessage(messageLines[0], type);

    for (int i = 1; i < messageLines.length; i++) {  
      printMessage((type.equals(MsgType.GAMEMASTER) ? "            " : "") + messageLines[i], MsgType.DEFAULT);
    }
  }

  // UNDEAD GAME UI METHODS: --------------------------------------------------------------------------------------------

  private static void executeStartCommand() {
    if (gameInstance.isRunning()) {
      printMessage("The game has already started!", MsgType.ERROR);
      printMessage(new String[] {
        "Or did you want to restart another game?",
        "  [1] YES",
        "  [2] Not really",
      }, MsgType.GAMEMASTER);

      readLine();

      switch (currentLine) {
        case "1":
          gameInstance = new UndeadGame();
          break;
        case "2":
          printMessage("Alright, then.", MsgType.GAMEMASTER);
          return;
        default:
          printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
          return;
      }
    }

    printMessage(new String[] {"Starting new game..."}, MsgType.GAMEMASTER);

    gameInstance.setIsRunning(true);
    
    printMessage(new String[] {
      "Choose your character (reply with a number):",
      "  [1] Vampire - Can bite enemies to heal itself. Cannot attack when HP is 0.",
      "  [2] Zombie - Can eat enemies to heal itself. Cannot attack when HP is lower than 50.",
      "  [3] Ghost - Can haunt to heal, and only receives 10% of the actual damage.",
      "  [4] Skeleton - Can only attack, and has 70% of its HP as attack damage.",
      "  [5] Lich - A type of immortal skeleton that can cast a spell which takes the 10% of their target's HP and adds it to its HP",
      "  [6] Mummy - A type of Zombie that can revive after dying once, but cannot eat its own kind",
    }, MsgType.GAMEMASTER);

    readLine();
    String choice = currentLine;

    printMessage("What is your character's name?", MsgType.GAMEMASTER);

    readLine();

    switch (choice) {
      case "1":
        gameInstance.setPlayer(new Vampire(playerName));
        break;
      case "2":
        gameInstance.setPlayer(new Zombie(playerName));
        break;
      case "3":
        gameInstance.setPlayer(new Ghost(playerName));
        break;
      case "4":
        gameInstance.setPlayer(new Skeleton(playerName));
        break;
      case "5":
        gameInstance.setPlayer(new Lich(playerName));
        break;
      case "6":
        gameInstance.setPlayer(new Mummy(playerName));
        break;
      default:
        printMessage("You picked an invalid undead character. Perhaps you failed to input a number?", MsgType.ERROR);
        gameInstance = new UndeadGame();
        return;
    }

    gameInstance.getPlayer().setName(currentLine);

    setRandomEnemy();

    printMessage("\"" + gameInstance.getPlayer().getName() + "\" has successfully risen from the dead!", MsgType.GAMEMASTER);
    
    printMessage(new String[] {
      "Your mortal enemy is \"" + gameInstance.getEnemy().getName() + "\"!",
      "  Use command \"" + prefix + "listskills\" to list your usable skills.",
      "  Use command \"" + prefix + "useskill\" to use one of those skills and/or attack your enemy.",
      "  Use command \"" + prefix + "forfeit\" to forfeit the game.",
      "  Use command \"" + prefix + "stats\" to display the current stats of your undead and the enemy's.",
      "  Use command \"" + prefix + "gamehelp\" to display this list of game-related commands again.",
      "",
      "Good luck, " + gameInstance.getPlayer().getName() + "!"
    }, MsgType.GAMEMASTER);
  }

  private static void setRandomEnemy() {
    do {
      switch ((int) (Math.random() * 6)) {
        case 0:
          gameInstance.setEnemy(new Vampire("MARCY"));
          break;
        case 1:
          gameInstance.setEnemy(new Zombie("ZOMATHY"));
          break;
        case 2:
          gameInstance.setEnemy(new Ghost("CASPER"));
          break;
        case 3:
          gameInstance.setEnemy(new Skeleton("SANS UNDERTALE"));
          break;
        case 4:
          gameInstance.setEnemy(new Lich("LICHELLE"));
          break;
        case 5:
          gameInstance.setEnemy(new Mummy("TUTANKHAMUN"));
          break;
      }
    } while (gameInstance.getEnemy().getType() == gameInstance.getPlayer().getType());
  }

  private static void executeListSkillsCommand() {
    if (!gameInstance.isRunning()) {
      printMessage("Start the game first before using game commands!", MsgType.ERROR);
      return;
    }

    printMessage("Your skills are:", MsgType.GAMEMASTER);

    String[] skills = new String[3];

    switch (gameInstance.getPlayer().getType()) {      
      case VAMPIRE:
        skills[0] = "Attack - Attack your enemy with your claws! (Damage: 100% of your HP)";
        skills[1] = "Bite - Heal yourself by 80% of your target's HP with a bite!";
        break;
      case ZOMBIE:
        if (((Zombie) gameInstance.getPlayer()).getZombieType() == Zombie.ZombieType.NORMAL) {
          skills[0] = "Attack - Attack your enemy with your deadly virus! (Damage: 50% of your HP)";
          skills[1] = "Eat - Heal yourself by 50% of your target's HP from eating their brains!";
        } else {
          skills[0] = "Attack - Attack your enemy with your deadly virus! (Damage: 50% of your HP + 10% of target's HP)";
          skills[1] = "Eat - Heal yourself by 50% of your target's HP from eating their brains! (Only works on non-zombies!)";
          skills[2] = "Revive - Revive yourself with 100% HP! (You can only revive once!)";
        }
        break;
      case GHOST:
        skills[0] = "Attack - Attack your enemy with your ghostly powers! (Damage: 20% of your HP)";
        skills[1] = "Haunt - Heal yourself by 10% of your target's HP by haunting them!";
        break;
      case SKELETON:
        if (((Skeleton) gameInstance.getPlayer()).getSkeletonType() == Skeleton.SkeletonType.LICH) {
          skills[0] = "Attack - Attack your enemy with your skeletal bow! (Damage: 70% of your HP)";
          skills[1] = "Cast Spell - Cast a spell on your enemy to heal yourself by taking 10% of their HP!";
        } else {
          skills[0] = "Attack - Attack your enemy and rattle their bones! (Damage: 70% of your HP)";
        }
        break;
      default:
        break;
    }

    for (int i = 0; i < skills.length; i++) {
      if (skills[i] != null) {
        printMessage("              [" + (i + 1) + "] " + skills[i], MsgType.DEFAULT);
      }
    }
  }

  private static void executeActionCommand(String[] args) {
    if (!gameInstance.isRunning()) {
      printMessage("Start the game first before using game commands!", MsgType.ERROR);
      return;
    }
    
    String skill = "";

    if (args.length == 0) {
      printMessage("Choose an available skill from your list of skills!", MsgType.GAMEMASTER);
      executeListSkillsCommand();
      readLine();
      skill = currentLine;
    } else {
      skill = args[0];
    }

    displayAttack(skill, gameInstance.getPlayer(), gameInstance.getEnemy());

    enemyAttack();

    if (checkWinCondition()) return;
  }

  private static void enemyAttack() {
    printMessage(new String[] {"..."}, MsgType.GAMEMASTER);

    String skill = "";

    switch (gameInstance.getEnemy().getType()) {
      case ZOMBIE:
        if (((Zombie) gameInstance.getEnemy()).getZombieType() == Zombie.ZombieType.NORMAL) {
          skill = "1";
          break;
        }
      
        switch ((int) (Math.random() * 3)) {
          case 0:
            skill = "1";
            break;
          case 1:
            skill = "2";
            break;
          case 2:
            skill = "3";
            break;
        }

        break;
      case SKELETON:
        if (((Skeleton) gameInstance.getEnemy()).getSkeletonType() == Skeleton.SkeletonType.NORMAL) {
          skill = "1";
          break;
        }

        break;
      default:
        switch ((int) (Math.random() * 2)) {
          case 0:
            skill = "1";
            break;
          case 1:
            skill = "2";
            break;
        }
        
        break;
    }

    displayAttack(skill, gameInstance.getEnemy(), gameInstance.getPlayer());
  }

  private static void executeForfeitCommand() {
    if (!gameInstance.isRunning()) {
      printMessage("Start the game first before using game commands!", MsgType.ERROR);
      return;
    }

    printMessage("You forfeited! " + gameInstance.getEnemy().getName() + " wins!", MsgType.GAMEMASTER);
    gameInstance.setGameOver(true);
  }

  private static void executeStatsCommand() {
    if (!gameInstance.isRunning()) {
      printMessage("Start the game first before using game commands!", MsgType.ERROR);
      return;
    }
    
    printMessage(new String[] {
      "Your stats:",
      "  Name: " + gameInstance.getPlayer().getName(),
      "  HP: " + gameInstance.getPlayer().getHPstring(),
      "  Type: " + gameInstance.getPlayer().getType(),
      "  Can it attack?: " + (gameInstance.getPlayer().canAttack() ? "Yes" : "No"),
      "  Is it dead?: " + (gameInstance.getPlayer().isDead() ? "Yes" : "No")
    }, MsgType.GAMEMASTER);

    printMessage(new String[] {
      "Enemy stats:",
      "  Name: " + gameInstance.getEnemy().getName(),
      "  HP: " + gameInstance.getEnemy().getHPstring(),
      "  Type: " + gameInstance.getEnemy().getType(),
      "  Can it attack?: " + (gameInstance.getEnemy().canAttack() ? "Yes" : "No"),
      "  Is it dead?: " + (gameInstance.getEnemy().isDead() ? "Yes" : "No")
    }, MsgType.GAMEMASTER);
  }

  // HELPER METHODS: ----------------------------------------------------------------------------------------------------

  private static boolean checkWinCondition() {
    int deadPlayer = gameInstance.checkWhoDied();

    if (deadPlayer == 2) {
      printMessage(new String[] {"Congratulations, " + playerName + "! Your creature triumphed!"}, MsgType.GAMEMASTER);
      return true;
    } else if (deadPlayer == 1) {
      printMessage(new String[] {"You lose! I guess your creature will be going back six feet under."}, MsgType.GAMEMASTER);
      return true;
    }

    return false;
  }

  private static void displayAttack(String skill, Undead source, Undead target) {
    int deltaHP = 0;

    switch (skill) {
      case "1":
        if (!source.canAttack()) {
          printMessage(new String[] {
            source.getName() + " tried to attack, but it's incapacitated!",
            source.getName() + "'s current HP: " + source.getHPstring(),
          }, MsgType.GAMEMASTER);
          return;
        }

        deltaHP = source.attack(target);

        printMessage(new String[] {
          source.getName() + " attacks " + target.getName() + "!",
          "Damage dealt: " + deltaHP + " HP",
          target.getName() + "'s HP is now " + target.getHPstring() + "!"
        }, MsgType.GAMEMASTER);

        break;
      case "2":
        if (source.getType() == Undead.Type.VAMPIRE) {
          deltaHP = ((Vampire) source).bite(target);

          printMessage(new String[] {
            source.getName() + " bites " + target.getName() + " for 80% of its HP! Sluuurp!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else if (source.getType() == Undead.Type.ZOMBIE && ((Zombie) source).getZombieType() == Zombie.ZombieType.MUMMY) {
          deltaHP = ((Mummy) source).eat(target);
          
          if (deltaHP == -444) {
            printMessage(new String[] {
              source.getName() + " tried to eat " + target.getName() + ", but it's a zombie!",
              source.getName() + "'s current HP: " + source.getHPstring(),
            }, MsgType.GAMEMASTER);
            return;
          }

          printMessage(new String[] {
            source.getName() + " eats " + target.getName() + " whole for half its HP! Yummy!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else if (source.getType() == Undead.Type.ZOMBIE) {
          deltaHP = ((Zombie) source).eat(target);
          
          printMessage(new String[] {
            source.getName() + " eats " + target.getName() + " whole for half its HP! Yummy!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else if (source.getType() == Undead.Type.GHOST) {
          deltaHP = ((Ghost) source).haunt(target);
          
          printMessage(new String[] {
            source.getName() + " haunts " + target.getName() + " for 10% of its HP! Spooooky!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else if (source.getType() == Undead.Type.SKELETON && ((Skeleton) source).getSkeletonType() == Skeleton.SkeletonType.LICH) {
          deltaHP = ((Lich) source).castSpell(target);

          printMessage(new String[] {
            source.getName() + " casts a malevolent spell on " + target.getName() + "! It takes 10% of its HP!",
            "HP taken from enemy: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!",
            target.getName() + "'s HP is now " + target.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else {
          printMessage("You can't use this skill!", MsgType.ERROR);
          return;
        }
        break;
      case "3":
        if (((Zombie) source).getZombieType() == Zombie.ZombieType.MUMMY) {
          deltaHP = ((Mummy) source).revive();

          if (deltaHP == -444) {
            printMessage(new String[] {
              source.getName() + " tried to revive itself, but it's already alive!",
              source.getName() + "'s current HP: " + source.getHPstring(),
            }, MsgType.GAMEMASTER);
            return;
          }

          printMessage(new String[] {
            source.getName() + " revives itself with 100% HP!",
            "HP healed: " + deltaHP + " HP",
            source.getName() + "'s HP is now " + source.getHPstring() + "!"
          }, MsgType.GAMEMASTER);
        } else {
          printMessage("You can't use this skill!", MsgType.ERROR);
          return;
        }
        break;
      default:
        printMessage("You picked an invalid skill. Perhaps you failed to input a number?", MsgType.ERROR);
        return;
    }
  }

  private static void printWarning() {
    if (!gameInstance.getPlayer().canAttack()) {
      printMessage(new String[] {
        "You suddenly can't attack or use some skills! You're immobilized from not having enough HP.",
        gameInstance.getPlayer().getName() + "'s current HP: " + gameInstance.getPlayer().getHPstring(),
        "Either you forfeit the game, or you use an available skill that can heal you."
      }, MsgType.GAMEMASTER);
    }
  }
}
