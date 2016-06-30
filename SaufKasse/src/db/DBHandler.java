package db;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import beans.Invoice;
import beans.Product;
import beans.Voucher;

public class DBHandler {

	private static Logger log = Logger.getLogger(DBHandler.class.getName());

	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite:kasse.db";

	// Database credentials
	private static final String USER = "root";
	private static final String PASS = "passwd";

	// Variables
	private Connection connection;
	private Statement stmt;

	private static final String CREATE_TABLE1 = "CREATE TABLE Invoice (id INTEGER PRIMARY KEY AUTOINCREMENT, timestamp DATETIME NOT NULL);";
	private static final String CREATE_TABLE2 = "CREATE TABLE ProductCategory ( id INTEGER PRIMARY KEY AUTOINCREMENT, description TEXT NOT NULL);";
	private static final String CREATE_TABLE3 = "CREATE TABLE Voucher (id INTEGER PRIMARY KEY AUTOINCREMENT, price DECIMAL(12, 2) NOT NULL, color TEXT NOT NULL, description TEXT NOT NULL);";
	private static final String CREATE_TABLE4 = "CREATE TABLE Product (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, isDrink BOOLEAN NOT NULL, voucher INTEGER NOT NULL REFERENCES Voucher (id), product_category INTEGER NOT NULL REFERENCES ProductCategory (id));";
	private static final String CREATE_TABLE5 = "CREATE INDEX idx_product__product_category ON Product (product_category);";
	private static final String CREATE_TABLE6 = "CREATE INDEX idx_product__voucher ON Product (voucher);";
	private static final String CREATE_TABLE7 = "CREATE TABLE InvoiceLine ( id INTEGER PRIMARY KEY AUTOINCREMENT, product INTEGER NOT NULL REFERENCES Product (id), count INTEGER NOT NULL, invoice INTEGER NOT NULL REFERENCES Invoice (id));";
	private static final String CREATE_TABLE8 = "CREATE INDEX idx_invoiceline__invoice ON InvoiceLine (invoice);";
	private static final String CREATE_TABLE9 = "CREATE INDEX idx_invoiceline__product ON InvoiceLine (product);";


	public DBHandler() {
		openConnection();

	}

	private void openConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = connection.createStatement();
			stmt.execute(CREATE_TABLE1);
			stmt.execute(CREATE_TABLE2);
			stmt.execute(CREATE_TABLE3);
			stmt.execute(CREATE_TABLE4);
			stmt.execute(CREATE_TABLE5);
			stmt.execute(CREATE_TABLE6);
			stmt.execute(CREATE_TABLE7);
			stmt.execute(CREATE_TABLE8);
			stmt.execute(CREATE_TABLE9);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.toString());
		}
	}

	public ArrayList<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Voucher> getAllVouchers() {
		// TODO Auto-generated method stub
		return null;
	}

	public int generateNewInvoice() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void invalidateProductOrVocherInDB(int id, String string) {
		// TODO Auto-generated method stub

	}

	public void saveNewVoucher(Voucher v) {
		// TODO Auto-generated method stub

	}

	public void storeInvoice(Invoice currentInvoice) {
		// TODO Auto-generated method stub

	}

	/*
	 * 
	 * public ArrayList<Voucher> getAllVouchers() { Connection conn = connect();
	 * Statement stmt = null; ArrayList<Voucher> voucherList = new
	 * ArrayList<Voucher>(); try { stmt = conn.createStatement();
	 * 
	 * String sql = "SELECT id,description, price, color, isActive FROM voucher"
	 * ; ResultSet rs = stmt.executeQuery(sql);
	 * 
	 * Voucher v; // Extract data from result set while (rs.next()) { //
	 * Retrieve by column name int id = rs.getInt("id"); double name =
	 * rs.getDouble("price"); String description = rs.getString("description");
	 * Color color; try { color = Color.decode(rs.getString("color"));
	 * 
	 * } catch (Exception e) { color = null; // Not defined } Boolean isActive =
	 * rs.getBoolean("isActive");
	 * 
	 * if (isActive) { v = new Voucher(id, description, name, color);
	 * voucherList.add(v); } }
	 * 
	 * // Clean-up environment rs.close(); stmt.close(); disconnect();
	 * 
	 * } catch (SQLException se) { // Handle errors for JDBC
	 * se.printStackTrace(); } catch (Exception e) { // Handle errors for
	 * Class.forName e.printStackTrace(); } finally { // finally block used to
	 * close resources try { if (stmt != null) stmt.close(); } catch
	 * (SQLException se2) { } // nothing we can do
	 * 
	 * disconnect(); } return voucherList;
	 * 
	 * }
	 * 
	 * public ArrayList<Product> getAllProducts() { Connection conn = connect();
	 * Statement stmt = null; ArrayList<Product> productList = new
	 * ArrayList<Product>(); try { stmt = conn.createStatement();
	 * 
	 * String sql = "SELECT id, name, isDrink, voucher, isActive FROM product";
	 * ResultSet rs = stmt.executeQuery(sql);
	 * 
	 * Product p; // Extract data from result set while (rs.next()) { //
	 * Retrieve by column name int id = rs.getInt("id"); String name =
	 * rs.getString("name"); Boolean isDrink = rs.getBoolean("isDrink"); int
	 * voucher = rs.getInt("voucher"); Boolean isActive =
	 * rs.getBoolean("isActive");
	 * 
	 * if (isActive) { p = new Product(id, name, isDrink, voucher);
	 * productList.add(p); }
	 * 
	 * }
	 * 
	 * // Clean-up environment rs.close(); stmt.close(); disconnect();
	 * 
	 * } catch (SQLException se) { // Handle errors for JDBC
	 * se.printStackTrace(); } catch (Exception e) { // Handle errors for
	 * Class.forName e.printStackTrace(); } finally { // finally block used to
	 * close resources try { if (stmt != null) stmt.close(); } catch
	 * (SQLException se2) { } // nothing we can do
	 * 
	 * disconnect(); }
	 * 
	 * return productList;
	 * 
	 * }
	 * 
	 * public int generateNewInvoice() { Connection conn = connect(); Statement
	 * stmt = null; int lastid = 0; try { stmt = conn.createStatement();
	 * 
	 * String sqlInsert =
	 * "INSERT INTO Invoice (timestamp) VALUES (CURRENT_TIMESTAMP)";
	 * stmt.executeUpdate(sqlInsert);
	 * 
	 * ResultSet rs = stmt.executeQuery(
	 * "select last_insert_id() as last_id from invoice"); rs.next(); lastid =
	 * rs.getInt("last_id");
	 * 
	 * // Clean-up environment stmt.close(); disconnect();
	 * 
	 * } catch (SQLException se) { // Handle errors for JDBC
	 * se.printStackTrace(); } catch (Exception e) { // Handle errors for
	 * Class.forName e.printStackTrace(); } finally { // finally block used to
	 * close resources try { if (stmt != null) stmt.close(); } catch
	 * (SQLException se2) { } // nothing we can do
	 * 
	 * disconnect(); }
	 * 
	 * return lastid; }
	 * 
	 * public Timestamp getInvoiceTimestamp(int InvoiceID) { Connection conn =
	 * connect(); Statement stmt = null; Timestamp ts = null; try { stmt =
	 * conn.createStatement();
	 * 
	 * String sqlInsert = "SELECT timestamp From invoice WHERE id=" + InvoiceID;
	 * ResultSet rs = stmt.executeQuery(sqlInsert);
	 * 
	 * rs.next(); ts = rs.getTimestamp("timestamp");
	 * 
	 * // Clean-up environment stmt.close(); disconnect();
	 * 
	 * } catch (SQLException se) { // Handle errors for JDBC
	 * se.printStackTrace(); } catch (Exception e) { // Handle errors for
	 * Class.forName e.printStackTrace(); } finally { // finally block used to
	 * close resources try { if (stmt != null) stmt.close(); } catch
	 * (SQLException se2) { } // nothing we can do
	 * 
	 * disconnect(); }
	 * 
	 * return ts; }
	 * 
	 * public void storeInvoice(Invoice invoice) { Connection conn = connect();
	 * Statement stmt = null; try { stmt = conn.createStatement();
	 * 
	 * for (int i = 0; i < invoice.getInvoiceLines().size(); i++) { if
	 * (invoice.getInvoiceLines().get(i).getCount() != 0) {
	 * 
	 * String sqlInsert; if (invoice.getInvoiceLines().get(i).getProductID() ==
	 * 0) { sqlInsert =
	 * "INSERT INTO invoiceline (count, invoice, voucher) VALUES (" +
	 * invoice.getInvoiceLines().get(i).getCount() + ", " + invoice.getId() +
	 * ", " + invoice.getInvoiceLines().get(i).getVoucherID() + ")"; } else {
	 * sqlInsert =
	 * "INSERT INTO invoiceline (product, count, invoice, voucher) VALUES (" +
	 * invoice.getInvoiceLines().get(i).getProductID() + ", " +
	 * invoice.getInvoiceLines().get(i).getCount() + ", " + invoice.getId() +
	 * ", " + invoice.getInvoiceLines().get(i).getVoucherID() + ")"; }
	 * 
	 * stmt.executeUpdate(sqlInsert); } }
	 * 
	 * // Clean-up environment stmt.close(); disconnect();
	 * 
	 * } catch (SQLException se) { // Handle errors for JDBC
	 * se.printStackTrace(); } catch (Exception e) { // Handle errors for
	 * Class.forName e.printStackTrace(); } finally { // finally block used to
	 * close resources try { if (stmt != null) stmt.close(); } catch
	 * (SQLException se2) { } // nothing we can do
	 * 
	 * disconnect(); }
	 * 
	 * }
	 * 
	 * public Connection connect() { if (connection == null) { try { // Register
	 * JDBC driver Class.forName("com.mysql.jdbc.Driver");
	 * 
	 * // Open a connection connection = DriverManager.getConnection(DB_URL,
	 * USER, PASS); } catch (ClassNotFoundException e) { e.printStackTrace(); }
	 * catch (SQLException e) { e.printStackTrace(); } } return connection;
	 * 
	 * }
	 * 
	 * public void disconnect() { if (connection != null) { try {
	 * connection.close(); connection = null; } catch (SQLException e) {
	 * e.printStackTrace(); } } }
	 * 
	 * public void invalidateProductOrVocherInDB(int iD, String classname) {
	 * Connection conn = connect(); Statement stmt = null; try { stmt =
	 * conn.createStatement();
	 * 
	 * String sqlInsert; sqlInsert = "UPDATE `kasse`.`" + classname +
	 * "` SET `isActive`='0' WHERE `id`='" + iD + "';";
	 * 
	 * stmt.executeUpdate(sqlInsert);
	 * 
	 * // Clean-up environment stmt.close();
	 * 
	 * disconnect();
	 * 
	 * } catch (
	 * 
	 * SQLException se)
	 * 
	 * { // Handle errors for JDBC se.printStackTrace(); } catch (
	 * 
	 * Exception e)
	 * 
	 * { // Handle errors for Class.forName e.printStackTrace(); } finally
	 * 
	 * { // finally block used to close resources try { if (stmt != null)
	 * stmt.close(); } catch (SQLException se2) { } // nothing we can do
	 * 
	 * disconnect(); } }
	 * 
	 * public void saveNewVoucher(Voucher v) { Connection conn = connect();
	 * Statement stmt = null; try { stmt = conn.createStatement(); Color vcol =
	 * v.getColor(); String sqlInsert; sqlInsert =
	 * "INSERT INTO voucher (price, description, color, isActive) VALUES (" +
	 * v.getPrice() + ", '" + v.getDescription() + "', '" +
	 * String.format("#%02x%02x%02x", vcol.getRed(), vcol.getGreen(),
	 * vcol.getBlue()) + "', " + true + ")"; System.out.println(sqlInsert);
	 * stmt.executeUpdate(sqlInsert);
	 * 
	 * // Clean-up environment stmt.close(); disconnect();
	 * 
	 * } catch (
	 * 
	 * SQLException se)
	 * 
	 * { // Handle errors for JDBC se.printStackTrace(); } catch (
	 * 
	 * Exception e)
	 * 
	 * { // Handle errors for Class.forName e.printStackTrace(); } finally
	 * 
	 * { // finally block used to close resources try { if (stmt != null)
	 * stmt.close(); } catch (SQLException se2) { } // nothing we can do
	 * 
	 * disconnect(); }
	 * 
	 * }
	 * 
	 */

}
