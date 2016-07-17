package ui.panels;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import alertobjects.InvoiceAlert;
import beans.Invoice;
import interfaces.PaySellingChangerInterface;
import utilities.Variables;

public class InvoiceSumPanel extends AbstractKassenPanel implements PaySellingChangerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DecimalFormat format;

	private JTextField jtfTotalSum;
	private JTextField jtfDepositSum;
	private JTextField jtfPayedSum;
	private JLabel jl1;
	private JLabel jl2;
	private JLabel jl3;

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

		jl2 = new JLabel("Davon Pfand");
		jl2.setFont(font);
		setConstraintSettings(0, 2, 0, 0, 1, 1);
		add(jl2, gbc);
		
		jtfDepositSum = new JTextField("0,00 €");
		jtfDepositSum.setFont(font);
		jtfDepositSum.setHorizontalAlignment(JTextField.CENTER);
		setConstraintSettings(0, 3, 0, 0, 1, 1);
		
		jtfDepositSum.setEditable(false);
		add(jtfDepositSum, gbc);
		
		jl3 = new JLabel("Noch zu zahlen");
		jl3.setFont(font);
		setConstraintSettings(0, 4, 0, 0, 1, 1);
		jl3.setVisible(false);
		add(jl3, gbc);

		jtfPayedSum = new JTextField("0,00 €");
		jtfPayedSum.setFont(font);
		jtfPayedSum.setHorizontalAlignment(JTextField.CENTER);
		setConstraintSettings(0, 5, 0, 0, 1, 1);
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
	
	public void updateDepositSum(double depositsum) {
		jtfDepositSum.setText(""+format.format(depositsum)+" €");
	}

	public void changeToPayMode() {
		jtfPayedSum.setVisible(true);
		jl3.setVisible(true);
	}

	public void changeToSellingMode() {
		jtfPayedSum.setVisible(false);
		jl3.setVisible(false);
	}

	public void changeFont(Font buttonAndComboFont) {
		jl1.setFont(buttonAndComboFont);
		jl2.setFont(buttonAndComboFont);
		jl3.setFont(buttonAndComboFont);
		jtfPayedSum.setFont(buttonAndComboFont);
		jtfDepositSum.setFont(buttonAndComboFont);
		jtfTotalSum.setFont(buttonAndComboFont);				
	}


	public void updateAfterInvoiceChange(InvoiceAlert ia) {

		Invoice inv = ia.getInvoice();
		
		updateInvoiceSum(inv.getInvoiceSum());
		
		int depositCount = 0;
		int depositReturnCount = 0;
		for (int i = 0; i < inv.getInvoiceLines().size(); i++) {
			if (inv.getInvoiceLines().get(i).getProductID() == Variables.ProductDepositID){
				depositCount = depositCount + inv.getInvoiceLines().get(i).getCount();
			} else if (inv.getInvoiceLines().get(i).getProductID() == Variables.ProductDepositReturnID){
				depositReturnCount = depositReturnCount + inv.getInvoiceLines().get(i).getCount();
			}
		}
		
		//TODO Variable Invoice Value + besser auszeichnung
		updateDepositSum((depositCount - depositReturnCount) * 2.0);
		
	}

}
