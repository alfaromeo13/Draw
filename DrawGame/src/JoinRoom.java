import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.xml.bind.*;
import com.rabbitmq.client.BuiltinExchangeType;

public class JoinRoom extends JFrame {
	private JPanel panela;
	private JLabel label;
	private int brojIgraca;// broj ljudi koji je vec prikljucen na exchange
	private static final long serialVersionUID = 1L;
	private JList<String> rumovi;
	private DefaultListModel<String> model;
	private JScrollPane sp;

	private List<String> getValuesFromRabbitWebAPI(String link, String parametar)
			throws MalformedURLException, IOException {
		// ovom dobijamo JSON poruke koje cemo parsirati da bi dobili naziv roomova
		List<String> spisak = new LinkedList<>();
		String loginPassword = "admin:admin";
		String encoded = DatatypeConverter.printBase64Binary(loginPassword.getBytes());
		URL url = new URL(link);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("Authorization", "Basic " + encoded);
		String response = "";
		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			String line;
			while ((line = in.readLine()) != null)
				response += line;
		}
		String[] split = response.split(parametar);
		brojIgraca = split.length;
		// preskacemo prvi element "[{" ili ""
		for (int i = 1; i < split.length; i++) {
			String nameRaw = split[i];
			int index = nameRaw.indexOf("\"");
			if (index > 0 && !nameRaw.startsWith("amq.")) {
				String name = nameRaw.substring(0, index);
				spisak.add(name);
			}
		}
		return spisak;
	}

	@SuppressWarnings("unchecked")
	public JoinRoom(String username, List<String> rijeci) throws MalformedURLException, IOException {
		panela = new JPanel(null);
		rumovi = new JList<>();
		rumovi.setOpaque(false);
		rumovi.setBackground(Color.darkGray);
		rumovi.setForeground(Color.white);
		rumovi.setFont(new Font("Dyuthi", Font.BOLD, 15));
		rumovi.setCellRenderer(new MyCellRenderer("s2", 1, 999));
		label = new JLabel("    Pridruzi se sobi?");
		label.setForeground(Color.darkGray);
		label.setFont(new Font("Dyuthi", Font.BOLD, 15));
		model = new DefaultListModel<>();
		String URL = "https://24e1-79-143-107-31.ngrok-free.app";
		List<String> spisakRumova = getValuesFromRabbitWebAPI(URL + "/api/exchanges", "\"name\":\""); // za
		for (String s : spisakRumova)
			model.addElement(s);
		rumovi.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {// dvostruki klik
					int response = JOptionPane.showConfirmDialog(null, label, "Potvrda", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {
						String korisnici_url = URL + "/api/exchanges/app/" + rumovi.getSelectedValue()
								+ "/bindings/source";
						SwingPaint p;
						try {
							getValuesFromRabbitWebAPI(korisnici_url, "source");
							Igrac i = new Igrac(brojIgraca, username, 0);
							p = new SwingPaint(i);
							p.getDrawArea().removeMouseListener(p.getDrawArea().getA1());
							p.getDrawArea().removeMouseMotionListener(p.getDrawArea().getA2());
							p.getStanje().setId_trenutnogIgraca(1);// igraci igraju redom po spisku pa je prvi creator
							p.getStanje().setRoomName(rumovi.getSelectedValue());
							p.getChannel().exchangeDeclare(rumovi.getSelectedValue(), BuiltinExchangeType.FANOUT);
							p.setRijeci(rijeci);
							p.getP1().setVisible(false);
							p.getP2().setVisible(false);
							p.getP3().setVisible(false);
							p.getCetkica().setEnabled(false);
							p.getGumica().setEnabled(false);
							p.getStart().setVisible(false);
							p.getReset().setEnabled(false);
							p.getCrna().setEnabled(false);
							p.getPlava().setEnabled(false);
							p.getZelena().setEnabled(false);
							p.getCrvena().setEnabled(false);
							p.getLjubicasta().setEnabled(false);
							p.getZuta().setEnabled(false);
							p.getKafena().setEnabled(false);
							p.getSiva().setEnabled(false);
							p.getNarandzasta().setEnabled(false);
							p.getRoza().setEnabled(false);
							p.getBijela().setEnabled(false);
							new Primaoc(p.getStanje(), p).execute();// omoguci primanje poruka
							new Posiljaoc(i, rumovi.getSelectedValue(), p.getChannel()).start();// posalji svoje stanje
							p.setVisible(true);
							dispose();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
		dlcr.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
		rumovi.setCellRenderer(dlcr);
		rumovi.setModel(model);
		sp = new JScrollPane(rumovi);
		sp.setOpaque(true);
		sp.getViewport().setOpaque(false);
		sp.setBackground(Color.darkGray);
		sp.setBounds(0, 0, 221, 600);
		sp.getVerticalScrollBar().setOpaque(false);
		sp.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 1));
		sp.getVerticalScrollBar().setUI(new MyScrollBar());
		panela.add(sp);
		setContentPane(panela);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Igra crtanja");
		setBounds(300, 200, 221, 600);
		setVisible(true);
	}
}