/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.jada.util.action;

import java.io.IOException;
import java.util.BitSet;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

/**
 * @author Marco Spasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsultazioniBP extends SelezionatoreListaBP 
	implements SelectionListener, SearchProvider {

	private String componentSessioneName;
	private String multiSelezione;
	private Integer recordPerPagina;
	private Class bulkClass;
	private CompoundFindClause findclause;
	private CompoundFindClause baseclause;
	private java.lang.String searchResultColumnSet;
	private java.lang.String freeSearchSet;
	private java.lang.String archiveEnabled;	
	private java.lang.String filterEnabled;	

	private int navPosition = 0;

	public ConsultazioniBP(String s) {
		super(s);
	}

	public ConsultazioniBP() {
		super();
	}

	public it.cnr.jada.ejb.CRUDComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException, BusinessProcessException {
	
		return (it.cnr.jada.ejb.CRUDComponentSession)createComponentSession("JADAEJB_CRUDComponentSession", it.cnr.jada.ejb.CRUDComponentSession.class);
	}
	
	protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			super.init(config,context);
			setBulkClassName(config.getInitParameter("bulkClassName"));	
			setComponentSessioneName(config.getInitParameter("componentSessionName"));
			setMultiSelezione(config.getInitParameter("multiSelezione"));
            setArchiveEnabled(config.getInitParameter("archiveEnabled"));
			setSearchResultColumnSet(config.getInitParameter("searchResultColumnSet"));
			setColumns(getBulkInfo().getColumnFieldPropertyDictionary(getSearchResultColumnSet()));
			setFreeSearchSet(config.getInitParameter("freeSearchSet"));
			setRecordPerPagina(new Integer(config.getInitParameter("recordPerPagina")!=null?config.getInitParameter("recordPerPagina"):"20"));
			setFilterEnabled(config.getInitParameter("filterEnabled")!=null?config.getInitParameter("filterEnabled"):"Y");
			if (getMultiSelezione()!= null && getMultiSelezione().equals("Y"))
			   setMultiSelection(true);			      
			setPageSize(getRecordPerPagina().intValue());
			//TODO Da testare Mario
			if (context.getBusinessProcess().getBPLevel() == 1)
			  openIterator(context);
					
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}		

	public void openIterator(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try	{	
			OggettoBulk model = (OggettoBulk)getBulkInfo().getBulkClass().newInstance();

			it.cnr.jada.util.RemoteIterator ri =
				it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
				(context, createComponentSession().cerca(context.getUserContext(),CompoundFindClause.and(getBaseclause(),getFindclause()),model));
			this.setIterator(context,ri);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	
	public it.cnr.jada.util.jsp.Button[] createNavigatorToolbar() {
		int i = 0;
		it.cnr.jada.util.jsp.Button[] toolbar;
		if (getFilterEnabled().equals("Y")) {
			toolbar = new it.cnr.jada.util.jsp.Button[6];
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.filter");
			toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.removeFilter");
			setNavPosition(2);
		}
		else
			toolbar = new it.cnr.jada.util.jsp.Button[4];
		
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Navigator.previousFrame");
		toolbar[i-1].setSeparator(true);
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Navigator.previous");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Navigator.next");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Navigator.nextFrame");
	
		return toolbar;
	}

	public Button[] createToolbar()
	{
		java.util.Vector listButton = new java.util.Vector();
		listButton.addElement(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.print"));
		listButton.addElement(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.excel"));
		listButton.addElement(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.hiddenColumn"));
		setMostraHideColumns(true);
		Button button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.selectAll");
		button.setSeparator(true);
		listButton.addElement(button);
		listButton.addElement(new Button(Config.getHandler().getProperties(getClass()), "Toolbar.deselectAll"));
		listButton = addButtonsToToolbar(listButton);
		button = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.archive");
		button.setSeparator(true);
		listButton.addElement(button);
		Button abutton[] = new Button[listButton.size()];
		for(int i = 0;i < listButton.size();i++){
			abutton[i] = (Button)listButton.get(i);
		}		
		return abutton;
	}
	/**
	 * Metodo che permette di aggiungere dei Bottoni
	 * a quelli di default nel caso di Multiselezione
	 * i Bottoni sono quattro: "Toolbar.print", "Toolbar.excel", "Toolbar.selectAll", "Toolbar.deselectAll"
	 * se invece non � una multiselezione sono due: "Toolbar.print", "Toolbar.excel"
	 * 
	 * @param listButton
	 * @return
	 */
	public java.util.Vector addButtonsToToolbar(java.util.Vector listButton){
		return listButton;
	}	
	public void writeHTMLNavigator(JspWriter jspwriter)
		throws IOException, ServletException
	{
		Button abutton[] = getNavigatorToolbar();
		jspwriter.println("<div class=\"Toolbar\">");
		jspwriter.println("<table cellspacing=\"0\" cellpadding=\"0\">");
		jspwriter.println("<tr align=center valign=middle>");
		for (int i = 0; i<getNavPosition();i++) {
			jspwriter.print("<td>");
			abutton[i].write(jspwriter, this);
			jspwriter.println("</td>");
		}
		jspwriter.print("<td");
		if(abutton[getNavPosition()].hasSeparator())
			jspwriter.print(" class=\"VSeparator\"");
		jspwriter.print(">");
		abutton[getNavPosition()].write(jspwriter, this);
		jspwriter.println("</td>");
		jspwriter.print("<td>");
		abutton[getNavPosition()+1].write(jspwriter, this);
		jspwriter.println("</td>");
		getLastPage();
		for(int i = getFirstPage(); i < getLastPage(); i++)
		{
			jspwriter.print("<td width=\"16\">");
			if(getCurrentPage() != i)
				JSPUtils.button(jspwriter, null, String.valueOf(i), "javascript:submitForm('doGotoPage(" + i + ")')");
			else
				JSPUtils.button(jspwriter, null, String.valueOf(i), null, "background: Highlight;color: HighlightText;");
			jspwriter.println("</td>");
		}

		jspwriter.print("<td>");
		abutton[getNavPosition()+2].write(jspwriter, this);
		jspwriter.println("</td>");
		jspwriter.print("<td>");
		abutton[getNavPosition()+3].write(jspwriter, this);
		jspwriter.println("</td>");
		for(int j = getNavPosition()+4; j < abutton.length; j++)
		{
			jspwriter.print("<td>");
			abutton[getNavPosition()+j].write(jspwriter, this);
			jspwriter.println("</td>");
		}

		jspwriter.println("</tr>");
		jspwriter.println("</table>");
		jspwriter.println("</div>");
	}

	public RemoteIterator search(
		ActionContext actioncontext,
		CompoundFindClause compoundfindclause,
		OggettoBulk oggettobulk)
		throws BusinessProcessException {
			/*
			 * Mi conservo la findClause per poi utilizzarla
			 * nel selectAll
			 */
			setFindclause(compoundfindclause);
			return findFreeSearch(actioncontext,
								  compoundfindclause,
								  oggettobulk);
	}
	public it.cnr.jada.util.RemoteIterator findFreeSearch(
		ActionContext context,
		it.cnr.jada.persistency.sql.CompoundFindClause clauses,
		OggettoBulk model) 
		throws it.cnr.jada.action.BusinessProcessException {
	
		try {
			clauses = CompoundFindClause.and(clauses,getBaseclause());
			it.cnr.jada.util.RemoteIterator ri =
				it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator
				(context, createComponentSession().cerca(context.getUserContext(),clauses,model));
			//this.setIterator(context,ri);
			return ri;			
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public OggettoBulk createEmptyModelForFreeSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		try {
			return createComponentSession().inizializzaBulkPerRicercaLibera(context.getUserContext(),(OggettoBulk)getBulkInfo().getBulkClass().newInstance());
		} catch(Exception e) {
			throw handleException(e);
		}
	}	
	/**
	 * @return
	 */
	public String getComponentSessioneName() {
		return componentSessioneName;
	}

	/**
	 * @param string
	 */
	public void setComponentSessioneName(String string) {
		componentSessioneName = string;
	}
	/**
	 * @return java.lang.Class
	 */
	public java.lang.Class getBulkClass() {
		return bulkClass;
	}
	/**
	 * @param newBulkClass java.lang.Class
	 */
	public void setBulkClass(java.lang.Class newBulkClass) {
		bulkClass = newBulkClass;
	}
	/**
	 * Imposta il valore della propriet� 'bulkClassName'
	 *
	 * @param bulkClassName	Il valore da assegnare a 'bulkClassName'
	 * @throws ClassNotFoundException	
	 */
	public void setBulkClassName(java.lang.String bulkClassName) throws ClassNotFoundException {
		bulkClass = getClass().getClassLoader().loadClass(bulkClassName);
		setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(bulkClass));
	}

	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#deselectAll(it.cnr.jada.action.ActionContext)
	 */
	public void deselectAll(ActionContext actioncontext) {}
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#getSelection(it.cnr.jada.action.ActionContext, it.cnr.jada.bulk.OggettoBulk[], java.util.BitSet)
	 */
	public BitSet getSelection(ActionContext actioncontext, OggettoBulk[] aoggettobulk, BitSet bitset) throws BusinessProcessException {
		return bitset;
	}
	
	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#initializeSelection(it.cnr.jada.action.ActionContext)
	 */
	public void initializeSelection(ActionContext actioncontext) throws BusinessProcessException {
		int dummy = 0;
		
	}

	/* (non-Javadoc)
	 * @see it.cnr.jada.util.action.SelectionListener#setSelection(it.cnr.jada.action.ActionContext, it.cnr.jada.bulk.OggettoBulk[], java.util.BitSet, java.util.BitSet)
	 */
	public BitSet setSelection(ActionContext actioncontext, OggettoBulk[] aoggettobulk, BitSet bitset, BitSet bitset1) throws BusinessProcessException {
		return null;
	}

	public void setFindclause(CompoundFindClause clause) {
		findclause = clause;
	}

	public CompoundFindClause getFindclause() {
		return findclause;
	}

	public CompoundFindClause getBaseclause() {
		return baseclause;
	}

	public void setBaseclause(CompoundFindClause clause) {
		baseclause = clause;
	}

	public void addToBaseclause(CompoundFindClause clause) {
		baseclause = CompoundFindClause.and(baseclause, clause);
	}

	
	public boolean isArchiveButtonHidden() 
	{
		if (getArchiveEnabled()!= null && getArchiveEnabled().equals("Y"))
			return false;
		else
			return true;
	}

	public boolean isFilterButtonHidden() 
	{
		if (getFindclause() == null)
			return false;
		else
			return true;
	}

	public boolean isRemoveFilterButtonHidden() 
	{
		if (isFilterButtonHidden())
			return false;
		else
			return true;
	}

	public boolean isObbligazioniButtonHidden()
	{
		return false;
	}
	
	public int getNavPosition() {
		return navPosition;
	}

	public void setNavPosition(int i) {
		navPosition = i;
	}

	/**
	 * @return
	 */
	public String getMultiSelezione() {
		return multiSelezione;
	}

	/**
	 * @param string
	 */
	public void setMultiSelezione(String string) {
		multiSelezione = string;
	}

	/**
	 * @return
	 */
	public Integer getRecordPerPagina() {
		return recordPerPagina;
	}

	/**
	 * @param integer
	 */
	public void setRecordPerPagina(Integer integer) {
		recordPerPagina = integer;
	}

	/**
	 * @return
	 */
	public java.lang.String getFreeSearchSet() {
		return freeSearchSet;
	}

	/**
	 * @return
	 */
	public java.lang.String getSearchResultColumnSet() {
		return searchResultColumnSet;
	}

	/**
	 * @param string
	 */
	public void setFreeSearchSet(java.lang.String string) {
		freeSearchSet = string;
	}

	/**
	 * @param string
	 */
	public void setSearchResultColumnSet(java.lang.String string) {
		searchResultColumnSet = string;
		setColumns(getBulkInfo().getColumnFieldPropertyDictionary(getSearchResultColumnSet()));
	}

	/**
	 * @return
	 */
	public java.lang.String getArchiveEnabled() {
		return archiveEnabled;
	}

	/**
	 * @param string
	 */
	public void setArchiveEnabled(java.lang.String string) {
		archiveEnabled = string;
	}

	public java.lang.String getFilterEnabled() {
		return filterEnabled;
	}
	

	public void setFilterEnabled(java.lang.String filterEnabled) {
		this.filterEnabled = filterEnabled;
	}
	

}
