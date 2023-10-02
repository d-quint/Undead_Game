package undeadgame.creatures;

public class Undead {
  public enum Type {
    ZOMBIE,
    VAMPIRE,
    SKELETON,
    GHOST,
    NORMAL
  }
  
  private Type undeadType;

  // Attributes:
  private String name;
  private int hp = 100;
  private boolean isDead;
  private boolean canAttack;

  private int MAX_HP;

  // Constructor:
  public Undead() {
    undeadType = Type.NORMAL;
    
    this.hp = 100;
    this.name = "Undead - " + name;
    this.isDead = false;
    this.canAttack = true;

    this.MAX_HP = 100;
  }
  
  public Undead(String name, int hp) {
    undeadType = Type.NORMAL;
    
    this.hp = hp;
    this.name = "Undead " + name;
    this.isDead = false;
    this.canAttack = true;

    this.MAX_HP = hp;
  }

  // Setters:
  public void setType(Type type) {
    this.undeadType = type;
  }

  public void setName(String name) {
    this.name = "Undead " + name;
  }

  public void setHP(int hp) {
    this.hp = hp;
  }

  public void setMaxHP(int maxHP) {
    this.MAX_HP = maxHP;
  }

  public void isDead(boolean isDead) {
    this.isDead = isDead;
  }

  public void canAttack(boolean canAttack) {
    this.canAttack = canAttack;
  }

  // Getters:
  public Type getType() {
    return undeadType;
  }

  public String getName() {
    return name;
  }

  public int getHP() {
    return hp;
  }

  public int getMaxHP() {
    return MAX_HP;
  }

  public boolean isDead() {
    return isDead;
  }

  public boolean canAttack() {
    return canAttack;
  }

  // Custom methods:

  /**
   * This method is called when the undead attacks another undead. 
   * The undead, by default, will receive damage equal to its HP.
   * 
   * @param target
   * @return
   */
  public int attack(Undead target) {
    return target.receiveDamage(hp);
  }

  /**
   * This method is called when the undead receives damage from another undead.
   * The undead will receive damage equal to the damage parameter.
   * 
   * @param damage
   * @return
   */
  public int receiveDamage(int damage) {
    hp -= damage;
    update(); // Check if the undead is dead or immobilized.
    return damage;
  }

  /**
   * This method is called after every attack. If the HP of the undead is 0 or
   * less, it will die. Otherwise, it will not die.
   * 
   * Since not all undeads die at 0 HP, this method is overridden in the subclasses.
   */
  public void update() {
    if (hp <= 0) {
      hp = 0;
      isDead = true;
    } else {
      isDead = false;
    }
  }

  public String getHPstring() {
    return getHP() + "/" + getMaxHP();
  }
}
