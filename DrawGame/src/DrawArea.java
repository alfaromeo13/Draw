import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import com.rabbitmq.client.Channel;

@SuppressWarnings("serial")
public class DrawArea extends JComponent implements Serializable {
	private Image image;// Slika u koju cemo da crtamo
	private Graphics2D g2;// objekat na koji se crta
	private int dugmeId;// svako dugme boje ima svoj id
	private int debljinaLinije;
	private Tacka tacka;
	private Stanje stanje;
	private Channel channel;

	MouseListener a1 = new MouseAdapter() {
		// cuvamo kordinate x i y kad je miš pritisnut
		public void mousePressed(MouseEvent e) {
			tacka.addT(e.getX(), e.getY());
		}

		public void mouseReleased(MouseEvent e) {
			tacka.addT(-1, -1);
			new Posiljaoc(tacka, stanje.getRoomName(), channel).start();
		}
	};

	MouseMotionAdapter a2 = new MouseMotionAdapter() {
		public void mouseDragged(MouseEvent e) {
			int prosloX = tacka.getX1();
			int prosloY = tacka.getY1();
			tacka.addT(e.getX(), e.getY());
			if (g2 != null) {
				tacka.setDugmeId(dugmeId);
				tacka.setDebljinaLinije(debljinaLinije);
				new Posiljaoc(tacka, stanje.getRoomName(), channel).start();
				g2.drawLine(prosloX, prosloY, tacka.getX1(), tacka.getY1());
				repaint();
				tacka.addT(e.getX(), e.getY());
			}
		}
	};

	public DrawArea(Stanje stanje, SwingPaint sp, Channel ch) {
		this.stanje = stanje;
		this.channel = ch;
		dugmeId = 0;
		tacka = new Tacka();
		setDoubleBuffered(false);
		addMouseListener(a1);
		addMouseMotionListener(a2);
	}

	/*
	 * metoda koja se poziva "kada treba da se pozove. There are a number of factors
	 * that determine when a component needs to be re-painted, ranging from moving,
	 * re-sizing, changing focus, being hidden by other frames, and so on and so
	 * forth. Many of these events are detected auto-magically, and paintComponent
	 * is called internally when it is determined that that operation is necessary.
	 * Ova funkcija sluzi da se crta slika na specificnoj lokaciji.Bez ove f-je
	 * crtanje ne bi bilo moguce jer je ovo funkcija koji CRTA SVE.Objekat image je
	 * ta slika po kojoj crtamo.0,0 predstavlja gornji lijevi ugao te slike. Ovo
	 * null je parametar koji nije bitan nesto tamo za javu izignorisi
	 */
	public void paintComponent(Graphics g) {// poziva se na svako crtanje :)
		if (image == null) {// ako nema slike po kojoj pisemo kreiramo je
			image = createImage(getSize().width, getSize().height);
			g2 = (Graphics2D) image.getGraphics();
			g2.setStroke(new BasicStroke(3));// debljina linije
			debljinaLinije = 3;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// bez ove linije koda sve linije koje crtamo ne bi bile "pune"
			clear();
		}
		g.drawImage(image, 0, 0, null);
	}

	public void small() {
		g2.setStroke(new BasicStroke(3));
		debljinaLinije = 3;
	}

	public void medium() {
		g2.setStroke(new BasicStroke(6));
		debljinaLinije = 6;
	}

	public void large() {
		g2.setStroke(new BasicStroke(9));
		debljinaLinije = 9;
	}

	public void clear() {
		g2.setPaint(Color.white);
		// crtamo bijeli pravougaonik po cijeloj povrsini da ocistimo polje za crtanje
		g2.fillRect(0, 0, getSize().width, getSize().height);
		tacka.addT(-5, -5);
		tacka.setDugmeId(0);
		tacka.setDebljinaLinije(100000);
		new Posiljaoc(tacka, stanje.getRoomName(), channel).start();
		repaint();
		g2.setPaint(Color.black);
		dugmeId = 0;
		g2.setStroke(new BasicStroke(3));
		debljinaLinije = 3;
	}

	public void red() {
		dugmeId = 3;
		g2.setPaint(Color.red);
	}

	public void black() {
		dugmeId = 0;
		g2.setPaint(Color.black);
	}

	public void white() {
		dugmeId = 10;
		g2.setPaint(Color.white);
	}

	public void magenta() {
		dugmeId = 4;
		g2.setPaint(Color.magenta);
	}

	public void green() {
		dugmeId = 2;
		g2.setPaint(Color.green);
	}

	public void blue() {
		dugmeId = 1;
		g2.setPaint(Color.blue);
	}

	public void yellow() {
		dugmeId = 5;
		g2.setPaint(Color.yellow);
	}

	public void brown() {
		dugmeId = 6;
		g2.setPaint(Color.decode("#8b4513"));
	}

	public void gray() {
		dugmeId = 7;
		g2.setPaint(Color.gray);
	}

	public void orange() {
		dugmeId = 8;
		g2.setPaint(Color.orange);
	}

	public void pink() {
		dugmeId = 9;
		g2.setPaint(Color.pink);
	}

	public MouseListener getA1() {
		return a1;
	}

	public MouseMotionAdapter getA2() {
		return a2;
	}

	public Graphics2D getG2() {
		return g2;
	}

	public void setG2(Graphics2D g2) {
		this.g2 = g2;
	}

	public Image getImage() {
		return image;
	}

	public int getDebljinaLinije() {
		return debljinaLinije;
	}

	public void setDebljinaLinije(int debljinaLinije) {
		this.debljinaLinije = debljinaLinije;
	}

	public int getDugmeId() {
		return dugmeId;
	}

	public void setDugmeId(int dugmeId) {
		this.dugmeId = dugmeId;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}