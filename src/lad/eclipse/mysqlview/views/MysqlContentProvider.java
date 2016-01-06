package lad.eclipse.mysqlview.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lad.eclipse.model.DBConfig;
import lad.eclipse.model.DataBase;
import lad.eclipse.model.MysqlInfomation;
import lad.eclipse.model.iDBObj;
import lad.eclipse.mysqlview.Activator;
import lad.eclipse.mysqlview.templates.DocumentDll;
import lad.eclipse.mysqlview.templates.MD5Util;
import lad.eclipse.mysqlview.templates.SerializeUtil;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class MysqlContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {
	
	public static List<DataBase> ld = new ArrayList<DataBase>();
	public static Map<String,DBConfig> configT = new HashMap<String,DBConfig>();
	private DataBase initDb;
	
	public MysqlContentProvider(DataBase initDb){
		this.initDb = initDb;
	}
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		
		if(initDb == null){
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			String dbConfig = store.getString("DB_CONFIG");
			String[] configs = dbConfig.split("\n");
			ld = new ArrayList<DataBase>();
			
			for(String config:configs){
				config=config.trim();
				if(config.equals("") || config.indexOf("#")==0){
					continue;
				}
				try {
					Object dbt = DocumentDll.getKeyObj(MD5Util.MD5(config));
					DBConfig dbc = new DBConfig(config);
					configT.put(dbc.getDbName(), dbc);
					
//					if(dbt == null){
						DataBase db = new MysqlInfomation(dbc).getDb();
						ld.add(db);
						DocumentDll.setKey(MD5Util.MD5(config), SerializeUtil.serializeObject(db));
//					}else{
//						ld.add((DataBase) dbt);
//					}
				} catch (Exception e) {
				}
			}
			
			return ld.toArray(new Object[ld.size()]);
		}else{
			if(ld == null){
				ld = new ArrayList<DataBase>();
			}
			int i = 0;
			for(DataBase d : ld){
				if(d.getName().equals(initDb.getName())){
					DataBase db = new MysqlInfomation(configT.get(initDb.getName())).getDb();
					ld.set(i, db);
					try {
						DocumentDll.setKey(MD5Util.MD5(configT.get(initDb.getName()).getDbPath()), SerializeUtil.serializeObject(db));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				i++;
			}
			return ld.toArray(new Object[ld.size()]);
		}
	}

	public Object getParent(Object child) {
		return ((iDBObj) child).getParent();
	}

	public Object[] getChildren(Object parent) {
		return ((iDBObj) parent).getChildren();
	}

	public boolean hasChildren(Object parent) {
		iDBObj obj = (iDBObj) parent;
		return (obj.getChildren() != null) && (obj.getChildren().length > 0);
	}
}