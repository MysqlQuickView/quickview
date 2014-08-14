package lad.eclipse.mysqlview.templates;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lad.eclipse.model.DBConfig;
import lad.eclipse.model.Sqlite;
import lad.eclipse.mysqlview.Activator;

public class DocumentDll {
	public static Sqlite sqlite = null;
	
	public static Sqlite getSqlite() throws Exception{
		if(sqlite==null){
			DBConfig config = new DBConfig(Activator.getDefault().getDbFile().getAbsolutePath(), null);
			sqlite = new Sqlite(config);
		}
		return sqlite;
	}
	
	public static void initSqlite(File file){
		DBConfig config = new DBConfig(Activator.getDefault().getDbFile().getAbsolutePath(), null);
		try {
			sqlite = new Sqlite(config);
			sqlite.executeUpdate("CREATE TABLE maps("
	        		+ "\"key\" text NOT NULL,"
	        		+ "\"value\" text NOT NULL,"
	        		+ "PRIMARY KEY(\"key\"));");
			sqlite.executeUpdate("CREATE TABLE template("
	        		+ "\"id\" integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
	        		+ "\"fid\" integer NOT NULL,"
	        		+ "\"title\" text NOT NULL,"
	        		+ "\"description\" text NOT NULL,"
	        		+ "\"content\" text NOT NULL);");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addcategore(String name) {
		try {
			getSqlite().executeUpdate("insert into template(fid,title,description,content) values( 0,'"+name+"','category','')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addtemplate(Template template) {
		try {
			template.initArgs();
			getSqlite().executeUpdate("insert into template(fid,title,description,content) values( "
			+template.getFid()+",'"
			+template.getTitle()+"','"
			+template.getDescription()+"','"
			+template.getContent()+"')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void edittemplate(Template template) {
		try {
			template.initArgs();
			getSqlite().executeUpdate("update template set "
					+ "title='" +template.getTitle()+"',"
					+ "description='" +template.getDescription()+"',"
					+ "content='" +template.getContent()+"' "
					+ "where id =" + template.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void up(Template template) {
	}

	public static void down(Template template) {
	}

	public static void removeTemplate(Template template) {
		try {
			if(template.isCategory()){
				getSqlite().executeUpdate("DELETE FROM template where id="+template.getId());
				getSqlite().executeUpdate("DELETE FROM template where fid="+template.getId());
			}else{
				getSqlite().executeUpdate("DELETE FROM template where id="+template.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<Template> getElements(Object parent){
		List<Template> result = new ArrayList<Template>();
		try {
			Map<Integer,List<Template>> templateMap = new HashMap<Integer,List<Template>>();
			Map<Integer,String> templateMap2 = new HashMap<Integer,String>();
			
			ResultSet rs = getSqlite().executeQuery("select * from template order by fid,id");
			while(rs.next()){
				int fid = rs.getInt("fid");
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String description = rs.getString("description");
				String content = rs.getString("content");
				if(fid == 0){
					templateMap.put(id, new ArrayList<Template>());
					templateMap2.put(id, title);
				}else{
					Template t = new Template();
					t.setFid(fid);
					t.setId(id);
					t.setTitle(title);
					t.setDescription(description);
					t.setContent(content);
					templateMap.get(fid).add(t);
				}
			}
			
			for(Integer key:templateMap.keySet()){
				Template t = new Template();
				t.setTitle(templateMap2.get(key));
				t.setFid(0);
				t.setId(key);
				t.setChildren(templateMap.get(key));
				result.add(t);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		result.sort(new Comparator<Template>() {
			@Override
			public int compare(Template o1, Template o2) {
				return o1.getId()-o2.getId();
			}
		});
		return result;
	}
}