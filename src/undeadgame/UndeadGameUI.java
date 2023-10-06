package undeadgame;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.Scanner;

import undeadgame.util.*;

/**
 * A class encapsulating all the UI-related methods of the game
 */
public class UndeadGameUI {
  public static ArrayList<Command> commands;

  public static final Scanner READ = new Scanner(System.in);
  public static final int DELAY = 300; // in ms

  public static char prefix = '!';
  public static String currentLine = "";
  public static String playerName = "";
  public static boolean shouldExit = false;

  public static UndeadGame gameInstance;

  private static void loop() {
    readLine();
    processLine();
  }

  public static boolean run() {
    setup();
    
    if (playerName.isEmpty()) {
      introduction();
    } else {
      printMessage(new String[] {
        "Alright, " + playerName + "!",
        "Just a quick reminder of the commands:"
      }, MsgType.GAMEMASTER);

      displayCommands();
    }

    while (!gameInstance.isGameOver()) {
      loop();

      if (shouldExit) {
        return false;
      }
    }

    printMessage("Thanks for playing!", MsgType.GAMEMASTER);

    do {
      switch (displayChoices("Do you want to play again?", new String[] {"YES", "NO"}, MsgType.GAMEMASTER)) {
      case 1:
        return true;
      case 2:
        printMessage("Goodbye!", MsgType.GAMEMASTER);
        return false;
      default:
        printMessage("You picked an invalid option.", MsgType.ERROR);
        break;
      }
    } while (true); 
  }

  // UI-RELATED UTILITY METHODS:

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

  public static int displayChoices(String prompt, String[] choices, MsgType type) {
    printMessage(prompt, type);

    for (int i = 0; i < choices.length; i++) {
      printMessage("  [" + (i + 1) + "] " + choices[i], type);
    }

    readLine();

    int choice = -1;

    try {
      choice = Integer.parseInt(currentLine);
    } catch (NumberFormatException e) {
      printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
      return displayChoices(prompt, choices, type);
    }

    if (choice < 1 || choice > choices.length) {
      printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
      return displayChoices(prompt, choices, type);
    }

    return choice;
  }

  public static void readLine() {
    System.out.println("    | ");
    printMessage("  " + (playerName.isEmpty() ? "STRANGER:" : playerName + ":"), MsgType.INPUT);

    currentLine = READ.nextLine();
    System.out.println("    | ");
  }

  // PRIVATE METHODS:

  private static void setup() {
    gameInstance = new UndeadGame();
    commands = new ArrayList<Command>();
    
    initializeCommands();
  }

  private static void initializeCommands() {
    // Initialize all commands, aliases, descriptions, and their actions
    commands.add(new Command("START", "Starts the game", new Runnable() {
      public boolean run(String[] args) {
        if (gameInstance.isRunning()) {
          printMessage("The game has already started!", MsgType.ERROR);
        }

        switch (displayChoices("Did you want to restart the game?", new String[] {"YES", "NO"}, MsgType.GAMEMASTER)) {
          case 1:
            gameInstance = new UndeadGame();
            printMessage("Game restarted successfully.", MsgType.GAMEMASTER);
            gameInstance.start();
            break;
          case 2:
            break;
          default:
            printMessage("You picked an invalid option.", MsgType.ERROR);
        }

        return true;
      }
    }));

    commands.add(
      new Command("HELP", "Lists all usable commands", new Runnable() {
      public boolean run(String[] args) {
        displayCommands();

        return true;
      }
    }));

    commands.add(new Command("EXIT", "Exit the program", new Runnable() {
      public boolean run(String[] args) {
        shouldExit = true;

        return true;
      }
    }));

    commands.add(new Command("SETPREFIX", new String[] {"NEW_PREFIX"}, "Sets the prefix for commands", new Runnable() {
      public boolean run(String[] args) {
        prefix = args[1].charAt(0);
        printMessage("The prefix has been set to \"" + prefix + "\".", MsgType.GAMEMASTER);

        return true;
      }
    }));

    commands.add(new Command("SETNAME", new String[] {"NEW_NAME"}, "Sets the name of the player", new Runnable() {
      public boolean run(String[] args) {
        playerName = args[1].toUpperCase();
        printMessage("Your name has been set to \"" + playerName + "\".", MsgType.GAMEMASTER);

        return true;
      }
    }));
  }

  private static void displayCommands() {
    printMessage("Here are the commands:", MsgType.GAMEMASTER);

    // Display a box-like structure similar to the embed in Discord
    printMessage("---------------------------------------------------------------------", MsgType.GAMEMASTER);
    printMessage("   COMMAND <ARG/S> - DESCRIPTION ", MsgType.GAMEMASTER);
    printMessage("---------------------------------------------------------------------", MsgType.GAMEMASTER);

    // Print all command information ((prefix)name <args, ...> | description)
    for (Command command : commands) {
      if (command != null) {
        // Print name and arguments
        String commandInfo = "  " + prefix + command.getName();

        if (command.getArgs().length > 0) {
          commandInfo += " <";

          for (int i = 0; i < command.getArgs().length; i++) {
            commandInfo += command.getArgs()[i] + (i == command.getArgs().length - 1 ? "" : ", ");
          }

          commandInfo += ">";
        }

        commandInfo += " - " + command.getDescription();

        printMessage(commandInfo, MsgType.GAMEMASTER); // Print the command information
      }
    }

    printMessage("---------------------------------------------------------------------", MsgType.GAMEMASTER);
  }

  private static void introduction() {
    System.out.println();
    printMessage("Welcome to Undead Game! What's your name?", MsgType.GAMEMASTER);
    
    readLine();
    playerName = currentLine.toUpperCase();

    printMessage("Hello, " + playerName + "!", MsgType.GAMEMASTER);
    
    displayCommands();
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
    for (Command command : commands) {
      if (command == null || !command.isCommand(args[0])) {
        continue;
      }

      if (args.length - 1 < command.getArgs().length) {
        printMessage("You didn't provide enough arguments for the command \"" + command.getName() + "\".", MsgType.ERROR);
        printMessage("Make sure to provide the correct amount of arguments.", MsgType.WARNING);
        return;
      }

      if (command.run(args)) { // Run the command
        return;
      }
    }

    printMessage("Unknown command \"" + args[0] + "\".", MsgType.ERROR);
  }
}
