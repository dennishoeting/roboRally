package prototyp.shared.util;

import java.io.Serializable;

/**
 * Position eines Roboters
 * 
 * @author Marcus
 * @verison 1.0
 */
public class Position implements Serializable {

	/**
	 * Seriennummer
	 */
	private static final long serialVersionUID = -5658244097253493097L;

	/** die X-Koordinate */
	private int i;

	/** die Y-Koordinate */
	private int j;

	public Position() {
	}

	/**
	 * Konstruktor zum Erzeugen einer Position
	 * 
	 * @param x
	 * @param y
	 */
	public Position(final int i, final int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + i;
		result = prime * result + j;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Position) {
			final Position p = (Position) obj;
			return this.i == p.i && this.j == p.j;
		}
		return false;
	}
	

	/**
	 * Liefert die X-Koordinate einer Position
	 * 
	 * @return die X-Koordinate
	 */
	public final int getI() {
		return this.i;
	}

	/**
	 * Liefert die Y-Koordinate einer Position
	 * 
	 * @return die Y-Koordinate
	 */
	public final int getJ() {
		return this.j;
	}

	public void setI(int i) {
		this.i = i;
	}

	public void setJ(int j) {
		this.j = j;
	}

	/**
	 * Überschreiben der toString() Methode
	 */
	@Override
	public String toString() {
		return this.i + ", " + this.j;
	}
}
