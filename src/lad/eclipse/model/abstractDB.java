package lad.eclipse.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class abstractDB {
	protected Connection conn = null;
	protected Statement stmt;

	public abstractDB(DBConfig config) throws Exception {
		init(config);
	}

	public abstract void init(DBConfig paramDBConfig) throws Exception;

	public ResultSet executeQuery(String query) throws Exception {
		return this.stmt.executeQuery(query);
	}

	public void executeUpdate(String query) throws SQLException {
		this.stmt.executeUpdate(query);
	}
	

	public void close() throws SQLException {
		this.stmt.close();
		this.conn.close();
	}
}