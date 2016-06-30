package model;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;

import alertobjects.CalcFieldAlert;
import alertobjects.InvoiceAlert;
import alertobjects.MasterDataChangedAlert;
import alertobjects.OpenSumAlert;
import beans.Invoice;
import beans.Product;
import beans.Voucher;
import db.DBHandler;

public class Model extends Observable {

	private Invoice currentInvoice;
	private String calcText;
	private double paidSum;

	private ArrayList<Voucher> voucherArrayMock;
	private ArrayList<Product> productArrayMock;

	private ArrayList<Voucher> voucherCache;
	private ArrayList<Product> productCache;

	private DBHandler dbh;

	public Model(DBHandler dbh) {

		super();

		currentInvoice = null;
		calcText = "";
		paidSum = 0;

		this.dbh = dbh;

	}

	// Caching
	private void loadVoucherAndProductsIfNecessary() {
		if (productCache == null || voucherCache == null) {
			productCache = dbh.getAllProducts();
			voucherCache = dbh.getAllVouchers();

			setChanged();
			notifyObservers(new MasterDataChangedAlert(productCache, voucherCache));
			clearChanged();
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

	// Booking
	public void orderProduct(int productID) {

		generateInvoiceIfMissing();

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

		// Text l�schen
		deleteCalcValue();
	}

	public void orderVoucher(int voucherID) {

		generateInvoiceIfMissing();

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

		// text l�schen
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

	private void generateInvoiceIfMissing() {
		if (currentInvoice == null) {
			currentInvoice = new Invoice(dbh.generateNewInvoice(), getAllVouchers().size());
		}
	}

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
		currentInvoice = null;
		generateInvoiceIfMissing();
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
		// Wenn InvoiceSum kleiner bezahltem Betrag
		if (currentInvoice.getInvoiceSum() - paidSum <= 0) {
			return true;
		} else {
			return false;
		}
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

	public void inactivateProduct(Product product) {
		dbh.invalidateProductOrVocherInDB(product.getId(), "product");
		invalidateCaches();
		loadVoucherAndProductsIfNecessary();
	}

	public void inactivateVoucher(Voucher voucher) {
		dbh.invalidateProductOrVocherInDB(voucher.getId(), "voucher");
		invalidateCaches();
		loadVoucherAndProductsIfNecessary();
	}

	// gibt zur�ck ob Voucher noch bei einem Produkt angeh�ngt ist.
	public boolean isVoucherDeletable(Voucher voucher) {
		for (int i = 0; i < productCache.size(); i++) {
			if (productCache.get(i).getVoucherID() == voucher.getId()) {
				return false;
			}
		}
		return true;
	}

	public void saveNewVoucherAndRevalidateCaches(Voucher v) {
		dbh.saveNewVoucher(v);
		invalidateCaches();
		loadVoucherAndProductsIfNecessary();
	}

}
