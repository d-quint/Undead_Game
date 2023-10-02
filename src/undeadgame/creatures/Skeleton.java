package undeadgame.creatures;

// Skeleton: Skeleton as an undead, receives all the similar characteristics of an undead. Skeleton may attack other undead. Its attack damage is 70% of its HP. If skeleton HP is reducing to 0, same with the zombie, it will die. Skeleton has an 80 HP.

public class Skeleton extends Undead {
  public enum SkeletonType {
    NORMAL,
    LICH
  }

  private SkeletonType skeletonType;

  // Constructor:
  public Skeleton(String name) {
    super(name + " (Skeleton)", 80);
    super.setType(Type.SKELETON);
    super.setMaxHP(80);
    this.setSkeletonType(SkeletonType.NORMAL);
  }

  // Setters:
  public void setSkeletonType(SkeletonType type) {
    this.skeletonType = type;
  }

  @Override
  public void setName(String name) {
    super.setName(name + " (Skeleton)");
  }

  // Getters:
  public SkeletonType getSkeletonType() {
    return this.skeletonType;
  }

  @Override
  public int attack(Undead target) {
    return target.receiveDamage((int)(super.getHP() * 0.7)); // Skeleton's attack damage is 70% of its HP.
  }

  @Override
  public void update() {
    if (super.getHP() > super.getMaxHP()) { // Limit the skeleton's HP to its max HP.
      super.setHP(super.getMaxHP());
    }

    super.update();
  }
}
