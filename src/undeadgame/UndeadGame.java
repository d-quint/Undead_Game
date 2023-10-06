package undeadgame;

import java.util.ArrayList;

import undeadgame.creatures.*;
import undeadgame.util.MsgType;

public class UndeadGame {
  private boolean gameOver;
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
    gameOver = false;
    running = false;

    creatures = new ArrayList<Undead>();
    gameCommands = new ArrayList<Command>();
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
          int choice = UndeadGameUI.displayChoices("What type of undead do you want to raise?", 
            new String[]{"SKELETON", "LICH", "ZOMBIE", "MUMMY", "VAMPIRE", "GHOST"}, 
          MsgType.GAMEMASTER);

          Undead undead = null;
          String undeadName = "";

          switch (UndeadGameUI.displayChoices("Do you want to name it?", new String[]{"YES", "NO"}, MsgType.GAMEMASTER)) {
            case 1:
              UndeadGameUI.printMessage("What do you want to name it? (Avoid spaces)", MsgType.GAMEMASTER);
              UndeadGameUI.readLine();
              undeadName = UndeadGameUI.currentLine.trim().split(" ")[0].toUpperCase();
              break;
            default:
              break;
          }

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

          if (alreadyExists(undead)) {
            UndeadGameUI.printMessage("An undead named " + undead.getName() + " already exists!", MsgType.ERROR);
            return true;
          }

          if (undeadName.isEmpty()) {
            undead.setName(checkType(undead) + "_" + (creatures.size() + 1));
          }

          creatures.add(undead);

          UndeadGameUI.printMessage("You have successfully raised an undead named " + undead.getName() + "!", MsgType.GAMEMASTER);

          return true;
        }
    ));

    gameCommands.add(
      new Command("LISTUNDEADS",

        "List all the undeads you have raised",

        args -> {
          if (creatures.isEmpty()) {
            UndeadGameUI.printMessage("You have not raised any undeads yet!", MsgType.ERROR);
            return true;
          }

          UndeadGameUI.printMessage("Here are all the undeads you have raised:", MsgType.GAMEMASTER);

          for (Undead undead : creatures) {
            displayUndeadInformation((Commandable)undead);
          }

          return true;
        }
    ));
    
    gameCommands.add(
      new Command("ATTACK",  

        new String[]{"ATTACKER_NAME", "TARGET_NAME"}, 

        "Pick an undead to command and attack another undead", 

        args -> {
          Undead attacker = null;

          for (Undead undead : creatures) {
            String name = undead.getName().split(" ")[0].toUpperCase();
            
            if (name.equals(args[1].toUpperCase())) {
              attacker = undead;
              break;
            }
          }

          if (attacker == null) {
            UndeadGameUI.printMessage("An undead named " + args[1] + " does not exist!", MsgType.ERROR);
            return true;
          }

          Undead target = null;

          for (Undead undead : creatures) {
            String name = undead.getName().split(" ")[0].toUpperCase();
            
            if (name.equals(args[2].toUpperCase())) {
              target = undead;
              break;
            }
          }

          if (target == null) {
            UndeadGameUI.printMessage("An undead named " + args[2] + " does not exist!", MsgType.ERROR);
            return true;
          }

          if (attacker.isDead()) {
            if ((attacker instanceof Mummy)) {
              if (!((Mummy)attacker).diedOnce()) {
                UndeadGameUI.printMessage(attacker.getName() + " is dead! However, it can still revive!", MsgType.ERROR);
                switch (UndeadGameUI.displayChoices("Do you want to revive " + attacker.getName() + "?", new String[]{"YES", "NO"}, MsgType.GAMEMASTER)) {
                  case 1:
                    reviveMummy((Mummy)attacker);
                    break;
                  default:
                    return true;
                }
              }
            } else {
              UndeadGameUI.printMessage(attacker.getName() + " is dead! It cannot attack!", MsgType.ERROR);
              return true;
            }
          }

          if (target.isDead()) {
            UndeadGameUI.printMessage(target.getName() + " is dead! It cannot be attacked!", MsgType.ERROR);
            return true;
          }

          if (attacker == target) {
            UndeadGameUI.printMessage(attacker.getName() + " cannot attack itself!", MsgType.ERROR);
            return true;
          }

          // Check if they are incapacitated immortals
          if (!canImmortalAttack(attacker) || !canImmortalAttack(target)) {
            return true;
          }

          commenceAttack(attacker, target);

          return true;
        }
    ));
  }

  private boolean canImmortalAttack(Undead attacker) {
    if ((attacker instanceof Lich)) {
      if (!((Lich)attacker).canItAttack()) {
        UndeadGameUI.printMessage(new String[] {
          attacker.getName() + " has been incapacitated! It cannot attack anymore!",
        }, MsgType.GAMEMASTER);

        return false;
      }
    }

    if (attacker instanceof Vampire) {
      if (!((Vampire)attacker).canItAttack()) {
        UndeadGameUI.printMessage(new String[] {
          attacker.getName() + " has been incapacitated! It cannot attack anymore!",
        }, MsgType.GAMEMASTER);

        return false;
      }
    }

    if (attacker instanceof Zombie) {
      if (attacker instanceof Mummy) {
        if (!((Mummy)attacker).canItAttack()) {
          UndeadGameUI.printMessage(new String[] {
            attacker.getName() + " has been incapacitated! It cannot attack anymore!",
          }, MsgType.GAMEMASTER);

          return false;
        }
      }

      if (!((Zombie)attacker).canItAttack()) {
        UndeadGameUI.printMessage(new String[] {
          attacker.getName() + " has been incapacitated! It cannot attack anymore!",
        }, MsgType.GAMEMASTER);

        return false;
      }
    }

    return true;
  }

  private void commenceAttack(Undead attacker, Undead target) {
    // Let user pick one of the attacker's skills
    int skill = UndeadGameUI.displayChoices("What skill do you want to use?", getSkillDesc(attacker), MsgType.GAMEMASTER);

    // Check if the skill is valid
    if (skill < 1 || skill > getSkills(attacker).length) {
      UndeadGameUI.printMessage("Invalid skill!", MsgType.ERROR);
      return;
    }

    int deltaHP = 0;
    boolean isHeal = false;
    boolean isAttack = false;

    switch (skill) {
      case 1:
        deltaHP = ((Commandable)attacker).normalAttack((Commandable)target);
        isAttack = true;
        break;
      case 2:
        deltaHP = ((Commandable)attacker).skill1((Commandable)target);
        isHeal = true;

        if (attacker instanceof Lich) {
          isHeal = isAttack = true;
        }

        if (attacker instanceof Mummy && deltaHP == -444) {
          // Mummy's EAT skill failed because it cannot eat its own kind

          UndeadGameUI.printMessage(new String[] {
            attacker.getName() + " tried to use " + getSkills(attacker)[skill - 1] + ", but it cannot eat its own kind!"
          }, MsgType.GAMEMASTER);
        }

        break;
      default:
        break;
    }

    // Display the result of the attack

    UndeadGameUI.printMessage(new String[] {
      attacker.getName() + " used " + getSkills(attacker)[skill - 1] + " on " + target.getName() + "! It's super effective!",
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

    if (target.isDead()) {
      UndeadGameUI.printMessage(new String[] {
        target.getName() + " has been slain!",
      }, MsgType.GAMEMASTER);
    }

    // Check if target is incapacitated
    canImmortalAttack(target);
  }

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

    for (int i = 0; i < skillNames.length; i++) {
      skillDescCopy[i] = skillNames[i] + ": " + skillDesc[i];
    }

    return skillDescCopy;
  }

  private boolean alreadyExists(Undead undead) {
    String name1 = undead.getName().split(" ")[0];

    for (Undead other : creatures) {
      String name2 = other.getName().split(" ")[0];

      if (name1.equals(name2)) {
        return true;
      }
    }

    return false;
  }

  public void reviveMummy(Mummy target) {
    int deltaHP = target.revive();

    if (deltaHP == -444) {
      // Mummy's REVIVE skill failed because it cannot be used yet (the mummy is still alive)

      UndeadGameUI.printMessage(new String[] {
        target.getName() + " tried to use REVIVE, but it cannot be used in its current state!",
        "Make sure " + target.getName() + " is dead (HP is 0) and has never used REVIVE before!"
      }, MsgType.GAMEMASTER);

      return;
    }

    UndeadGameUI.printMessage(new String[] {
      target.getName() + " used REVIVE! It's super effective!",
      target.getName() + " has been revived back to full HP! Glory to the might of Ra!"
    }, MsgType.GAMEMASTER);

    return; 
  }

  /**
   * Starts the game.
   * This method should be called after the game is initialized.
   */
	public void start() {
    running = true;

    introduction();

    initializeCommands();

    // Append the game-specific commands to the list of commands in UndeadGameUI
    for (Command command : gameCommands) {
      UndeadGameUI.commands.add(command);
    }
	}

  private void introduction() {
    UndeadGameUI.printMessage(new String[] {
      "Welcome to UndeadGame!",
      "This is a game where you can raise the dead and command them to do your bidding.",
      "You can command undeads to attack other undeads, use their skills against them, and more.",
      "To see the updated list of commands, just type " + UndeadGameUI.prefix + "HELP.",
      "To begin spawning undeads, type " + UndeadGameUI.prefix + "POPULATE."
    }, MsgType.GAMEMASTER);

    UndeadGameUI.printMessage(new String[] {"That's about it! Relish in the power of commanding the undead!"}, MsgType.GAMEMASTER);
  }

  private void displayUndeadInformation(Commandable undead) {
    UndeadGameUI.printMessage(new String[] {
      "Name: " + ((Undead)undead).getName(),
      "Type: " + checkType((Undead)undead),
      "Health: " + undead.getHPString(),
      "Dead?: " + (((Undead)undead).isDead() ? "Yes" : "No"),
    }, MsgType.GAMEMASTER);
  }

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
}