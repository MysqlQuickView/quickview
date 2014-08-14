package lad.eclipse.mysqlview.views;

import java.util.ArrayList;
import java.util.List;

import lad.eclipse.model.DBConfig;
import lad.eclipse.model.DataBase;
import lad.eclipse.model.MysqlInfomation;
import lad.eclipse.model.iDBObj;
import lad.eclipse.mysqlview.Activator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class MysqlContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String dbConfig = store.getString("DB_CONFIG");
		String[] configs = dbConfig.split("\n");
		
		List<DataBase> ld = new ArrayList<DataBase>();
		
		for(String config:configs){
			config=config.trim();
			if(config.equals("") || config.indexOf("#")==0){
				continue;
			}
			try {
				DBConfig dbc = new DBConfig(config,null);
				ld.add(new MysqlInfomation(dbc).getDb());
			} catch (Exception e) {
			}
		}
		
		return ld.toArray(new Object[ld.size()]);
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