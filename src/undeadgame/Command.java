package undeadgame;

/**
 * An interface containing a single method, run().
 * This is used to encapsulate a command's action into a single object.
 */
interface Runnable {
  /**
   * The method to be executed when the command is run.
   * 
   * @param args The arguments passed to the command
   * @return Whether the command was executed successfully
   */
  public boolean run(String[] args);
}

/**
 * A class encapsulating a command.
 * It stores the command's name, arguments, description, and action to perform.
 */
public class Command {
  String name;
  String[] args;
  String description;

  Runnable action; // The action to be executed when the command is run.

  public Command(String name, String[] args, String description, Runnable action) {
    this.name = name;
    this.args = args;
    this.description = description;
    this.action = action;
  }

  public Command(String name, String description, Runnable action) {
    this(name, new String[]{}, description, action);
  }

  public Command(String name, Runnable action) {
    this(name, new String[]{}, "", action);
  }

  public String getName() {
    return name;
  }

  public String[] getArgs() {
    return args;
  }

  public String getDescription() {
    return description;
  }

  public Runnable getAction() {
    return action;
  }

  public boolean run(String args[]) {
    return action.run(args);
  }

  /**
  * Checks if the given string is the command's name.
  * 
  * @param command The string to check
  * @return Whether the string is the command's name
  */
  public boolean isCommand(String command) {
    if (command.toUpperCase().equals(name)) {
      return true;
    }

    return false;
  }
}