package it.spasia.generator.wizards;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import it.spasia.generator.artifacts.ArtifactGenerator;
import it.spasia.generator.artifacts.CheckSN;
import it.spasia.generator.artifacts.TableComments;
import it.spasia.generator.artifacts.TablePackageStructure;
import it.spasia.generator.model.GeneratorBean;
import it.spasia.generator.properties.Messages;
import it.spasia.generator.properties.Preferences;
import it.spasia.generator.util.DatabaseMetadataUtil;
import it.spasia.generator.util.DatabaseUtil;
import it.spasia.generator.util.TextUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * Pagina wizard per la scelta della tabella sorgente
 * 
 * @author Marco Spasiano
 * @version 1.0 [7-Aug-2006]
 * [23-Aug-2006] Strings Externalization 
 */
public class DatabaseMetadataWizardPage extends BeanGeneratorWizardPage {
	public final static String PAGE_NAME= "DatabaseMetadataWizardPage"; //$NON-NLS-1$	
	private Combo schemaCombo;
	private Combo tableTypeCombo;
	private Text filterText;
	private ListViewer listViewer;
	private String[] tables;


	/**
	 * Costruttore
	 */
	public DatabaseMetadataWizardPage(GeneratorBean bean) {
		super(PAGE_NAME, bean);
		setTitle(Messages.DatabaseMetadataWizardPage_titolo);
		setDescription(Messages.DatabaseMetadataWizardPage_description);
	}

	/**
	 * Creazione dei controlli
	 * 
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		
		Label label = new Label(composite, SWT.NULL);
		label.setText(Messages.DatabaseMetadataWizardPage_schema);
		schemaCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		schemaCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		schemaCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				comboChanged();
			}
		});
		
		label = new Label(composite, SWT.NULL);
		label.setText(Messages.DatabaseMetadataWizardPage_tipo);
		tableTypeCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		tableTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tableTypeCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				comboChanged();
			}
		});

		label = new Label(composite, SWT.NULL);
		label.setText(Messages.DatabaseMetadataWizardPage_filtro);
		filterText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		filterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filterText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				filterChanged();
			}
		});

		Button button= new Button(composite, SWT.PUSH);
		button.setFont(composite.getFont());	
		button.setText("Generate Artifacts from list stored in table ["+bean.getString(Preferences.LIST_TABLES)+"]");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				generateAllArtifacts();
			}
			public void widgetSelected(SelectionEvent e) {
				generateAllArtifacts();
			}
		});		
		
		List list = new List(composite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		list.setLayoutData(gridData);
		list.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				tableSelected();
			}
		});
		listViewer = new ListViewer(list);
		listViewer.setContentProvider(new ArrayContentProvider());
		setControl(composite);
	}
	private void generateAllArtifacts(){
		try{
			for (Enumeration e = DatabaseUtil.tablePackageStructure.keys();e.hasMoreElements();){
				String chiave = (String)e.nextElement();
				TablePackageStructure tps = DatabaseUtil.tablePackageStructure.get(chiave);
				bean.setPackageName(tps.getPackageName());
				bean.setSourceFolder(tps.getModuleName()+"EJB/ejbModule");
				//bean.setTargetXMLFolder("/"+tps.getModuleName()+"Web/WebContent/bulkinfos");
				bean.setPrefix(tps.getBulkName());
				bean.setTable(tps.getTableName());
				try {
					getContainer().run(true, true, new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
							ArtifactGenerator generator = new ArtifactGenerator(bean);
							generator.generate(monitor);
            	
						}
					});
				}catch (InvocationTargetException e1) {
					if (e1.getTargetException() instanceof CoreException) {
						ErrorDialog.openError(getContainer().getShell(),
												Messages.BulkGeneratorWizard_error,
												null, // no special message
												((CoreException) e1.getTargetException()).getStatus());
					} else {
						// CoreExceptions are handled above, but unexpected runtime exceptions and errors may still occur.
						MessageDialog.openError(
								getContainer().getShell(),
								Messages.BulkGeneratorWizard_message, 
								e1.getTargetException().getMessage());
					}
				}catch (InterruptedException e2) {  }				
				DatabaseUtil.getInstance().openConnection(bean);
			}
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
		}
	}

	/**
	 * la selezione di uno dei combo � cambiata (nuova query)
	 */
	private void comboChanged() {
		bean.setFilter(null);
		filterText.setText(""); //$NON-NLS-1$
		doTablesQuery();
	}
	private void doTablesQuery() {
		int index = schemaCombo.getSelectionIndex();
		bean.setSchema(schemaCombo.getItem(index));
		index = tableTypeCombo.getSelectionIndex();
		bean.setTableType(tableTypeCombo.getItem(index));
		populateTables();
		populateList();
	}

		
	/**
	 * il filtro sulle tabelle � cambiato (nuova visualizzazione)
	 */
	private void filterChanged() {
		bean.setFilter(filterText.getText());
		populateList();
	}

	/**
	 * � stata selezionata una tabella (inizializza next page)
	 */
	private void tableSelected() {
		int index = listViewer.getList().getSelectionIndex();
		if (index > -1) {
			bean.setTable(listViewer.getList().getItem(index));
			initializeNextPage(bean);
			setPageComplete(true);
			setErrorMessage(null);
		}		
	}
	/**
	 * popola la lista di visualizzazione delle tabelle
	 */
	private void populateList() {
		listViewer.getList().removeAll();
		for (int i = 0; i < tables.length; i++) {
			if (select(tables[i]))
				listViewer.add(tables[i]);
		}
		setPageComplete(false);
	}

	/**
	 * seleziona l'elemento passato in base al campo filtro
	 */
	private boolean select(String table) {
		if (isTextEmpty(bean.getFilter())) return true;
		if (table.toUpperCase().startsWith(bean.getFilter().toUpperCase()))
			return true;
		return false;
	}

	/**
	 * popolamento dell'array delle tabelle in base al contenuto del bean
	 */
	private void populateTables() {
		DatabaseUtil dbUtil = DatabaseUtil.getInstance();
		DatabaseMetadataUtil dbmdUtil=null;
		DatabaseUtil.tablePackageStructure.clear();
		try {
			dbmdUtil = new DatabaseMetadataUtil(dbUtil.getDatabaseMetaData());
		} catch (Exception e2) {
			setErrorMessage(e2.getMessage());
		}
		
		if (bean.getDriver().equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")){
			try{
				String[] listTables = dbmdUtil.getTables(null, bean.getSchema(), bean.getString(Preferences.LIST_TABLES), new String[]{"TABLE"});
				if (listTables == null || listTables.length ==0){
					String create ="CREATE TABLE "+bean.getString(Preferences.LIST_TABLES)+"("+
									"TABLE_NAME VARCHAR2(30) NOT NULL,"+
									"BULK_NAME VARCHAR2(30) NOT NULL,"+
									"PACKAGE_NAME VARCHAR2(250) NOT NULL,"+
									"MODULO VARCHAR2(30) NOT NULL)";
					PreparedStatement statement = dbUtil.getConnection().prepareStatement(create);
					statement.execute();
					String alter = "ALTER TABLE "+bean.getString(Preferences.LIST_TABLES)+" ADD PRIMARY KEY (TABLE_NAME)";
					statement = dbUtil.getConnection().prepareStatement(alter);
					statement.execute();
				}
			} catch (Exception e) {
				setErrorMessage(e.getMessage());
			}
			
			String query = "Select * from "+bean.getString(Preferences.LIST_TABLES)+
						" Where modulo is not null";
			try {
				PreparedStatement statement = dbUtil.getConnection().prepareStatement(query);
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					TablePackageStructure tps = new TablePackageStructure();
					tps.setBulkName(rs.getString("BULK_NAME"));
					tps.setModuleName(rs.getString("MODULO"));
					tps.setPackageName(rs.getString("PACKAGE_NAME"));
					tps.setTableName(rs.getString("TABLE_NAME"));
					DatabaseUtil.tablePackageStructure.put(tps.getTableName(), tps);
				}
			} catch (SQLException e1) {
				setErrorMessage(e1.getMessage());
			}
			query = "select a.table_name TABLE_NAME,b.column_name COLUMN_NAME,a.search_condition SEARCH_CONDITION "+
					"from all_constraints a, all_cons_columns b "+ 
					"where a.owner = '" +schemaCombo.getItem(schemaCombo.getSelectionIndex())+"' "+
					"and a.owner = b.owner "+
					"and a.table_name = b.table_name "+
					"and a.constraint_name = b.constraint_name "+
					"and a.constraint_type = 'C' "+
					"order by position";
			try {
				PreparedStatement statement = dbUtil.getConnection().prepareStatement(query);
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					String search = rs.getString("SEARCH_CONDITION");
					search = TextUtil.removeSpaces(search);
					if (search.trim().endsWith("('S','N')")){
						CheckSN sn = new CheckSN();
						sn.setTableName(rs.getString("TABLE_NAME"));
						sn.setColumnName(rs.getString("COLUMN_NAME"));
						DatabaseUtil.checkSN.put("TABLE-"+sn.getTableName()+"-COLUMN-"+sn.getColumnName(), sn);
					}
				}
			} catch (SQLException e1) {
				setErrorMessage(e1.getMessage());
			}
			query = "Select * from user_tab_comments";
			try {
				PreparedStatement statement = dbUtil.getConnection().prepareStatement(query);
				ResultSet rs = statement.executeQuery();
				while(rs.next()){
					TableComments tc = new TableComments();
					tc.setTableName(rs.getString("TABLE_NAME"));
					tc.setComments(rs.getString("COMMENTS"));
					DatabaseUtil.tableComments.put("TABLE-"+tc.getTableName(), tc);
				}
			} catch (SQLException e1) {
				setErrorMessage(e1.getMessage());
			}		
		}		
		try {
			String[] wrapper = new String[] {bean.getTableType()};
			tables = dbmdUtil.getTables(null, bean.getSchema(), null, wrapper);
			
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * Inizializzazione dei campi di navigazione database.
	 * Questa inizializzazione � chiamata dalla pagina precedente all'apertura 
	 * della connessione. Tutti i campi vengono inizializzati ai valori predefiniti.
	 * L'eventuale selezione precedente viene persa.
	 * 
	 * @see BeanGeneratorWizardPage#initializePage()
	 */
	public void initializePage() {
		DatabaseUtil dbUtil = DatabaseUtil.getInstance();
		try {
			DatabaseMetadataUtil dbmdUtil = new DatabaseMetadataUtil(dbUtil.getDatabaseMetaData());
			schemaCombo.setItems(dbmdUtil.getSchemas());
			int index = schemaCombo.indexOf(bean.getSchema());
			if (index > -1)
				schemaCombo.select(index);
			else
				schemaCombo.select(0);
			tableTypeCombo.setItems(dbmdUtil.getTableTypes());
			bean.getTableType();
			index = tableTypeCombo.indexOf("TABLE"); //$NON-NLS-1$
			if (index > -1) 
				tableTypeCombo.select(index);
			else				
				tableTypeCombo.select(0);
			bean.setFilter(null);
			doTablesQuery();
			
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
		}
	}
}
