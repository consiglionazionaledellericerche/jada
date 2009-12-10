package it.spasia.generator.artifacts;
import java.util.List;

import it.spasia.generator.model.GeneratorBean;
import it.spasia.generator.properties.Preferences;

/**
 * Created on 18-nov-04
 * @author Marco Spasiano
 * @version 1.0
 * @version 2.0 - progetto eclipse wizard
 * [14-aug-2006] utilizzo di GeneratorBean
 * [21-aug-2006] utilizzo di ArtifactContents e inibizione di write()
 * [22-aug-2006] aggiunto metodo astratto generate()
 */
public abstract class XMLBase implements ArtifactContents {
	private static final String TAB = "\t", CRLF = "\n";
	private int nTabs = -1;
	private StringBuffer document;

	protected String the_class;
	protected String the_package;
	protected String the_table;
	
	protected GeneratorBean bean;

	public XMLBase(GeneratorBean bean) {
		this.bean = bean;
		
		String header = bean.getString(Preferences.XML_HEADER);
		if (header != null && header.trim().length() > 0)
			document = new StringBuffer(header);

		the_class = bean.getPrefix();
		the_package = bean.getPackageName();
		the_table = bean.getTable();			
	}
	protected void addAttribute(String name, String value) {
		document.append(CRLF);
		indent(++nTabs);
		document.append(name);
		document.append("=\"");
		document.append(value);
		document.append("\"");
		nTabs--;
	}
	protected void openTag(String name) {
		document.append(CRLF);
		indent(++nTabs);
		document.append("<");
		document.append(name);
	}
	protected void closeTag() {
		document.append(">");
	}
	protected void closeEmptyTag() {
		document.append(" /");
		closeTag();
		nTabs--;
	}
	protected void closeTag(String name) {
		document.append(CRLF);
		indent(nTabs);
		document.append("</");
		document.append(name);
		closeTag();
		nTabs--;
	}
	private void indent(int times) {
		for (int i = 0; i < times; i++) {
			document.append(TAB);

		}
	}
	
	// commentato perch� la scrittura la fa il programma esterno (ver2.0)
	protected void write() throws Exception {
//		the_file = the_file.replace('.', '_');		
//		File file = new File(the_file + ".xml");
//		FileOutputStream   fout = new FileOutputStream(file);
//		OutputStreamWriter out  = new OutputStreamWriter(fout);
//		out.write(document.toString());
//		out.flush();
//		out.close();
	}

	/** ritorna il contenuto generato (ver2.0)*/
	public String getContents() {
		return document == null ? null : document.toString();
	}
	/** genera il contenuto */
	public abstract void generate(List columns) throws Exception;
}
