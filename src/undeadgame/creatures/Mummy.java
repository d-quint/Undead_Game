package undeadgame.creatures;

// Mummy: Mummy is an undead like zombie, but it does not eat its own kind. Mummy can attack other undead; its attack damage is equal to the half of its HP plus 10% of the undead HP. If its HP reached 0, it will die and needs to be revive again. When revived it will have its initial HP again.

public class Mummy extends Zombie {
  public boolean diedOnce;

  // Constructor:
  public Mummy(String name) {
    super(name);
    super.setZombieType(ZombieType.MUMMY);
    diedOnce = false;
  }

  // Setters:

  @Override
  public void setName(String name) {
    super.setName(name + " (Mummy)");
  }

  @Override
  public int attack(Undead target) {
    // Mummy's attack damage is equal to the half of its HP plus 10% of the target undead HP.
    return target.receiveDamage(super.getHP() / 2 + (int) (target.getHP() * 0.1));
  }

  @Override
  public void update() {
    if (super.getHP() > super.getMaxHP()) { // Limit the mummy's HP to its max HP.
      super.setHP(super.getMaxHP());
    }

    if (super.getHP() <= 0) { // If the mummy's HP is 0 or less, it cannot attack anymore.
      super.setHP(0);
      super.canAttack(false);

      if (!diedOnce) { // If the mummy died once, it can be revived.
        diedOnce = true;
        return;
      }
    } else {
      super.canAttack(true);
    }

    super.update();
  }

  @Override
  public int eat(Undead target) {
    if (target.getType() == Type.ZOMBIE) { // Mummy does not eat its own kind.
      return -444;
    } else {
      return super.eat(target);
    }
  }

  // Custom methods:

  /**
   * Revives the mummy, but only once per game.
   * 
   * @return  The amount of HP received by the mummy.
   */
  public int revive() {
    if (diedOnce && super.getHP() == 0) {
      return -super.receiveDamage(-super.getMaxHP());
    } 

    return -444;
  }
}