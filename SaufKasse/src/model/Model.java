package model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;

import org.jfree.data.category.DefaultCategoryDataset;

import alertobjects.CalcFieldAlert;
import alertobjects.InvoiceAlert;
import alertobjects.OpenSumAlert;
import beans.Invoice;
import beans.Product;
import beans.Voucher;
import db.DBHandler;

public class Model extends Observable {

	private Invoice currentInvoice;
	private String calcText;
	private double paidSum;

	private ArrayList<Voucher> voucherCache;
	private ArrayList<Product> productCache;

	private DBHandler dbh;

	public Model(DBHandler dbh) {

		super();

		currentInvoice = new Invoice();
		calcText = "";
		paidSum = 0;

		this.dbh = dbh;

	}

	// Caching
	private void loadVoucherAndProductsIfNecessary() {
		if (productCache == null || voucherCache == null) {
			productCache = dbh.getAllProducts();
			voucherCache = dbh.getAllVouchers();
		}
	}

	public void invalidateCaches() {
		productCache = null;
		voucherCache = null;
	}

	// Voucher Getters
	public ArrayList<Voucher> getAllVouchers() {
		loadVoucherAndProductsIfNecessary();
		return voucherCache;
	}

	public Voucher getVoucherByName(String vName) {
		for (int i = 0; i < getAllVouchers().size(); i++) {
			if (getAllVouchers().get(i).getDescription().equals(vName)) {
				return getAllVouchers().get(i);
			}
		}
		return null;
	}

	public Voucher getVoucherById(int searchedId) {
		ArrayList<Voucher> al = getAllVouchers();
		for (int i = 0; i < al.size(); i++) {
			if (al.get(i).getId() == searchedId) {
				return al.get(i);
			}
		}
		return null;
	}

	// Product Getters
	public ArrayList<Product> getAllProducts() {
		loadVoucherAndProductsIfNecessary();
		return productCache;
	}

	public Product getProductById(int searchedId) {
		ArrayList<Product> al = getAllProducts();
		for (int i = 0; i < al.size(); i++) {
			if (al.get(i).getId() == searchedId) {
				return al.get(i);
			}
		}
		return null;
	}

	private ArrayList<Product> getProductsByCategory(int catid) {
		ArrayList<Product> al = new ArrayList<Product>();
		for (int i = 0; i < getAllProducts().size(); i++) {
			if (getAllProducts().get(i).getProdCat() == catid) {
				al.add(getAllProducts().get(i));
			}
		}
		return al;
	}

	// Booking
	public void orderProduct(int productID) {

		int vid;

		vid = getProductById(productID).getVoucherID();

		if (calcText == "" || !calcText.substring(calcText.length() - 1).equals("X")) {

			// einmal buchen
			currentInvoice.orderProduct(productID, vid, getVoucherById(vid).getPrice(),
					getProductById(productID).getName());

		} else {

			// Mehrfachbuchen
			currentInvoice.orderProduct(productID, vid, Integer.parseInt(calcText.substring(0, calcText.length() - 2)),
					getVoucherById(vid).getPrice(), getProductById(productID).getName());
		}

		notifyInvoiceChange();

		// Text löschen
		deleteCalcValue();
	}

	public void orderVoucher(int voucherID) {

		if (calcText == "" || !calcText.substring(calcText.length() - 1).equals("X")) {
			// einmal buchen
			currentInvoice.orderVoucher(voucherID, getVoucherById(voucherID).getPrice(),
					getVoucherById(voucherID).getDescription());

		} else {
			// Mehrfachbuchen
			currentInvoice.orderVoucher(voucherID, Integer.parseInt(calcText.substring(0, calcText.length() - 2)),
					getVoucherById(voucherID).getPrice(), getVoucherById(voucherID).getDescription());
		}

		notifyInvoiceChange();

		// text löschen
		deleteCalcValue();
	}

	// Calculator
	public void addCalculatorNumber(int i) {
		if (isValidNumberInStringCheck(calcText + i)) {
			calcText = calcText + i;
			notifyCalcChange();
		}
	}

	public void addCalculatorComma() {
		calcText = calcText + ",";
		notifyCalcChange();
	}

	public void multiplyCount() {
		if (calcText != "") {
			calcText = calcText + " X";
			notifyCalcChange();
		}

	}

	public void deleteCalcValue() {
		if (calcText != "") {
			calcText = "";
		}
		notifyCalcChange();
	}

	private void notifyCalcChange() {
		setChanged();
		notifyObservers(new CalcFieldAlert(calcText));
		clearChanged();
	}

	// Invoice
	private void notifyInvoiceChange() {
		setChanged();
		notifyObservers(new InvoiceAlert(currentInvoice));
		clearChanged();

		// Aktualisiere Offene Summe
		notifySumChange();
	}

	public void deleteInvoiceLines(int selectedTableRowStart, int selectedTableRowEnd) {
		for (int i = selectedTableRowEnd; i >= selectedTableRowStart; i--) {
			currentInvoice.deleteInvoiceLine(i);
		}
		notifyInvoiceChange();
	}

	public int getCurrentInvoiceLineCount() {
		return currentInvoice.getInvoiceLines().size();
	}

	public void saveAndInitInvoice() {
		// Store Invoice
		dbh.storeInvoice(currentInvoice);

		deleteAndInitNewInvoice();

	}

	public void deleteAndInitNewInvoice() {

		// Generate New Empty Invocie
		currentInvoice = new Invoice();
		notifyInvoiceChange();

		// SetPaidSum to Zero
		deletePaidSum();
	}

	// Payment
	public void payUnroundSum() {
		// Get Sum from CalcField and cast to double
		double cash = 0;

		if (calcText.equals("")) {
			paidSum = currentInvoice.getInvoiceSum();

		} else {
			try {
				NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
				Number number = format.parse(calcText);
				cash = number.doubleValue();

			} catch (Exception e) {
				// Nothing we can do
			}

			paidSum = paidSum + cash;
		}

		notifySumChange();
		deleteCalcValue();
	}

	public void paySum(double cash) {

		paidSum = paidSum + cash;
		notifySumChange();

		deleteCalcValue();

	}

	private void notifySumChange() {
		setChanged();
		notifyObservers(new OpenSumAlert(currentInvoice.getInvoiceSum() - paidSum));
		clearChanged();
	}

	public boolean isPaidSumBiggerThanInvoiceSum() {

		return currentInvoice.getInvoiceSum() - paidSum <= 0;

	}

	public void deletePaidSum() {
		paidSum = 0;
		notifySumChange();
	}

	// Util
	private boolean isValidNumberInStringCheck(String s) {
		try {
			if (s.indexOf(',') > 0) {
				NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
				Number number = format.parse(s);
				number.doubleValue();
				return true;
			} else {
				Integer.parseInt(s);
				return true;
			}

		} catch (Exception e) {

			return false;

		}

	}

	// gibt zurück ob Voucher noch bei einem Produkt angehängt ist.
	public boolean isVoucherDeletable(Voucher voucher) {
		for (int i = 0; i < productCache.size(); i++) {
			if (productCache.get(i).getVoucherID() == voucher.getId()) {
				return false;
			}
		}
		return true;
	}

	public DefaultCategoryDataset getSoldProductsDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < getAllProducts().size(); i++) {
			dataset.setValue(dbh.getProductPurchase(i + 1), "", getProductById(i + 1).getName());
		}

		return dataset;
	}

}
