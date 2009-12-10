package it.spasia.generator.wizards;

import it.spasia.generator.model.GeneratorBean;
import it.spasia.generator.properties.Messages;
import it.spasia.generator.util.DatabaseUtil;
import it.spasia.generator.util.OracleDatabaseUtil;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Pagina wizard per la connessione al database
 * 
 * @author Marco Spasiano
 * @version 1.0 
 * [7-Aug-2006] creazione
 * [23-Aug-2006] Strings Externalization 
 */
public class DatabaseConnectionWizardPage extends BeanGeneratorWizardPage  {
	public final static String PAGE_NAME= "DatabaseConnectionWizardPage"; //$NON-NLS-1$
	private Text driverText;
	private Text urlText;
	private Text userText;
	private Text passText;
	private Button openButton;

	/**
	 * Costruttore
	 */
	public DatabaseConnectionWizardPage(GeneratorBean bean) {
		super(PAGE_NAME, bean);
		setTitle(Messages.DatabaseConnectionWizardPage_title);
		setDescription(Messages.DatabaseConnectionWizardPage_description);
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
		label.setText(Messages.DatabaseConnectionWizardPage_driver);
		driverText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		driverText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		driverText.setText(bean.getDriver());
		driverText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(composite, SWT.NULL);
		label.setText(Messages.DatabaseConnectionWizardPage_url);
		urlText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		urlText.setText(bean.getUrl());
		urlText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(composite, SWT.NULL);
		label.setText(Messages.DatabaseConnectionWizardPage_user);
		userText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		userText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		userText.setText(bean.getUser());
		userText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		label = new Label(composite, SWT.NULL);
		label.setText(Messages.DatabaseConnectionWizardPage_password);
		passText = new Text(composite, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
		passText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		passText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		label = new Label(composite, SWT.NULL);
		label.setText(""); //$NON-NLS-1$
		openButton = new Button(composite, SWT.RIGHT | SWT.PUSH);
		openButton.setText(Messages.DatabaseConnectionWizardPage_open);
		passText.setText(bean.getPassword());
		openButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleOpenConnection();
			}
		});
		dialogChanged();
		setControl(composite);		
	}

	/**
	 * Apertura della connessione al database
	 */
	private void handleOpenConnection() {
		dialogChanged();
		DatabaseUtil util = DatabaseUtil.getInstance();
		try {
			util.openConnection(bean);
			setMessage(Messages.DatabaseConnectionWizardPage_connection_ok, WizardPage.INFORMATION);
			openButton.setEnabled(false);
			initializeNextPage(bean);
			setPageComplete(true);
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * controllo formale dei dati di input
	 */
	private void dialogChanged() {
		setPageComplete(false);
		
		populateBean();
		
		if (isTextEmpty(bean.getDriver()))
			setErrorMessage(Messages.DatabaseConnectionWizardPage_driver_mandatory);	
		else
		if (isTextEmpty(bean.getUrl()))
			setErrorMessage(Messages.DatabaseConnectionWizardPage_url_mandatory);			
		else
		if (isTextEmpty(bean.getUser()))
			setErrorMessage(Messages.DatabaseConnectionWizardPage_user_mandatory);			
		else
		if (isTextEmpty(bean.getPassword()))
			setErrorMessage(Messages.DatabaseConnectionWizardPage_password_mandatory);			
		else
			setErrorMessage(null);

		openButton.setEnabled(getErrorMessage() == null);
	}

	/** popola il bean di trasporto dati dai campi di input */
	private void populateBean() {
		bean.setDriver(driverText.getText());
		bean.setUrl(urlText.getText());
		bean.setUser(userText.getText());
		bean.setPassword(passText.getText());
	}
	/**
	 * @see BeanGeneratorWizardPage#initialize(GeneratorBean)
	 */
	public void initializePage() {
		// do nothing
	}
	

}