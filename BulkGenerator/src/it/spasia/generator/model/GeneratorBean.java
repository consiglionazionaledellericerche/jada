package it.spasia.generator.model;

import it.spasia.generator.util.DatabaseInfo;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;



/**
 * Incapsula i dati necessari alla generazione dei vari artefatti
 * 
 * @author Marco Spasiano
 * @version 1.0 [7-Aug-2006] creazione
 */
public class GeneratorBean implements DatabaseInfo {
	private String driver;
	private String url;
	private String user;
	private String password;
	private String catalog;
	private String schema;
	private String filter;
	private String table;
	private String tableType;
	private String prefix;
	private String packageName;
	private String sourceFolder;
	private String targetXMLFolder;
	private IPreferenceStore store;
	private IStructuredSelection selection;
	private boolean generationAllowed;

	public String getSourceFolder() {
		return sourceFolder;
	}
	public void setSourceFolder(String sourceFolder) {
		this.sourceFolder = sourceFolder;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getTableType() {
		return tableType;
	}
	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String className) {
		this.prefix = className;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public IPreferenceStore getStore() {
		return store;
	}
	public void setStore(IPreferenceStore store) {
		this.store = store;
	}
	/** ritorna la preferenza associata alla chiave indicata */
	public String getString(String key) {
		return store == null ? null : store.getString(key);
	}
	public boolean isGenerationAllowed() {
		return generationAllowed;
	}
	public void setGenerationAllowed(boolean isGenerationAllowed) {
		this.generationAllowed = isGenerationAllowed;
	}
	public IStructuredSelection getSelection() {
		return selection;
	}
	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}
	public String getTargetXMLFolder() {
		return targetXMLFolder;
	}
	public void setTargetXMLFolder(String targetXMLFolder) {
		this.targetXMLFolder = targetXMLFolder;
	}
}
