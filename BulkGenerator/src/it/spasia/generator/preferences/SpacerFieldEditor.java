package it.spasia.generator.preferences;

import org.eclipse.swt.widgets.Composite;

/**
 * Spaziatore verticale (utilizza una label vuota)
 * 
 * @author Marco Spasiano
 * @version 1.0 [2-Aug-2006]
 */
public class SpacerFieldEditor extends LabelFieldEditor {
	// Implemented as an empty label field editor.
	public SpacerFieldEditor(Composite parent) {
		super("", parent);
	}
}
