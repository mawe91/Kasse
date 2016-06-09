package beans;

public class Product {

	private int id;
	private String name;
	private boolean isDrink;
	private int voucherID;

	public Product(int id, String name, boolean isDrink, int voucher) {
		super();
		this.id = id;
		this.name = name;
		this.isDrink = isDrink;
		this.voucherID = voucher;
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

	public boolean isDrink() {
		return isDrink;
	}

	public void setDrink(boolean isDrink) {
		this.isDrink = isDrink;
	}

	public int getVoucherID() {
		return voucherID;
	}

	public void setVoucherID(int voucher) {
		this.voucherID = voucher;
	}
	
	

}
