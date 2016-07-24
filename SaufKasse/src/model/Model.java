package model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jsoup.Jsoup;

import alertobjects.CalcFieldAlert;
import alertobjects.InvoiceAlert;
import alertobjects.OpenSumAlert;
import beans.Invoice;
import beans.Product;
import beans.Voucher;
import db.DBHandler;
import utilities.Variables;

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

	// Booking
	public void orderProduct(int productID) {

		int vid;

		vid = getProductById(productID).getVoucherID();

		if (calcText == "" || !calcText.substring(calcText.length() - 1).equals("X")) {

			// einmal buchen
			currentInvoice.orderProduct(productID, vid, getVoucherById(vid).getPrice(),
					getProductById(productID).getName());
			// Pfand buchen wenn notwendig
			if (getProductById(productID).isDepositIncluded()) {
				currentInvoice.orderProduct(Variables.productDepositID, Variables.voucherDepositID,
						getVoucherById(Variables.voucherDepositID).getPrice(), getProductById(Variables.productDepositID).getName());
			}

		} else {

			// Mehrfachbuchen
			int orderquantity = Integer.parseInt(calcText.substring(0, calcText.length() - 2));
			currentInvoice.orderProduct(productID, vid, orderquantity, getVoucherById(vid).getPrice(),
					getProductById(productID).getName());
			// Pfand buchen wenn notwendig
			if (getProductById(productID).isDepositIncluded()) {
				currentInvoice.orderProduct(Variables.productDepositID, Variables.voucherDepositID, orderquantity,
						getVoucherById(Variables.voucherDepositID).getPrice(), getProductById(Variables.productDepositID).getName());
			}
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
			int orderquantity = Integer.parseInt(calcText.substring(0, calcText.length() - 2));
			currentInvoice.orderVoucher(voucherID, orderquantity, getVoucherById(voucherID).getPrice(),
					getVoucherById(voucherID).getDescription());
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

	public DefaultCategoryDataset getSoldCountDataset(String entity, ArrayList<?> entityArray) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < entityArray.size(); i++) {
			String name;
			if (entity.equals("product")) {
				name = getProductById(i + 1).getName();
			} else {
				name = getVoucherById(i + 1).getDescription();
				name = Jsoup.parse(name).text();
			}
			dataset.setValue(dbh.getPurchaseCountWhere(entity + "=" + (i + 1)), "", name);
		}

		return dataset;
	}

	public XYDataset getSalesTimeSeries() {
		final TimeSeriesCollection dataset = new TimeSeriesCollection();

		TimeSeries ts;
		for (int i = 0; i < getAllProducts().size(); i++) {
			ts = dbh.getProductTimeSeries(i);
			dataset.addSeries(ts);
		}

		return dataset;

	}

	public double getTotalSaleSum() {

		return dbh.getTotalInEuro("invoiceline.voucher>0");
	}

	public double getTotalDepositSum() {
		return dbh.getTotalInEuro("invoiceline.product='" + Variables.productDepositID + "' OR invoiceline.product='"
				+ Variables.productDepositReturnID + "'");
	}

	public double getTotalVoucherSum() {
		return dbh.getTotalInEuro("invoiceline.product IS NULL");
	}

	public double getTotalProductSum() {

		return dbh.getTotalInEuro("invoiceline.product IS NOT NULL");
	}

	public int getSoldProductWithoutVoucher() {

		int count = 0;
		for (int i = 1; i < Variables.productDepositID; i++) {
			count = count + dbh.getPurchaseCountWhere("product=" + i);
		}
		return count;
	}

	public int getSoldVouchersWithoutDepositWithoutProducts() {

		return dbh.getPurchaseCountWhere("product IS NULL");
	}

	public int getSoldVouchersWithoutDepositWithProducts() {

		return dbh.getPurchaseCountWhere("product IS NOT '" + Variables.productDepositID + "' AND product IS NOT '"
				+ Variables.productDepositReturnID + "'");
	}

	public int getSoldAndRefundDepositDifference() {
		return dbh.getPurchaseCountWhere("product='" + Variables.productDepositID + "'")
				- dbh.getPurchaseCountWhere("product='" + Variables.productDepositReturnID + "'");
	}

	public DefaultCategoryDataset getSoldVoucherDatasetWithoutDeposit() {

		ArrayList<Voucher> ar = new ArrayList<>();
		for (int i = 0; i < getAllVouchers().size(); i++) {
			if (getAllVouchers().get(i).getId() != Variables.voucherDepositID
					&& getAllVouchers().get(i).getId() != Variables.voucherDepositReturnID) {
				ar.add(getAllVouchers().get(i));
			}
		}

		return getSoldCountDataset("voucher", ar);

	}

	public DefaultCategoryDataset getSoldProductsWithoutDeposit() {

		ArrayList<Product> ar = new ArrayList<>();
		for (int i = 0; i < getAllProducts().size(); i++) {
			if (getAllProducts().get(i).getId() != Variables.productDepositID
					&& getAllProducts().get(i).getId() != Variables.productDepositReturnID) {
				ar.add(getAllProducts().get(i));
			}
		}
		return getSoldCountDataset("product", ar);
	}

	public DefaultPieDataset getDepositPieData() {

		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Pfand Ausgabe", dbh.getPurchaseCountWhere("product=" + Variables.productDepositID));
		dataset.setValue("Pfand Rückgabe", dbh.getPurchaseCountWhere("product=" + Variables.productDepositReturnID));

		return dataset;
	}

}
