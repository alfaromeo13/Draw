import java.awt.*;
import java.io.*;
import javax.swing.*;
import com.rabbitmq.client.*;

public class Primaoc extends SwingWorker<Integer, Integer> {
	private Stanje stanje;
	private SwingPaint sp;
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	private String queueName;
	private int x_staro = -1, y_staro = -1;

	public Primaoc(Stanje stanje, SwingPaint sp) throws Exception {
		this.stanje = stanje;
		this.sp = sp;
		factory = sp.getFactory();
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.exchangeDeclare(stanje.getRoomName(), BuiltinExchangeType.FANOUT);
		queueName = channel.queueDeclare().getQueue();
		channel.queueBind(queueName, stanje.getRoomName(), "");
	}

	@Override // Ceka dolazece poruke
	protected Integer doInBackground() throws Exception {
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			try {
				byte[] body = delivery.getBody();
				ByteArrayInputStream bis = new ByteArrayInputStream(body);
				ObjectInputStream ois = new ObjectInputStream(bis);
				Object obj = ois.readObject();
				if (obj instanceof Igrac) {
					Igrac i = (Igrac) obj;
					if (i.getId() != sp.getIgrac().getId()) {
						if (stanje.getId_trenutnogIgraca() == sp.getIgrac().getId()) {
							stanje.getIgraci().put(i.getId(), i);
							sp.getModel1().addElement(i.getId() + "#" + i.getUsername() + " 0p");
							new Posiljaoc(stanje, stanje.getRoomName(), channel).start();
						}
					}
				} else if (obj instanceof Tacka) {
					if (stanje.getId_trenutnogIgraca() != sp.getIgrac().getId())
						paint((Tacka) obj, sp.getDrawArea());
				} else if (obj instanceof Stanje) {
					Stanje novo_stanje = (Stanje) obj;
					if (novo_stanje.getId_trenutnogIgraca() != sp.getIgrac().getId()) {
						// dodaj poruke
						sp.getModel2().removeAllElements();
						for (String s : novo_stanje.getPoruke())
							sp.getModel2().addElement(s);

						// korisnici
						for (Igrac i : novo_stanje.getIgraci().values()) {
							if (!stanje.getIgraci().containsKey(i.getId())) {
								stanje.getIgraci().put(i.getId(), i);
								sp.getModel1()
										.addElement(i.getId() + "#" + i.getUsername() + " " + i.getBrPoena() + "p");
							}
						}

						// ako klikne start
						if (novo_stanje.getVrTajmera() != 0) {
							stanje.setId_trenutnogIgraca(novo_stanje.getId_trenutnogIgraca());
							String izraz = "";
							stanje.setIzabranaRijec(novo_stanje.getIzabranaRijec());
							for (int i = 0; i < stanje.getIzabranaRijec().length(); i++)
								izraz += "_ ";
							sp.getRijec().setText(izraz);
							sp.getRijec().setBounds(625 - 10 * izraz.length(), 30, 300, 70);
							Tajmer t = sp.getT();// uvijek ce postojati latency tj kasnjenje
							t = new Tajmer(sp.getSat(), sp, novo_stanje.getVrTajmera());
							t.execute();
						}
					}
				} else // ako primis poruku
					sp.getModel2().addElement((String) obj);
				ois.close();
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
		});
		while (true) // odrzava konekciju i ne dozvoljava da se zatvori konekcija
			Thread.sleep(1000000);
	}

	// funkcija koja crta partiju korisnicima
	public void paint(Tacka nova, DrawArea dr) {
		Graphics2D g2 = dr.getG2();
		if (nova.getX1() != -1) {
			if (nova.getX1() == -5) {
				g2.setPaint(Color.white);
				g2.fillRect(0, 0, dr.getSize().width, dr.getSize().height);
				g2.setPaint(Color.black);
			} else {
				if (x_staro == -1) {
					x_staro = nova.getX1();
					y_staro = nova.getY1();
				}
				g2.setPaint(sp.getDugmad()[nova.getDugmeId()].getBackground());
				g2.setStroke(new BasicStroke(nova.getDebljinaLinije()));
				g2.drawLine(x_staro, y_staro, nova.getX1(), nova.getY1());
				x_staro = nova.getX1();
				y_staro = nova.getY1();
			}
			dr.repaint();// moramo pozvati
		} else {
			x_staro = -1;
			y_staro = -1;
		}
	}
}