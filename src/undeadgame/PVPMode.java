package undeadgame;

import undeadgame.creatures.Ghost;
import undeadgame.creatures.Lich;
import undeadgame.creatures.Mummy;
import undeadgame.creatures.Skeleton;
import undeadgame.creatures.Vampire;
import undeadgame.creatures.Zombie;
import undeadgame.util.MsgType;

public class PVPMode extends UndeadGame {
  public PVPMode() {
    super(GameMode.PVP);
  }

  public void start() {
    super.getPlayer().setName(UndeadGameUI.currentLine);

    setRandomEnemy();

    UndeadGameUI.printMessage("\"" + super.getPlayer().getName() + "\" has successfully risen from the dead!", MsgType.GAMEMASTER);
    
    UndeadGameUI.printMessage(new String[] {
      "Your mortal enemy is \"" + super.getEnemy().getName() + "\"!",
      "  Use command \"" + UndeadGameUI.prefix + "listskills\" to list your usable skills.",
      "  Use command \"" + UndeadGameUI.prefix + "useskill\" to use one of those skills and/or attack your enemy.",
      "  Use command \"" + UndeadGameUI.prefix + "forfeit\" to forfeit the game.",
      "  Use command \"" + UndeadGameUI.prefix + "stats\" to display the current stats of your undead and the enemy's.",
      "  Use command \"" + UndeadGameUI.prefix + "gamehelp\" to display this list of game-related commands again.",
      "",
      "Good luck, " + super.getPlayer().getName() + "!"
    }, MsgType.GAMEMASTER);
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

  @Override
  public boolean executeGameCommand(String[] args) {
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

  private void executeStatsCommand() {
    if (!super.isRunning()) {
      UndeadGameUI.printMessage("Start the game first before using game commands!", MsgType.ERROR);
      return;
    }

    UndeadGameUI.printMessage(new String[] {
      "Your stats:",
      "  Name: " + super.getPlayer().getName(),
      "  HP: " + super.getPlayer().getHPstring(),
      "  Type: " + super.getPlayer().getType(),
      "  Can it attack?: " + (super.getPlayer().canAttack() ? "Yes" : "No"),
      "  Is it dead?: " + (super.getPlayer().isDead() ? "Yes" : "No")
    }, MsgType.GAMEMASTER);

    UndeadGameUI.printMessage(new String[] {
      "Enemy stats:",
      "  Name: " + super.getEnemy().getName(),
      "  HP: " + super.getEnemy().getHPstring(),
      "  Type: " + super.getEnemy().getType(),
      "  Can it attack?: " + (super.getEnemy().canAttack() ? "Yes" : "No"),
      "  Is it dead?: " + (super.getEnemy().isDead() ? "Yes" : "No")
    }, MsgType.GAMEMASTER);
  }

  private void executeForfeitCommand() {
    if (!super.isRunning()) {
      UndeadGameUI.printMessage("Start the game first before using game commands!", MsgType.ERROR);
      return;
    }

    UndeadGameUI.printMessage("You forfeited! " + super.getEnemy().getName() + " wins!", MsgType.GAMEMASTER);
    super.setGameOver(true);
  }

  private void executeActionCommand(String[] args) {
    if (!super.isRunning()) {
      UndeadGameUI.printMessage("Start the game first before using game commands!", MsgType.ERROR);
      return;
    }

    String skill = "";

    if (args.length == 0) {
      UndeadGameUI.printMessage("Choose an available skill from your list of skills!", MsgType.GAMEMASTER);
      executeListSkillsCommand();
      UndeadGameUI.readLine();
      skill = UndeadGameUI.currentLine;
    } else {
      skill = args[0];
    }

    super.displayAttack(skill, super.getPlayer(), super.getEnemy());

    enemyAttack();

    if (checkWinCondition())
      return;
  }

  private boolean checkWinCondition() {
    int deadPlayer = super.checkWhoDied();

    if (deadPlayer == 2) {
      UndeadGameUI.printMessage(new String[] { "Congratulations, " + UndeadGameUI.playerName + "! Your creature triumphed!" },
          MsgType.GAMEMASTER);
      return true;
    } else if (deadPlayer == 1) {
      UndeadGameUI.printMessage(new String[] { "You lose! I guess your creature will be going back six feet under." },
          MsgType.GAMEMASTER);
      return true;
    }

    return false;
  }

  private void enemyAttack() {
    UndeadGameUI.printMessage(new String[] { "..." }, MsgType.GAMEMASTER);

    String skill = "";

    switch (super.getEnemy().getType()) {
      case ZOMBIE:
        if (((Zombie) super.getEnemy()).getZombieType() == Zombie.ZombieType.NORMAL) {
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
        if (((Skeleton) super.getEnemy()).getSkeletonType() == Skeleton.SkeletonType.NORMAL) {
          skill = "1";
          break;
        }
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

    displayAttack(skill, super.getEnemy(), super.getPlayer());
  }

  private void executeListSkillsCommand() {
    if (!super.isRunning()) {
      UndeadGameUI.printMessage("Start the game first before using game commands!", MsgType.ERROR);
      return;
    }

    UndeadGameUI.printMessage("Your skills are:", MsgType.GAMEMASTER);

    String[] skills = new String[3];

    switch (super.getPlayer().getType()) {
      case VAMPIRE:
        skills[0] = "Attack - Attack your enemy with your claws! (Damage: 100% of your HP)";
        skills[1] = "Bite - Heal yourself by 80% of your target's HP with a bite!";
        break;
      case ZOMBIE:
        if (((Zombie) super.getPlayer()).getZombieType() == Zombie.ZombieType.NORMAL) {
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
        if (((Skeleton) super.getPlayer()).getSkeletonType() == Skeleton.SkeletonType.LICH) {
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
        UndeadGameUI.printMessage("              [" + (i + 1) + "] " + skills[i], MsgType.DEFAULT);
      }
    }
  }

  private void setRandomEnemy() {
    do {
      switch ((int) (Math.random() * 6)) {
        case 0:
          super.setEnemy(new Vampire("MARCY"));
          break;
        case 1:
          super.setEnemy(new Zombie("ZOMATHY"));
          break;
        case 2:
          super.setEnemy(new Ghost("CASPER"));
          break;
        case 3:
          super.setEnemy(new Skeleton("SANS"));
          break;
        case 4:
          super.setEnemy(new Lich("LICHELLE"));
          break;
        case 5:
          super.setEnemy(new Mummy("TUTANKHAMUN"));
          break;
      }
    } while (super.getEnemy().getType() == super.getPlayer().getType());
  }
}
