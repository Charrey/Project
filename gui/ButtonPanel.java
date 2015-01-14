package Project.gui;

import java.awt.GridLayout;
import java.awt.event.MouseListener;

import javax.swing.*;


public class ButtonPanel extends JPanel {

	public JLabel[] labels;


	public ButtonPanel(int width, MouseListener mouseListener) {
		labels = new JLabel[width];
		
		for (int i = 0; i < 7; i++) {
			labels[i] = new JLabel(Integer.toString(i));
			labels[i].setHorizontalAlignment(SwingConstants.CENTER);
			add(labels[i]);
			labels[i].addMouseListener(mouseListener);
		}
		setLayout(new GridLayout(1, labels.length));
		setSize(labels.length * 64, 100);
		this.setVisible(true);
	}

}
