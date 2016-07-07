package alertobjects;

import beans.Invoice;

public class InvoiceAlert {
	
	private Invoice invoice;
	
	public InvoiceAlert(Invoice inv) {
		invoice=inv;
	}

	public Invoice getInvoice() {
		return invoice;
	}
	
}
