import java.io.*;
import java.util.*;

@SuppressWarnings("serial")
public class Stanje implements Serializable {
	private String roomName;
	private int vrTajmera;
	private int id_trenutnogIgraca;// id onoga koji crta
	private String izabranaRijec = "";
	private LinkedList<String> poruke;
	private HashMap<Integer, Igrac> igraci;

	public int getId_trenutnogIgraca() {
		return id_trenutnogIgraca;
	}

	public void setId_trenutnogIgraca(int id_trenutnogIgraca) {
		this.id_trenutnogIgraca = id_trenutnogIgraca;
	}

	public String getIzabranaRijec() {
		return izabranaRijec;
	}

	public void setIzabranaRijec(String izabranaRijec) {
		this.izabranaRijec = izabranaRijec;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setIgraci(HashMap<Integer, Igrac> igraci) {
		this.igraci = igraci;
	}

	public HashMap<Integer, Igrac> getIgraci() {
		return igraci;
	}

	public int getVrTajmera() {
		return vrTajmera;
	}

	public void setVrTajmera(int vrTajmera) {
		this.vrTajmera = vrTajmera;
	}

	public LinkedList<String> getPoruke() {
		return poruke;
	}

	public void setPoruke(LinkedList<String> poruke) {
		this.poruke = poruke;
	}

	public Stanje() {
		igraci = new HashMap<>();
		poruke = new LinkedList<>();
	}
}