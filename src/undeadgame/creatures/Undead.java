package undeadgame.creatures;

public class Undead {
	// attributes
	private String name;
	private int hp;
	private boolean isDead;

	// default constructor
	public Undead() {
		hp = 100;
		name = "Undead";
		isDead = false;
	}

	public Undead(String name, int hp) {
		this.hp  = hp;
		this.name = name;
		isDead = false;
	}

	// setter & getter
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getHp() {
		return hp;
	}

	public void isDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isDead() {
		return isDead;
	}

	// custom method
	public void attack(Undead u) {
		u.setHp(u.getHp() - hp);
	}
}