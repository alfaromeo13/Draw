import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Tajmer extends SwingWorker<Integer, Integer> {
	private JLabel sat;
	private SwingPaint sp;
	private int vrijeme;

	public JLabel getSat() {
		return sat;
	}

	public void setSat(JLabel sat) {
		this.sat = sat;
	}

	public int getVrijeme() {
		return vrijeme;
	}

	public void setVrijeme(int vrijeme) {
		this.vrijeme = vrijeme;
	}

	// dogadjaj za timer
	ActionListener taskPerformer = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			sp.getStanje().setVrTajmera(vrijeme);
			sat.setText(--vrijeme + "");
		}
	};

	public Tajmer(JLabel sat, SwingPaint sp, int vrijeme) {
		this.sat = sat;
		this.sp = sp;
		this.vrijeme = vrijeme;
	}

	@Override
	protected Integer doInBackground() throws Exception {
		Timer t = new Timer(1000, taskPerformer);
		t.start();
		Thread.sleep(vrijeme * 1000);
		t.stop();

		Stanje stanje = sp.getStanje();

		int naredniId = stanje.getId_trenutnogIgraca() + 1;
		if (naredniId > stanje.getIgraci().size())
			naredniId = 1;
		// ako sam ja vec trenutni igrac
		if (stanje.getId_trenutnogIgraca() == sp.getIgrac().getId())
			sp.izCrtacaUNecrtaca(naredniId);// ugasi odasiljac i cekaj na tudji impuls
		else if (sp.getIgrac().getId() == naredniId) // ako nisam da li sam onda ja taj koji treba da crta
			sp.izNeCrtacaUCrtaca(naredniId);
		else {// ako nisam ni jedan resetuj tajmer i sl
			sp.getUnos().setEnabled(true);
			stanje.setId_trenutnogIgraca(naredniId);
			sp.getModel2().removeAllElements();
			stanje.setVrTajmera(0);
			stanje.setIzabranaRijec("");
			sat.setText("60");
		}
		return 1;
	}
}