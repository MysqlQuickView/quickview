package lad.eclipse.model;

import java.sql.DriverManager;

public class MySql extends abstractDB {
	public MySql(DBConfig config) throws Exception {
		super(config);
	}

	public void init(DBConfig config) throws Exception {
		if(config.getDbPath()!=null){
			config.init();
		}
		String dburl = "jdbc:mysql://" + config.getHost() + "/"
				+ "information_schema"
				+ "?useUnicode=true&characterEncoding=GB2312";
		Class.forName("com.mysql.jdbc.Driver");
		this.conn = DriverManager.getConnection(dburl, config.getUsername(),
				config.getPassword());
		
		this.stmt = this.conn.createStatement();
	}
}