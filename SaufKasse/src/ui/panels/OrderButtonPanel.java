package ui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import beans.Product;
import beans.Voucher;
import controller.Controller;
import interfaces.PaySellingChangerInterface;
import utilities.Variables;

public class OrderButtonPanel extends AbstractKassenPanel implements PaySellingChangerInterface {

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

		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Produkte", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(128, 128, 128)));

		uiElements = new ArrayList<JButton>();
		removeAll();
		revalidate();

		Font font = Variables.buttonAndComboFont;

		int addedButtonsAlcCol = 0;
		int addedButtonsAlcFreeCol = 0;
		int addedButtonsMealCol = 0;

		JButton buttonToAdd;

		// Loop to define UI Elements for Each Product
		for (int i = 0; i < alp.size(); i++) {
			Product product = alp.get(i);

			buttonToAdd = generateNewJButton(product.getName(), listener, font);

			if (product.getProdCat() == 1 || product.getProdCat() == 3) {
				setConstraintSettings(0, addedButtonsAlcCol, 0.5, 0.5, 1, 1);
				addedButtonsAlcCol++;
			} else if (product.getProdCat() == 2) {
				setConstraintSettings(1, addedButtonsAlcFreeCol, 0.5, 0.5, 1, 1);
				addedButtonsAlcFreeCol++;
			} else if (product.getProdCat() == 4) {
				setConstraintSettings(2, addedButtonsMealCol, 0.5, 0.5, 1, 1);
				addedButtonsMealCol++;
			}

			uiElements.add(buttonToAdd);
			add(buttonToAdd, gbc);
		}

		int addedButtonsVoucher = 0;
		for (int i = 0; i < alv.size(); i++) {
			if (alv.get(i).getId() != Variables.voucherDepositID
					&& alv.get(i).getId() != Variables.voucherDepositReturnID) {
				Voucher voucher = alv.get(i);
				buttonToAdd = generateNewJButton(voucher.getDescription(), listener, font);
				setConstraintSettings(3, addedButtonsVoucher, 0.5, 0.5, 1, 1);
				buttonToAdd.setBackground(voucher.getColor());
				buttonToAdd.setHorizontalAlignment(JButton.CENTER);
				uiElements.add(buttonToAdd);
				add(buttonToAdd, gbc);
				addedButtonsVoucher++;
			}
		}

		int maxRowCount = 0;
		if (addedButtonsAlcCol > maxRowCount) {
			maxRowCount = addedButtonsAlcCol;
		}
		if (addedButtonsAlcFreeCol > maxRowCount) {
			maxRowCount = addedButtonsAlcFreeCol;
		}
		if (addedButtonsMealCol > maxRowCount) {
			maxRowCount = addedButtonsMealCol;
		}
		if (addedButtonsVoucher > maxRowCount) {
			maxRowCount = addedButtonsVoucher;
		}

		int addedDepositButtons = 0;
		for (int i = 0; i < alp.size(); i++) {
			Product p = alp.get(i);
			if (p.getProdCat() == 5) {
				int rowPosition = 0;
				if (addedDepositButtons != 0) {
					rowPosition = 2 * addedDepositButtons;
				}
				setConstraintSettings(rowPosition, maxRowCount + 1, 0.5, 0.5, 1, 2);
				buttonToAdd = generateNewJButton(p.getName(), listener, font);
				uiElements.add(buttonToAdd);
				addedDepositButtons++;
				add(buttonToAdd, gbc);
			}
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
