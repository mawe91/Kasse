package ui.panels;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import alertobjects.InvoiceAlert;
import alertobjects.MasterDataChangedAlert;
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
	InvoiceSumPanel InvoiceSumPanel;

	public ScreenInfoArea(Controller controller) {

		super();
		
		setLayout(new GridLayout(0, 3));
				
		// Orderlist
		orderListPanel = new OrderListPanel(controller);
		add(orderListPanel);

		// sumAndDeletePanel
		InvoiceSumPanel = new InvoiceSumPanel();
		add(InvoiceSumPanel);
		
		// VoucherPanel
		voucherPanel = new VoucherPanel();
		add(voucherPanel);

	}

	public void initialize(ArrayList<Voucher> alv) {
		voucherPanel.initialize(alv);
	}

	public void changeToPayMode() {
		orderListPanel.changeToPayMode();
		InvoiceSumPanel.changeToPayMode();
		voucherPanel.changeToPayMode();
	}

	public void changeToSellingMode() {
		orderListPanel.changeToSellingMode();
		InvoiceSumPanel.changeToSellingMode();
		voucherPanel.changeToSellingMode();
		
	}

	public int getSelectedTableRow() {
		return orderListPanel.getSelectedTableRow();
	}
	
	public void updateAfterInvoiceChange(InvoiceAlert ia) {
		orderListPanel.printInvoice(ia.getInvoice());
		InvoiceSumPanel.updateInvoiceSum(ia.getInvoice().getInvoiceSum());
		InvoiceSumPanel.updateInvoiceID(ia.getInvoice().getId());
		voucherPanel.updateVoucherCount(ia.getInvoice().getVoucherCount());
	}

	public void updateAfterPaidSumChanged(OpenSumAlert psa) {

		InvoiceSumPanel.updateOpenSum(psa.getOpenSum());
		
	}

	public void changeFont(Font buttonAndComboFont) {
		orderListPanel.changeFont(buttonAndComboFont);
		InvoiceSumPanel.changeFont(buttonAndComboFont);
		voucherPanel.changeFont(buttonAndComboFont);
	}

	public void updateVoucherView(MasterDataChangedAlert mdca) {

		voucherPanel.repaintVoucherViewWithNewVoucherData(mdca.getVoucherList());
	}

	

}
