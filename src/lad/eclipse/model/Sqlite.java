package lad.eclipse.model;

import java.sql.DriverManager;

public class Sqlite extends abstractDB {
	public Sqlite(DBConfig config) throws Exception {
		super(config);
	}

	public void init(DBConfig config) throws Exception {
		Class.forName("org.sqlite.JDBC");
		this.conn = DriverManager.getConnection( "jdbc:sqlite:"+config.getDbPath());
		this.stmt = this.conn.createStatement();
	}
}