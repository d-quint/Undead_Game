package undeadgame;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.Scanner;

import undeadgame.util.*;

/**
 * A class encapsulating all the UI-related methods of the game.
 * It implements the "console"-like flow of the application.
 * By constantly looping, it reads the user's input, processes it, and displays a response based on it.
 */
public class UndeadGameUI {
  /**
   * An arraylist containing all the general-purpose commands.
   */
  public static ArrayList<Command> commands;

  public static final Scanner READ = new Scanner(System.in); // Scanner for reading user input
  public static final int DELAY = 300; // Delay between each message in milliseconds

  public static char prefix = '!'; // The prefix for commands
  public static String currentLine = ""; // The current line of input
  public static String playerName = ""; // The name of the player
  public static boolean shouldExit = false; // Whether the program should exit

  public static UndeadGame gameInstance; // The instance of the game

  /**
   * The main loop of the application.
   * It constantly loops, reading the user's input, processing it, and displaying a response based on it.
   */
  private static void loop() {
    readLine();
    processLine();
  }

  /**
   * The main method of the application.
   * It sets up the game, and runs the main loop.
   */
  public static void run() {
    setup(); // Set up the application.
    
    // Check if the player has a name.
    if (playerName.isEmpty()) {
      // If not, introduce them to the application and ask for their name.
      introduction();
    } else {
      // If they do, greet them and display the commands.
      printMessage(new String[] {
        "Alright, " + playerName + "!",
        "Just a quick reminder of the commands:"
      }, MsgType.GAMEMASTER);

      displayCommands();
    }

    // Start the main loop.
    while (true) {
      loop();

      // If shouldExit is true, exit the application.
      if (shouldExit) {
        printMessage("Goodbye, " + playerName + "!", MsgType.GAMEMASTER);
        return;
      }
    }
  }

  // UI-RELATED UTILITY METHODS:

  /**
   * Prints a message to the console.
   * 
   * @param message The message to print
   * @param type    The type of message to print. It can be DEFAULT, INPUT, GAMEMASTER, ERROR, or WARNING.
   * @see MsgType
   */
  public static void printMessage(String message, MsgType type) {
    System.out.print("    | "); // Style the application by adding a line-like thingy next to the messages.
    
    // Check the type of message, and print it accordingly.
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

    // Sleep for a bit to make the application feel more natural.
    try {
      Thread.sleep(DELAY); // Delay between each message.
    } catch (InterruptedException e) {
      e.printStackTrace(); // Print the stack trace if an error occurs.
    }
  }

  /**
   * Prints a message to the console.
   * 
   * @param messageLines The lines of the message to print
   * @param type         The type of message to print. It can be DEFAULT, INPUT, GAMEMASTER, ERROR, or WARNING.
   * @see MsgType
   */
  public static void printMessage(String[] messageLines, MsgType type) {
    printMessage("", MsgType.DEFAULT); // Print an empty line to make the message look nicer.
    
    printMessage(messageLines[0], type); // Print the first line of the message.

    // Then, print the rest of the lines of the message.
    for (int i = 1; i < messageLines.length; i++) {  
      printMessage((type.equals(MsgType.GAMEMASTER) ? "            " : "") + messageLines[i], MsgType.DEFAULT);
    }
  }

  /**
   * Displays a prompt with a list of choices.
   * 
   * @param   prompt  The prompt to display
   * @param   choices The choices to display
   * @param   type    The type of message to display. It can be DEFAULT, INPUT, GAMEMASTER, ERROR, or WARNING.
   * @return          The choice the user picked
   */
  public static int displayChoices(String prompt, String[] choices, MsgType type) {
    printMessage(prompt, type); // Print the prompt.

    // Then, print all the choices.
    for (int i = 0; i < choices.length; i++) {
      printMessage("  [" + (i + 1) + "] " + choices[i], type);
    }

    readLine(); // Read the user's input.

    int choice = -1;

    // Finally, check if the user's input is valid.
    try {
      choice = Integer.parseInt(currentLine);
    } catch (NumberFormatException e) {
      printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
      return displayChoices(prompt, choices, type);
    }

    // If the user's input is invalid, display the choices again.
    if (choice < 1 || choice > choices.length) {
      printMessage("You picked an invalid option. Perhaps you failed to input a number?", MsgType.ERROR);
      return displayChoices(prompt, choices, type);
    }

    return choice; // Return the choice the user picked.
  }

  /**
   * Reads a line of input from the user.
   */
  public static void readLine() {
    System.out.println("    | ");
    printMessage("  " + (playerName.isEmpty() ? "STRANGER:" : playerName + ":"), MsgType.INPUT);

    currentLine = READ.nextLine();
    System.out.println("    | ");
  }

  // PRIVATE METHODS:

  /**
   * Sets up the application.
   * It initializes the game instance, and all the commands.
   */
  private static void setup() {
    gameInstance = new UndeadGame();
    commands = new ArrayList<Command>();
    
    initializeCommands(); // Initialize all commands.
  }

  /**
   * Initializes all commands.
   * It adds all the commands to the commands arraylist.
   */
  private static void initializeCommands() {
    // COMMAND 1: START - Starts the game instance, allowing you to use more game-related commands.
    commands.add(
      new Command("START",

        "Starts the game instance, allowing you to use more game-related commands",
        
        args -> {
          // First, check if the game is already running.
          if (gameInstance.isRunning()) {
            printMessage("The game has already started!", MsgType.ERROR);

            // Then, ask the user if they had wanted to restart the game.
            switch (displayChoices("Did you want to restart the game?", new String[] { "YES", "NO" }, MsgType.GAMEMASTER)) {
              case 1: // If they did, restart the game.
                printMessage("Game restarted successfully.", MsgType.GAMEMASTER);
                gameInstance.reset();
                gameInstance.start();
                break;
              case 2: // If they didn't, return.
                printMessage("Alright, " + playerName + "!", MsgType.GAMEMASTER);
                break;
              default:
                printMessage("You picked an invalid option.", MsgType.ERROR);
            }

            return true; 
          }

          // Else, start the game appropriately.
          printMessage("Game started successfully.", MsgType.GAMEMASTER);
          
          gameInstance.start(); // Start the game.

          return true; // Return true to indicate that the command ran successfully.
        }
    ));

    // COMMAND 2: HELP - Lists all currently usable commands.
    commands.add(
      new Command("HELP",

        "Lists all currently usable commands",

        args -> {
          displayCommands(); // Display all commands.
          return true; // Return true to indicate that the command ran successfully.
        }
    ));

    // COMMAND 3: EXIT - Exits the program.
    commands.add(
      new Command("EXIT",

        "Exits the program",
        
        args -> {
          shouldExit = true; // Set shouldExit to true to indicate that the program should exit.
          return true; // Return true to indicate that the command ran successfully.
        }
    ));

    // COMMAND 4: SETPREFIX - Sets a new prefix for typing commands.
    commands.add(
      new Command("SETPREFIX",

        new String[] { "NEW_PREFIX" },

        "Sets a new prefix for typing commands",

        args -> {
          // Set the prefix to the first character of the second argument.
          prefix = args[1].charAt(0); 

          // Then, print a message to indicate that the prefix has been set.
          printMessage("The prefix has been set to \"" + prefix + "\".", MsgType.GAMEMASTER);

          return true; // Return true to indicate that the command ran successfully.
        }
    ));

    // COMMAND 5: SETNAME - Sets a new name for the player.
    commands.add(
      new Command("SETNAME",

        new String[] { "NEW_NAME" },

        "Sets a new name for the player",

        args -> {
          // Set the name of the player to the second argument.
          playerName = args[1].toUpperCase();

          // Then, print a message to indicate that the name has been set.
          printMessage("Your name has been set to \"" + playerName + "\".", MsgType.GAMEMASTER);

          return true; // Return true to indicate that the command ran successfully.
        }
    ));
  }

  /**
   * Displays all commands.
   * It prints all the commands and information about them to the console.
   */
  private static void displayCommands() {
    printMessage("Here are the commands:", MsgType.GAMEMASTER);

    // Display a box-like structure to make the commands look nicer.
    printMessage("-------------------------------------------------------------------------------------------", MsgType.GAMEMASTER);
    printMessage("   COMMAND <ARG/S> - DESCRIPTION ", MsgType.GAMEMASTER);
    printMessage("-------------------------------------------------------------------------------------------", MsgType.GAMEMASTER);

    // Print all command information ((prefix)name <args, ...> | description).
    // Loop through all commands currently stored in the commands arraylist.
    for (Command command : commands) {
      // Check if the current command exists.
      if (command != null) {
        // Print its name and arguments (if they exist).
        String commandInfo = "  " + prefix + command.getName();

        if (command.getArgs().length > 0) {
          commandInfo += " <";

          for (int i = 0; i < command.getArgs().length; i++) {
            commandInfo += command.getArgs()[i] + (i == command.getArgs().length - 1 ? "" : ", ");
          }

          commandInfo += ">";
        }

        // Print its description.
        commandInfo += " - " + command.getDescription();

        // Finally, print the command information to the user.
        printMessage(commandInfo, MsgType.GAMEMASTER);
      }
    }

    // Display a box-like structure to make the commands look nicer.
    printMessage("-------------------------------------------------------------------------------------------", MsgType.GAMEMASTER);
  }

  /**
   * Introduces the user to the application.
   * It prompts the user for their name, and greets them.
   */
  private static void introduction() {
    System.out.println();
    printMessage("Welcome to Undead Game! What's your name?", MsgType.GAMEMASTER);
    
    readLine();
    playerName = currentLine.toUpperCase();

    printMessage("Hello, " + playerName + "!", MsgType.GAMEMASTER);
    
    displayCommands();
  }

  /**
   * Processes the current line of input.
   * It checks if the current line of input is a command, and executes it if it is.
   */
  private static void processLine() {
    // Check if the current line of input is a command.
    if (currentLine.startsWith("" + prefix)) {
      String command = currentLine.substring(1); // Remove the prefix from the command.
      String[] args = command.split(" "); // Split the command into arguments.

      executeCommand(args); // Execute the command.
    } else { // If the current line of input is not a command, print an error message.
      printMessage("Unknown command \"" + currentLine + "\".", MsgType.ERROR);
      printMessage("Make sure to start your commands with \"" + prefix + "\".", MsgType.WARNING);
    }
  }

  /**
   * Executes a command.
   * It loops through all the commands, and executes the one that matches the given arguments.
   */
  private static void executeCommand(String[] args) {
    // Loop through all commands currently stored in the commands arraylist.
    for (Command command : commands) {
      // If the current command does not exist,
      // or if the current command is not the command the user is trying to run,
      // continue to the next command.
      if (command == null || !command.isCommand(args[0])) {
        continue;
      }

      // Check if the user provided enough arguments for the command.
      if (args.length - 1 < command.getArgs().length) {
        printMessage("You didn't provide enough arguments for the command \"" + command.getName() + "\".", MsgType.ERROR);
        printMessage("Make sure to provide the correct amount of arguments.", MsgType.WARNING);
        return;
      }

      // Finally, run the command. Return if it runs successfully.
      if (command.run(args)) {
        return;
      }

      // If the command does not run successfully, print an error message.
      printMessage("The command \"" + command.getName() + "\" failed to run.", MsgType.ERROR);
    }

    printMessage("Unknown command \"" + args[0] + "\".", MsgType.ERROR);
  }
}