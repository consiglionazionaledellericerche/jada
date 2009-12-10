package it.spasia.generator.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import it.spasia.generator.BulkGeneratorPlugin;
import it.spasia.generator.properties.Messages;
import it.spasia.generator.properties.Preferences;

/**
 * Gestione delle preferenze per la generazione dei documenti XML
 * 
 * @author Marco Spasiano
 * @version 1.0 [1-Aug-2006]
 */

public class XMLPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public XMLPreferencePage() {
		super(GRID);
		setPreferenceStore(BulkGeneratorPlugin.getDefault().getPreferenceStore());
		setDescription(Messages.XMLPreferencePage_description);
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
			
		addField(new SpacerFieldEditor(getFieldEditorParent()));		

		addField(new StringFieldEditor(Preferences.XML_HEADER, Messages.XMLPreferencePage_header, getFieldEditorParent()));
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}