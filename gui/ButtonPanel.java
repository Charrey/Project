package Project.gui;

import java.awt.GridLayout;
import javax.swing.*;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.List;
import java.util.LinkedList;

public class ButtonPanel extends JPanel {

	public JLabel[] labels;

	// Reminder: b.width() == labels.length!

	public ButtonPanel(int width) {
		labels = new JLabel[width];

		for (int i = 0; i < 7; i++) {
			labels[i] = new JLabel(Integer.toString(i));
			labels[i].setHorizontalAlignment(SwingConstants.CENTER);
			add(labels[i]);
		}
		setLayout(new GridLayout(1, labels.length));
		setSize(labels.length * 64, 100);
		this.setVisible(true);
	}

}
