import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import java.util.List;
import com.rabbitmq.client.*;

public class Pocetni extends JFrame {
	private JPanel panela;
	private JLabel labela;
	private JTextField username;
	private JButton join, create;
	private static final long serialVersionUID = 1L;

	public Pocetni(List<String> rijeci) {
		panela = new JPanel(null);
		labela = new JLabel(new ImageIcon("src/Images/user1.png"));
		labela.setBounds(200, 30, 100, 100);
		panela.add(labela);
		labela = new JLabel("Unesite username :");
		labela.setForeground(Color.black);
		labela.setFont(new Font("Dyuthi", Font.BOLD, 17));
		labela.setBounds(180, 140, 200, 20);
		panela.add(labela);
		username = new JTextField();
		username.setForeground(Color.darkGray);
		username.setBorder(BorderFactory.createLineBorder(Color.black));
		username.setBounds(175, 170, 150, 20);
		username.setFont(new Font("Dyuthi", Font.BOLD, 14));
		panela.add(username);
		join = new JButton("JOIN ROOM");
		join.setForeground(Color.white);
		join.setBackground(Color.darkGray);
		join.setBounds(175, 200, 150, 20);
		join.setFont(new Font("Dyuthi", Font.BOLD, 12));
		create = new JButton("CREATE ROOM");
		create.setForeground(Color.white);
		create.setBackground(Color.darkGray);
		create.setBounds(175, 230, 150, 20);
		create.setFont(new Font("Dyuthi", Font.BOLD, 12));
		panela.add(join);
		panela.add(create);
		join.addActionListener(x -> {
			if (username.getText().length() > 0) {
				try {
					new JoinRoom(username.getText(), rijeci);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		create.addActionListener(x -> {
			if (username.getText().length() > 0) {
				String nazivSobe = JOptionPane.showInputDialog(null, "Unesite naziv sobe:");
				if (nazivSobe.length() > 0) {
					try {
						ConnectionFactory factory = CNF.cf();
						try (Connection connection = factory.newConnection();
								Channel channel = connection.createChannel()) {
							//ako ne postoji pravi novi
							channel.exchangeDeclare(nazivSobe, BuiltinExchangeType.FANOUT);
						} // sada smo kreirali sobu
						SwingPaint p = new SwingPaint(new Igrac(1, username.getText(), 5));
						p.getStanje().setId_trenutnogIgraca(1);
						p.getStanje().setRoomName(nazivSobe);
						p.getUnos().setEnabled(false);
						p.setRijeci(rijeci);
						// declare pravi exchange ako vec ne postoji
						p.getChannel().exchangeDeclare(nazivSobe, BuiltinExchangeType.FANOUT);
						new Primaoc(p.getStanje(), p).execute();// omoguci primanje poruka
						p.setVisible(true);
						dispose();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		setContentPane(panela);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Igra crtanja");
		setBounds(300, 200, 500, 300);
		setVisible(true);
	}
}