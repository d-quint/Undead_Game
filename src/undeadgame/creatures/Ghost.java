package undeadgame.creatures;

/**
 * This class is used to represent a Ghost. Ghosts are like virtual version of an
 * undead. It inherits all the characteristics that the undead has. Ghost may
 * attack other undead. Its attack damage is only 20% of its HP. Ghost only
 * receives 10% of the damage being done to it. If ghost HP is reduced to 0, it
 * will be perished. Ghost initial HP would be the half of the initial HP of the
 * undead. Ghost can haunt which increases its HP by the 10% of the undead being
 * haunt.
 */
public class Ghost extends Undead implements Commandable {
  public static final int MAX_HP = 50; // Ghost's max HP is 50.

  private static final String[] skills = {"NORMAL ATTACK", "HAUNT"}; // Ghost's skills.
  private static final String[] skillDesc = {
    "Attack your enemy with your ghostly powers! (Damage: 20% of your HP)",
    "Heal yourself by 10% of your target's HP by haunting them!"
  }; // Ghost's skill descriptions.

  // Constructor:
  public Ghost(String name) {
    super(name + " (Ghost)", MAX_HP);
  }

  @Override
  public void setName(String name) {
    super.setName(name + " (Ghost)");
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

  
  // Custom methods (Overridden methods from Commandable interface):
  @Override
  public String getHPString() {
    return super.getHp() + "/" + MAX_HP;
  }

  @Override
  public int receiveDamage(int damage) {
    damage = (int)(damage * 0.1); // Ghost only receives 10% of the damage being done to it.
    super.setHp(super.getHp() - damage); // Reduce the ghost's HP by the damage received.

    this.update();

    return damage;
  }

  @Override
  public void update() {
    int cappedHp = Math.min(super.getHp(), MAX_HP); // Cap the ghost's HP to the max HP.
    super.setHp(cappedHp);

    if (super.getHp() <= 0) { // If ghost HP is reduced to 0, it will be perished.
      super.setHp(0);
      super.isDead(true);
    }
  }

  /**
   * NORMAL ATTACK
   * This method is called when the ghost attacks another undead. The ghost will
   * deal damage to the target. The damage is 20% of the ghost's HP.
   * 
   * @param  target  The undead being attacked.
   * @return         The amount of damage done to the target.
   */
  @Override
  public int normalAttack(Commandable target) {
    return target.receiveDamage((int)(super.getHp() * 0.2)); // Ghost's attack damage is 20% of its HP.
  }

  /**
   * SKILL 1: HAUNT
   * This method is called when the ghost haunts another undead. The ghost will
   * receive 10% of the HP of the undead being haunted. The ghost's HP will be
   * increased by the amount of HP received.
   * 
   * @param  target  The undead being haunted.
   * @return         The amount of HP received by the ghost.
   */
  @Override
  public int skill1(Commandable target) {
    int heal = (int) (((Undead)target).getHp() * 0.1);
    super.setHp(super.getHp() + heal);
    return heal;
  }

  @Override
  public int skill2(Commandable u) {
    // Throw an error indicating that the ghost has no skill 2.
    throw new UnsupportedOperationException(super.getName() + " has no skill 2!");
  }
}
