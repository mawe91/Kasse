package ui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public abstract class AbstractKassenPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GridBagConstraints gbc;
	
	
	public AbstractKassenPanel() {
		
		super();
		setLayout(new GridBagLayout());
		
		gbc = new GridBagConstraints();
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.fill = GridBagConstraints.BOTH;

	}
	
	public JButton generateNewJButton(String s, Font font) {
		JButton jb = new JButton(s);
		jb.setFont(font);
		return jb;
	}
	
	public JButton generateNewJButton(String s,ActionListener al, Font font) {
		JButton jb = generateNewJButton(s, font);
		jb.addActionListener(al);
		return jb;
	}
	
	public JButton generateNewJButton(String s,ActionListener al, Font font, int gridX, int gridY, double weightX, double weightY, int gridHight,
			int gridWidth) {
		setConstraintSettings(gridX, gridY, weightX, weightY, gridHight, gridWidth);
		return generateNewJButton(s,al,font);
	}

	public JButton generateNewJButton(String s,ActionListener al, Font font, Color backCol, int gridX, int gridY, double weightX, double weightY,
			int gridHight, int gridWidth) {
		JButton jb = generateNewJButton(s,al,font, gridX, gridY, weightX, weightY, gridHight, gridWidth);
		jb.setBackground(backCol);
		return jb;
	}
	
	public void setConstraintSettings(int gridX, int gridY, double weightX, double weightY, int gridHight,
			int gridWidth) {
		gbc.gridx = gridX;
		gbc.gridy = gridY;
		gbc.weightx = weightX;
		gbc.weighty = weightY;
		gbc.gridheight = gridHight;
		gbc.gridwidth = gridWidth;
	}
	
	public TitledBorder getNewTitledBorder(String s) {
		TitledBorder border = BorderFactory.createTitledBorder(s);
		border.setTitleJustification(TitledBorder.CENTER);
		border.setTitlePosition(TitledBorder.ABOVE_TOP);
		return border;
	}
	
	public Border getNewLineBorder() {
		return BorderFactory.createLineBorder(Color.black);
	}	

}
