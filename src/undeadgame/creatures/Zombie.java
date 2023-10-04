package undeadgame.creatures;

/**
 * Zombie is a subclass of Undead. As an undead, it inherits all the common
 * characteristics of the undead class. Zombie can eat another undead as a
 * result it will increase its HP by the half of the HP of the undead being
 * eaten. Zombie may attack other undead. Its attack damage is half of its HP.
 * Zombie could only attack if its HP is greater than 50. If the zombie’s HP is
 * reduced to 0, it will die. On creation, zombie has the default HP of the
 * undead.
 */
public class Zombie extends Undead implements Commandable {
  public static final int MAX_HP = 100;
  
  private boolean isMummy;
  private boolean canAttack;

  private static final String[] skills = { "NORMAL ATTACK", "EAT" }; // Zombie's skills.
  private static final String[] skillDesc = { "Attack your target by infecting them! (Damage: 50% of your HP)", "Feast on the brains of your target and gain 50% of their HP!" }; // Zombie's skill descriptions.

  // Constructor:
  public Zombie(String name) {
    super(name + " (Zombie)", MAX_HP);
    this.isMummy = false;
  }

  // Setters:
  public void isMummy(boolean isMummy) {
    this.isMummy = isMummy;
  }

  @Override
  public void setName(String name) {
    super.setName(name + " (Zombie)");
  }

  public void canAttack(boolean canAttack) {
    this.canAttack = canAttack;
  }

  // Getters:
  public boolean isItMummy() {
    return this.isMummy;
  }

  public boolean canItAttack() {
    return this.canAttack;
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

  // Custom methods:

  @Override
  public void update() {
    if (super.getHp() > 50) { // If the zombie's HP is less than or equal to 50, it can't attack.
      this.canAttack(true);
    } else {
      this.canAttack(false);
    }

    // Cap the zombie's HP to its max HP.
    Math.min(super.getHp(), MAX_HP);

    if (super.getHp() <= 0) { // If the zombie's HP is less than or equal to 0, it dies.
      super.setHp(0);
      super.isDead(true);
    }
  }

  @Override
  public String getHPString() {
    return super.getHp() + " / " + MAX_HP;
  }

  @Override
  public int receiveDamage(int damage) {
    super.setHp(super.getHp() - damage);

    this.update();

    return damage; // Zombie receives damage normally.
  }

  /**
   * NORMAL ATTACK
   * This method is called when the zombie attacks another undead. The zombie
   * will attack by infecting the target. The target will receive damage equal to
   * half of the zombie’s HP.
   * 
   * @param  target  The undead being attacked.
   * @return         The amount of damage received by the target.
   */
  @Override
  public int normalAttack(Commandable target) {
    return target.receiveDamage(super.getHp() / 2); // Zombie's attack damage is half of its HP.
  }

  /**
   * SKILL 1: EAT
   * This method is called when the zombie eats another undead. The zombie will
   * receive half of the HP of the undead being eaten. The zombie's HP will be
   * increased by the amount of HP received.
   * 
   * @param  target  The undead being eaten.
   * @return         The amount of HP received by the Zombie.
   */
  @Override
  public int skill1(Commandable target) {
    int heal = ((Undead)target).getHp() / 2;
    return -this.receiveDamage(-heal);
  }

  @Override
  public int skill2(Commandable u) {
    // Throw an error indicating that the zombie has no skill 2.
    throw new UnsupportedOperationException(super.getName() + " has no skill 2!");
  }
}