package lad.eclipse.mysqlview.preferences;

import lad.eclipse.mysqlview.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault("COPY_JS", "");
		store.setDefault("RUN_JS", "");
		store.setDefault("DB_CONFIG", "#jdbc:mysql://localhost:3306/test?user=root&password=");
	}
}