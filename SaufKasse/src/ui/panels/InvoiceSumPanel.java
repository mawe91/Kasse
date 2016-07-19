package ui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import alertobjects.InvoiceAlert;
import beans.Invoice;
import utilities.Variables;

public class InvoiceSumPanel extends AbstractKassenPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DecimalFormat format;
	
	private JLabel lblDepositSpent;
	private JTextField tfDepositSpent;
	
	private JLabel lblDepositReturn;
	private JTextField tfDepositReturn;
	
	private JLabel lblInvoiceSum;
	private JTextField tfInvoiceSum;
	
	private JLabel lblDepositSum;
	private JTextField tfDepositSum;
	
	private JLabel lblOpenSum;
	private JTextField tfOpenSum;

	public InvoiceSumPanel() {
		format = new DecimalFormat("#0.00");

		Font font = Variables.buttonAndComboFont;
		
		setConstraintSettings(0, 0, 0.5, 0.5, 1, 1);
		JPanel sumPanel = new JPanel();
		sumPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Summen", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)));
		add(sumPanel,gbc);
		sumPanel.setLayout(new GridLayout(0, 2, 5, 5));
		
		lblInvoiceSum = new JLabel("Rechnungssumme");
		lblInvoiceSum.setHorizontalAlignment(SwingConstants.RIGHT);
		sumPanel.add(lblInvoiceSum);
		
		tfInvoiceSum = new JTextField();
		tfInvoiceSum.setText("0,00 \u20AC");
		tfInvoiceSum.setHorizontalAlignment(SwingConstants.CENTER);
		tfInvoiceSum.setEditable(false);
		tfInvoiceSum.setColumns(10);
		sumPanel.add(tfInvoiceSum);
		
		lblDepositSum = new JLabel("davon Pfand");
		lblDepositSum.setHorizontalAlignment(SwingConstants.RIGHT);
		sumPanel.add(lblDepositSum);
		
		tfDepositSum = new JTextField();
		tfDepositSum.setText("0,00 \u20AC");
		tfDepositSum.setHorizontalAlignment(SwingConstants.CENTER);
		tfDepositSum.setEditable(false);
		tfDepositSum.setColumns(10);
		sumPanel.add(tfDepositSum);
		
		lblOpenSum = new JLabel("Offener Betrag");
		lblOpenSum.setHorizontalAlignment(SwingConstants.RIGHT);
		sumPanel.add(lblOpenSum);
		
		tfOpenSum = new JTextField();
		tfOpenSum.setEditable(false);
		tfOpenSum.setHorizontalAlignment(SwingConstants.CENTER);
		tfOpenSum.setText("0,00 \u20AC");
		sumPanel.add(tfOpenSum);
		tfOpenSum.setColumns(10);
		
		setConstraintSettings(0, 1, 0.5, 0.5, 1, 1);
		JPanel depositPanel = new JPanel();
		depositPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Pfandmärkchen", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		add(depositPanel,gbc);
		depositPanel.setLayout(new GridLayout(0, 2, 5, 5));
		
		lblDepositSpent = new JLabel("Ausgabe");
		lblDepositSpent.setHorizontalAlignment(SwingConstants.RIGHT);
		depositPanel.add(lblDepositSpent);
		
		tfDepositSpent = new JTextField();
		tfDepositSpent.setEditable(false);
		tfDepositSpent.setHorizontalAlignment(SwingConstants.CENTER);
		tfDepositSpent.setText("0");
		depositPanel.add(tfDepositSpent);
		tfDepositSpent.setColumns(10);
		
		lblDepositReturn = new JLabel("R\u00FCcknahme");
		lblDepositReturn.setHorizontalAlignment(SwingConstants.RIGHT);
		depositPanel.add(lblDepositReturn);
		
		tfDepositReturn = new JTextField();
		tfDepositReturn.setEditable(false);
		tfDepositReturn.setHorizontalAlignment(SwingConstants.CENTER);
		tfDepositReturn.setText("0");
		depositPanel.add(tfDepositReturn);
		tfDepositReturn.setColumns(10);
		
		changeFont(font);		
	}

	public void updateOpenSum(double openSum) {
		tfOpenSum.setText("" + format.format(openSum) + " €");
	}
	
	public void changeFont(Font buttonAndComboFont) {
		lblDepositSpent.setFont(buttonAndComboFont);
		tfDepositSpent.setFont(buttonAndComboFont);
		
		lblDepositReturn.setFont(buttonAndComboFont);
		tfDepositReturn.setFont(buttonAndComboFont);
		
		lblInvoiceSum.setFont(buttonAndComboFont);
		tfInvoiceSum.setFont(buttonAndComboFont);
		
		lblDepositSum.setFont(buttonAndComboFont);
		tfDepositSum.setFont(buttonAndComboFont);
		
		lblOpenSum.setFont(buttonAndComboFont);
		tfOpenSum.setFont(buttonAndComboFont);		
	}


	public void updateAfterInvoiceChange(InvoiceAlert ia) {

		Invoice inv = ia.getInvoice();
		
		int depositCount = 0;
		int depositReturnCount = 0;
		for (int i = 0; i < inv.getInvoiceLines().size(); i++) {
			if (inv.getInvoiceLines().get(i).getProductID() == Variables.ProductDepositID){
				depositCount = depositCount + inv.getInvoiceLines().get(i).getCount();
			} else if (inv.getInvoiceLines().get(i).getProductID() == Variables.ProductDepositReturnID){
				depositReturnCount = depositReturnCount + inv.getInvoiceLines().get(i).getCount();
			}
		}
		
		tfInvoiceSum.setText("" + format.format(inv.getInvoiceSum()) + " €");
		tfDepositSum.setText(""+format.format((depositCount - depositReturnCount) * 2.0)+" €");
		
		int depDiff = depositCount-depositReturnCount;
		if (depDiff>0){
			tfDepositSpent.setText("" + depDiff);
			tfDepositReturn.setText("0");
		} else if (depDiff==0){
			tfDepositSpent.setText("0");
			tfDepositReturn.setText("0");
		} else {
			tfDepositSpent.setText("0");
			tfDepositReturn.setText("" + Math.abs(depDiff));
		}
		
		
	}

}
