import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.UIManager;
import java.util.List;

public class Glavna {
	public static void main(String[] args) throws InterruptedException, FontFormatException, IOException {
		System.setProperty("sun.java2d.uiScale", "1.0");
		List<String> rijeci = new LinkedList<>();
		Scanner sc = new Scanner(new File("src/1-1000.txt"));
		while (sc.hasNextLine())
			rijeci.add(sc.nextLine());
		sc.close();
		new Pocetni(rijeci);
		UIManager.put("OptionPane.yesButtonText", "Da");
		UIManager.put("OptionPane.noButtonText", "Ne");
	}
}