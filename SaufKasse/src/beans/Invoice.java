package beans;

import java.util.ArrayList;

public class Invoice {

	private int id;
	private ArrayList<InvoiceLine> invoiceLines;
	private int voucherCount;

	public Invoice(int id, int voucherNumber) {

		this.id = id;
		this.invoiceLines = new ArrayList<InvoiceLine>();
		this.voucherCount=voucherNumber;
	}

	public int getId() {
		return id;
	}

	public ArrayList<InvoiceLine> getInvoiceLines() {
		return invoiceLines;
	}

	public void orderProduct(int pid, int vid, double singlePrice, String name) {
		orderProduct(pid, vid, 1, singlePrice, name);
	}

	public void orderProduct(int pid, int vid, int count, double singlePrice, String name) {
		invoiceLines.add(new InvoiceLine(pid, vid, count, singlePrice, name));
	}

	public void orderVoucher(int vid, double singlePrice, String name) {
		orderVoucher(vid, 1, singlePrice, name);
	}

	public void orderVoucher(int vid, int count, double singlePrice, String name) {
		invoiceLines.add(new InvoiceLine(vid, count, singlePrice, name));
	}

	public double getInvoiceSum() {
		double total = 0;
		for (int i = 0; i < invoiceLines.size(); i++) {
			total = total + invoiceLines.get(i).getInvoiceLineSum();
		}
		return total;
	}

	public int[] getVoucherCount() {
		int[] vouchers = new int[voucherCount];

		for (int i = 0; i < vouchers.length; i++) {
			vouchers[i] = 0;
		}

		for (int i = 0; i < invoiceLines.size(); i++) {
			vouchers[invoiceLines.get(i).getVoucherID() - 1] = vouchers[invoiceLines.get(i).getVoucherID() - 1]
					+ invoiceLines.get(i).getCount();
		}

		return vouchers;
	}

	public void deleteInvoiceLine(int lineNumber) {

		invoiceLines.remove(lineNumber);

	}

}
