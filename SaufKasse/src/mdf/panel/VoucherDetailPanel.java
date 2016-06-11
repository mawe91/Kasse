package mdf.panel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import beans.Voucher;
import controller.Controller;
import ui.panels.AbstractKassenPanel;
import utilities.Variables;

public class VoucherDetailPanel extends AbstractKassenPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField desicriptionTextField;
	private JTextField colorTextField;
	private JTextField priceTextField;

	private DecimalFormat format;

	private JButton del;
	private JButton save;

	public VoucherDetailPanel(Controller contr) {

		format = new DecimalFormat("#0.00");

		setConstraintSettings(0, 0, 0.1, 0, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel label = new JLabel("Beschreibung");
		label.setFont(Variables.buttonAndComboFont);
		add(label, gbc);

		gbc.gridx = 1;

		desicriptionTextField = new JTextField("Rot");
		desicriptionTextField.setFont(Variables.buttonAndComboFont);
		desicriptionTextField.setHorizontalAlignment(JTextField.CENTER);
		add(desicriptionTextField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;

		label = new JLabel("Farbe");
		label.setFont(Variables.buttonAndComboFont);
		add(label, gbc);

		gbc.gridx = 1;

		colorTextField = new JTextField(" ");
		colorTextField.setBackground(Color.BLACK);
		colorTextField.setFont(Variables.buttonAndComboFont);
		colorTextField.setHorizontalAlignment(JTextField.CENTER);
		add(colorTextField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;

		label = new JLabel("Preis");
		label.setFont(Variables.buttonAndComboFont);
		add(label, gbc);

		gbc.gridx = 1;

		priceTextField = new JTextField("1,10 €");
		priceTextField.setFont(Variables.buttonAndComboFont);
		priceTextField.setHorizontalAlignment(JTextField.CENTER);
		add(priceTextField, gbc);

		gbc.gridy = 3;
		gbc.gridx = 0;

		del = new JButton("Löschen");
		del.setFont(Variables.buttonAndComboFont);
		del.setActionCommand("MasterData.Löschen");
		del.addActionListener(contr);
		add(del, gbc);

		gbc.gridx = 1;

		save = new JButton("Speichern");
		save.setFont(Variables.buttonAndComboFont);
		save.setActionCommand("MasterData.Speichern");
		save.addActionListener(contr);
		save.setEnabled(false);
		add(save, gbc);
		
		setFieldsEditable(false);

	}

	public void updateValues(Voucher v, boolean deletable) {
		desicriptionTextField.setText(v.getDescription());
		colorTextField.setBackground(v.getColor());
		colorTextField.setText(String.format("#%02X%02X%02X", v.getColor().getRed(), v.getColor().getGreen(), v.getColor().getBlue()));
		del.setEnabled(deletable);
		priceTextField.setText(format.format(v.getPrice()) + " €");
		
		setFieldsEditable(false);

		save.setEnabled(false);

		revalidate();
		repaint();
	}

	public void showNewVoucherForm() {

		// Leere Felder laden
		desicriptionTextField.setText("");
		colorTextField.setText("");
		colorTextField.setBackground(Color.WHITE);
		priceTextField.setText("");

		// Bearbeiten ermöglichen
		setFieldsEditable(true);

		// Speichern aktivieren, löschen deaktivieren
		save.setEnabled(true);
		del.setEnabled(false);

		repaint();
		revalidate();
	}

	private void setFieldsEditable(boolean editable) {
		desicriptionTextField.setEditable(editable);
		priceTextField.setEditable(editable);
	}

	public Voucher getNewVoucher() {
		// TODO Auto-generated method stub
		return new Voucher(desicriptionTextField.getText(), Double.parseDouble(priceTextField.getText()), Color.decode(colorTextField.getText()));
	}

}
