package com.unitvectory.shak.jarvis.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * The abstract database that provides some common functionality for a DAO.
 * 
 * @author Jared Hatfield
 * 
 */
public class AbstractDatabase {

	/**
	 * the data source
	 */
	private DataSource ds;

	/**
	 * Creates a new instance of the AbstractDatabase class.
	 * 
	 * @param ds
	 *            the data source
	 */
	public AbstractDatabase(DataSource ds) {
		this.ds = ds;
	}

	/**
	 * Gets a database connection.
	 * 
	 * @return the connection
	 * @throws SQLException
	 */
	protected Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}

	/**
	 * Closes everything.
	 * 
	 * @param con
	 *            the connection
	 * @param stmt
	 *            the statement
	 * @param rs
	 *            the result set
	 */
	protected void closeEverything(Connection con, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}
}
