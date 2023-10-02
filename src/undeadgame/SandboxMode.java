package undeadgame;

public class SandboxMode extends UndeadGame {
  public SandboxMode() {
    super(GameMode.SANDBOX);
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
        //executeListSkillsCommand();
        break;
      case "useskill":
        //executeActionCommand(commandArgs);
        break;
      case "forfeit":
        //executeForfeitCommand();
        break;
      case "stats":
        //executeStatsCommand();
        break;
      default:
        return false;
    }

    return true;
  }
}
