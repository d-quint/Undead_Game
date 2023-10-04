package undeadgame.creatures;

/**
 * This class is used to represent a Mummy. A mummy is an  undead like zombie, but it
 * does not eat its own kind. Mummy can attack other undead; its attack damage is equal to
 * the half of its HP plus 10% of the undead HP. If its HP reached 0, it will die and needs
 * to be revive again. When revived it will have its initial HP again.
 */
public class Mummy extends Zombie {
  public boolean diedOnce;

  private static final String[] skills = { "NORMAL ATTACK", "EAT", "REVIVE" } ; // Mummy's skills.
  private static final String[] skillDesc = { "Attack your target by infecting them! (Damage: 50% of your HP)", "Feast on the brains of your target and gain 50% of their HP!", "By the power vested in you by the gods, revive yourself with 100% HP! (You can only revive once!)" }; // Mummy's skill descriptions.  

  // Constructor:
  public Mummy(String name) {
    super(name);
    super.isMummy(true);
    diedOnce = false;
  }

  // Setters:

  @Override
  public void setName(String name) {
    super.setName(name + " (Mummy)");
  }

  // Skill information static getters:
  public static String getSkillName(int skill) {
    if (skill < 0 || skill >= skills.length) {
      return null;
    }

    return skills[skill];
  }

  public static String getSkillInfo(int skill) {
    if (skill < 0 || skill >= skillDesc.length) {
      return null;
    }

    return skillDesc[skill];
  }

  @Override
  public void update() {
    if (super.getHp() <= 0) { // If the mummy's HP is 0 or less, it cannot attack anymore.
      super.setHp(0);
      super.canAttack(false);

      if (!diedOnce) { // If the mummy died once, it can be revived.
        diedOnce = true;
        return;
      }
    } else {
      super.canAttack(true);
    }

    // Cap the mummy's HP to zombie's max HP.
    Math.min(super.getHp(), Zombie.MAX_HP);

    super.update();
  }  

  /**
   * NORMAL ATTACK
   * This method is called when the mummy attacks another undead.
   * The mummy's attack damage is equal to the half of its HP plus 10% of the target undead HP.
   * 
   * @param target  The target of the attack.
   * @return        The amount of damage dealt to the target.
   */
  @Override
  public int normalAttack(Commandable target) {
    // Mummy's attack damage is equal to the half of its HP plus 10% of the target undead HP.
    return target.receiveDamage(super.getHp() / 2 + (int) (((Undead)target).getHp() * 0.1));
  }

  @Override
  public int skill2(Commandable target) {
    if (((Undead)target).getName().contains(" (Mummy)")) { // Mummy does not eat its own kind.
      return -444; // Error code
    } else {
      return super.skill2(target); // Mummy eats target
    }
  }

  // Custom methods:

  /**
   * Revives the mummy, but only once per game.
   * 
   * @return  The amount of HP received by the mummy.
   */
  public int revive() {
    if (diedOnce && super.getHp() == 0) {
      return -super.receiveDamage(-MAX_HP);
    } 

    return -444; // Error code
  }
}