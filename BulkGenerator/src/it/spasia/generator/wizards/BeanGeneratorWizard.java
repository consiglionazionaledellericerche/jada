package it.spasia.generator.wizards;

import it.spasia.generator.BulkGeneratorPlugin;
import it.spasia.generator.artifacts.ArtifactGenerator;
import it.spasia.generator.model.GeneratorBean;
import it.spasia.generator.properties.Messages;
import it.spasia.generator.properties.Preferences;
import it.spasia.generator.util.DatabaseUtil;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
//import org.eclipse.ui.IWorkbenchWizard;

/**
 * Wizard per la generazione degli artefatti di tipo bulk
 * 
 * @author Marco Spasiano
 * @version 1.0 
 * [7-Aug-2006] creazione
 * [23-Aug-2006] Strings Externalization
 * [24-Aug-2006] Ristrutturazione pagina GenerationWizard
 */
public class BeanGeneratorWizard extends Wizard implements INewWizard {
	private DatabaseConnectionWizardPage databaseConnection;
	private DatabaseMetadataWizardPage databaseMetadata;
	private GenerationWizardPage generationWizardPage;
	private IStructuredSelection selection;
	private GeneratorBean bean;

	/**
	 * Constructor for BeanGeneratorWizard.
	 */
	public BeanGeneratorWizard() {
		super();
		setWindowTitle(Messages.BulkGeneratorWizard_title);
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		initializeBean();
		
		databaseConnection = new DatabaseConnectionWizardPage(bean);
		addPage(databaseConnection);

		databaseMetadata = new DatabaseMetadataWizardPage(bean);
		addPage(databaseMetadata);
		
		generationWizardPage = new GenerationWizardPage(bean);
		addPage(generationWizardPage);
	}
	
    /** indica all'applicazione se abilitare o meno il tasto finizh */
	public boolean canFinish() {
		return bean.isGenerationAllowed();
	}

	/**
     * Questo metodo viene chiamato quando il wizard viene "cancellato"
     */
    public boolean performCancel() {
    	DatabaseUtil.getInstance().closeConnectionQuietly();
        return true;
    }

	/**
	 * Metodo chiamato alla pressione di Finish
	 */
	public boolean performFinish() {
	      // Perform the operation in a separate thread
	      // so that the operation can be canceled
	      try {
	         getContainer().run(true, true, new IRunnableWithProgress() {
	            public void run(IProgressMonitor monitor)
	               throws
	                  InvocationTargetException,
	                  InterruptedException {
	            	
	            	ArtifactGenerator generator = new ArtifactGenerator(bean);
	            	generator.generate(monitor);
	            	
	            }
	         });
	      }
	      catch (InvocationTargetException e) {
	            if (e.getTargetException() instanceof CoreException) {
	                ErrorDialog.openError(
	                		getContainer().getShell(),
	                		Messages.BulkGeneratorWizard_error,
	                		null, // no special message
	                		((CoreException) e.getTargetException()).getStatus());
	            } else {
	                // CoreExceptions are handled above, but unexpected runtime exceptions and errors may still occur.
	                MessageDialog.openError(
	                		getContainer().getShell(),
	                		Messages.BulkGeneratorWizard_message, 
	                		e.getTargetException().getMessage());
	            }
	      }
	      catch (InterruptedException e) {
	         // User canceled, so stop but don't close wizard
	         return false;
	      }
	      return true;
	   }
	
	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
	
	/** 
	 * popola il bean di trasporto dati con i dati preferiti
	 */
	private void initializeBean() {
		bean = new GeneratorBean();
		bean.setSelection(selection);
		bean.setStore(BulkGeneratorPlugin.getDefault().getPreferenceStore());
		bean.setDriver(bean.getString(Preferences.DB_DRIVER));
		bean.setUrl(bean.getString(Preferences.DB_URL));
		bean.setUser(bean.getString(Preferences.DB_USER));
		bean.setPassword(bean.getString(Preferences.DB_PASSWORD));
		bean.setSchema(bean.getString(Preferences.DB_SCHEMA));
	}
}