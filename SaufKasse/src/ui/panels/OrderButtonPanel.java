package ui.panels;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import beans.Product;
import beans.Voucher;
import controller.Controller;
import utilities.Variables;

public class OrderButtonPanel extends AbstractKassenPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<JButton> uiElements;
	ActionListener listener;

	public OrderButtonPanel(Controller controller) {
		listener = controller;
	}

	public void initialize(ArrayList<Product> alp, ArrayList<Voucher> alv) {

		uiElements = new ArrayList<JButton>();
		removeAll();
		revalidate();
		
		Font font = Variables.buttonAndComboFont;

		int addedButtonsFirstCol = 0;
		int addedButtonsSecondCol = 0;

		JButton buttonToAdd;

		// Loop to define UI Elements for Each Product
		for (int i = 0; i < alp.size(); i++) {
			Product product = alp.get(i);

			buttonToAdd = generateNewJButton(product.getName(), listener, font);

			//TODO Anpassen auf Kategorie
			if (true) {
				setConstraintSettings(0, addedButtonsFirstCol, 0.5, 0.5, 1, 1);
				addedButtonsFirstCol++;
			} else {
				setConstraintSettings(1, addedButtonsSecondCol, 0.5, 0.5, 1, 1);
				addedButtonsSecondCol++;
			}

			uiElements.add(buttonToAdd);
			add(buttonToAdd, gbc);
		}

		for (int i = 0; i < alv.size(); i++) {
			Voucher voucher = alv.get(i);
			buttonToAdd = generateNewJButton(voucher.getDescription(), listener, font);
			setConstraintSettings(2, i, 0.5, 0.5, 1, 1);
			uiElements.add(buttonToAdd);
			add(buttonToAdd, gbc);

		}

		repaint();
		revalidate();

	}


	public void changeToPayMode() {
		this.setVisible(false);
	}



	public void changeToSellingMode() {
		this.setVisible(true);
		
	}

	public void changeFont(Font buttonAndComboFont) {
		for (int i = 0; i < uiElements.size(); i++) {
			uiElements.get(i).setFont(buttonAndComboFont);
		}
	}

}
