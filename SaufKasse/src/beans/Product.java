package beans;

public class Product {

	private int id;
	private String name;
	private int prodCat;
	private int voucherID;
	private boolean includeDeposit;

	public Product(int id, String name, int prodCat, int voucher, boolean incDeposit) {
		super();
		this.id = id;
		this.name = name;
		this.prodCat = prodCat;
		this.voucherID = voucher;
		this.includeDeposit = incDeposit;
	}

	public Product() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProdCat() {
		return prodCat;
	}

	public void setProdCat(int prodCat) {
		this.prodCat = prodCat;
	}

	public int getVoucherID() {
		return voucherID;
	}

	public void setVoucherID(int voucher) {
		this.voucherID = voucher;
	}

	public boolean isDepositIncluded() {
		return includeDeposit;
	}

	public void setDepositIncluded(boolean includeDeposit) {
		this.includeDeposit = includeDeposit;
	}
	
	

}
