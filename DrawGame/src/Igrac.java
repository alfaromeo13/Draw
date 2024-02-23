import java.io.Serializable;

@SuppressWarnings("serial")
public class Igrac implements Serializable {
	private int id;
	private String username;
	private int brPoena;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getBrPoena() {
		return brPoena;
	}

	public void setBrPoena(int brPoena) {
		this.brPoena = brPoena;
	}

	public Igrac(int id, String username, int brPoena) {
		this.id = id;
		this.username = username;
		this.brPoena = brPoena;
	}
}