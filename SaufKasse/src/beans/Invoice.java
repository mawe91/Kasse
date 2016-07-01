package beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Invoice {

	private int id;
	private ArrayList<InvoiceLine> invoiceLines;

	public Invoice() {
		this.invoiceLines = new ArrayList<InvoiceLine>();
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

	public Map<Integer, Integer> getVoucherCount() {
		Map<Integer, Integer> myMap = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < invoiceLines.size(); i++) {
			Integer freq = myMap.get(invoiceLines.get(i).getVoucherID());
			myMap.put(invoiceLines.get(i).getVoucherID(), (freq == null) ? 1 : freq + 1);
		}

		return myMap;
	}

	public void deleteInvoiceLine(int lineNumber) {

		invoiceLines.remove(lineNumber);

	}

}
