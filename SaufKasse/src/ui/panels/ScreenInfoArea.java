package ui.panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import alertobjects.InvoiceAlert;
import alertobjects.OpenSumAlert;
import beans.Voucher;
import controller.Controller;

public class ScreenInfoArea extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	OrderListPanel orderListPanel;
	VoucherPanel voucherPanel;
	InvoiceSumPanel invoiceSumPanel;

	public ScreenInfoArea(Controller controller) {

		super();
		
		setLayout(new GridLayout(0, 3));
				
		// Orderlist
		orderListPanel = new OrderListPanel(controller);
		add(orderListPanel);

		// sumAndDeletePanel
		invoiceSumPanel = new InvoiceSumPanel();
		add(invoiceSumPanel);
		
		// VoucherPanel
		voucherPanel = new VoucherPanel();
		add(voucherPanel);

	}

	public void initialize(ArrayList<Voucher> alv) {
		voucherPanel.initialize(alv);
	}

	public void changeToPayMode() {
		invoiceSumPanel.changeToPayMode();
	}

	public void changeToSellingMode() {
		invoiceSumPanel.changeToSellingMode();		
	}

	public int getSelectedTableRow() {
		return orderListPanel.getSelectedTableRow();
	}
	
	public void updateAfterInvoiceChange(InvoiceAlert ia) {
		orderListPanel.printInvoice(ia.getInvoice());
		invoiceSumPanel.updateAfterInvoiceChange(ia);
		voucherPanel.updateVoucherCount(ia.getInvoice().getVoucherCount());
	}

	public void updateAfterPaidSumChanged(OpenSumAlert psa) {

		invoiceSumPanel.updateOpenSum(psa.getOpenSum());
		
	}

	public void changeFont(Font buttonAndComboFont) {
		orderListPanel.changeFont(buttonAndComboFont);
		invoiceSumPanel.changeFont(buttonAndComboFont);
		voucherPanel.changeFont(buttonAndComboFont);
	}
}
