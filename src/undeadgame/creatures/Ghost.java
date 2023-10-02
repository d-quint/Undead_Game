package undeadgame.creatures;

// // Ghost: Ghost are like virtual version of an undead. It inherits all the characteristics that the undead has. Ghost may attack other undead. Its attack damage is only 20% of its HP. Ghost only receives 10% of the damage being done to it. If ghost HP is reduced to 0, it will be perished. Ghost initial HP would be the half of the initial HP of the undead. Ghost can haunt which increases its HP by the 10% of the undead being haunt.

public class Ghost extends Undead {
  // Constructor:
  public Ghost(String name) {
    super(name + " (Ghost)", 50);
    super.setType(Type.GHOST);
    super.setMaxHP(50);
  }

  @Override
  public void setName(String name) {
    super.setName(name + " (Ghost)");
  }

  @Override
  public int attack(Undead target) {
    return target.receiveDamage((int)(super.getHP() * 0.2)); // Ghost's attack damage is 20% of its HP.
  }

  @Override
  public int receiveDamage(int damage) {
    return super.receiveDamage((int) (damage * 0.1)); // Ghost only receives 10% of the damage being done to it.
  }

  @Override
  public void update() {
    if (super.getHP() > super.getMaxHP()) { // Limit the ghost's HP to its max HP.
      super.setHP(super.getMaxHP());
    }

    super.update();
  }

  // Custom methods:

  /**
   * This method is called when the ghost haunts another undead. The ghost will
   * receive 10% of the HP of the undead being haunted. The ghost's HP will be
   * increased by the amount of HP received.
   * 
   * @param  target  The undead being haunted.
   * @return         The amount of HP received by the ghost.
   */
  public int haunt(Undead target) {
    int heal = (int) (target.getHP() * 0.1);
    return -super.receiveDamage(-heal);
  }
}
