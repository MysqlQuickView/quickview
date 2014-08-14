package lad.eclipse.model;

import lad.eclipse.action.EditorUtils;

public class DBConfig {
	private String username;
	private String password;
	private String host;
	private String dbName;
	private String dbPath;

	public DBConfig(String dbPath, String password) {
		this.dbPath = dbPath;
		this.password = password;
	}

	public DBConfig(String host, String dbName, String username, String password) {
		this.host = host;
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDbName() {
		return this.dbName==null?"":this.dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbPath() {
		return this.dbPath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}
	
	public void init(){
		if(dbPath!=null && dbPath.trim().length()>0){
			host = EditorUtils.getRegStr(".+//(.+?)/",dbPath);
			dbName = EditorUtils.getRegStr(".+/(.+?)\\?",dbPath);
			username = EditorUtils.getRegStr(".+user=(.+?)&",dbPath);
			password = EditorUtils.getRegStr(".+password=([^&]*)",dbPath);
		}
	}
}