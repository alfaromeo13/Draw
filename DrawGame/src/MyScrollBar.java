import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyScrollBar extends BasicScrollBarUI {
	private final Dimension d = new Dimension();

	@SuppressWarnings("serial")
	@Override
	protected JButton createDecreaseButton(int orientation) {
		return new JButton() {
			@Override
			public Dimension getPreferredSize() {
				return d;
			}
		};
	}

	@SuppressWarnings("serial")
	@Override
	protected JButton createIncreaseButton(int orientation) {
		return new JButton() {
			@Override
			public Dimension getPreferredSize() {
				return d;
			}
		};
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
	}

	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color color = Color.darkGray;
		g2.setPaint(color);
		g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
		g2.setPaint(color);
		g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
		g2.dispose();
	}

	@Override
	protected void setThumbBounds(int x, int y, int width, int height) {
		super.setThumbBounds(x, y, width, height);
		scrollbar.repaint();
	}
}