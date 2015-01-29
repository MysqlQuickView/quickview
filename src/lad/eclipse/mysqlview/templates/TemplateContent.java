package lad.eclipse.mysqlview.templates;

import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import lad.eclipse.action.EditorUtils;
import lad.eclipse.model.Column;
import lad.eclipse.model.iDBObj;
import lad.eclipse.mysqlview.Activator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TemplateContent {
	public static String getContent(List<iDBObj> keys, String spaceStr,
			String function) {
		if(keys!=null){
			String tablename = ((iDBObj) keys.get(0)).getParent().getName();
			return scan(keys, tablename, spaceStr, function);
		}else{
			return scan(new ArrayList<iDBObj>(), "", spaceStr, function);
		}
		
	}

	private static String scan(List<iDBObj> keys, String tablename,
			String spaceString, String functionName) {
		
		JSONArray array = new JSONArray();
		for(iDBObj obj : keys){
			Column column = (Column)obj;
			JSONObject jobj = new JSONObject();
			try {
				jobj.put("name", column.getName());
				jobj.put("oname", column.getObjName());
				jobj.put("type", column.getTypeName());
				jobj.put("comment", column.getComment());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(jobj);
		}
		String selectKeys = array.toString();
		
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByName("JavaScript"); // 得到脚本引擎
		try {
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			String js = store.getString("RUN_JS");
			engine.eval(new java.io.FileReader(js));
			Invocable inv = (Invocable) engine;
			Object a = inv.invokeFunction("create", new Object[] { 
					functionName,
					selectKeys,
					tablename,
					EditorUtils.getUrl(),
					EditorUtils.getSelectionText(),
					EditorUtils.getSysClipboardText(),
					spaceString
					});
			return a.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
}