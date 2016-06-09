package beans;

import java.awt.Color;
import java.text.DecimalFormat;

public class Voucher {

	private int id;
	private String description;
	private double price;
	private Color color;
	private DecimalFormat format;

	public Voucher(int id, String description, double price, Color color) {
		super();
		format = new DecimalFormat("#0.00");
		this.id = id;
		this.description = description;
		this.price = price;
		this.color = color;
	}

	public Voucher() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return id + " - " + description + " (" + format.format(price) + " €)";
	}
	
	
	
	
}
