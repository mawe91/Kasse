package mdf.panel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import beans.Product;
import beans.Voucher;
import controller.Controller;
import ui.panels.AbstractKassenPanel;
import utilities.Variables;

public class ProductDetailPanel extends AbstractKassenPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField productNameField;
	JComboBox<Voucher> voucherComboBox;

	public ProductDetailPanel(Controller contr) {
		super();

		setConstraintSettings(0, 0, 0.1, 0, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel label = new JLabel("Beschreibung");
		label.setFont(Variables.buttonAndComboFont);
		add(label, gbc);

		gbc.gridx = 1;

		productNameField = new JTextField("Schnitzel");
		productNameField.setFont(Variables.buttonAndComboFont);
		productNameField.setHorizontalAlignment(JTextField.CENTER);
		productNameField.setEditable(false);
		add(productNameField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;

		label = new JLabel("Märkle");
		label.setFont(Variables.buttonAndComboFont);
		add(label, gbc);

		gbc.gridx = 1;

		voucherComboBox = new JComboBox<Voucher>();
		voucherComboBox.setFont(Variables.buttonAndComboFont);
		add(voucherComboBox, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;

		JButton del = new JButton("Löschen");
		del.setActionCommand("MasterData.Löschen");
		del.setFont(Variables.buttonAndComboFont);
		del.addActionListener(contr);
		add(del, gbc);

	}

	public void updateValues(Product p, ArrayList<Voucher> voucherCacheMDF) {
		productNameField.setText(p.getName());
		voucherComboBox.removeAllItems();
		for (int j = 0; j < voucherCacheMDF.size(); j++) {
			voucherComboBox.addItem(voucherCacheMDF.get(j));
		}
		
		//Select right Voucher from id
		for (int i = 0; i < voucherComboBox.getItemCount(); i++) {
			if (voucherComboBox.getItemAt(i).getId() == p.getVoucherID()){
				voucherComboBox.setSelectedIndex(i);
			}
		}
		
		//TODO: ComboBox not editable
			
		voucherComboBox.setEnabled(false);
		
		revalidate();
		repaint();
	}

}
