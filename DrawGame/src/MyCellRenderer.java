import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

@SuppressWarnings({ "rawtypes", "serial" })
public class MyCellRenderer extends JLabel implements ListCellRenderer {
	String k1;
	int trenutniIgracId;
	int idIgraca;
	final static ImageIcon icon = new ImageIcon("src/Images/user.png");

	public MyCellRenderer(String k1, int trenutniIgracId, int idIgraca) {
		this.k1 = k1;
		this.trenutniIgracId = trenutniIgracId;
		this.idIgraca = idIgraca;
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		String s = value.toString();
		setText(s);
		if (k1.equals("s1"))
			setBorder(new EmptyBorder(10, 10, 10, 10));
		else
			setBorder(new EmptyBorder(2, 2, 2, 2));
		if (k1.equals("s1"))
			setIcon(icon);
		setForeground(list.getForeground());
		if (trenutniIgracId == Character.getNumericValue(s.charAt(0)))
			setForeground(Color.orange);
		if (idIgraca == Character.getNumericValue(s.charAt(0)))
			setForeground(Color.yellow);
		if (k1.equals("s2"))
			setBackground(list.getBackground());
		setFont(list.getFont());
		setOpaque(false);
		return this;
	}
}