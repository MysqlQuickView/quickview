package lad.eclipse.mysqlview.preferences;

import lad.eclipse.mysqlview.Activator;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class MysqlPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	public MysqlPreferencePage() {
		super(1);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("please set The information of Mysql!");
	}

	public void createFieldEditors() {
		
		addField(new TextFieldEditor("DB_CONFIG", "&db_config:",
				getFieldEditorParent()));
		
		addField(new FileFieldEditor("COPY_JS", "&copy_js:",
				getFieldEditorParent()));
		
		addField(new FileFieldEditor("RUN_JS", "&run_js:",
				getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
}