package undeadgame.creatures;

/**
 * This class is used to represent a Skeleton. Skeletons are like virtual version of an
 * undead. It inherits all the characteristics that the undead has. Skeletons may
 * attack other undead. Its attack damage is 70% of its HP. If skeleton HP is reducing to 0,
 * same with the zombie, it will die. Skeleton has an 80 HP.
 */
public class Skeleton extends Undead implements Commandable {
  public static final int MAX_HP = 80;
  private boolean isLich;

  public static final String[] skills = { "NORMAL ATTACK" } ; // Skeleton's skills.
  public static final String[] skillDesc = { "Attack your enemy and rattle their bones! (Damage: 70% of your HP)" }; // Skeleton's skill descriptions.

  // Constructor:
  public Skeleton(String name) {
    super(name + " (Skeleton)", MAX_HP);
    this.isLich = false;
  }

  // Setters:
  public void setLich(boolean isLich) {
    this.isLich = isLich;
  }

  @Override
  public void setName(String name) {
    super.setName(name + " (Skeleton)");
  }

  // Getters:
  public boolean isLich() {
    return this.isLich;
  }

  @Override
  public String getHPString() {
    return this.getHp() + "/" + MAX_HP;
  }

  @Override
  public void update() {
    int cappedHP = Math.min(super.getHp(), MAX_HP); // Limit the skeleton's HP to its max HP.
    super.setHp(cappedHP);

    if (super.getHp() <= 0) { // If skeleton's HP is 0 or less, it dies.
      super.setHp(0);
      super.isDead(true);
    }
  }

  @Override
  public int receiveDamage(int damage) {
    super.setHp(super.getHp() - damage);

    this.update(); 

    return damage; // Skeleton receives damage normally.
  }

  /**
   * NORMAL ATTACK
   * This method is called when the skeleton attacks another undead. The skeleton will
   * deal damage to the target. The damage is 70% of the skeleton's HP.
   * 
   * @param target The undead being attacked.
   * @return The amount of damage done to the target.
   */
  @Override
  public int normalAttack(Commandable u) {
    return u.receiveDamage((int)(super.getHp() * 0.7));
  }

  @Override
  public int skill1(Commandable u) {
    // Throw an error indicating that the skeleton has no skill 1.
    throw new UnsupportedOperationException(super.getName() + " has no skill 1!");
  }
}