import java.awt.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import com.rabbitmq.client.*;

public class SwingPaint extends JFrame {
	private Tajmer t;
	private JTextField unos;
	private String unosTekst;
	private DrawArea drawArea;
	private Stanje stanje;// stanje koje saljemo svim korisnicima
	private JScrollPane sp1, sp2;
	private JLabel rijec, sat, opis;
	private Igrac igrac;
	private List<String> rijeci;
	private JList<String> lista_poruka, lista_igraca;
	private JPanel panela, controls, p1, p2, p3;
	private DefaultListModel<String> model1, model2;
	private static final long serialVersionUID = 1L;
	private JButton cetkica, gumica, start, testno = new JButton();
	private JButton reset, crna, plava, zelena, crvena, malo, srednje, veliko;
	private JButton ljubicasta, zuta, kafena, siva, narandzasta, roza, bijela;
	private JButton dugmad[] = new JButton[14];
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;

	// dogadjaji za paletu
	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == reset) {
				drawArea.clear();
			} else if (e.getSource() == malo) {
				drawArea.small();
			} else if (e.getSource() == srednje) {
				drawArea.medium();
			} else if (e.getSource() == veliko) {
				drawArea.large();
			} else if (e.getSource() == crna) {
				drawArea.black();
				p1.setBackground(Color.black);
				p2.setBackground(Color.black);
				p3.setBackground(Color.black);
			} else if (e.getSource() == plava) {
				drawArea.blue();
				p1.setBackground(Color.blue);
				p2.setBackground(Color.blue);
				p3.setBackground(Color.blue);
			} else if (e.getSource() == zelena) {
				drawArea.green();
				p1.setBackground(Color.green);
				p2.setBackground(Color.green);
				p3.setBackground(Color.green);
			} else if (e.getSource() == crvena) {
				drawArea.red();
				p1.setBackground(Color.red);
				p2.setBackground(Color.red);
				p3.setBackground(Color.red);
			} else if (e.getSource() == ljubicasta) {
				drawArea.magenta();
				p1.setBackground(Color.magenta);
				p2.setBackground(Color.magenta);
				p3.setBackground(Color.magenta);
			} else if (e.getSource() == zuta) {
				drawArea.yellow();
				p1.setBackground(Color.yellow);
				p2.setBackground(Color.yellow);
				p3.setBackground(Color.yellow);
			} else if (e.getSource() == kafena) {
				drawArea.brown();
				p1.setBackground(kafena.getBackground());
				p2.setBackground(kafena.getBackground());
				p3.setBackground(kafena.getBackground());
			} else if (e.getSource() == siva) {
				drawArea.gray();
				p1.setBackground(Color.gray);
				p2.setBackground(Color.gray);
				p3.setBackground(Color.gray);
			} else if (e.getSource() == narandzasta) {
				drawArea.orange();
				p1.setBackground(Color.orange);
				p2.setBackground(Color.orange);
				p3.setBackground(Color.orange);
			} else if (e.getSource() == roza) {
				drawArea.pink();
				p1.setBackground(Color.pink);
				p2.setBackground(Color.pink);
				p3.setBackground(Color.pink);
			} else if (e.getSource() == bijela) {
				drawArea.white();
				p1.setBackground(Color.white);
				p2.setBackground(Color.white);
				p3.setBackground(Color.white);
			} else if (e.getSource() == gumica) {
				drawArea.white();
				cetkica.setBackground(Color.lightGray);
				gumica.setBackground(testno.getBackground());
			} else if (e.getSource() == cetkica) {
				drawArea.black();
				gumica.setBackground(Color.lightGray);
				cetkica.setBackground(testno.getBackground());
			}
		}
	};

	public void randomRijec() {
		String izabrana = rijeci.get(new Random().nextInt(rijeci.size()));
		stanje.setIzabranaRijec(izabrana);
		rijec.setText(izabrana);
		rijec.setBounds(625 - 11 * izabrana.length(), 30, 300, 70);
	}

	// dogadjaj za velicine
	MouseListener hover = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			if (e.getSource() == malo)
				malo.setContentAreaFilled(true);
			else if (e.getSource() == srednje)
				srednje.setContentAreaFilled(true);
			else
				veliko.setContentAreaFilled(true);
		}

		public void mouseExited(MouseEvent e) {
			malo.setContentAreaFilled(false);
			srednje.setContentAreaFilled(false);
			veliko.setContentAreaFilled(false);
		}
	};

// channel.exchangeDelete("hello"); ovo brise exchange  ....................

	@SuppressWarnings("unchecked")
	public SwingPaint(Igrac igrac) throws Exception {
		factory = CNF.cf();
		connection = factory.newConnection();
		channel = connection.createChannel();
		this.igrac = igrac;
		stanje = new Stanje();
		stanje.getIgraci().put(igrac.getId(), igrac);
		sat = new JLabel("60");
		rijec = new JLabel();
		rijec.setForeground(Color.black);
		opis = new JLabel("Igraci i poeni:");
		opis.setFont(new Font("Dyuthi", Font.BOLD, 25));
		opis.setForeground(Color.black);
		opis.setBounds(65, 30, 250, 40);
		File font_file = new File("src/font/RADIOLAND.TTF");
		Font font = Font.createFont(Font.TRUETYPE_FONT, font_file);
		Font sizedFont = font.deriveFont(44f);
		sat.setFont(sizedFont);
		sat.setForeground(Color.darkGray);
		sat.setBounds(250, 20, 100, 100);
		rijec.setFont(new Font("Dyuthi", Font.BOLD, 43));
		setTitle("Igra crtanja");
		panela = new JPanel(null);
		rijeci = new ArrayList<>();
		panela.setBackground(Color.lightGray);
		controls = new JPanel(new GridLayout(1, 1, 3, 3));
		controls.setBounds(375, 627, 500, 40);
		controls.setBackground(Color.lightGray);
		reset = new JButton();
		reset.addActionListener(actionListener);
		crna = new JButton();
		crna.setBackground(Color.black);
		crna.addActionListener(actionListener);
		plava = new JButton();
		plava.setBackground(Color.blue);
		plava.addActionListener(actionListener);
		zelena = new JButton();
		zelena.setBackground(Color.green);
		zelena.addActionListener(actionListener);
		crvena = new JButton();
		crvena.setBackground(Color.red);
		crvena.addActionListener(actionListener);
		ljubicasta = new JButton();
		ljubicasta.setBackground(Color.magenta);
		ljubicasta.addActionListener(actionListener);
		zuta = new JButton();
		zuta.setBackground(Color.yellow);
		zuta.addActionListener(actionListener);
		kafena = new JButton();
		kafena.setBackground(Color.decode("#8b4513"));
		kafena.addActionListener(actionListener);
		siva = new JButton();
		siva.setBackground(Color.gray);
		siva.addActionListener(actionListener);
		narandzasta = new JButton();
		narandzasta.setBackground(Color.orange);
		narandzasta.addActionListener(actionListener);
		roza = new JButton();
		roza.setBackground(Color.pink);
		roza.addActionListener(actionListener);
		bijela = new JButton();
		bijela.setBackground(Color.white);
		bijela.addActionListener(actionListener);
		cetkica = new JButton(new ImageIcon(("src/Images/paint-brush(1).png")));
		cetkica.setBorderPainted(false);
		cetkica.addActionListener(actionListener);
		cetkica.setBounds(260, 626, 41, 41);
		gumica = new JButton(new ImageIcon(("src/Images/alfa.png")));
		gumica.setBorderPainted(false);
		gumica.addActionListener(actionListener);
		gumica.setBackground(Color.lightGray);
		gumica.setBounds(317, 626, 41, 41);
		reset = new JButton(new ImageIcon(("src/Images/reset(1).png")));
		reset.setBorderPainted(false);
		reset.setBackground(Color.lightGray);
		reset.setBounds(949, 50, 51, 50);
		reset.addActionListener(actionListener);
		p1 = new JPanel(null);
		p2 = new JPanel(null);
		p3 = new JPanel(null);
		p1.setBackground(Color.black);
		malo = new JButton(new ImageIcon(("src/Images/mala.png")));
		malo.setBorderPainted(false);
		malo.setContentAreaFilled(false);
		malo.setOpaque(false);
		malo.setBounds(-1, 0, 19, 19);
		p2.setBackground(Color.black);
		srednje = new JButton(new ImageIcon(("src/Images/srednja.png")));
		srednje.setBorderPainted(false);
		srednje.setContentAreaFilled(false);
		srednje.setOpaque(false);
		srednje.setBounds(-1, -1, 26, 26);
		p3.setBackground(Color.black);
		veliko = new JButton(new ImageIcon(("src/Images/velika.png")));
		veliko.setBorderPainted(false);
		veliko.setContentAreaFilled(false);
		veliko.setOpaque(false);
		veliko.setBounds(0, 0, 37, 37);
		malo.addActionListener(actionListener);
		srednje.addActionListener(actionListener);
		veliko.addActionListener(actionListener);
		malo.addMouseListener(hover);
		srednje.addMouseListener(hover);
		veliko.addMouseListener(hover);
		p1.add(malo);
		p2.add(srednje);
		p3.add(veliko);
		p1.setBounds(890, 637, 18, 18);
		p2.setBounds(923, 632, 25, 25);
		p3.setBounds(964, 625, 36, 36);
		drawArea = new DrawArea(stanje, this, channel);
		drawArea.setBorder(BorderFactory.createLineBorder(Color.darkGray, 1));
		drawArea.setBounds(250, 100, 750, 500);
		lista_igraca = new JList<>();
		lista_igraca.setOpaque(false);
		lista_igraca.setForeground(Color.black);
		lista_igraca.setFont(new Font("Dyuthi", Font.BOLD, 15));
		lista_igraca.setCellRenderer(new MyCellRenderer("s1", 1, igrac.getId()));
		lista_igraca.setSelectionModel(new DisabledItemSelectionModel());
		model1 = new DefaultListModel<>();
		model1.addElement(igrac.getId() + "#" + igrac.getUsername() + " " + igrac.getBrPoena() + "p");
		lista_igraca.setModel(model1);
		sp1 = new JScrollPane(lista_igraca);
		sp1.setOpaque(false);
		sp1.getViewport().setOpaque(false);
		sp1.setBounds(13, 70, 221, 550);
		sp1.getVerticalScrollBar().setOpaque(false);
		sp1.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 1));
		sp1.setLayout(new ScrollPL());
		sp1.getVerticalScrollBar().setUI(new MyScrollBar());
		start = new JButton("START");
		start.setForeground(Color.yellow);
		start.setBackground(Color.darkGray);
		start.setFont(new Font("Dyuthi", Font.BOLD, 15));
		start.setBounds(67, 640, 100, 40);
		start.addActionListener(x -> {
			start.setVisible(false);
			try {
				startGame();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		lista_poruka = new JList<>();
		lista_poruka.setOpaque(false);
		lista_poruka.setBackground(Color.white);
		lista_poruka.setForeground(Color.black);
		lista_poruka.setBorder(BorderFactory.createLineBorder(Color.black));
		lista_poruka.setFont(new Font("Dyuthi", Font.BOLD, 15));
		lista_poruka.setCellRenderer(new MyCellRenderer("s2", 1, igrac.getId()));
		lista_poruka.setSelectionModel(new DisabledItemSelectionModel());
		model2 = new DefaultListModel<>();
		lista_poruka.setModel(model2);
		sp2 = new JScrollPane(lista_poruka);
		sp2.setOpaque(true);
		sp2.setBackground(Color.white);
		sp2.getViewport().setOpaque(false);
		sp2.setBounds(1015, 35, 221, 565);
		sp2.setBorder(BorderFactory.createLineBorder(Color.black));
		sp2.getVerticalScrollBar().setOpaque(false);
		sp2.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 1));
		sp2.setLayout(new ScrollPL());
		sp2.getVerticalScrollBar().setUI(new MyScrollBar());
		unos = new JTextField();
		unos.addActionListener(x -> {// sada salji stanje ostalim korisnicima
			if (unos.getText().length() > 0) {
				try {
					unosTekst = " " + igrac.getId() + "#" + igrac.getUsername() + ":" + unos.getText();
					if (stanje.getIzabranaRijec().equalsIgnoreCase(unos.getText())) {
						unosTekst = "<html><div style='margin:0 -3 0 -3; padding: 0 3 0 3; background:yellow;'><b><i>"
								+ igrac.getId() + " " + igrac.getUsername() + " je pogodio!</i></b></div></html>";
						unos.setEnabled(false);
					}
					stanje.getPoruke().add(unosTekst);
					new Posiljaoc(unosTekst, stanje.getRoomName(), channel).start();
					unos.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		unos.setFont(new Font("Dyuthi", Font.BOLD, 15));
		unos.setForeground(Color.darkGray);
		unos.setBorder(BorderFactory.createLineBorder(Color.black));
		unos.setBounds(1016, 600, 219, 25);
		controls.add(crna);
		controls.add(plava);
		controls.add(zelena);
		controls.add(crvena);
		controls.add(ljubicasta);
		controls.add(zuta);
		controls.add(kafena);
		controls.add(siva);
		controls.add(narandzasta);
		controls.add(roza);
		controls.add(bijela);
		dugmad[0] = crna;
		dugmad[1] = plava;
		dugmad[2] = zelena;
		dugmad[3] = crvena;
		dugmad[4] = ljubicasta;
		dugmad[5] = zuta;
		dugmad[6] = kafena;
		dugmad[7] = siva;
		dugmad[8] = narandzasta;
		dugmad[9] = roza;
		dugmad[10] = bijela;
		panela.add(drawArea);
		panela.add(controls);
		panela.add(cetkica);
		panela.add(gumica);
		panela.add(sat);
		panela.add(reset);
		panela.add(rijec);
		panela.add(p1);
		panela.add(p2);
		panela.add(p3);
		panela.add(sp1);
		panela.add(start);
		panela.add(sp2);
		panela.add(opis);
		panela.add(unos);
		setSize(1300, 750);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(panela);
		setResizable(false);
	}

	public void startGame() throws Exception {
		stanje.getPoruke().clear();
		model2.removeAllElements();
		randomRijec();
		t = new Tajmer(sat, this, 60);
		stanje.setVrTajmera(60);
		new Posiljaoc(stanje, stanje.getRoomName(), channel).start();
		t.execute();
	}

	public void izCrtacaUNecrtaca(int naredniId) throws IOException {
		stanje.setId_trenutnogIgraca(naredniId);
		model2.removeAllElements();
		stanje.getPoruke().clear();
		unos.setEnabled(true);
		stanje.setVrTajmera(0);
		stanje.setIzabranaRijec("");
		sat.setText("60");
		drawArea.removeMouseListener(drawArea.getA1());
		drawArea.removeMouseMotionListener(drawArea.getA2());
		getP1().setVisible(false);
		getP2().setVisible(false);
		getP3().setVisible(false);
		getCetkica().setEnabled(false);
		getGumica().setEnabled(false);
		getStart().setVisible(false);
		getReset().setEnabled(false);
		getCrna().setEnabled(false);
		getPlava().setEnabled(false);
		getZelena().setEnabled(false);
		getCrvena().setEnabled(false);
		getLjubicasta().setEnabled(false);
		getZuta().setEnabled(false);
		getKafena().setEnabled(false);
		getSiva().setEnabled(false);
		getNarandzasta().setEnabled(false);
		getRoza().setEnabled(false);
		getBijela().setEnabled(false);
	}

	public void izNeCrtacaUCrtaca(int naredniId) throws IOException {
		stanje.setId_trenutnogIgraca(naredniId);
		unos.setEnabled(false);
		stanje.setVrTajmera(0);
		stanje.setIzabranaRijec("");
		sat.setText("60");
		model2.removeAllElements();
		drawArea.setDugmeId(drawArea.getDugmeId());// setuj boju
		drawArea.setDebljinaLinije(drawArea.getDebljinaLinije());// setaj debljinu linije
		drawArea.addMouseListener(drawArea.getA1());
		drawArea.addMouseMotionListener(drawArea.getA2());
		getP1().setVisible(true);
		getP2().setVisible(true);
		getP3().setVisible(true);
		getCetkica().setEnabled(true);
		getGumica().setEnabled(true);
		getStart().setVisible(true);
		getReset().setEnabled(true);
		getCrna().setEnabled(true);
		getPlava().setEnabled(true);
		getZelena().setEnabled(true);
		getCrvena().setEnabled(true);
		getLjubicasta().setEnabled(true);
		getZuta().setEnabled(true);
		getKafena().setEnabled(true);
		getSiva().setEnabled(true);
		getNarandzasta().setEnabled(true);
		getRoza().setEnabled(true);
		getBijela().setEnabled(true);
	}

	public Tajmer getT() {
		return t;
	}

	public void setT(Tajmer t) {
		this.t = t;
	}

	public DrawArea getDrawArea() {
		return drawArea;
	}

	public void setDrawArea(DrawArea drawArea) {
		this.drawArea = drawArea;
	}

	public JList<String> getlista_igraca() {
		return lista_igraca;
	}

	public JLabel getRijec() {
		return rijec;
	}

	public void setRijec(JLabel rijec) {
		this.rijec = rijec;
	}

	public Stanje getStanje() {
		return stanje;
	}

	public void setStanje(Stanje stanje) {
		this.stanje = stanje;
	}

	public JButton getReset() {
		return reset;
	}

	public void setReset(JButton reset) {
		this.reset = reset;
	}

	public JButton getStart() {
		return start;
	}

	public void setStart(JButton start) {
		this.start = start;
	}

	public JTextField getUnos() {
		return unos;
	}

	public void setUnos(JTextField unos) {
		this.unos = unos;
	}

	public JButton getCetkica() {
		return cetkica;
	}

	public void setCetkica(JButton cetkica) {
		this.cetkica = cetkica;
	}

	public JButton getGumica() {
		return gumica;
	}

	public void setGumica(JButton gumica) {
		this.gumica = gumica;
	}

	public List<String> getRijeci() {
		return rijeci;
	}

	public void setRijeci(List<String> rijeci) {
		this.rijeci = rijeci;
	}

	public JPanel getControls() {
		return controls;
	}

	public void setControls(JPanel controls) {
		this.controls = controls;
	}

	public JPanel getP1() {
		return p1;
	}

	public void setP1(JPanel p1) {
		this.p1 = p1;
	}

	public JPanel getP2() {
		return p2;
	}

	public void setP2(JPanel p2) {
		this.p2 = p2;
	}

	public JPanel getP3() {
		return p3;
	}

	public void setP3(JPanel p3) {
		this.p3 = p3;
	}

	public void setlista_igraca(JList<String> lista_igraca) {
		this.lista_igraca = lista_igraca;
	}

	public DefaultListModel<String> getModel1() {
		return model1;
	}

	public void setModel1(DefaultListModel<String> model1) {
		this.model1 = model1;
	}

	public DefaultListModel<String> getModel2() {
		return model2;
	}

	public void setModel2(DefaultListModel<String> model2) {
		this.model2 = model2;
	}

	public JButton[] getDugmad() {
		return dugmad;
	}

	public void setDugmad(JButton[] dugmad) {
		this.dugmad = dugmad;
	}

	public Igrac getIgrac() {
		return igrac;
	}

	public void setIgrac(Igrac igrac) {
		this.igrac = igrac;
	}

	public JLabel getSat() {
		return sat;
	}

	public void setSat(JLabel sat) {
		this.sat = sat;
	}

	public JButton getCrna() {
		return crna;
	}

	public void setCrna(JButton crna) {
		this.crna = crna;
	}

	public JButton getPlava() {
		return plava;
	}

	public void setPlava(JButton plava) {
		this.plava = plava;
	}

	public JButton getZelena() {
		return zelena;
	}

	public void setZelena(JButton zelena) {
		this.zelena = zelena;
	}

	public JButton getCrvena() {
		return crvena;
	}

	public void setCrvena(JButton crvena) {
		this.crvena = crvena;
	}

	public JButton getLjubicasta() {
		return ljubicasta;
	}

	public void setLjubicasta(JButton ljubicasta) {
		this.ljubicasta = ljubicasta;
	}

	public JButton getZuta() {
		return zuta;
	}

	public void setZuta(JButton zuta) {
		this.zuta = zuta;
	}

	public JButton getKafena() {
		return kafena;
	}

	public void setKafena(JButton kafena) {
		this.kafena = kafena;
	}

	public JButton getSiva() {
		return siva;
	}

	public void setSiva(JButton siva) {
		this.siva = siva;
	}

	public JButton getNarandzasta() {
		return narandzasta;
	}

	public void setNarandzasta(JButton narandzasta) {
		this.narandzasta = narandzasta;
	}

	public JButton getRoza() {
		return roza;
	}

	public void setRoza(JButton roza) {
		this.roza = roza;
	}

	public JButton getBijela() {
		return bijela;
	}

	public void setBijela(JButton bijela) {
		this.bijela = bijela;
	}

	public ConnectionFactory getFactory() {
		return factory;
	}

	public void setFactory(ConnectionFactory factory) {
		this.factory = factory;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	class DisabledItemSelectionModel extends DefaultListSelectionModel {
		private static final long serialVersionUID = 1L;

		@Override
		public void setSelectionInterval(int index0, int index1) {
			super.setSelectionInterval(-1, -1);
		}
	}
}