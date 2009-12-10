package it.spasia.generator.wizards;


import it.spasia.generator.artifacts.ArtifactNames;
import it.spasia.generator.model.GeneratorBean;
import it.spasia.generator.model.StatusInfo;
import it.spasia.generator.properties.Messages;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IStringButtonAdapter;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.JavaElementSorter;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
/**
 * Pagina wizard di scelta della destinazione degli artefatti
 * 
 * @author Marco Spasiano
 * @version 1.0 [09-Aug-2006]
 * [23-Aug-2006] Strings Externalization 
 */
public class GenerationWizardPage extends NewTypeWizardPage implements IBeanGeneratorWizardPage {
	public final static String PAGE_NAME= "GenerationWizardPage";
	/** Field ID of the type name input field. */	
	protected final static String PREFIX= PAGE_NAME + ".prefix";
	protected final static String FOLDER= PAGE_NAME + ".folder";
	private Text fPrefixTextField;
	private Text fFolderTextField;
	protected IStatus fPrefixStatus;
	protected IStatus fFolderStatus;
	private Button overwriteCheckbox;
	private Boolean overwriteSelected;
	
	private GeneratorBean bean;
	private ArrayList<String> artifacts;
	private ListViewer viewer;	
	
	public GenerationWizardPage(GeneratorBean bean) {
		super(true, PAGE_NAME);
		setTitle(Messages.GenerationWizardPage_title); 
		setDescription(Messages.GenerationWizardPage_description);
		
		this.bean = bean;
		artifacts = new ArrayList<String>();
		
		fPrefixStatus = new StatusInfo();
		
	}

	// -------- Initialization ---------
	
	/**
	 * The wizard owning this page is responsible for calling this method with the
	 * current selection. The selection is used to initialize the fields of the wizard 
	 * page.
	 * 
	 * @param selection used to initialize the fields
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem= getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
	}

	/**
	 * @see BeanGeneratorWizardPage#initializePage()
	 */
	public void initializePage() {		
		String temp = bean.getTable() == null ? "" : bean.getTable().toLowerCase(); //$NON-NLS-1$
		temp = Character.toUpperCase(temp.charAt(0)) + temp.substring(1);
		String as[] = null;
        StringTokenizer stringtokenizer = new StringTokenizer(temp, "_");
        as = new String[stringtokenizer.countTokens()];
        for(int k = 0; k < as.length; k++)
            as[k] = stringtokenizer.nextToken();
        String prefisso = "";
        for(int j = 0; j < as.length; j++){
        	prefisso = prefisso.concat(Character.toUpperCase(as[j].charAt(0)) + as[j].substring(1));
        }
		
		bean.setPrefix(prefisso);
		bean.setGenerationAllowed(false);
		overwriteSelected = null;
		fPrefixTextField.setText(bean.getPrefix());
	}
	
	// ------ validation --------
		
	/*
	 * @see NewContainerWizardPage#handleFieldChanged
	 */
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		if (fieldName == CONTAINER || fieldName == PACKAGE || fieldName == PREFIX || fieldName == FOLDER) {
			fPrefixStatus= prefixChanged();			
			fFolderStatus= folderChanged();
			bean.setPackageName(getPackageText());
			bean.setPrefix(fPrefixTextField.getText());
			bean.setSourceFolder(getPackageFragmentRootText());
			bean.setTargetXMLFolder(fFolderTextField.getText());
			overwriteSelected =  null;
		
			doStatusUpdate();
		}
	}
	
	// update page status
	private void doStatusUpdate() {
		// status of all used components
		IStatus[] status= new IStatus[] {
			fContainerStatus,
			fPackageStatus,
			fPrefixStatus,
			fFolderStatus
		};
		
		// the mode severe status will be displayed and the OK button enabled/disabled.
		updateStatus(status);
		
		updateArtifacts();
		updateButtons();
        updateMessage(null);
	}
	
	// ------ UI --------
	
	/*
	 * @see WizardPage#createControl
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		
		int nColumns= 4;
		
		GridLayout layout= new GridLayout();
		layout.numColumns= nColumns;		
		composite.setLayout(layout);
		
		// pick & choose the wanted UI components
		
		createContainerControls(composite, nColumns);
		createFolderControls(composite, nColumns);
		createPackageControls(composite, nColumns);
		
		createPrefixControls(composite, nColumns);		
		createSeparator(composite, nColumns);
		createArtifactList(composite, nColumns);
		createOverwriteCheckBox(composite, nColumns);
		
		setControl(composite);		
		Dialog.applyDialogFont(composite);
		
		init(bean.getSelection());
	}

	private void createOverwriteCheckBox(Composite composite, int columns) {
		overwriteCheckbox = new Button(composite, SWT.CHECK);
		GridData gd= new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan= columns;
		overwriteCheckbox.setLayoutData(gd);
		overwriteCheckbox.setText(Messages.GenerationWizardPage_artifact_overwrite);
		overwriteCheckbox.setSelection(true);
		overwriteCheckbox.setVisible(false);
		overwriteCheckbox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				overwriteSelected = new Boolean(overwriteCheckbox.getSelection());
				updateMessage(null);
				updateButtons();
			}
		});
	}
	// ------ Campo Folder --------

	private void createFolderControls(Composite composite, int columns) {
		Label label = new Label(composite, SWT.LEFT);
		label.setFont(composite.getFont());	
		label.setText(Messages.GenerationWizardPage_folder);
		fFolderTextField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fFolderTextField.setFont(composite.getFont());
		fFolderTextField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				folderTextFieldChanged();
			}
		});
		GridData gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.grabExcessHorizontalSpace= false;
		gd.horizontalSpan= 1;
		fFolderTextField.setLayoutData(gd);
		
		label= new Label(composite, SWT.LEFT);
		gd= new GridData();
		gd.horizontalAlignment= GridData.BEGINNING;
		gd.grabExcessHorizontalSpace= false;
		gd.horizontalSpan= 1;
		gd.horizontalIndent= 0;
		gd.widthHint= 0;
		gd.heightHint= 0;
		label.setLayoutData(gd);
		
		Button button= new Button(composite, SWT.PUSH);
		button.setFont(composite.getFont());	
		button.setText("Browse...");
		gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.grabExcessHorizontalSpace= false;
		gd.horizontalSpan= 1;
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				fFolderTextField.setText(chooseContainerFolder().getPath().toString());
			}
			public void widgetSelected(SelectionEvent e) {
				fFolderTextField.setText(chooseContainerFolder().getPath().toString());
			}
		});		
	}
	protected PackageFragmentRoot chooseContainerFolder() {
		IJavaElement initElement= getPackageFragmentRoot();
		Class[] acceptedClasses= new Class[] { IJavaProject.class, PackageFragmentRoot.class};
		TypedElementSelectionValidator validator= new TypedElementSelectionValidator(acceptedClasses, false) {
			public boolean isSelectedValid(Object element) {
				try {
					if (element instanceof IJavaProject) {
						IJavaProject jproject= (IJavaProject)element;
						IPath path= jproject.getProject().getFullPath();
						return (jproject.findElement(path) != null);
					} else if (element instanceof PackageFragmentRoot) {
						return true;
					}
					return true;
				} catch (JavaModelException e) {
					JavaPlugin.log(e.getStatus()); // just log, no UI in validation
				}
				return false;
			}
		};
		
		acceptedClasses= new Class[] {IJavaProject.class};
		ViewerFilter filter= new TypedViewerFilter(acceptedClasses) {
			public boolean select(Viewer viewer, Object parent, Object element) {
				if (element instanceof PackageFragmentRoot && ((PackageFragmentRoot)element).getResource() != null) {
					return true;
				}
				return super.select(viewer, parent, element);
			}
		};		

		StandardJavaElementContentProvider provider= new StandardJavaElementContentProvider();
		ILabelProvider labelProvider= new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT); 
		ElementTreeSelectionDialog dialog= new ElementTreeSelectionDialog(getShell(), labelProvider, provider);
		dialog.setValidator(validator);
		dialog.setSorter(new JavaElementSorter());
		dialog.setTitle(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_title); 
		dialog.setMessage(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_description); 
		dialog.addFilter(filter);
		dialog.setInput(JavaCore.create(getWorkspaceRoot()));
		dialog.setInitialSelection(initElement);
		dialog.setHelpAvailable(false);
		
		if (dialog.open() == Window.OK) {
			Object element= dialog.getFirstResult();
			if (element instanceof PackageFragmentRoot) {
				return (PackageFragmentRoot)element;
			}
			return null;
		}
		return null;
	}	

	// ------ Campo Prefix --------

	private void createPrefixControls(Composite composite, int columns) {
		Label label = new Label(composite, SWT.LEFT);
		label.setFont(composite.getFont());	
		label.setText(Messages.GenerationWizardPage_prefix);
		fPrefixTextField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		fPrefixTextField.setFont(composite.getFont());
		fPrefixTextField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				prefixTextFieldChanged();
			}
		});
		GridData gd= new GridData();
		gd.horizontalAlignment= GridData.FILL;
		gd.grabExcessHorizontalSpace= false;
		gd.horizontalSpan= 2;
		fPrefixTextField.setLayoutData(gd);
		
		label= new Label(composite, SWT.LEFT);
		gd= new GridData();
		gd.horizontalAlignment= GridData.BEGINNING;
		gd.grabExcessHorizontalSpace= false;
		gd.horizontalSpan= 1;
		gd.horizontalIndent= 0;
		gd.widthHint= 0;
		gd.heightHint= 0;
		label.setLayoutData(gd);
	}
	
	private void prefixTextFieldChanged() {
		fPrefixStatus= prefixChanged();
		// tell all others
		handleFieldChanged(PREFIX);
	}
	private void folderTextFieldChanged() {
		fFolderStatus= folderChanged();
		// tell all others
		handleFieldChanged(FOLDER);
	}

	private IStatus prefixChanged() {
		StatusInfo status= new StatusInfo();
		String prefix= fPrefixTextField.getText();
		// must not be empty
		if (prefix.length() == 0) {
			status.setError(Messages.GenerationWizardPage_prefix_err_empty); 
			return status;
		}
		if (prefix.indexOf('.') != -1) {
			status.setError(Messages.GenerationWizardPage_prefix_err_qualified); 
			return status;
		}
		IStatus val= JavaConventions.validateJavaTypeName(prefix);
		if (val.getSeverity() == IStatus.ERROR) {
			status.setError(val.getMessage()); 
		} else if (val.getSeverity() == IStatus.WARNING) {
			status.setWarning(val.getMessage());
		}
		return status;
	}
	private IStatus folderChanged() {
		StatusInfo status= new StatusInfo();
		String folder= fFolderTextField.getText();
		// must not be empty
		if (folder.length() == 0) {
			status.setError(Messages.GenerationWizardPage_folder_err_empty); 
			return status;
		}
		if (folder.indexOf('.') != -1) {
			status.setError(Messages.GenerationWizardPage_folder_err_qualified); 
			return status;
		}
		return status;
	}

	private void updateButtons() {		
		bean.setGenerationAllowed(false);
		boolean overwrite = overwriteSelected == null ? true : overwriteSelected.booleanValue();
		if (isCurrentPage() && isNotErrorPending() && overwrite)
			bean.setGenerationAllowed(true);
		if (isControlCreated())
			getWizard().getContainer().updateButtons();
	}

	// -------- Lista degli artefatti --------
	
	private void createArtifactList(Composite composite, int columns) {
		Label label = new Label(composite, SWT.LEFT);
		label.setText(Messages.GenerationWizardPage_artifacts_caption);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = columns;
		label.setLayoutData(gd);
		List list = new List(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = columns;
		list.setLayoutData(gd);
		viewer = new ListViewer(list);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setInput(artifacts);
	}
	
	private void updateArtifacts() {
		// widgets not yet created
		if (isControlCreated()== false) return;
		
		boolean exist = false;
		artifacts.clear();
		
		if (isNotErrorPending()) {
			//if (isTexNontEmpty(bean.getPackageName()) && isTexNontEmpty(bean.getPrefix())) {
				artifacts.add(getArtifactsRootName());
				ArtifactNames names = new ArtifactNames(bean);
				for (int i=0; i < names.getArtifactNames().length ;i++) {
					String name = names.getArtifactNames()[i];
					if (exist(name)) {
						exist = true;
						name = name + " (*)"; //$NON-NLS-1$
					}
					artifacts.add(name);
				}
			//}
		}
		viewer.setInput(artifacts);
		overwriteCheckbox.setVisible(exist);
		if (exist) { 
			overwriteSelected = new Boolean(false);
			updateMessage(Messages.GenerationWizardPage_artifact_exist);
		}
		boolean overwrite = overwriteSelected == null ? true : overwriteSelected.booleanValue();
		overwriteCheckbox.setSelection(overwrite);
	}
	
	private String getArtifactsRootName() {
		StringBuffer sb = new StringBuffer();
		sb.append("["); //$NON-NLS-1$
		sb.append(getWorkspaceRoot().getLocation().toString());
		try {
			sb.append("/"+bean.getSourceFolder()+"/"+bean.getPackageName().replace('.', '/'));
		} catch (Throwable t) {
			sb.append(Messages.GenerationWizardPage_root_err);
		}
		sb.append("]"); //$NON-NLS-1$
		return sb.toString();
	}
	
	private String getArtifactsXMLRootName() {
		StringBuffer sb = new StringBuffer();
		sb.append(getWorkspaceRoot().getLocation().toString());
		try {
			sb.append(bean.getTargetXMLFolder()+"/");
		} catch (Throwable t) {
			sb.append(Messages.GenerationWizardPage_root_err);
		}
		return sb.toString();
	}

	private String getArtifactsXRootName() {
		StringBuffer sb = new StringBuffer();
		sb.append("["); //$NON-NLS-1$
		sb.append(getWorkspaceRoot().getLocation().toString());
		try {
			sb.append("/"+bean.getTargetXMLFolder()+"/");
		} catch (Throwable t) {
			sb.append(Messages.GenerationWizardPage_root_err);
		}
		sb.append("]"); //$NON-NLS-1$
		return sb.toString();
	}
	
	// ritorna vero se il nome indicato esiste nel workspace selezionato
	private boolean exist(String name) {
		if (name.endsWith(ArtifactNames.JAVA_EXT)) {
			return existJava(name);
		} else {
			return existFile(name);
		}
	}
	// ritorna vero se esiste l'elemento java indicato
	private boolean existJava(String name) {
		String clazz = ArtifactNames.getName(name);
		IPackageFragment pack = getPackageFragment();
		ICompilationUnit cu = pack.getCompilationUnit(clazz);
		IResource resource = cu.getResource();
		if (resource != null)
			return existResource(resource);
		return false;
	}
	// ritorna vero se esiste il file indicato
	private boolean existFile(String name) {
		IPath path= new Path(name); //$NON-NLS-1$
		IResource resource = getWorkspaceRoot().findMember(path);
		if (resource != null)
			return existResource(resource);
		return false;
	}
	// ritorna vero se esiste la risorsa indicata
	private boolean existResource(IResource resource) {
		if (resource.exists()) {
			return true;
		}
		IPath location = resource.getLocation();
		if (location != null && location.toFile().exists()) {
			return true;
		}
		return false;
	}
	private boolean isErrorPending() {
		if (fContainerStatus.matches(IStatus.ERROR)) 
			return true;
		if (fPackageStatus.matches(IStatus.ERROR)) 
			return true;
		if (fPrefixStatus.matches(IStatus.ERROR))
			return true;
		return false;			
	}
	private boolean isNotErrorPending() {
		return !isErrorPending();
	}
	/** Intercetta quando la pagina � resa visibile */
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) 
        	doStatusUpdate();
        else
        	bean.setGenerationAllowed(false);
    }

	private void updateMessage(String message) {
		if (isCurrentPage())
			if (getErrorMessage() == null && getMessage() == null)
				setMessage(message, IMessageProvider.WARNING);
	}
}