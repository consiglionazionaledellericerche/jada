package it.spasia.generator.preferences;

import it.spasia.generator.BulkGeneratorPlugin;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Componente tabellare per la gestione di properties (key, value)
 * 
 * @author Marco Spasiano
 * @version 1.0 [4-Aug-2006]
 */
public class TablePropertiesFieldEditor extends FieldEditor {
	private static final String DEFAULT_ADD_LABEL = "Add";
	private static final String DEFAULT_REMOVE_LABEL = "Remove";
	private static final String KEY_PROPERTY = "key";
	private static final String VALUE_PROPERTY = "value";

	private Composite top;
	private TableViewer viewer;
	private Button addButton;
	private Button delButton;

	// indica se il campo property key pu� essere editato o no
	// se questo campo � false allora vengono esclusi anche i bottoni
	// add e remove (sar� possibile solo modificare i valori associati
	// alle chiavi
	private boolean readOnlyKey;

	// indica il raggruppamento delle chiavi (es. "sql.")
	private String keyPrefix;

	public TablePropertiesFieldEditor(String name, String labelText, Composite parent) {
        init(name, labelText);
        createControl(parent);
		setKeyPrefix(null);
	}

    /**
     * Creates a new field editor.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     * @param readOnlyKey set to true for disabling editing of key column
     */	 
	public TablePropertiesFieldEditor(String name, String labelText, Composite parent, boolean readOnlyKey) {
		setReadOnlyKey(readOnlyKey);
        init(name, labelText);
        createControl(parent);
		setKeyPrefix(null);
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns(int numColumns) {
		((GridData) top.getLayoutData()).horizontalSpan = numColumns;
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid (Composite, int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		top = parent;

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns;
		top.setLayoutData(gd);

		Label label = getLabelControl(top);
		GridData labelData = new GridData();
		labelData.horizontalSpan = numColumns;
		label.setLayoutData(labelData);

		Table table = new Table(top, SWT.BORDER | SWT.FULL_SELECTION);
		viewer = new TableViewer(table);

		attachContentProvider();
		attachLabelProvider();
		attachCellEditors();

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(20));
		layout.addColumnData(new ColumnWeightData(80));
		table.setLayout(layout);
		table.setLinesVisible(true);

		TableColumn keyColumn = new TableColumn(table, SWT.LEFT);
		keyColumn.setText("Key");
		TableColumn valColumn = new TableColumn(table, SWT.LEFT);
		valColumn.setText("Value");
		table.setHeaderVisible(true);

		GridData tableData = new GridData(GridData.FILL_BOTH);
		tableData.horizontalSpan = numColumns;

		table.setLayoutData(tableData);

		// se falso salta la creazione dei bottoni add e remove
		// e la creazione del listener di selezione per la tabella
		if (isReadOnlyKey()) return;

		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectionChanged();
			}
		});

		// Create a composite for the add and remove buttons
		Composite addRemoveGroup = new Composite(top, SWT.NONE);

		GridData addRemoveData = new GridData(GridData.FILL_HORIZONTAL);
		addRemoveData.horizontalSpan = numColumns;
		addRemoveGroup.setLayoutData(addRemoveData);

		GridLayout addRemoveLayout = new GridLayout();
		addRemoveLayout.numColumns = numColumns;
		addRemoveLayout.marginHeight = 0;
		addRemoveLayout.marginWidth = 0;
		addRemoveGroup.setLayout(addRemoveLayout);

		// Create a composite for the add and remove buttons.
		Composite buttonGroup = new Composite(addRemoveGroup, SWT.NONE);
		buttonGroup.setLayoutData(new GridData());

		GridLayout buttonLayout = new GridLayout(2, false);
		buttonGroup.setLayout(buttonLayout);

		// Create the add button.
		addButton = new Button(buttonGroup, SWT.NONE);
		addButton.setText(DEFAULT_ADD_LABEL);
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				EditableTableItem item = new EditableTableItem("","");
				viewer.add(item);
				viewer.editElement(item, 0);
			}
		});
		GridData addData = new GridData(GridData.FILL_HORIZONTAL);
		addData.widthHint = convertHorizontalDLUsToPixels(addButton, IDialogConstants.BUTTON_WIDTH);
		addButton.setLayoutData(addData);

		// Create the remove button.
		delButton = new Button(buttonGroup, SWT.NONE);
		delButton.setEnabled(false);
		delButton.setText(DEFAULT_REMOVE_LABEL);
		delButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int index = viewer.getTable().getSelectionIndex();
				EditableTableItem item = (EditableTableItem)viewer.getElementAt(index);
				getPreferenceStore().setValue(keyPrefix + item.key, "");
				getPreferenceStore().setValue(getPreferenceName(), getKeysString());
				viewer.getTable().remove(index);
				selectionChanged();
			}
		});
		GridData removeData = new GridData(GridData.FILL_HORIZONTAL);
		removeData.widthHint = convertHorizontalDLUsToPixels(delButton, IDialogConstants.BUTTON_WIDTH);
		delButton.setLayoutData(removeData);
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad() {
		IPreferenceStore store = BulkGeneratorPlugin.getDefault().getPreferenceStore();
		String sqlKeys = store.getString(getPreferenceName());
		String[] keys = parseString(sqlKeys);
		for (int i = 0; i < keys.length; i++) {
			String value = getPreferenceStore().getString(keyPrefix + keys[i]);
			viewer.add(new EditableTableItem(keys[i], value));
		}
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
		// setta la stringa chiavi al valore di default
		String sqlKeys = getPreferenceStore().getDefaultString(getPreferenceName());
		getPreferenceStore().setToDefault(getPreferenceName());
		// settando al valore di default ogni elemento in tabella viene rimosso
		for (int i = 0; i < viewer.getTable().getItemCount(); i++) {
			EditableTableItem item = (EditableTableItem) viewer.getElementAt(i);
			getPreferenceStore().setToDefault(keyPrefix + item.key);
		}
		viewer.getTable().removeAll();
		String[] keys = parseString(sqlKeys);
		for (int i = 0; i < keys.length; i++) {
			String value = getPreferenceStore().getDefaultString(keyPrefix + keys[i]);
			viewer.add(new EditableTableItem(keys[i], value));
		}
	}


	/**
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
		String s = getKeysString();
		getPreferenceStore().setValue(getPreferenceName(), s);
		for (int i = 0; i < viewer.getTable().getItemCount(); i++) {
			EditableTableItem item = (EditableTableItem) viewer.getElementAt(i);
			getPreferenceStore().setValue(keyPrefix + item.key, item.value);
		}
				
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		// The button composite
		return 1;
	}

	// Sets the enablement of the remove button depending
	// on the selection in the list.
	private void selectionChanged() {
		int index = viewer.getTable().getSelectionIndex();
		delButton.setEnabled(index >= 0);
	}

	// definizione fornitore dati celle
	private void attachLabelProvider() {
		viewer.setLabelProvider(new ITableLabelProvider() {
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				EditableTableItem item = (EditableTableItem) element;
				switch (columnIndex) {
					case 0 :
						return item.key;
					case 1 :
						return item.value;
					default :
						return "Invalid column: " + columnIndex;
				}
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener lpl) {
			}

		});
	}

	private void attachContentProvider() {
		viewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				return (Object[]) inputElement;
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		});
	}

	// definizione editor di celle
	private void attachCellEditors() {
		Composite parent = viewer.getTable();
		CellEditor[] cellEditor = new CellEditor[2];
		cellEditor[0] = new TextCellEditor(parent);
		cellEditor[1] = new TextCellEditor(parent);
		viewer.setCellEditors(cellEditor);
		viewer.setColumnProperties(new String[]{KEY_PROPERTY, VALUE_PROPERTY});
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (VALUE_PROPERTY.equals(property))
					return true;
				else
					return !readOnlyKey;
			}

			public Object getValue(Object element, String property) {
				EditableTableItem item = (EditableTableItem) element;
				if (VALUE_PROPERTY.equals(property))
					return item.value;
				else
					return item.key;
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				EditableTableItem data = (EditableTableItem) tableItem.getData();
				if (VALUE_PROPERTY.equals(property))
					data.value = value.toString();
				else
					data.key = value.toString();
				viewer.refresh(data);
			}
		});

	}
	/** ritorna il suffisso per il raggruppamento delle chiavi */
	public String getKeyPrefix() {
		return keyPrefix;
	}

	/** imposta il suffisso per il raggruppamento delle chiavi 
	 *  se null estrae il suffisso dal nome label passato a creazione
	 */
	public void setKeyPrefix(String keySuffix) {
		if (keySuffix == null) {
			int index = getPreferenceName().indexOf('.');
			if (index < 0) {
				this.keyPrefix = "";
			} else {
				this.keyPrefix = getPreferenceName().substring(0,++index);
			}
		} else {
			this.keyPrefix = keySuffix;
		}
	}
	
	/**
	 * Parses the single String representation of the properties keys returning an arrays of them
	 */
	private String[] parseString(String stringList) {
		StringTokenizer st = new StringTokenizer(stringList, ";");
		ArrayList<Object> v = new ArrayList<Object>();
		while (st.hasMoreElements()) {
			v.add(st.nextElement());
		}
		return (String[]) v.toArray(new String[v.size()]);
	}

	/** Builds a string representation of keys within the table */
	private String getKeysString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < viewer.getTable().getItemCount(); i++) {
			String key = ((EditableTableItem) viewer.getElementAt(i)).key;
			if (key != null && key.trim().length() > 0 ) {
				sb.append(sb.length() == 0 ? "" : ";");
				sb.append(((EditableTableItem) viewer.getElementAt(i)).key);
			}
		}
		return sb.toString();
	}
	/** bean rappresentante una riga della tabella */
	class EditableTableItem {
		public String key;
		public String value;

		public EditableTableItem() {
		}
		public EditableTableItem(String key, String value) {
			this.key = key;
			this.value = value;
		}
		public boolean equals(Object o) {
			if ((o instanceof EditableTableItem) == false)
				return false;
			EditableTableItem item = (EditableTableItem) o;
			String s1 = this.key + this.value;
			String s2 = item.key + item.value;
			return s1.equalsIgnoreCase(s2);
		}
		public String toString() {
			return key + "=" + value;
		}
	}
	public boolean isReadOnlyKey() {
		return readOnlyKey;
	}

	public void setReadOnlyKey(boolean readOnlyKey) {
		this.readOnlyKey = readOnlyKey;
	}

}