package beans;

public class InvoiceLine {

	private int productID;
	private int voucherID;
	private String voucherOrProductName;
	private int count;
	private double singlePrice;
	

	public InvoiceLine(int voucherID, int count, double singlePrice, String voucherOrProductName) {
		super();
		this.voucherID = voucherID;
		this.count = count;
		this.singlePrice = singlePrice;
		this.voucherOrProductName = voucherOrProductName;
	}
	
	public InvoiceLine(int productID, int voucherID, int count, double singlePrice, String voucherOrProductName) {
		super();
		this.productID = productID;
		this.voucherID = voucherID;
		this.count = count;
		this.singlePrice = singlePrice;
		this.voucherOrProductName = voucherOrProductName;
	}

	public int getProductID() {
		return productID;
	}
	
	public int getVoucherID() {
		return voucherID;
	}

	public int getCount() {
		return count;
	}

	public double getSinglePrice() {
		return singlePrice;
	}
	
	public double getInvoiceLineSum() {
		return singlePrice*count;
	}

	public String getVoucherOrProductName() {
		return voucherOrProductName;
	}

	@Override
	public String toString() {
		return "InvoiceLine [productID=" + productID + ", voucherID=" + voucherID + ", voucherOrProductName="
				+ voucherOrProductName + ", count=" + count + ", singlePrice=" + singlePrice + "]";
	}
	
	

}
