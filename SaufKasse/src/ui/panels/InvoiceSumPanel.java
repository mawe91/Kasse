package ui.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import utilities.Variables;

public class InvoiceSumPanel extends AbstractKassenPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DecimalFormat format;

	private JTextField jtfTotalSum;
	private JLabel jl1;
	private JLabel jl2;
	private JTextField jtfPayedSum;

	public InvoiceSumPanel() {
		format = new DecimalFormat("#0.00");

		Font font = Variables.buttonAndComboFont;
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_START;

		jl1 = new JLabel("Gesamtsumme");
		jl1.setFont(font);
		setConstraintSettings(0, 0, 0.5, 0, 1, 1);
		add(jl1, gbc);

		jtfTotalSum = new JTextField("0,00 €");
		jtfTotalSum.setFont(font);
		jtfTotalSum.setEditable(false);
		jtfTotalSum.setHorizontalAlignment(JTextField.CENTER);
		setConstraintSettings(0, 1, 0, 0, 1, 1);
		add(jtfTotalSum, gbc);
		
		jl2 = new JLabel("Noch zu zahlen");
		jl2.setFont(font);
		setConstraintSettings(0, 2, 0, 0, 1, 1);
		jl2.setVisible(false);
		add(jl2, gbc);

		jtfPayedSum = new JTextField("0,00 €");
		jtfPayedSum.setFont(font);
		jtfPayedSum.setHorizontalAlignment(JTextField.CENTER);
		setConstraintSettings(0, 3, 0, 0, 1, 1);
		jtfPayedSum.setVisible(false);
		jtfPayedSum.setEditable(false);
		add(jtfPayedSum, gbc);	
	}
	

	public void updateInvoiceSum(double sum) {
		jtfTotalSum.setText("" + format.format(sum) + " €");
	}

	public void updateOpenSum(double openSum) {
		jtfPayedSum.setText("" + format.format(openSum) + " €");
	}

	public void changeToPayMode() {
		jtfPayedSum.setVisible(true);
		jl2.setVisible(true);
	}

	public void changeToSellingMode() {
		jtfPayedSum.setVisible(false);
		jl2.setVisible(false);
	}

	public void changeFont(Font buttonAndComboFont) {
		jl1.setFont(buttonAndComboFont);
		jl2.setFont(buttonAndComboFont);
		jtfPayedSum.setFont(buttonAndComboFont);
		jtfTotalSum.setFont(buttonAndComboFont);				
	}

}
