package ui.panels;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import beans.Product;
import beans.Voucher;
import controller.Controller;
import interfaces.PaySellingChangerInterface;
import utilities.Variables;

public class OrderButtonPanel extends AbstractKassenPanel implements PaySellingChangerInterface{

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

		int addedButtonsAlcCol = 0;
		int addedButtonsAlcFreeCol = 0;
		int addedButtonsMealCol = 0;

		JButton buttonToAdd;

		// Loop to define UI Elements for Each Product
		for (int i = 0; i < alp.size(); i++) {
			Product product = alp.get(i);

			buttonToAdd = generateNewJButton(product.getName(), listener, font);

			if (product.getProdCat()==1 || product.getProdCat()==3) {
				setConstraintSettings(0, addedButtonsAlcCol, 0.5, 0.5, 1, 1);
				addedButtonsAlcCol++;
			} else if (product.getProdCat() == 2){
				setConstraintSettings(1, addedButtonsAlcFreeCol, 0.5, 0.5, 1, 1);
				addedButtonsAlcFreeCol++;
			} else if (product.getProdCat() ==4 ){
				setConstraintSettings(2, addedButtonsMealCol, 0.5, 0.5, 1, 1);
				addedButtonsMealCol++;				
			}

			uiElements.add(buttonToAdd);
			add(buttonToAdd, gbc);
		}

		for (int i = 0; i < alv.size(); i++) {
			Voucher voucher = alv.get(i);
			buttonToAdd = generateNewJButton(voucher.getDescription(), listener, font);
			setConstraintSettings(3, i, 0.5, 0.5, 1, 1);
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
