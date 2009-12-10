package it.spasia.generator.preferences;

import it.spasia.generator.BulkGeneratorPlugin;
import it.spasia.generator.properties.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Inizializza le preferenze al loro valore di default
 * 
 * @author Marco Spasiano
 * @version 1.0 [7-Aug-2006]
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	/*
	 * Inizializza le preferenze al loro valore di default
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		ArrayList<String> sqlKeys = new ArrayList<String>();
		ArrayList<String> javaKeys = new ArrayList<String>();
		IPreferenceStore store = BulkGeneratorPlugin.getDefault().getPreferenceStore();
		ResourceBundle resource = BulkGeneratorPlugin.getDefault().getResourceBundle();
		for(Enumeration e = resource.getKeys();e.hasMoreElements();) {
			String key = (String)e.nextElement();
			store.setDefault(key, resource.getString(key));
			if (key.startsWith(Preferences.SQL_PREFIX)) 
				sqlKeys.add(key.substring(Preferences.SQL_PREFIX.length()));
			if (key.startsWith(Preferences.TYPE_PREFIX)) 
				javaKeys.add(key.substring(Preferences.TYPE_PREFIX.length()));
		}
		store.setDefault(Preferences.SQL_ITEMS,getKeysString(sqlKeys));
		store.setDefault(Preferences.TYPE_ITEMS,getKeysString(javaKeys));
	}

	// crea una stringa con le chiavi di accesso alle propriet� correlate
	private String getKeysString(ArrayList<String> list) {
		StringBuffer sb = new StringBuffer();
		Collections.sort(list);
		for (Iterator it = list.iterator(); it.hasNext();) {
			String key = (String) it.next();
			sb.append(key.length() == 0 ? "" : ";");
			sb.append(key);										
		}
		return sb.toString();
	}

}
