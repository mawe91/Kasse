package alertobjects;

import beans.Invoice;

public class InvoiceAlert {
	
	private Invoice invoice;
	
	public InvoiceAlert(Invoice inv) {
		// TODO Auto-generated constructor stub
		invoice=inv;
	}

	public Invoice getInvoice() {
		return invoice;
	}
	
}
