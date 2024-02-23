import java.io.Serializable;

@SuppressWarnings("serial")
public class Tacka implements Serializable {
	private int x1, y1;
	private int dugmeId;
	private int debljinaLinije;

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public void addT(int x1, int y1) {
		this.x1 = x1;
		this.y1 = y1;
	}

	public int getDugmeId() {
		return dugmeId;
	}

	public void setDugmeId(int dugmeId) {
		this.dugmeId = dugmeId;
	}

	public int getDebljinaLinije() {
		return debljinaLinije;
	}

	public void setDebljinaLinije(int debljinaLinije) {
		this.debljinaLinije = debljinaLinije;
	}

	public Tacka() {

	}

	public Tacka(int x1, int y1, int dugmeId, int debljinaLinije) {
		this.x1 = x1;
		this.y1 = y1;
		this.dugmeId = dugmeId;
		this.debljinaLinije = debljinaLinije;
	}
}