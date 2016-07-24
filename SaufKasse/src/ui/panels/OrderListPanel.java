package ui.panels;


import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import beans.Invoice;
import beans.InvoiceLine;
import controller.Controller;
import utilities.Variables;

public class OrderListPanel extends AbstractKassenPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultTableModel orderTableModel;
	private JScrollPane jsp;
	JTable orderTable;
	
	public OrderListPanel(Controller controller) {
		
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Bon", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)));

		orderTableModel = new DefaultTableModel();
		orderTableModel.addColumn("Produkte");
		orderTableModel.addColumn("Menge");
		orderTableModel.addColumn("Preis");
		orderTable = new JTable(orderTableModel);

		// Max Size second Column
		orderTable.getColumnModel().getColumn(0).setPreferredWidth(400);
		orderTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		orderTable.getColumnModel().getColumn(2).setPreferredWidth(30);
		
		// Orinatation Center in second column
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		orderTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		orderTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		
		
		// Row hight
		orderTable.setRowHeight(40);

		// Font Settings
		orderTable.setFont(Variables.buttonAndComboFont);
		orderTable.getTableHeader().setFont(Variables.menuFont);

		// Scrollcontainer
		jsp = new JScrollPane(orderTable);

		// Add to JPanel
		setConstraintSettings(0, 0, 1, 1, 1, 1);
		add(jsp, gbc);

		orderTable.getSelectionModel().addListSelectionListener(controller);
		
	}

	public void resetOrderList() {
		int rows = orderTableModel.getRowCount();
		if (rows != 0) {
			while (rows > 0) {
				orderTableModel.removeRow(0);
				rows = orderTableModel.getRowCount();
			}

		}

	}

	public void printInvoice(Invoice inv) {
		resetOrderList();
		if (inv != null) {
			for (int i = inv.getInvoiceLines().size(); i > 0; i--) {
				InvoiceLine il = inv.getInvoiceLines().get(i - 1);
				String name = il.getVoucherOrProductName();
				name =  name.replace("<br>", " ");
				orderTableModel.addRow(
						new Object[] { name, "" + il.getCount(), Variables.moneyFormatter.format(il.getInvoiceLineSum())});

			}

		}

	}
	
	public int getSelectedTableRow(){
		return orderTable.getSelectedRow();
	}

	public void changeFont(Font buttonAndComboFont) {
		orderTable.setFont(buttonAndComboFont);
		orderTable.getTableHeader().setFont(Variables.buttonAndComboFont);
	}

}
