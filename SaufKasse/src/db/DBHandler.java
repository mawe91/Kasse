package db;

import java.awt.Color;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

import beans.Invoice;
import beans.Product;
import beans.Voucher;

public class DBHandler {

	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite:" + System.getenv("APPDATA") + "\\Kasse\\kasse.db";

	// Database credentials
	private static final String USER = "root";
	private static final String PASS = "passwd";

	private File dir = new File(System.getenv("APPDATA") + "\\Kasse");

	// Variables
	private Connection connection;
	private Statement stmt;

	private static final String CREATE_TABLE1 = "CREATE TABLE Invoice (id INTEGER PRIMARY KEY AUTOINCREMENT, timestamp DATETIME NOT NULL);";
	private static final String CREATE_TABLE2 = "CREATE TABLE ProductCategory ( id INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT NOT NULL);";
	private static final String CREATE_TABLE3 = "CREATE TABLE Voucher (id INTEGER PRIMARY KEY AUTOINCREMENT, price DECIMAL(12, 2) NOT NULL, color TEXT NOT NULL, description TEXT NOT NULL);";
	private static final String CREATE_TABLE4 = "CREATE TABLE Product (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, voucher INTEGER NOT NULL REFERENCES Voucher (id), product_category INTEGER NOT NULL REFERENCES ProductCategory (id));";
	private static final String CREATE_TABLE5 = "CREATE INDEX idx_product__product_category ON Product (product_category);";
	private static final String CREATE_TABLE6 = "CREATE INDEX idx_product__voucher ON Product (voucher);";
	private static final String CREATE_TABLE7 = "CREATE TABLE InvoiceLine (id INTEGER PRIMARY KEY AUTOINCREMENT, product INTEGER REFERENCES Product (id), count INTEGER NOT NULL, invoice INTEGER NOT NULL REFERENCES Invoice (id), voucher INTEGER NOT NULL REFERENCES Voucher (id));";
	private static final String CREATE_TABLE8 = "CREATE INDEX idx_invoiceline__invoice ON InvoiceLine (invoice);";
	private static final String CREATE_TABLE9 = "CREATE INDEX idx_invoiceline__product ON InvoiceLine (product);";

	private static final String DATA_INIT_VOUCHER1 = "INSERT INTO voucher (id, price, color, description) VALUES (1,3.0,'#FF0004','<html>Bier - Schorle<br>Rote Wurst<br>Curry Wurst</html>');";
	private static final String DATA_INIT_VOUCHER2 = "INSERT INTO voucher (id, price, color, description) VALUES (2,3.5,'#F0FFFF','Steak');";
	private static final String DATA_INIT_VOUCHER3 = "INSERT INTO voucher (id, price, color, description) VALUES (3,2.5,'#C1FFC1','Weizenbier');";
	private static final String DATA_INIT_VOUCHER4 = "INSERT INTO voucher (id, price, color, description) VALUES (4,2.0,'#FFD700','<html>Spezi<br>Apfelschorle</html>');";
	private static final String DATA_INIT_VOUCHER5 = "INSERT INTO voucher (id, price, color, description) VALUES (5,1.7,'#4876FF','Cola, Bluna, Sprudel');";
	private static final String DATA_INIT_VOUCHER6 = "INSERT INTO voucher (id, price, color, description) VALUES (6,12.0,'#FFC1C1','Wein');";
	private static final String DATA_INIT_VOUCHER7 = "INSERT INTO voucher (id, price, color, description) VALUES (7,9.0,'#FFC1C1','<html>Wein<br>(Mittagessen)</html>');";
	private static final String DATA_INIT_VOUCHER8 = "INSERT INTO voucher (id, price, color, description) VALUES (8,2.0,'#FFFFFF','Pfand');";
	private static final String DATA_INIT_VOUCHER9 = "INSERT INTO voucher (id, price, color, description) VALUES (9,-2.0,'#FFFFFF','Pfand Rückgabe');";

	private static final String DATA_INIT_ProductCategory1 = "INSERT INTO productcategory (id, description) VALUES (1,'Alkohol');";
	private static final String DATA_INIT_ProductCategory2 = "INSERT INTO productcategory (id, description) VALUES (2,'Alkoholfrei');";
	private static final String DATA_INIT_ProductCategory3 = "INSERT INTO productcategory (id, description) VALUES (3,'Flaschen');";
	private static final String DATA_INIT_ProductCategory4 = "INSERT INTO productcategory (id, description) VALUES (4,'Essen');";
	private static final String DATA_INIT_ProductCategory5 = "INSERT INTO productcategory (id, description) VALUES (5,'Pfand');";

	private static final String DATA_INIT_Product1 = "INSERT INTO product (id, name, voucher, product_category) VALUES (1,'Bier',1,1);";
	private static final String DATA_INIT_Product2 = "INSERT INTO product (id, name, voucher, product_category) VALUES (2,'Weizen',3,1);";
	private static final String DATA_INIT_Product3 = "INSERT INTO product (id, name, voucher, product_category) VALUES (3,'Radler',1,1);";
	private static final String DATA_INIT_Product4 = "INSERT INTO product (id, name, voucher, product_category) VALUES (4,'1/4 Wein',2,1);";
	private static final String DATA_INIT_Product5 = "INSERT INTO product (id, name, voucher, product_category) VALUES (5,'Weinschorle',1,1);";
	private static final String DATA_INIT_Product6 = "INSERT INTO product (id, name, voucher, product_category) VALUES (6,'Cola',5,2);";
	private static final String DATA_INIT_Product7 = "INSERT INTO product (id, name, voucher, product_category) VALUES (7,'Spezi',4,2);";
	private static final String DATA_INIT_Product8 = "INSERT INTO product (id, name, voucher, product_category) VALUES (8,'Bluna',5,2);";
	private static final String DATA_INIT_Product9 = "INSERT INTO product (id, name, voucher, product_category) VALUES (9,'Sprudel',5,2);";
	private static final String DATA_INIT_Product10 = "INSERT INTO product (id, name, voucher, product_category) VALUES (10,'Apfelschorle',4,2);";
	private static final String DATA_INIT_Product11 = "INSERT INTO product (id, name, voucher, product_category) VALUES (11,'Fl. Wein',6,3);";
	private static final String DATA_INIT_Product12 = "INSERT INTO product (id, name, voucher, product_category) VALUES (12,'Fl. Sekt',6,3);";
	private static final String DATA_INIT_Product13 = "INSERT INTO product (id, name, voucher, product_category) VALUES (13,'Rote Wurst',1,4);";
	private static final String DATA_INIT_Product14 = "INSERT INTO product (id, name, voucher, product_category) VALUES (14,'Curry Wurst',1,4);";
	private static final String DATA_INIT_Product15 = "INSERT INTO product (id, name, voucher, product_category) VALUES (15,'Schnitzel',2,4);";
	private static final String DATA_INIT_Product16 = "INSERT INTO product (id, name, voucher, product_category) VALUES (16,'Pommes',4,4);";
	private static final String DATA_INIT_Product17 = "INSERT INTO product (id, name, voucher, product_category) VALUES (17,'Mittagessen',7,4);";
	private static final String DATA_INIT_Product18 = "INSERT INTO product (id, name, voucher, product_category) VALUES (18,'Pfand',7,5);";
	private static final String DATA_INIT_Product19 = "INSERT INTO product (id, name, voucher, product_category) VALUES (19,'Pfand Rückgabe',7,5);";

	private static final String GET_PRODUCTS = "SELECT * FROM product;";
	private static final String GET_VOUCHERS = "SELECT * FROM voucher;";

	public DBHandler() {
		dir.mkdir();
		openConnection();
	}

	private void openConnection() {

		try {

			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = connection.createStatement();

			// populate db and index if nessassary
			stmt.execute(CREATE_TABLE1);
			stmt.execute(CREATE_TABLE2);
			stmt.execute(CREATE_TABLE3);
			stmt.execute(CREATE_TABLE4);
			stmt.execute(CREATE_TABLE5);
			stmt.execute(CREATE_TABLE6);
			stmt.execute(CREATE_TABLE7);
			stmt.execute(CREATE_TABLE8);
			stmt.execute(CREATE_TABLE9);

			// Voucher Init
			stmt.execute(DATA_INIT_VOUCHER1);
			stmt.execute(DATA_INIT_VOUCHER2);
			stmt.execute(DATA_INIT_VOUCHER3);
			stmt.execute(DATA_INIT_VOUCHER4);
			stmt.execute(DATA_INIT_VOUCHER5);
			stmt.execute(DATA_INIT_VOUCHER6);
			stmt.execute(DATA_INIT_VOUCHER7);
			stmt.execute(DATA_INIT_VOUCHER8);
			stmt.execute(DATA_INIT_VOUCHER9);

			// Product Category Init
			stmt.execute(DATA_INIT_ProductCategory1);
			stmt.execute(DATA_INIT_ProductCategory2);
			stmt.execute(DATA_INIT_ProductCategory3);
			stmt.execute(DATA_INIT_ProductCategory4);
			stmt.execute(DATA_INIT_ProductCategory5);

			// Product Init
			stmt.execute(DATA_INIT_Product1);
			stmt.execute(DATA_INIT_Product2);
			stmt.execute(DATA_INIT_Product3);
			stmt.execute(DATA_INIT_Product4);
			stmt.execute(DATA_INIT_Product5);
			stmt.execute(DATA_INIT_Product6);
			stmt.execute(DATA_INIT_Product7);
			stmt.execute(DATA_INIT_Product8);
			stmt.execute(DATA_INIT_Product9);
			stmt.execute(DATA_INIT_Product10);
			stmt.execute(DATA_INIT_Product11);
			stmt.execute(DATA_INIT_Product12);
			stmt.execute(DATA_INIT_Product13);
			stmt.execute(DATA_INIT_Product14);
			stmt.execute(DATA_INIT_Product15);
			stmt.execute(DATA_INIT_Product16);
			stmt.execute(DATA_INIT_Product17);
			stmt.execute(DATA_INIT_Product18);
			stmt.execute(DATA_INIT_Product19);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Product> getAllProducts() {
		ArrayList<Product> productList = new ArrayList<Product>();
		try {
			ResultSet rs = stmt.executeQuery(GET_PRODUCTS);
			Product p;
			while (rs.next()) {
				int id = rs.getInt("id");
				int voucher = rs.getInt("voucher");
				int productCategory = rs.getInt("product_category");
				String name = rs.getString("name");
				p = new Product(id, name, productCategory, voucher);
				productList.add(p);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productList;
	}

	public ArrayList<Voucher> getAllVouchers() {
		ArrayList<Voucher> voucherList = new ArrayList<Voucher>();
		try {
			ResultSet rs = stmt.executeQuery(GET_VOUCHERS);
			Voucher v;
			while (rs.next()) {
				int id = rs.getInt("id");
				double price = rs.getDouble("price");
				String description = rs.getString("description");
				Color color;
				try {
					color = Color.decode(rs.getString("color"));
				} catch (Exception e) {
					color = null;
				}
				v = new Voucher(id, description, price, color);
				voucherList.add(v);

			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return voucherList;
	}

	public void storeInvoice(Invoice invoice) {

		try {
			// generate invoice
			String INSERT_RECORD = "insert into invoice(timestamp) values(?)";

			PreparedStatement pstmt = connection.prepareStatement(INSERT_RECORD);
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());
			pstmt.setTimestamp(1, sqlDate);

			pstmt.executeUpdate();
			pstmt.close();

			// get Invoice ID
			ResultSet rs = stmt.executeQuery("SELECT id from invoice order by ROWID DESC limit 1");
			rs.next();
			int invoiceId = rs.getInt("id");
			invoice.setId(invoiceId);
			rs.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// StoreInvoiceLines
		for (int i = 0; i < invoice.getInvoiceLines().size(); i++) {
			if (invoice.getInvoiceLines().get(i).getCount() != 0) {

				String sqlInsert;
				if (invoice.getInvoiceLines().get(i).getProductID() == 0) {
					sqlInsert = "INSERT INTO invoiceline (count, invoice, voucher) VALUES ("
							+ invoice.getInvoiceLines().get(i).getCount() + ", " + invoice.getId() + ", "
							+ invoice.getInvoiceLines().get(i).getVoucherID() + ")";
				} else {
					sqlInsert = "INSERT INTO invoiceline (product, count, invoice, voucher) VALUES ("
							+ invoice.getInvoiceLines().get(i).getProductID() + ", "
							+ invoice.getInvoiceLines().get(i).getCount() + ", " + invoice.getId() + ", "
							+ invoice.getInvoiceLines().get(i).getVoucherID() + ")";
				}

				try {
					stmt.executeUpdate(sqlInsert);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public int getProductPurchase(int pid) {

		int count = -1;

		try {
			ResultSet rs = stmt.executeQuery("SELECT sum(count) FROM InvoiceLine WHERE product=" + pid + ";");
			while (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	public TimeSeries getProductTimeSeries(int pid) {
		TimeSeries s1 = new TimeSeries("Produkt-ID: " + pid);

		try {
			ResultSet rs = stmt.executeQuery(
					"SELECT invoice.timestamp, invoiceline.count FROM Invoice INNER JOIN Invoiceline ON Invoice.id=Invoiceline.invoice WHERE Invoiceline.product="
							+ pid + ";");
			int soldsum = 0;
			while (rs.next()) {
				long ts = new Timestamp(rs.getLong("timestamp")).getTime();
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(ts);
				// Wenn Time Objekt schon besteht
				Minute m = new Minute(cal.get(Calendar.MINUTE), cal.get(Calendar.HOUR_OF_DAY),
						cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
				soldsum = rs.getInt("count");
				if (s1.getDataItem(m) != null) {
					soldsum = s1.getDataItem(m).getValue().intValue() + soldsum;
					s1.delete(m);
				}
				s1.add(m, soldsum);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return s1;
	}
}
