package ui.panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import alertobjects.CalcFieldAlert;
import beans.Product;
import beans.Voucher;
import controller.Controller;

public class ScreenControlArea extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CalculatorPanel calculaterPanel;
	PaymentPanel paymentPanel;

	public ScreenControlArea(Controller controller) {
		
		super();
		
		setLayout(new GridLayout(0, 2));

		// Calculater Panel
		calculaterPanel = new CalculatorPanel(controller);
		add(calculaterPanel);

		// MoneyPanel
		paymentPanel = new PaymentPanel(controller);
		add(paymentPanel);

	}



	public void updateCalcField(CalcFieldAlert cfa) {
		
		calculaterPanel.updateTextField(cfa.getValue());
	}

	public void changeToPayMode() {
		
		calculaterPanel.changeToPayMode();
		paymentPanel.changeToPayMode();
	}

	public void changeToSellingMode() {
		
		calculaterPanel.changeToSellingMode();
		paymentPanel.changeToSellingMode();
	}

	public void setZeilenstornoEnabled(boolean b) {
		paymentPanel.setZeilenstornoEnabled(b);
	}

	public void setChangeToPaymodeEnabled(boolean b) {
		paymentPanel.setChangeToPaymodeEnabled(b);
	}

	public void changeFont(Font buttonAndComboFont) {
		calculaterPanel.changeFont(buttonAndComboFont);
		paymentPanel.changeFont(buttonAndComboFont);
	}

}
