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
  public static final Scanner READ = new Scanner(System.in);
  public static final int DELAY = 300; // in ms

  public static char prefix = '!';
  public static String currentLine = "";

  public static String playerName = "";
  public static UndeadGame gameInstance;

  public static boolean shouldExit = false;

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
      "  [2] Not really"
    }, MsgType.GAMEMASTER);

    do {
      readLine();

      switch (currentLine) {
      case "1":
        return true;
      case "2":
        printMessage("Goodbye!", MsgType.GAMEMASTER);
        break;
      default:
        printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
        break;
      }
    } while (!currentLine.equals("1") && !currentLine.equals("2") && !currentLine.equals("3"));

    return false;
  }

  public static void introduction() {
    System.out.println();
    printMessage("Welcome to Undead Game! What's your name?", MsgType.GAMEMASTER);
    
    readLine();
    playerName = currentLine.toUpperCase();

    printMessage("Hello, " + playerName + "!", MsgType.GAMEMASTER);
    printMessage("Type \"" + prefix + "quit\" to exit.", MsgType.GAMEMASTER);
    printMessage("Type \"" + prefix + "help\" for a quick rundown of general commands.", MsgType.GAMEMASTER);  
    printMessage("Type \"" + prefix + "start\" to start the game.", MsgType.GAMEMASTER);
  }

  public static void loop() {
    printWarning();

    readLine();
    processLine();
  }

  public static void readLine() {
    System.out.println("    | ");
    printMessage("  " + (playerName.isEmpty() ? "STRANGER:" : playerName + ":"), MsgType.INPUT);

    currentLine = READ.nextLine();
    System.out.println("    | ");
  }

  public static void processLine() {
    if (currentLine.startsWith("" + prefix)) {
      String command = currentLine.substring(1);
      String[] args = command.split(" ");

      executeCommand(args);
    } else {
      printMessage("Unknown command \"" + currentLine + "\".", MsgType.ERROR);
      printMessage("Make sure to start your commands with \"" + prefix + "\".", MsgType.WARNING);
    }
  }

  public static void executeCommand(String[] args) {
    if (((SandboxMode)gameInstance).executeGameCommand(args)) return; 
    if (((PVPMode)gameInstance).executeGameCommand(args)) return;   

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

  public static void printMessage(String message, MsgType type) {
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

  public static void printMessage(String[] messageLines, MsgType type) {
    printMessage("", MsgType.DEFAULT);
    
    printMessage(messageLines[0], type);

    for (int i = 1; i < messageLines.length; i++) {  
      printMessage((type.equals(MsgType.GAMEMASTER) ? "            " : "") + messageLines[i], MsgType.DEFAULT);
    }
  }

  public static void executeStartCommand() {
    if (gameInstance.isRunning()) {
      printMessage("The game has already started!", MsgType.ERROR);
      printMessage(new String[] {
        "Or did you want to restart another game?",
        "  [1] YES",
        "  [2] Not really",
      }, MsgType.GAMEMASTER);

      readLine();
      
      String response1, response2;

      while (!currentLine.equals("1") && !currentLine.equals("2")) {
        printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
        readLine();
      }

      response1 = currentLine;

      printMessage(new String[] {
        "Which game mode do you want?",
        "  [1] Player vs. Player (PVP)",
        "  [2] Sandbox (EVE)",
      }, MsgType.GAMEMASTER);

      readLine();

      while (!currentLine.equals("1") && !currentLine.equals("2")) {
        printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
        readLine();
      }

      response2 = currentLine;

      switch (response1) {
        case "1":
          if (response2.equals("1")) {
            gameInstance = new PVPMode();
          } else {
            gameInstance = new SandboxMode();
          }
          break;
        case "2":
          printMessage("Alright, then.", MsgType.GAMEMASTER);
          return;
        default:
          printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
          return;
      }
    }
    
    gameInstance.start();
  }

  public static void printWarning() {
    if (!gameInstance.getPlayer().canAttack()) {
      printMessage(new String[] {
        "You suddenly can't attack or use some skills! You're immobilized from not having enough HP.",
        gameInstance.getPlayer().getName() + "'s current HP: " + gameInstance.getPlayer().getHPstring(),
        "Either you forfeit the game, or you use an available skill that can heal you."
      }, MsgType.GAMEMASTER);
    }
  }
}
