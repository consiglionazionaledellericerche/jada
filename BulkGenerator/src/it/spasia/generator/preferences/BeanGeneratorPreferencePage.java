package it.spasia.generator.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import it.spasia.generator.BulkGeneratorPlugin;
import it.spasia.generator.properties.Messages;
import it.spasia.generator.properties.Preferences;

/**
 * Bulk Generator preferenze infos
 * 
 * @author Marco Spasiano
 * @version 1.0 
 * [31-Lug-2006] creazione
 * [23-Aug-2006] Strings Externalization
 */

public class BeanGeneratorPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public BeanGeneratorPreferencePage() {
		super(GRID);
		setPreferenceStore(BulkGeneratorPlugin.getDefault().getPreferenceStore());
		setDescription(Messages.BulkGeneratorPreferencePage_description);
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new SpacerFieldEditor(getFieldEditorParent()));
		String value = Messages.BulkGeneratorPreferencePage_version + getPreferenceStore().getDefaultString(Preferences.VERSION); 
		addField(new LabelFieldEditor(value, getFieldEditorParent()));
		value = Messages.BulkGeneratorPreferencePage_author + Messages.BulkGeneratorPreferencePage_0;  //$NON-NLS-2$
		addField(new LabelFieldEditor(value, getFieldEditorParent()));
		value = Messages.BulkGeneratorPreferencePage_project + getPreferenceStore().getDefaultString(Preferences.PROJECT); 
		addField(new LabelFieldEditor(value, getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		noDefaultAndApplyButton();
	}
	
}