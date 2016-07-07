package ui.panels;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import controller.Controller;
import interfaces.PaySellingChangerInterface;
import utilities.Variables;

public class CalculatorPanel extends AbstractKassenPanel implements PaySellingChangerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField jtfCalcField;

	JButton commaButton;
	JButton buyButton;
	JButton multiplyButton;

	boolean paymode;

	ArrayList<JButton> numbers;
	ArrayList<JComponent> otherUiElements;

	public CalculatorPanel(Controller controller) {
		super();

		paymode = false;

		numbers = new ArrayList<JButton>();
		otherUiElements = new ArrayList<JComponent>();
		
		Font font = Variables.buttonAndComboFont;

		jtfCalcField = new JTextField("");
		jtfCalcField.setFont(font);
		jtfCalcField.setEditable(false);
		setConstraintSettings(0, 0, 0.5, 0.5, 1, 3);
		otherUiElements.add(jtfCalcField);
		add(jtfCalcField, gbc);

		for (int i = 1; i < 10; i++) {
			int row = -1;
			if (i <= 3) {
				row = 3;
			} else if (i <= 6) {
				row = 2;
			} else if (i <= 9) {
				row = 1;
			}
			setConstraintSettings(i % 3 - 1, row, 0.5, 0.5, 1, 1);
			JButton jb = generateNewJButton("" + i, controller, font);
			numbers.add(jb);
			add(jb, gbc);
		}

		JButton zeroButton = generateNewJButton("0", controller, font, 0, 4, 0.5, 0.5, 1, 2);
		numbers.add(zeroButton);
		add(zeroButton, gbc);

		commaButton = generateNewJButton(",", controller, font, 2, 4, 0.5, 0.5, 1, 1);
		commaButton.setEnabled(false);
		otherUiElements.add(commaButton);
		add(commaButton, gbc);

		JButton deleteButton = generateNewJButton("del", controller, font, 3, 0, 0.5, 0.5, 1, 1);
		otherUiElements.add(deleteButton);
		add(deleteButton, gbc);

		multiplyButton = generateNewJButton("X", controller, font, 3, 1, 0.5, 0.5, 2, 1);
		multiplyButton.setEnabled(false);
		otherUiElements.add(multiplyButton);
		add(multiplyButton, gbc);

		buyButton = generateNewJButton("Bar", controller, font, 3, 3, 0.5, 0.5, 2, 1);
		buyButton.setEnabled(false);
		otherUiElements.add(buyButton);
		add(buyButton, gbc);

		//setBorder(getNewTitledBorder(""));
	}

	public void updateTextField(String s) {

		jtfCalcField.setText(s);
		setButtonStates();

	}

	public void changeToPayMode() {
		paymode = true;
		setButtonStates();
	}

	public void changeToSellingMode() {
		paymode = false;
		setButtonStates();
	}

	private void enableNumberButtons(boolean b) {
		for (int i = 0; i < numbers.size(); i++) {
			numbers.get(i).setEnabled(b);
		}

	}

	private void setButtonStates() {
		String s = jtfCalcField.getText();

		if (paymode) {

			multiplyButton.setEnabled(false);
			buyButton.setEnabled(true);

			// Wenn leeres Feld
			if (s.equals("")) {
				enableNumberButtons(true);
				commaButton.setEnabled(false);

				// Wenn drittletztes Zeichen Comma dann alles disablen
			} else if (s.length() > 3 && s.indexOf(',') == s.length() - 3) {
				enableNumberButtons(false);
				commaButton.setEnabled(false);

				// Wenn Text ein Komma enthält
			} else if (s.indexOf(',') > 0) {
				enableNumberButtons(true);
				commaButton.setEnabled(false);

				// Wenn Feld nicht leer aber kein Comma
			} else {
				enableNumberButtons(true);
				commaButton.setEnabled(true);

			}

		} else {
			commaButton.setEnabled(false);
			buyButton.setEnabled(false);

			// Wenn leeres Feld
			if (s.equals("") || isZero(s)) {
				enableNumberButtons(true);
				multiplyButton.setEnabled(false);

				// Wenn letztes Zeichen X dann alles sperren
			} else if (s.substring(s.length() - 1).equals("X")) {
				enableNumberButtons(false);
				multiplyButton.setEnabled(false);

				// Sonst Eingabe ermöglichen
			} else {
				enableNumberButtons(true);
				multiplyButton.setEnabled(true);
				buyButton.setEnabled(false);
			}

		}

	}

	private boolean isZero(String s) {
		try {
			if (Integer.parseInt(s) == 0) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}

	}
	
	public void changeFont(Font buttonAndComboFont) {
		for (int i = 0; i < numbers.size(); i++) {
			numbers.get(i).setFont(buttonAndComboFont);
		}
		for (int i = 0; i < otherUiElements.size(); i++) {
			otherUiElements.get(i).setFont(buttonAndComboFont);
		}
	}

}
