package undeadgame.creatures;

/**
 * Lich is a kind of undead like skeleton, but it has reach immortality. Lich has
 * another ability. It could cast a spell on undead which gets the 10% of their
 * HP and add it to its HP. Lich attack damage is equal to 70 percent of its Hp.
 * If Lich HP is reduced to 0, it cannot attack anymore but still alive.
 */
public class Lich extends Skeleton { 
  private boolean canAttack;

  public static final String[] skills = { "NORMAL ATTACK", "CAST SPELL" }; // Lich's skills.
  public static final String[] skillDesc = { "Attack your enemy with your skeletal bow! (Damage: 70% of your HP)", "Cast a spell on your enemy and heal yourself by taking 10% of their HP!" }; // Lich's skill descriptions.

  // Constructor:
  public Lich(String name) {
    super(name + " (Lich)");
    super.setLich(true);
    this.canAttack = true;
  }

  // Setters:
  @Override
  public void setName(String name) {
    super.setName(name + " (Lich)");
  }

  public void canAttack(boolean canAttack) {
    this.canAttack = canAttack;
  }

  // Getter:
  public boolean canItAttack() {
    return this.canAttack;
  }
  
  // Custom methods:

  @Override
  public void update() {
    if (super.getHp() <= 0) { // If the Lich's HP is 0 or less, it cannot attack anymore.
      super.setHp(0);
      this.canAttack(false);
    } else { 
      this.canAttack(true);
    }
  }

  /**
   * SKILL 1: CAST SPELL
   * This method is called when the Lich casts a spell on another undead. The Lich
   * will receive 10% of the HP of the undead being casted. The Lich will gain HP.
   * 
   * @param  target  The commandable undead being casted a spell.
   * @return         The amount of HP received by the Lich.
   */
  @Override
  public int skill1(Commandable target) {
    int heal = (int) (((Undead)target).getHp() * 0.1);
    return -super.receiveDamage(-heal);
  }
}
