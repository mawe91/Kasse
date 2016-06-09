package db;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import beans.Invoice;
import beans.Product;
import beans.Voucher;

public class DBHandler {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/kasse";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "ADVANCE91";

	private Connection connection;

	public DBHandler() {
		super();
	}

	public ArrayList<Voucher> getAllVouchers() {
		Connection conn = connect();
		Statement stmt = null;
		ArrayList<Voucher> voucherList = new ArrayList<Voucher>();
		try {
			stmt = conn.createStatement();

			String sql = "SELECT id,description, price, color, isActive FROM voucher";
			ResultSet rs = stmt.executeQuery(sql);

			Voucher v;
			// Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				int id = rs.getInt("id");
				double name = rs.getDouble("price");
				String description = rs.getString("description");
				Color color;
				try {
					color = Color.decode(rs.getString("color"));

				} catch (Exception e) {
					color = null; // Not defined
				}
				Boolean isActive = rs.getBoolean("isActive");

				if (isActive) {
					v = new Voucher(id, description, name, color);
					voucherList.add(v);
				}
			}

			// Clean-up environment
			rs.close();
			stmt.close();
			disconnect();

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do

			disconnect();
		}
		return voucherList;

	}

	public ArrayList<Product> getAllProducts() {
		Connection conn = connect();
		Statement stmt = null;
		ArrayList<Product> productList = new ArrayList<Product>();
		try {
			stmt = conn.createStatement();

			String sql = "SELECT id, name, isDrink, voucher, isActive FROM product";
			ResultSet rs = stmt.executeQuery(sql);

			Product p;
			// Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Boolean isDrink = rs.getBoolean("isDrink");
				int voucher = rs.getInt("voucher");
				Boolean isActive = rs.getBoolean("isActive");

				if (isActive) {
					p = new Product(id, name, isDrink, voucher);
					productList.add(p);
				}

			}

			// Clean-up environment
			rs.close();
			stmt.close();
			disconnect();

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do

			disconnect();
		}

		return productList;

	}

	public int generateNewInvoice() {
		Connection conn = connect();
		Statement stmt = null;
		int lastid = 0;
		try {
			stmt = conn.createStatement();

			String sqlInsert = "INSERT INTO Invoice (timestamp) VALUES (CURRENT_TIMESTAMP)";
			stmt.executeUpdate(sqlInsert);

			ResultSet rs = stmt.executeQuery("select last_insert_id() as last_id from invoice");
			rs.next();
			lastid = rs.getInt("last_id");

			// Clean-up environment
			stmt.close();
			disconnect();

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do

			disconnect();
		}

		return lastid;
	}

	public Timestamp getInvoiceTimestamp(int InvoiceID) {
		Connection conn = connect();
		Statement stmt = null;
		Timestamp ts = null;
		try {
			stmt = conn.createStatement();

			String sqlInsert = "SELECT timestamp From invoice WHERE id=" + InvoiceID;
			ResultSet rs = stmt.executeQuery(sqlInsert);

			rs.next();
			ts = rs.getTimestamp("timestamp");

			// Clean-up environment
			stmt.close();
			disconnect();

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do

			disconnect();
		}

		return ts;
	}

	public void storeInvoice(Invoice invoice) {
		Connection conn = connect();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

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

					stmt.executeUpdate(sqlInsert);
				}
			}

			// Clean-up environment
			stmt.close();
			disconnect();

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do

			disconnect();
		}

	}

	public Connection connect() {
		if (connection == null) {
			try {
				// Register JDBC driver
				Class.forName("com.mysql.jdbc.Driver");

				// Open a connection
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;

	}

	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void invalidateProductOrVocherInDB(int iD, String classname) {
		Connection conn = connect();
		Statement stmt = null;
		try {
			stmt = conn.createStatement();

			String sqlInsert;
			sqlInsert = "UPDATE `kasse`.`"+classname+"` SET `isActive`='0' WHERE `id`='" + iD + "';";

			stmt.executeUpdate(sqlInsert);

			// Clean-up environment
			stmt.close();

			disconnect();

		} catch (

		SQLException se)

		{
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (

		Exception e)

		{
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally

		{
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do

			disconnect();
		}
	}

}
