package undeadgame.creatures;

/**
 * This interface is used to implement all additional actions an Undead can do.
 * This takes away the frustration of having to check an Undead's type before using its methods.
 * It also preserves the original content of the Undead class.
 */
interface Commandable {
  /**
   * This method is used to return a String representation of the Undead's HP.
   * 
   * @return The String representation of the Undead's HP.
   */
  public String getHPString();

  /**
   * This method is used to check if the Undead is dead or immobilized.
   * This is called every time damage (or a heal) is received.
   */
  public void update();

  /**
   * This method is used to receive damage from an attack.
   * 
   * @param damage The amount of damage to receive.
   * @return The amount of damage received.
   */
  public int receiveDamage(int damage);

  /**
   * This method is used to attack another Undead. This is overridden based on their attack strengths.
   * 
   * @param u The Commandable Undead to attack.
   * @return The amount of damage dealt.
   */
  public int normalAttack(Commandable u);

  /**
   * This method is used to activate an undead's first skill. (Often a heal)
   *
   * @param u The target Commandable Undead.
   * @return The amount of damage dealt or HP healed.  
   */
  public int skill1(Commandable u);

  /**
   * This method is used to activate an undead's second skill. (Often a special one)
   * 
   * @param u The target Commandable Undead.
   * @return The amount of damage dealt or HP healed.
   */
  public int skill2(Commandable u);
}
