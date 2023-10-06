package undeadgame.creatures;

/**
 * This class is used to represent a Mummy. A mummy is an  undead like zombie, but it
 * does not eat its own kind. Mummy can attack other undead; its attack damage is equal to
 * the half of its HP plus 10% of the undead HP. If its HP reached 0, it will die and needs
 * to be revive again. When revived it will have its initial HP again.
 */
public class Mummy extends Zombie {
  public boolean diedOnce;

  public static final String[] skills = { "NORMAL ATTACK", "EAT", "REVIVE" } ; // Mummy's skills.
  public static final String[] skillDesc = { "Attack your target by wrapping them with cloth! (Damage: 50% of your HP + 10% of target's HP)", "Feast on the brains of your target and gain 50% of their HP, as long as they aren't of the same kind!", "By the power vested in you by the gods, revive yourself with 100% HP! (You can only revive once!)" }; // Mummy's skill descriptions.  

  // Constructor:
  public Mummy(String name) {
    super(name + " (Mummy)");
    super.isMummy(true);
    diedOnce = false;
  }

  // Setters:

  @Override
  public void setName(String name) {
    super.setName(name + " (Mummy)");
  }

  // Getters:

  public boolean diedOnce() {
    return diedOnce;
  }

  // Custom methods:

  @Override
  public void update() {
    if (super.getHp() == 0) {
      diedOnce = true;
    }

    super.update();
  }  

  /**
   * NORMAL ATTACK
   * This method is called when the mummy attacks another undead.
   * The mummy's attack damage is equal to the half of its HP plus 10% of the target undead HP.
   * 
   * @param target  The target of the attack.
   * @return        The amount of damage dealt to the target.
   */
  @Override
  public int normalAttack(Commandable target) {
    // Mummy's attack damage is equal to the half of its HP plus 10% of the target undead HP.
    return target.receiveDamage(super.getHp() / 2 + (int) (((Undead)target).getHp() * 0.1));
  }

  @Override
  public int skill1(Commandable target) {
    if (((Undead)target).getName().contains(" (Mummy)")) { // Mummy does not eat its own kind.
      return -444; // Error code
    } else {
      return super.skill1(target); // Mummy eats target
    }
  }

  /**
   * SPECIAL SKILL: REVIVE
   * Revives the mummy, but only once per game.
   * When a mummy dies for the first time, the user is prompted if they want to revive it.
   * 
   * @return  The amount of HP received by the mummy.
   */
  public int revive() {
    if (diedOnce && super.getHp() == 0) {
      return -super.receiveDamage(-MAX_HP);
    } 

    return -444; // Error code
  }
}