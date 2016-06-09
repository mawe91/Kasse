package ui.panels;


import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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

		orderTableModel = new DefaultTableModel();
		orderTableModel.addColumn("Produkte");
		orderTableModel.addColumn("Menge");
		orderTable = new JTable(orderTableModel);

		// Max Size second Column
		orderTable.getColumnModel().getColumn(1).setMaxWidth(1000);
		orderTable.getColumnModel().getColumn(1).setMinWidth(150);

		// Orinatation Center in second column
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		orderTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

		// Row hight
		orderTable.setRowHeight(40);

		// Font Settings
		orderTable.setFont(Variables.buttonAndComboFont);
		orderTable.getTableHeader().setFont(Variables.buttonAndComboFont);

		// Scrollcontainer
		jsp = new JScrollPane(orderTable);

		// Add to JPanel
		setConstraintSettings(0, 0, 1, 1, 1, 1);
		add(jsp, gbc);

		orderTable.getSelectionModel().addListSelectionListener(controller);
		
		//setBorder(getNewTitledBorder(""));
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
				orderTableModel.addRow(
						new Object[] { il.getVoucherOrProductName(), "" + il.getCount() });

			}

		}

	}
	
	public int getSelectedTableRow(){
		return orderTable.getSelectedRow();
	}

	public void changeToPayMode() {
		// TODO Auto-generated method stub

	}

	public void changeToSellingMode() {
		// TODO Auto-generated method stub

	}

	public void changeFont(Font buttonAndComboFont) {
		// TODO Auto-generated method stub
		orderTable.setFont(buttonAndComboFont);
		orderTable.getTableHeader().setFont(Variables.buttonAndComboFont);
	}

}
