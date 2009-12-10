package it.spasia.generator.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import it.spasia.generator.BulkGeneratorPlugin;
import it.spasia.generator.properties.Messages;
import it.spasia.generator.properties.Preferences;

/**
 * Gestione delle prefernze di connessione
 * 
 * @author Marco Spasiano
 * @version 1.0 
 * [31-Lug-2006] creazione
 * [23-Aug-2006] Strings Externalization
 */

public class DatabasePreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public DatabasePreferencePage() {
		super(GRID);
		setPreferenceStore(BulkGeneratorPlugin.getDefault().getPreferenceStore());
		setDescription(Messages.DatabasePreferencePage_title);
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		
		addField(new SpacerFieldEditor(getFieldEditorParent()));

		addField(
			new StringFieldEditor(Preferences.DB_DRIVER, Messages.DatabasePreferencePage_driver, getFieldEditorParent()));
		addField(
				new StringFieldEditor(Preferences.DB_URL, Messages.DatabasePreferencePage_url, getFieldEditorParent()));
		addField(
				new StringFieldEditor(Preferences.DB_USER, Messages.DatabasePreferencePage_user, getFieldEditorParent()));
		addField(
				new StringFieldEditor(Preferences.DB_PASSWORD, Messages.DatabasePreferencePage_password, getFieldEditorParent()));

		addField(new SpacerFieldEditor(getFieldEditorParent()));

		addField(
				new StringFieldEditor(Preferences.DB_SCHEMA, Messages.DatabasePreferencePage_schema, getFieldEditorParent()));
		addField(
				new StringFieldEditor(Preferences.LIST_TABLES, Messages.DatabasePreferencePage_listTables, getFieldEditorParent()));
		
}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}