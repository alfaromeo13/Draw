import java.awt.*;
import javax.swing.*;

public class ScrollPL extends ScrollPaneLayout {
	private static final long serialVersionUID = 1L;

	@Override
	public void layoutContainer(Container parent) {
		JScrollPane scrollPane = (JScrollPane) parent;

		Rectangle availR = scrollPane.getBounds();
		availR.x = availR.y = 0;

		Insets parentInsets = parent.getInsets();
		availR.x = parentInsets.left;
		availR.y = parentInsets.top;
		availR.width -= parentInsets.left + parentInsets.right;
		availR.height -= parentInsets.top + parentInsets.bottom;

		Rectangle vsbR = new Rectangle();
		vsbR.width = 7;// sirina scrolla :D
		vsbR.height = availR.height;
		vsbR.x = availR.x + availR.width - vsbR.width;
		vsbR.y = availR.y;

		if (viewport != null) {
			viewport.setBounds(availR);
		}
		if (vsb != null) {
			vsb.setVisible(true);
			vsb.setBounds(vsbR);
		}
	}
}
