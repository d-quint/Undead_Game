package undeadgame.creatures;

// Zombie: As an undead, it inherits all the common characteristics of the undead class. Zombie can eat another undead as a result it will increase its HP by the half of the HP of the undead being eaten. Zombie may attack other undead. Its attack damage is half of its HP. Zombie could only attack if its HP is greater than 50. If the zombie’s HP is reduced to 0, it will die. On creation, zombie has the default HP of the undead.

public class Zombie extends Undead {
  public enum ZombieType {
    NORMAL,
    MUMMY
  }

  private ZombieType zombieType;

  // Constructor:
  public Zombie(String name) {
    super(name + " (Zombie)", 100);
    super.setType(Type.ZOMBIE);
    super.setMaxHP(100);
    this.setZombieType(ZombieType.NORMAL);
  }

  // Getters:
  public ZombieType getZombieType() {
    return this.zombieType;
  }

  // Setters:
  public void setZombieType(ZombieType type) {
    this.zombieType = type;
  }

  @Override
  public void setName(String name) {
    super.setName(name + " (Zombie)");
  }

  @Override
  public int attack(Undead target) {
    return target.receiveDamage(super.getHP() / 2); // Zombie's attack damage is half of its HP.
  }

  @Override
  public void update() {
    if (super.getHP() > 50) { // If the zombie's HP is less than or equal to 50, it can't attack.
      super.canAttack(true);
    } else {
      super.canAttack(false);
    }

    if (super.getHP() > super.getMaxHP()) { // Limit the zombie's HP to its max HP.
      super.setHP(super.getMaxHP());
    }

    super.update();
  }

  // Custom methods:

  /**
   * This method is called when the zombie eats another undead. The zombie will
   * receive half of the HP of the undead being eaten. The zombie's HP will be
   * increased by the amount of HP received.
   * 
   * @param  target  The undead being eaten.
   * @return         The amount of HP received by the Zombie.
   */
  public int eat(Undead target) {
    int heal = target.getHP() / 2;
    return -this.receiveDamage(-heal);
  }
}