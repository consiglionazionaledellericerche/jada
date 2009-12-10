package it.spasia.generator.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import it.spasia.generator.BulkGeneratorPlugin;
import it.spasia.generator.properties.Preferences;

/**
 * Gestione delle preferenze per la definizione di un textarea
 * 
 * @author Marco Spasiano
 * @version 1.0 [1-Aug-2006]
 */

public class TextareaPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public TextareaPreferencePage() {
		super(GRID);
		setPreferenceStore(BulkGeneratorPlugin.getDefault().getPreferenceStore());
		String description = "";
		description += "Per l'input dei campi di tipo testo ";
		description += "che superano come lungezza la soglia indicata dal campo ";
		description += "threshold verr� generato un widget HTML di tipo Textarea ";
		description += "avente le dimensioni indicate.";		
		setDescription(description);
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
				new IntegerFieldEditor(Preferences.TEXTAREA_TEXT, "Threshold", getFieldEditorParent()));
		addField(
				new IntegerFieldEditor(Preferences.TEXTAREA_COLS, "Default Cols", getFieldEditorParent()));
		addField(
				new IntegerFieldEditor(Preferences.TEXTAREA_ROWS, "Default Rows", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}