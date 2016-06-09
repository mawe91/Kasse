package ui.panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import controller.Controller;
import utilities.Variables;

public class PaymentPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<JButton> scheineArray;
	ArrayList<JComponent> otherUiElements;

	JButton bezahlen;
	JButton kassieren;
	JButton zeilenstorno;
	JButton deleteSum;
	
	JButton jb;

	public PaymentPanel(Controller controller) {

		setLayout(new GridLayout(0, 2));

		Font font = Variables.buttonAndComboFont;

		JPanel bills = new JPanel();
		bills.setLayout(new GridLayout(4, 0));

		scheineArray = new ArrayList<JButton>();
		otherUiElements = new ArrayList<JComponent>();
		
		jb = new JButton("50 €");
		jb.setFont(font);
		jb.addActionListener(controller);
		scheineArray.add(jb);
		bills.add(jb);

		jb = new JButton("20 €");
		scheineArray.add(jb);
		jb.setFont(font);
		jb.addActionListener(controller);
		bills.add(jb);

		jb = new JButton("10 €");
		jb.addActionListener(controller);
		scheineArray.add(jb);
		jb.setFont(font);
		bills.add(jb);

		jb = new JButton("5 €");
		jb.addActionListener(controller);
		scheineArray.add(jb);
		jb.setFont(font);
		bills.add(jb);
		
		add(bills);
		
		
		JPanel control = new JPanel();
		control.setLayout(new GridLayout(5, 0));

		zeilenstorno = new JButton("Zeilenstorno");
		zeilenstorno.addActionListener(controller);
		zeilenstorno.setFont(font);
		zeilenstorno.setEnabled(false);
		otherUiElements.add(zeilenstorno);
		control.add(zeilenstorno);

		jb = new JButton("Bon abbrechen");
		jb.addActionListener(controller);
		jb.setFont(font);
		otherUiElements.add(jb);
		control.add(jb);

		deleteSum = new JButton ("Zahlung korrigieren");
		deleteSum.addActionListener(controller);
		deleteSum.setFont(font);
		deleteSum.setEnabled(false);
		otherUiElements.add(deleteSum);
		control.add(deleteSum);


		kassieren = new JButton("Weiter kassieren");
		kassieren.addActionListener(controller);
		kassieren.setFont(font);
		kassieren.setVisible(false);
		otherUiElements.add(kassieren);
		control.add(kassieren);
		
		bezahlen = new JButton("Bezahlen");
		bezahlen.addActionListener(controller);
		bezahlen.setFont(font);
		bezahlen.setEnabled(false);
		otherUiElements.add(bezahlen);
		control.add(bezahlen);

		
		add(control);

		for (int i = 0; i < scheineArray.size(); i++) {
			scheineArray.get(i).setVisible(false);
		}

		
	}


	public void changeToPayMode() {
		for (int i = 0; i < scheineArray.size(); i++) {
			scheineArray.get(i).setVisible(true);
		}
		bezahlen.setVisible(false);
		kassieren.setVisible(true);
		deleteSum.setEnabled(true);
	}

	public void changeToSellingMode() {
		for (int i = 0; i < scheineArray.size(); i++) {
			scheineArray.get(i).setVisible(false);
		}
		bezahlen.setVisible(true);
		kassieren.setVisible(false);
		deleteSum.setEnabled(false);
	}

	public void setZeilenstornoEnabled(boolean b) {
		zeilenstorno.setEnabled(b);
		repaint();
		revalidate();
	}

	public void setChangeToPaymodeEnabled(boolean b) {

		bezahlen.setEnabled(b);
	}
	
	public void changeFont(Font buttonAndComboFont) {
		for (int i = 0; i < scheineArray.size(); i++) {
			scheineArray.get(i).setFont(buttonAndComboFont);
		}
		for (int i = 0; i < otherUiElements.size(); i++) {
			otherUiElements.get(i).setFont(buttonAndComboFont);
		}
	}

}
