package it.spasia.generator.artifacts;

import it.spasia.generator.model.GeneratorBean;
import it.spasia.generator.model.TableMetaData;
import it.spasia.generator.properties.Messages;
import it.spasia.generator.properties.Preferences;
import it.spasia.generator.util.DatabaseUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

/**
 * Genera gli artefatti (java e xml) e li colloca nel dominio del modello eclipse
 * 
 * @author Marco Spasiano
 * @version 1.0 [21-Aug-2006]
 */
public class ArtifactGenerator {
	private GeneratorBean bean;
	private IPackageFragmentRoot packageRoot;
	private IProgressMonitor monitor;
	private ArtifactNames names;
	private List columns;

	public ArtifactGenerator(GeneratorBean bean) {
		this.bean = bean;
	}
	public void generate(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		this.monitor = monitor;
		try {
			doGeneration();
	    	DatabaseUtil.getInstance().closeConnectionQuietly();
		} catch (Exception e) {
			java.io.StringWriter sw = new java.io.StringWriter();
			e.printStackTrace(new java.io.PrintWriter(sw));
			String stackTrace = sw.toString();
			if (e instanceof InterruptedException){
				throw (InterruptedException) e;
			}else{
		    	DatabaseUtil.getInstance().closeConnectionQuietly();
		    	throw new InvocationTargetException(new Exception(stackTrace));
			}

		}
	}
	
	public void doGeneration() throws Exception {

		IPath path = new Path(bean.getSourceFolder());
		IResource res = getWorkspaceRoot().findMember(path);
		if (res == null)
			throw new InvocationTargetException(new Exception(Messages.ArtifactGenerator_err_source_null));
		int resType = res.getType();
		if (resType == IResource.PROJECT || resType == IResource.FOLDER) {
			IProject proj = res.getProject();
			if (!proj.isOpen()) {
				throw new Exception(Messages.ArtifactGenerator_err_ProjectClosed);
			}
			IJavaProject jproject = JavaCore.create(proj);
			packageRoot = jproject.getPackageFragmentRoot(res);
		}

		// creazione dei modelli dati
		columns = new TableMetaData(bean).getTableMetaData();
		names = new ArtifactNames(bean);
		if (monitor != null)
			monitor.beginTask(Messages.ArtifactGenerator_op_description, names.getArtifactNames().length);
		generateJava();
		generateXML();
		aggiornaListTables();
		if (monitor != null)
			monitor.done();
	}
	private void aggiornaListTables() throws Exception {
		if (!bean.getDriver().equalsIgnoreCase("oracle.jdbc.driver.OracleDriver"))
			return;
		DatabaseUtil dbUtil = DatabaseUtil.getInstance();
		String search = "Select * from "+bean.getString(Preferences.LIST_TABLES)+
						" Where TABLE_NAME = '"+bean.getTable()+"'";
		String modulo = bean.getSourceFolder().substring(0, bean.getSourceFolder().indexOf("/"));
		PreparedStatement statement = dbUtil.getConnection().prepareStatement(search);
		if (statement.executeQuery().next()){
			String update ="UPDATE "+bean.getString(Preferences.LIST_TABLES)+
			" set BULK_NAME = '"+bean.getPrefix()+"',"+
			"PACKAGE_NAME = '"+bean.getPackageName()+"',"+
			"MODULO = '"+modulo+"'"+
			" Where TABLE_NAME = '"+bean.getTable()+"'";
			statement = dbUtil.getConnection().prepareStatement(update);
			statement.execute();
		}else{
			String insert ="INSERT INTO "+bean.getString(Preferences.LIST_TABLES)+"("+
			"TABLE_NAME, BULK_NAME, PACKAGE_NAME, MODULO)"+
			"VALUES('"+bean.getTable()+"','"+bean.getPrefix()+"','"+bean.getPackageName()+"','"+modulo+"')";
			statement = dbUtil.getConnection().prepareStatement(insert);
			statement.execute();
		}
	}
	private void generateXML() throws Exception {		
		for (int i = 0; i < names.getArtifactNames().length; i++) {
			if (names.isXMLName(i)) {
				ArtifactContents generator = names.getGenerator(i);
				generator.generate(columns);
				IPath path = new Path(names.getArtifactName(i));
				createFile(getWorkspaceRoot().getFile(path), getContents(generator)); 
			}
		}
	}
	private void generateJava() throws Exception {		
		IPackageFragment pack=null;
		if (bean.getPackageName() == null || bean.getPackageName().trim().length() == 0)
			pack = packageRoot.getPackageFragment("");
		else
			pack = packageRoot.getPackageFragment(bean.getPackageName());
						
		if (!pack.exists()) {
			pack= packageRoot.createPackageFragment(bean.getPackageName(), true, null);
		}		

		for (int i = 0; i < names.getArtifactNames().length; i++) {
			if (names.isJavaName(i)) {
				ArtifactContents generator = names.getGenerator(i);
				generator.generate(columns);
				IPath path = pack.getPath().append(ArtifactNames.getName(names.getArtifactName(i)));
				createFile(getWorkspaceRoot().getFile(path), getContents(generator)); 
			}
		}
						
	}
	private InputStream getContents(ArtifactContents contents) {
		return new ByteArrayInputStream(contents.getContents().getBytes());

	}
	private IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	/**
	 * Creates a file resource given the file handle and contents.
	*/
	protected void createFile(IFile fileHandle, InputStream contents) throws Exception {
		if (contents == null)
			contents = new ByteArrayInputStream(new byte[0]);

		try {
			// Create a new file resource in the workspace
			IPath path = fileHandle.getFullPath();
			IWorkspaceRoot root = getWorkspaceRoot();
			int numSegments = path.segmentCount();
			if (numSegments > 2 && !root.getFolder(path.removeLastSegments(1)).exists()) {
				// If the direct parent of the path doesn't exist, try to create the
				// necessary directories.
				for (int i = numSegments - 2; i > 0; i--) {
					IFolder folder = root.getFolder(path.removeLastSegments(i));
					if (!folder.exists()) {
						folder.create(false, true, monitor);
					}
				}
			}
			fileHandle.create(contents, false, monitor);
		} catch (CoreException e) {
			// If the file already existed locally, just refresh to get contents
			if (e.getStatus().getCode() == IResourceStatus.PATH_OCCUPIED)
				//fileHandle.refreshLocal(IResource.DEPTH_ZERO, null);
				fileHandle.setContents(contents,IResource.FORCE,null);
			else
				throw e;
		}

		if (monitor.isCanceled())
			throw new InterruptedException();
		
		monitor.worked(1);
	}

}
