package undeadgame.creatures;

/**
 * This class represents a Vampire. A vampire inherits all the common
 * characteristics of the undead class. Vampire can bite which increases their
 * HP by 80% of the undead HP being bitten. Vampire could attack other undead.
 * Its attack damage is same as its HP. If its HP is reduced to 0, vampire will
 * not die but it cannot attack anymore. When vampires are created, they possess
 * a starting HP of 120.
 */
public class Vampire extends Undead implements Commandable {
  public static final int MAX_HP = 120;

  private boolean canAttack;

  public static final String[] skills = { "NORMAL ATTACK", "BITE" } ; // Vampire's skills.
  public static final String[] skillDesc = { "Attack your enemy with your sharp claws! (Damage: 100% of your HP)", "Take a sip of your target's blood, and gain 80% of their HP!" }; // Vampire's skill descriptions.

  // Constructor:
  public Vampire(String name) {
    super(name + " (Vampire)", MAX_HP);
    this.canAttack = true;
  }

  // Setters
  @Override
  public void setName(String name) {
    super.setName(name + " (Vampire)");
  }

  public void canAttack(boolean canAttack) {
    this.canAttack = canAttack;
  }

  // Getters
  public boolean canItAttack() {
    return this.canAttack;
  }

  // Custom methods:

  @Override
  public String getHPString() {
    return super.getHp() + "/" + MAX_HP;
  }

  @Override
  public void update() {
    if (super.getHp() <= 0) { // If the vampire's HP is 0 or less, it cannot attack anymore.
      super.setHp(0);
      this.canAttack(false);
    } else { 
      this.canAttack(true);
    }

    // Limit the HP of the vampire to the maximum HP.
    int cappedHP = Math.min(super.getHp(), MAX_HP);
    super.setHp(cappedHP);
  }

  @Override
  public int receiveDamage(int damage) {
    super.setHp(super.getHp() - damage);

    this.update();

    return damage; // Vampire receives damage normally.
  }

  /**
   * This method is called when the vampire attacks another undead. The vampire
   * will deal damage equal to its HP to the target.
   * 
   * @param  target  The undead being attacked.
   * @return         The amount of damage dealt to the target.
   */
  @Override
  public int normalAttack(Commandable target) {
    return target.receiveDamage(super.getHp()); // Vampire's attack damage is equal to its HP.
  }

  // Custom methods:

  /**
   * SKILL 1: BITE
   * This method is called when the vampire bites another undead. The vampire
   * will receive 80% of the HP of the undead being bitten. The vampire's HP
   * will be increased by the amount of HP received.
   * 
   * @param  target  The undead being bitten.
   * @return         The amount of HP received by the Vampire.
   */
  @Override
  public int skill1(Commandable target) {
    int heal = (int) (((Undead)target).getHp() * 0.8);
    return -this.receiveDamage(-heal);
  }
}