package undeadgame.creatures;

// Vampire: A vampire as an undead, inherits all the common characteristics of the undead class. Vampire can bite which increases their HP by 80% of the undead HP being bitten. Vampire could attack other undead. Its attack damage is same as its HP. If its HP is reduced to 0, vampire will not die but it cannot attack anymore. When vampires are created, they possess a starting HP of 120.

public class Vampire extends Undead {
  // Constructor:
  public Vampire(String name) {
    super(name + " (Vampire)", 120);
    super.setType(Type.VAMPIRE);
    super.setMaxHP(120);
  }

  @Override
  public void setName(String name) {
    super.setName(name + " (Vampire)");
  }

  @Override
  public int attack(Undead target) {
    return target.receiveDamage(super.getHP()); // Vampire's attack damage is equal to its HP.
  }

  @Override
  public void update() {
    if (super.getHP() <= 0) { // If the vampire's HP is 0 or less, it cannot attack anymore.
      super.setHP(0);
      super.canAttack(false);
    } else { 
      super.canAttack(true);
    }

    if (super.getHP() > super.getMaxHP()) { // Limit the vampire's HP to its max HP.
      super.setHP(super.getMaxHP());
    }
  }

  // Custom methods:

  /**
   * This method is called when the vampire bites another undead. The vampire
   * will receive 80% of the HP of the undead being bitten. The vampire's HP
   * will be increased by the amount of HP received.
   * 
   * @param  target  The undead being bitten.
   * @return         The amount of HP received by the Vampire.
   */
  public int bite(Undead target) {
    int heal = (int) (target.getHP() * 0.8);
    return -this.receiveDamage(-heal);
  }
}
