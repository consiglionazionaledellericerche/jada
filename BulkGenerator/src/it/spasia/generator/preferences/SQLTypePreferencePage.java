package it.spasia.generator.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import it.spasia.generator.BulkGeneratorPlugin;
import it.spasia.generator.properties.Messages;
import it.spasia.generator.properties.Preferences;

/**
 * Gestione delle preferenze di conversioni tipi SQL
 * 
 * @author Marco Spasiano
 * @version 1.0 [3-Aug-2006]
 * [23-Aug-2006] Strings Externalization 
 */

public class SQLTypePreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public SQLTypePreferencePage() {
		super(GRID);
		setPreferenceStore(BulkGeneratorPlugin.getDefault().getPreferenceStore());
		setDescription(Messages.SQLTypePreferencePage_description);
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
			
		//addField(new SpacerFieldEditor(getFieldEditorParent()));
		
		TablePropertiesFieldEditor table = new TablePropertiesFieldEditor(Preferences.SQL_ITEMS, "", getFieldEditorParent()); //$NON-NLS-1$
		addField(table);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}