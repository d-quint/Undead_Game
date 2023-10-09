package undeadgame;

import java.util.ArrayList;

import undeadgame.creatures.*;
import undeadgame.util.MsgType;

/**
 * The main class of the game.
 * This holds all game logic and handles how the game is played.
 * This class is also responsible for initializing the game-specific commands.
 */
public class UndeadGame {
  private boolean running;

  /**
   * An arraylist of all the game-specific commands in the game.
   */
  private ArrayList<Command> gameCommands;

  /**
   * An arraylist of all the creatures in the game.
   */
  private ArrayList<Undead> creatures;

  // Constructor:
  public UndeadGame() {
    running = false;

    creatures = new ArrayList<Undead>();
    gameCommands = new ArrayList<Command>();
  }

  // Getters:
  public boolean isRunning() {
    return running;
  }

  // Setters:
  public void setIsRunning(boolean running) {
    this.running = running;
  }

  /**
   * Initializes all the game-specific commands and sets up their functions.
   * This method should be called after the game is initialized.
   * This method should also be called after the game is reset.
   */
  private void initializeCommands() {

    // GAME COMMAND 1: POPULATE - Raise an undead from the depths of hell.
    gameCommands.add(
      new Command("POPULATE",  

        "Raise an undead from the depths of hell", 

        args -> {
          // First, ask the user what type of undead they want to raise.
          int choice = UndeadGameUI.displayChoices("What type of undead do you want to raise?", 
            new String[]{
              "SKELETON - Can only attack, and has 70% of its HP as attack damage.", 
              "LICH - An immortal Skeleton that can cast a spell, taking the 10% of their target's HP and adding it to its HP.", 
              "ZOMBIE - Can eat target to heal itself. Can only attack when HP is greater than 50.", 
              "MUMMY - A special Zombie that can revive after dying once, but cannot eat its own kind.", 
              "VAMPIRE - Can bite target to heal itself. Cannot attack when HP is 0.", 
              "GHOST - Can haunt to heal, and only receives 10% of the actual damage."
            }, 
          MsgType.GAMEMASTER);

          Undead undead = null;
          String undeadName = "";

          // Then, ask the user if they want to name the undead.
          switch (UndeadGameUI.displayChoices("Do you want to name it?", new String[]{"YES", "NO"}, MsgType.GAMEMASTER)) {
            case 1:
              UndeadGameUI.printMessage("What do you want to name it? (Avoid spaces)", MsgType.GAMEMASTER);
              UndeadGameUI.readLine();
              undeadName = UndeadGameUI.currentLine.trim().split(" ")[0].toUpperCase(); // Only get the first word
              break;
            default:
              break;
          }

          // Create an instance of an undead subtype based on the user's choice.
          switch (choice) {
            case 1:
              undead = new Skeleton(undeadName);
              break;
            case 2:
              undead = new Lich(undeadName);
              break;
            case 3:
              undead = new Zombie(undeadName);
              break;
            case 4:
              undead = new Mummy(undeadName);
              break;
            case 5:
              undead = new Vampire(undeadName);
              break;
            case 6:
              undead = new Ghost(undeadName);
              break;
            default:
              break;
          }

          // Check if the undead already exists.
          if (alreadyExists(undead)) {
            UndeadGameUI.printMessage("An undead named " + undeadName + " already exists!", MsgType.ERROR);
            return true;
          }

          // Check if the undead has a name. If not, give it a default name of TYPE_(curent number of undeads).
          if (undeadName.isEmpty()) {
            undead.setName(checkType(undead) + "_" + (creatures.size() + 1));
          }

          creatures.add(undead); // Add the undead to the list of creatures

          UndeadGameUI.printMessage("You have successfully raised an undead named " + undead.getName() + "!", MsgType.GAMEMASTER);

          return true; // Return true to indicate that the command has been executed successfully.
        }
    ));

    // GAME COMMAND 2: LISTUNDEADS - List all the undeads you have raised
    gameCommands.add(
      new Command("LISTUNDEADS",

        "List all the undeads you have raised",

        args -> {
          // First, check if the user has raised any undeads yet.
          if (creatures.isEmpty()) {
            UndeadGameUI.printMessage("You have not raised any undeads yet!", MsgType.ERROR);
            return true;
          }

          UndeadGameUI.printMessage("Here are all the undeads you have raised:", MsgType.GAMEMASTER);

          // Then, display all the undeads the user has raised.
          for (Undead undead : creatures) {
            displayUndeadInformation((Commandable)undead);
          }

          return true; // Return true to indicate that the command has been executed successfully.
        }
    ));
    
    // GAME COMMAND 3: ATTACK - Command an undead to attack another undead.
    gameCommands.add(
      new Command("ATTACK",  

        new String[]{"ATTACKER_NAME", "TARGET_NAME"}, 

        "Pick an undead to command and attack another undead", 

        args -> {
          Undead attacker = null;

          // First, loop through all the undeads and check if the specified attacker exists.
          for (Undead undead : creatures) {
            String name = undead.getName().split(" ")[0].toUpperCase(); // Get the first word and capitalize.
            
            // Check if the name matches the attacker's name.
            if (name.equals(args[1].toUpperCase())) {
              attacker = undead;
              break; // Break out of the loop if the attacker is found.
            }
          }

          // If the attacker is still set as null, it means that it does not exist.
          if (attacker == null) {
            UndeadGameUI.printMessage("An undead named " + args[1] + " does not exist!", MsgType.ERROR);
            return true;
          }

          Undead target = null;

          // Next, loop through all the undeads and check if the specified target exists.
          for (Undead undead : creatures) {
            String name = undead.getName().split(" ")[0].toUpperCase(); // Get the first word and capitalize.
            
            // Check if the name matches the target's name.
            if (name.equals(args[2].toUpperCase())) {
              target = undead;
              break; // Break out of the loop if the target is found.
            }
          }

          // If the target is still set as null, it means that it does not exist.
          if (target == null) {
            UndeadGameUI.printMessage("An undead named " + args[2] + " does not exist!", MsgType.ERROR);
            return true; 
          }

          // Next, check if the attacker is dead.
          if (attacker.isDead()) {
            // Additionally, check if the attacker is a Mummy.
            if ((attacker instanceof Mummy)) {
              // If this Mummy only died once so far, ask the user if they want to revive it.
              if (((Mummy)attacker).diedOnce()) {
                UndeadGameUI.printMessage(new String[] {
                  attacker.getName() + " is dead. Fortunately, it can still revive!"
                }, MsgType.GAMEMASTER);

                // Get the user's choice.
                switch (UndeadGameUI.displayChoices("Do you want to revive " + attacker.getName() + "?", new String[]{"YES", "NO"}, MsgType.GAMEMASTER)) {
                  case 1:
                    reviveMummy((Mummy)attacker); // Revive the Mummy.
                    return true;
                }
              } 
            }

            // Else, the attacker is dead and cannot attack.
            UndeadGameUI.printMessage(attacker.getName() + " is dead! It cannot attack!", MsgType.ERROR);
            return true;
          }

          // Next, check if the target is dead.
          if (target.isDead()) {
            UndeadGameUI.printMessage(target.getName() + " is dead! It cannot be attacked!", MsgType.ERROR);
            return true;
          }

          // Next, check if the attacker is the same as the target.
          if (attacker == target) {
            UndeadGameUI.printMessage(attacker.getName() + " cannot attack itself!", MsgType.ERROR);
            return true;
          }

          // Next, check if they are incapacitated immortals.
          if (!canImmortalAttack(attacker)) {
            return true;
          }

          // Finally, commence the attack.
          // This will display the results of the attack, and affect Undead states based on the attack.
          commenceAttack(attacker, target);

          return true; // Return true to indicate that the command has been executed successfully.
        }
    ));

    // GAME COMMAND 4: CLEANUP - Deletes all currently dead undeads.
    gameCommands.add(
      new Command("CLEANUP",  

        "Deletes all currently dead undeads", 

        args -> {
          // First, check if the user has raised any undeads yet.
          if (creatures.isEmpty()) {
            UndeadGameUI.printMessage("You have not raised any undeads yet!", MsgType.ERROR);
            return true;
          }

          // Initialize an arraylist to store all the found dead undeads.
          ArrayList<Undead> deadUndeads = new ArrayList<Undead>();

          // Loop through all the undeads and check if they are dead.
          // If they are, add them to the list of dead undeads.
          for (Undead undead : creatures) {
            if (undead.isDead()) {
              deadUndeads.add(undead);
            }
          }

          // Check if there are no dead undeads (which means deadUndeads is empty).
          if (deadUndeads.isEmpty()) {
            UndeadGameUI.printMessage("There are no dead undeads to clean up!", MsgType.ERROR);
            return true;
          }

          // Loop through all the dead undeads and remove them from the list of creatures.
          for (Undead undead : deadUndeads) {
            UndeadGameUI.printMessage(undead.getName() + "'s corpse has been cleaned up!", MsgType.GAMEMASTER);
            creatures.remove(undead);
          }

          UndeadGameUI.printMessage(new String[] {"You have successfully cleaned up all the dead undeads!"}, MsgType.GAMEMASTER);

          return true; // Return true to indicate that the command has been executed successfully.
        }
    ));

    // GAME COMMAND 5: CLOSE - Closes the game instance and returns back to the main menu.
    gameCommands.add(
      new Command("CLOSE",  

        "Closes the game instance and returns back to the main menu", 

        args -> {
          close(); // Close the game instance.
          UndeadGameUI.printMessage("Game closed successfully.", MsgType.GAMEMASTER);
          return true; // Return true to indicate that the command has been executed successfully.
        }
    ));
  }

  /**
   * This method checks if an immortal (or a Zombie) can attack.
   * If it cannot, it will display a message to the user.
   * 
   * @param  immortal The immortal to check.
   * @return          True if the immortal can attack, false otherwise.
   */
  private boolean canImmortalAttack(Undead immortal) {
    // Check if the immortal is a Lich.
    if ((immortal instanceof Lich)) {
      // If the Lich cannot attack, display a message to the user.
      if (!((Lich)immortal).canItAttack()) {
        UndeadGameUI.printMessage(new String[] {
          immortal.getName() + " has been incapacitated! It cannot attack anymore!",
        }, MsgType.GAMEMASTER);

        return false; // Return false to indicate that the immortal cannot attack.
      }
    }

    // Check if the immortal is a Vampire.
    if (immortal instanceof Vampire) {
      // If the Vampire cannot attack, display a message to the user.
      if (!((Vampire)immortal).canItAttack()) {
        UndeadGameUI.printMessage(new String[] {
          immortal.getName() + " has been incapacitated! It cannot attack anymore!",
        }, MsgType.GAMEMASTER);

        return false; // Return false to indicate that the immortal cannot attack.
      }
    }

    // Check if the immortal is a Zombie.
    if (immortal instanceof Zombie) {
      // If the Zombie is a Mummy, check if it can attack.
      if (immortal instanceof Mummy) {
        // If the Mummy cannot attack, display a message to the user.
        if (!((Mummy)immortal).canItAttack()) {
          UndeadGameUI.printMessage(new String[] {
            immortal.getName() + " has been incapacitated! It cannot attack anymore!",
          }, MsgType.GAMEMASTER);

          return false; // Return false to indicate that the immortal cannot attack.
        }
      }

      // Else, check if the Zombie can attack.
      if (!((Zombie)immortal).canItAttack()) {
        UndeadGameUI.printMessage(new String[] {
          immortal.getName() + " has been incapacitated! It cannot attack anymore!",
        }, MsgType.GAMEMASTER);

        return false; // Return false to indicate that the immortal cannot attack.
      }
    }

    return true; // Return true to indicate that the immortal can attack.
  }

  /**
   * This method commences an attack between two undeads.
   * This method will display the results of the attack, and affect Undead states based on the attack.
   */
  private void commenceAttack(Undead attacker, Undead target) {
    // First, let user pick one of the attacker's skills.
    int skill = UndeadGameUI.displayChoices("What skill do you want to use?", getSkillDesc(attacker), MsgType.GAMEMASTER);

    // Check if the skill is valid.
    if (skill < 1 || skill > getSkills(attacker).length) {
      UndeadGameUI.printMessage("Invalid skill!", MsgType.ERROR);
      return; // Return to indicate that the attack has failed to commence.
    }

    int deltaHP = 0; // The absolute change in HP of the target
    boolean isHeal = false; // Indicates if the skill is a healing skill
    boolean isAttack = false; // Indicates if the skill is an attacking skill

    // Check which skill the attacker used.
    switch (skill) {
      case 1: // NORMAL SKILL
        deltaHP = ((Commandable)attacker).normalAttack((Commandable)target);
        isAttack = true;
        break;
      case 2: // SPECIAL SKILL (SKILL 1)
        deltaHP = ((Commandable)attacker).skill1((Commandable)target);
        isHeal = true;

        // If the attacker is a Mummy and deltaHP returned an Error code,
        // it means that the Mummy tried to eat another Mummy.
        if (attacker instanceof Mummy && deltaHP == -444) {
          UndeadGameUI.printMessage(new String[] {
            attacker.getName() + " tried to use " + getSkills(attacker)[skill - 1] + ", but it cannot eat its own kind!"
          }, MsgType.GAMEMASTER);

          return; // Return to indicate that the attack has failed to commence.
        }

        break;
      case 3: // SPECIAL SKILL (SKILL 2)
        // If the attacker is a Mummy, its skill 2 is a revive.
        // But code will only reach here if attacker is alive, so they should not be able to use revive at this point.
        if (attacker instanceof Mummy) {
          UndeadGameUI.printMessage(new String[] {
            attacker.getName() + " tried to use REVIVE, but it is still alive!"
          }, MsgType.GAMEMASTER);

          return; // Return to indicate that the attack has failed to commence.
        }
        break;
      default:
        break;
    }

    // Finally, display the result of the attack.

    UndeadGameUI.printMessage(new String[] {
      attacker.getName() + " used " + getSkills(attacker)[skill - 1] + " on " + target.getName() + "! It's " + effectiveness(deltaHP) + " effective!",
    }, MsgType.GAMEMASTER);

    if (isAttack) {
      UndeadGameUI.printMessage(new String[] {
        target.getName() + " received " + deltaHP + " damage!",
        target.getName() + "'s HP is now " + ((Commandable)target).getHPString() + ".",
      }, MsgType.GAMEMASTER);
    }
    
    if (isHeal) {
      UndeadGameUI.printMessage(new String[] {
        attacker.getName() + " received " + deltaHP + " HP!",
        attacker.getName() + "'s HP is now " + ((Commandable)attacker).getHPString() + ".",
      }, MsgType.GAMEMASTER);
    }

    // Check if target is dead.
    if (target.isDead()) {
      UndeadGameUI.printMessage(new String[] {
        target.getName() + " has been slain!",
      }, MsgType.GAMEMASTER);

      // But if the target is not a Mummy, there is no need to check if it can attack.
      // We do this because a Mummy may still revive after dying once.
      if (!(target instanceof Mummy)) {
        return;
      }
    }

    // Check if target is incapacitated or cannot attack in its current state.
    canImmortalAttack(target);
  }

  /**
   * This method checks the effectiveness of an attack.
   * 
   * @param  deltaHP The absolute change in HP of the target or attacker.
   * @return         A string indicating the effectiveness of the attack.
   */
  private String effectiveness(int deltaHP) {
    if (deltaHP == 0) {
      return "not quite";
    } else if (deltaHP > 0 && deltaHP <= 30) {
      return "kinda";
    } else if (deltaHP > 30 && deltaHP <= 100) {
      return "super";
    } else if (deltaHP > 100) {
      return "super duper";
    }

    return "weirdly not";
  }

  /**
   * This method returns a String array of all the skill names of an undead.
   * 
   * @param  undead The undead to get the skills from.
   * @return        A String array of all the skills of the undead.
   */
  private String[] getSkills(Undead undead) {
    String[] skills = null;

    if (undead instanceof Skeleton) {
      skills = Skeleton.skills;
      if (undead instanceof Lich) {
        skills = Lich.skills;
      }
    } else if (undead instanceof Zombie) {
      skills = Zombie.skills;
      if (undead instanceof Mummy) {
        skills = Mummy.skills;
      }
    } else if (undead instanceof Vampire) {
      skills = Vampire.skills;
    } else if (undead instanceof Ghost) {
      skills = Ghost.skills;
    }

    return skills;
  }

  /**
   * This method returns a String array of all the skill descriptions of an undead.
   * 
   * @param  undead The undead to get the skill descriptions from.
   * @return        A String array of all the skill descriptions of the undead.
   */
  private String[] getSkillDesc(Undead undead) {
    String[] skillDesc = null;

    if (undead instanceof Skeleton) {
      skillDesc = Skeleton.skillDesc;
      if (undead instanceof Lich) {
        skillDesc = Lich.skillDesc;
      }
    } else if (undead instanceof Zombie) {
      skillDesc = Zombie.skillDesc;
      if (undead instanceof Mummy) {
        skillDesc = Mummy.skillDesc;
      }
    } else if (undead instanceof Vampire) {
      skillDesc = Vampire.skillDesc;
    } else if (undead instanceof Ghost) {
      skillDesc = Ghost.skillDesc;
    }

    String[] skillNames = getSkills(undead);

    String[] skillDescCopy = skillDesc.clone();

    // Finally, append the skill names to the skill descriptions.
    for (int i = 0; i < skillNames.length; i++) {
      skillDescCopy[i] = skillNames[i] + ": " + skillDesc[i];
    }

    return skillDescCopy;
  }

  /**
   * This method checks if an undead (or undead name) already exists in the arraylist.
   * 
   * @param  undead The undead to check.
   * @return        True if the undead already exists, false otherwise.
   */
  private boolean alreadyExists(Undead undead) {
    String name1 = undead.getName().split(" ")[0]; // Get the first word and capitalize.

    // Loop through all the undeads.
    for (Undead other : creatures) {
      String name2 = other.getName().split(" ")[0]; // Get the first word and capitalize.

      // Check if the name matches.
      if (name1.equals(name2)) {
        return true; // Return true to indicate that the undead already exists.
      }
    }

    return false; // Return false to indicate that the undead does not exist.
  }

  /**
   * This method is a helper method in reviving a Mummy.
   * 
   * @param  target  The Mummy to revive.
   * @return         True if the Mummy has been revived, false otherwise.
   */
  public boolean reviveMummy(Mummy target) {
    // First, attempt to revive the mummy.
    // If the mummy cannot be revived, display a message to the user.
    if (!target.revive()) {
      UndeadGameUI.printMessage(new String[] {
        target.getName() + " tried to use REVIVE, but it cannot be used in its current state!",
        "Make sure " + target.getName() + " is dead (HP is 0) and has never used REVIVE before!"
      }, MsgType.GAMEMASTER);

      return false; // Return false to indicate that the Mummy has not been revived.
    }

    // Else, the Mummy has been revived.
    UndeadGameUI.printMessage(new String[] {
      target.getName() + " used REVIVE! It's super effective!",
      target.getName() + " has been revived back to full HP! Glory to the might of Ra!"
    }, MsgType.GAMEMASTER);

    target.isDead(false); // Set the Mummy's dead state to false.

    return true; // Return true to indicate that the Mummy has been revived.
  }

  /**
   * Starts the game.
   * This method should be called after the game is initialized.
   */
  public void start() {
    running = true; // Set the game's running state to true.

    introduction(); // Display the introduction message.

    initializeCommands(); // Initialize the game-specific commands.

    // Append the game-specific commands to the list of commands in UndeadGameUI.
    for (Command command : gameCommands) {
      UndeadGameUI.commands.add(command);
    }
  }

  /**
   * This method displays the introduction message of the game.
   */
  private void introduction() {
    UndeadGameUI.printMessage(new String[] {
      "Welcome to Undead Game!",
      "This is a game where you can raise the dead and command them to do your bidding.",
      "You can command undeads to attack other undeads, use their skills against them, and more.",
      "To see the updated list of commands, just type " + UndeadGameUI.prefix + "HELP.",
      "To begin spawning undeads, type " + UndeadGameUI.prefix + "POPULATE."
    }, MsgType.GAMEMASTER);

    UndeadGameUI.printMessage(new String[] {"That's about it! Relish in the power of commanding the undead!"}, MsgType.GAMEMASTER);
  }

  /**
   * This method displays the information of an undead.
   * It displays the undead's name, type, health, and dead state.
   * 
   * @param undead The undead to display the information of.
   */
  private void displayUndeadInformation(Commandable undead) {
    UndeadGameUI.printMessage(new String[] {
      "Name: " + ((Undead)undead).getName(),
      "Type: " + checkType((Undead)undead),
      "Health: " + undead.getHPString(),
      "Dead?: " + (((Undead)undead).isDead() ? "Yes" : "No"),
    }, MsgType.GAMEMASTER);
  }

  /**
   * This method checks the type of an undead.
   * 
   * @param  undead The undead to check.
   * @return        A string indicating the type of the undead.
   */
  private String checkType(Undead undead) {
    if (undead instanceof Skeleton) {
      if (undead instanceof Lich) {
        return "LICH";
      }
      return "SKELETON";
    } else if (undead instanceof Zombie) {
      if (undead instanceof Mummy) {
        return "MUMMY";
      }
      return "ZOMBIE";
    } else if (undead instanceof Vampire) {
      return "VAMPIRE";
    } else if (undead instanceof Ghost) {
      return "GHOST";
    } else {
      return "UNKNOWN";
    }
  }

  /**
   * This method resets the game.
   * It clears all the creatures and game-specific commands from their respective arraylists.
   */
  public void reset() {
    UndeadGameUI.commands.removeAll(gameCommands);

    creatures.clear();
    gameCommands.clear();
  }

  /**
   * This method closes the game instance.
   * It resets the game and sets the game's running state to false.
   */
  public void close() {
    reset(); // Reset the game.

    running = false;
  }
}
