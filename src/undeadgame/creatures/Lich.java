package undeadgame.creatures;

// Lich: Lich is a kind of undead like skeleton, but it has reach immortality. Lich has another ability. It could cast a spell on undead which gets the 10% of their HP and add it to its HP. Lich attack damage is equal to 70 percent of its Hp. If Lich HP is reduced to 0, it cannot attack anymore but still alive.

public class Lich extends Skeleton { 
  // Constructor:
  public Lich(String name) {
    super(name);
    super.setName(name + " (Lich)");
    super.setSkeletonType(SkeletonType.LICH);
  }

  @Override
  public void update() {
    if (super.getHP() <= 0) { // If the Lich's HP is 0 or less, it cannot attack anymore.
      super.setHP(0);
      super.canAttack(false);
    } else { 
      super.canAttack(true);
    }
    
    if (super.getHP() > super.getMaxHP()) { // Limit the Lich's HP to its max HP.
      super.setHP(super.getMaxHP());
    }
  }

  // Setters:

  @Override
  public void setName(String name) {
    super.setName(name + " (Lich)");
  }
  
  // Custom methods:

  /**
   * This method is called when the Lich casts a spell on another undead. The Lich
   * will receive 10% of the HP of the undead being casted. The enemy will lose HP
   * in the process, while the Lich will gain HP.
   * 
   * @param  target  The undead being casted a spell.
   * @return         The amount of HP received by the Lich.
   */
  public int castSpell(Undead target) {
    int heal = target.receiveDamage((int)(target.getHP() * 0.1));
    return -super.receiveDamage(-heal);
  }
}