package lad.eclipse.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MysqlInfomation {
	private DataBase db;

	public MysqlInfomation(DBConfig mysqlconfig) {
		try {
			init(mysqlconfig);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(DBConfig config) throws Exception {
		try {
			config.init();
			MySql mysql = new MySql(config);
			this.db = new DataBase();
			this.db.setName(config.getDbName());
			String tablesql = "SELECT TABLE_NAME as name,TABLE_COMMENT FROM `TABLES` where TABLE_SCHEMA=\""
					+ config.getDbName() + "\"";
			ResultSet rs = mysql.executeQuery(tablesql);

			List tables = new ArrayList();
			while (rs.next()) {
				Table table = new Table();
				String tableName = rs.getString("name");
				table.setName(tableName);
				table.setComment(rs.getString("TABLE_COMMENT"));
				table.setParent(this.db);

				String columnsql = "SELECT * FROM `COLUMNS` where TABLE_SCHEMA=\""
						+ config.getDbName()
						+ "\" AND TABLE_NAME=\""
						+ tableName + "\"";

				MySql mysql2 = new MySql(config);
				ResultSet rs2 = mysql2.executeQuery(columnsql);
				List<Column> Columns = new ArrayList<Column>();

				while (rs2.next()) {
					Column column = new Column();
					column.setParent(table);
					column.setName(rs2.getString("COLUMN_NAME"));
					column.setComment(rs2.getString("COLUMN_COMMENT"));
					column.setTypename(rs2.getString("COLUMN_TYPE").replace(
							"unsigned", ""));

					if (rs2.getString("COLUMN_KEY").equals("PRI")) {
						table.setKey(column);
						column.setKey(true);
					}

					String typeName = rs2.getString("COLUMN_TYPE")
							.toLowerCase();
					int t = Column.TYPE_OTHER;
					if ((typeName.indexOf("bit") >= 0)
							|| (typeName.indexOf("bool") >= 0))
						t = Column.TYPE_BOOLEAN;
					else if (typeName.indexOf("int") >= 0)
						t = Column.TYPE_INTEGER;
					else if (typeName.indexOf("float") >= 0)
						t = Column.TYPE_FLOAT;
					else if (typeName.indexOf("double") >= 0)
						t = Column.TYPE_DOUBLE;
					else if (typeName.indexOf("decimal") >= 0)
						t = Column.TYPE_DECIMAL;
					else if (typeName.indexOf("char") >= 0)
						t = Column.TYPE_CHAR;
					else if (typeName.indexOf("text") >= 0)
						t = Column.TYPE_TEXT;
					else if (typeName.indexOf("blob") >= 0)
						t = Column.TYPE_BLOB;
					else if ((typeName.indexOf("time") >= 0)
							|| (typeName.indexOf("date") >= 0)
							|| (typeName.indexOf("year") >= 0)) {
						t = Column.TYPE_TIME;
					}

					column.setType(t);
					Columns.add(column);
				}
				mysql2.close();

				table.setChildren((Column[]) Columns.toArray(new Column[Columns.size()]));
				tables.add(table);
			}
			this.db.setChildren((Table[]) tables.toArray(new Table[tables.size()]));
			mysql.close();
		} catch (Exception e) {
			e.printStackTrace();
			this.db = new DataBase();
			this.db.setName(config.getDbName()+":NO DATA!");
		}
	}

	public DataBase getDb() {
		return this.db;
	}

	public String getDbJson() {
		return "";
	}

	public static void main(String[] args) {
		DBConfig config = new DBConfig("jdbc:mysql://localhost:3306/car?user=root&password=", "");
		MysqlInfomation mi = new MysqlInfomation(config);
		System.out.println(mi.getDb());
	}
}