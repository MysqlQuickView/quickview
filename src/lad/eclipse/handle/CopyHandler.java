package lad.eclipse.handle;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import lad.eclipse.action.EditorUtils;
import lad.eclipse.mysqlview.Activator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CopyHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public CopyHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByName("JavaScript"); // 得到脚本引擎
		try {
			IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			String cpjs = store.getString("RUN_JS");
			engine.eval(new java.io.FileReader(cpjs));
			Invocable inv = (Invocable) engine;
			Object a = inv.invokeFunction("copy", new Object[] { 
					EditorUtils.getUrl(), 
					EditorUtils.getSysClipboardText(),
					EditorUtils.getSelectionText(),
					EditorUtils.getEmptyString()
					});
			EditorUtils.insert(a.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
}
