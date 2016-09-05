package it.cnr.jada.action;

import it.cnr.jada.UserTransaction;
import it.cnr.jada.error.bulk.Application_errorBulk;
import it.cnr.jada.error.bulk.Application_errorHome;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
/**
 * Un BusinessProcess ha la responsabilit  di mantenere lo stato del processo di business che   stato 
 * avviato in seguito alla richiesta di un client. 
 * I BusinessProcess vengono mantenuti nella sessione, quindi sopravvivono tra una action e l'altra.
 * Ogni richiesta originata da un client determina la selezione di un business process corrente. 
 * Durante la valutazione della Action o nell'esecuzione di una JSP in seguito alla valutazione di un 
 * forward viene mantenuto il riferimento a tale business process, per cui   possibile colloquiare 
 * con tale istanza per reperire tutte le informazioni necessarie alla svolgimento della action e 
 * alla successiva costruzione della risposta.
 * Nell'ambito di una action   possibile accedere al business process corrente tramite l'ActionContext, 
 * mentre in una JSP mediante un metodo statico di BusinessProcess. 
 *   responsabilit  della JSP includere nella risposta il riferimento al business process in cui 
 * dovr  essere valutata la prossima action.
 * La valutazione di una action pu  portare alla creazione di un nuovo business process o alla chiusura 
 * di quello corrente. Nel caso di un nuovo business process   possibile aggiungerlo come figlio del 
 * business process corrente. In tal caso le ricerche di forward che non portano ad un risultato immediato, 
 * procedono nella gerarchia dei padri prima di passare alla mappatura statica.
 * Un business process pu  essere restituito da una action come forward; 
 * in tale caso verr  cercato il forward dal nome "default" nell'ambito del business process restituito.
 * Esiste sempre un business process che rappresenta la radice della gerarchia di tutti i business process. 
 * Se non specificato dalla richiesta esso   il business process di default.
 * Un BusinessProcess non pu  essere istanziato direttamente, ma solo tramite il metodo createBusinessProcess 
 * dell'ActionContext, specificando il nome contenuto all'interno del file di configurazione della ActionServlet. 
 * Subito dopo la creazione di un BusinessProcess viene invocato il metodo init con i parametri specificati 
 * nel file di configurazione.
 */
public class BusinessProcess implements Forward, Serializable{

	private final Map children;
	public static final String ROOTBPNAME = "rootbp";
	private BusinessProcess parent;
	private BusinessProcessMapping mapping;
	private String path;
	private String name;
	private final Map hooks;
	private UserTransaction userTransaction;
	private final int transactionPolicy;
	private boolean busy;
	private Properties resources;
	private Character function;
	public static final int IGNORE_TRANSACTION = 0;
	public static final int INHERIT_TRANSACTION = 1;
	public static final int REQUIRES_NEW_TRANSACTION = 2;
	public static final int REQUIRES_TRANSACTION = 3;

	protected BusinessProcess(){
		children = new HashMap();
		path = "";
		name = "rootbp";
		hooks = new HashMap();
		busy = false;
		transactionPolicy = 0;
	}
    /**
     * Costruisce un nuovo BusinessProcess
     * function - Una stringa contenente la modalit  transazionale del BusinessProcess. Le modalit  ammesse sono:
     * La modalit  transazionale di default   "Th"
     */
	protected BusinessProcess(String function){
		children = new HashMap();
		path = "";
		name = "rootbp";
		hooks = new HashMap();
		busy = false;
		if (function.length() > 0)
			this.function = function.charAt(0);  
		int i = function.indexOf('T') + 1;		
		int transactionType = 0;
		if(i > 0 && i < function.length())
			switch(function.charAt(i)){
			case 105: // 'i'
				transactionType = IGNORE_TRANSACTION;
				break;

			case 110: // 'n'
				transactionType = REQUIRES_NEW_TRANSACTION;
				break;

			case 114: // 'r'
				transactionType = REQUIRES_TRANSACTION;
				break;

			case 104: // 'h'
			default:
				transactionType = INHERIT_TRANSACTION;
				break;
			}
		transactionPolicy = transactionType;
	}
    /**
     * Aggiunge un business process come figlio del ricevente. 
     * Se esiste un altro BusinessProcess figlio con lo stesso nome di quello specificato 
     * viene prima chiuso quello gi  esistente
     */
	public void addChild(BusinessProcess businessprocess) throws BusinessProcessException{
		if(businessprocess.getName() == null)
			return;
		BusinessProcess businessprocess1 = getChild(businessprocess.getName());
		if(businessprocess1 != null)
			businessprocess1.closed();
		synchronized(children){
			children.put(businessprocess.getName(), businessprocess);
			businessprocess.parent = this;
			businessprocess.path = path + "/" + businessprocess.getName();
		}
	}
	 /**  
     * Aggiunge un business process come figlio del ricevente. 
     */
	public void addChild(BusinessProcess businessprocess,boolean remove) throws BusinessProcessException{
		// RP Per limitare il numero dei livelli delle consultazioni viene chiuso un livello intermedio
		// ed il nome del bp   sempre lo stesso, per evitare la chiusura dei figli che hanno lo stesso nome
		if(remove)
			addChild(businessprocess);
		else{
			if(businessprocess.getName() == null)
				return;
			synchronized(children){
				children.put(businessprocess.getName(), businessprocess);
				businessprocess.parent = this;
				businessprocess.path = path + "/" + businessprocess.getName();
			}
		}
	}
    /**
     * Aggiunge un HookForward al ricevente. Gli hook forward sono i primi forward ad essere ricercati.
     */
	protected HookForward addHookForward(String name, Forward forward){
		HookForward hookforward = new HookForward(name, forward);
		hooks.put(name, hookforward);
		return hookforward;
	}

	public synchronized void clearBusy(){
		busy = false;
	}

	public void closeAllChildren(ActionContext context) throws BusinessProcessException{
		synchronized(children){
			for(Iterator iterator = children.values().iterator(); iterator.hasNext(); iterator.remove()){
				BusinessProcess businessprocess = (BusinessProcess)iterator.next();
				businessprocess.closed(context);
			}
		}
	}
    /**
     * Metodo invocato all'atto della chiusura di un business process. 
     * L'implementazione di default invoca lo stesso metodo sui business process figli.
     */
	protected void closed(ActionContext context) throws BusinessProcessException{
		synchronized(children){
			for(Iterator iterator = children.values().iterator(); iterator.hasNext(); ((BusinessProcess)iterator.next()).closed(context));
		}
		if(getUserTransaction() != null)
			try{
				getUserTransaction().remove();
			}catch(Throwable throwable){
				throw new BusinessProcessException(throwable);
			}
		parent = null;
	}	
	
	public void closeAllChildren() throws BusinessProcessException{
		synchronized(children){
			for(Iterator iterator = children.values().iterator(); iterator.hasNext(); iterator.remove()){
				BusinessProcess businessprocess = (BusinessProcess)iterator.next();
				businessprocess.closed();
			}
		}
	}
    /**
     * Metodo invocato all'atto della chiusura di un business process. 
     * L'implementazione di default invoca lo stesso metodo sui business process figli.
     */
	protected void closed() throws BusinessProcessException{
		synchronized(children){
			for(Iterator iterator = children.values().iterator(); iterator.hasNext(); ((BusinessProcess)iterator.next()).closed());
		}
		if(getUserTransaction() != null)
			try{
				getUserTransaction().remove();
			}catch(Throwable throwable){
				throw new BusinessProcessException(throwable);
			}
		parent = null;
	}
    /**
     * Effettua una commit della UserTransaction associata al ricevente. 
     * Se il ricevente non   in modalit  transazionale esce senza effettuare nulla.
     */
	public void commitUserTransaction() throws BusinessProcessException{
		if(getUserTransaction() != null)
			try{
				getUserTransaction().commit();
			}catch(Throwable throwable){
				throw new BusinessProcessException(throwable);
			}
	}
    /**
     * Crea una componente per il ricevente.
     */
	public Object createComponentSession(String jndiName, Class sessionClass) throws BusinessProcessException{
		return createComponentSession(jndiName);
	}
    /**
     * Crea una componente per il ricevente.
     */
	public Object createComponentSession(String jndiName) throws BusinessProcessException{
		try{
			if(getUserTransaction() == null)
				return EJBCommonServices.createEJB(jndiName);
			else
				return EJBCommonServices.createEJB(userTransaction, jndiName);
		}catch(Throwable throwable){
			throw new BusinessProcessException(throwable);
		}
	}
    /**
     * Metodo statico da usare in una JSP per aggiungere il riferimento al business process in cui 
     * dovr  essere eseguita la prossima action. 
     * Va usato all'interno di una FORM che scatena una action.
     */
	public static void encode(BusinessProcess businessprocess, PageContext pagecontext) throws IOException{
		if(businessprocess == null)
			return;
		JspWriter jspwriter = pagecontext.getOut();
		jspwriter.print("<INPUT TYPE=HIDDEN NAME=\"");
		jspwriter.print(it.cnr.jada.action.BusinessProcess.class.getName());
		jspwriter.print("\" VALUE=\"");
		jspwriter.print(businessprocess.getPath());
		HttpSession httpsession = HttpActionContext.getSession((HttpServletRequest)pagecontext.getRequest(), false);
		if(httpsession != null){
			jspwriter.print('$');
			jspwriter.print(httpsession.getId().substring(0,httpsession.getId().indexOf('.')==-1?httpsession.getId().length():httpsession.getId().indexOf('.')));
		}
		jspwriter.println("\">");
	}
    /**
     * Metodo obsoleto. Mantenuto per compatibilit . Ritorna semplicemente la stringa che viene passata
     */
	@Deprecated
	public String encodePath(String path){
		return path;
	}
    /**
     * Costruisce un URL col riferimento ad un BusinessProcess utilizzabile per effettuare 
     * una GET mantenendo il BusinessProcess corrente.
     */
	public static String encodeUrl(HttpServletRequest httpservletrequest, BusinessProcess businessprocess, String url) throws IOException{
		if(businessprocess == null)
			return url;
		StringBuffer stringbuffer = new StringBuffer(url);
		stringbuffer.append('?');
		stringbuffer.append(it.cnr.jada.action.BusinessProcess.class.getName());
		stringbuffer.append('=');
		stringbuffer.append(businessprocess.getPath());
		HttpSession httpsession = httpservletrequest.getSession(false);
		if(httpsession != null){
			stringbuffer.append('$');
			stringbuffer.append(httpsession.getId().substring(0,httpsession.getId().indexOf('.')==-1?httpsession.getId().length():httpsession.getId().indexOf('.')));
		}
		return stringbuffer.toString();
	}
    /**
     * Costruisce un URL col riferimento ad un BusinessProcess utilizzabile per effettuare una 
     * GET mantenendo il BusinessProcess corrente.
     */
	public static String encodeUrl(HttpServletRequest httpservletrequest, String url) throws IOException{
		return encodeUrl(httpservletrequest, getBusinessProcess(httpservletrequest), url);
	}
    /**
     * Ricerca il "default" forward.
     */
	public Forward findDefaultForward(){
		return findForward("default");
	}
    /**
     * Ricerca un forward nell'ambito del ricevente. La ricerca valuta dapprima gli hook, 
     * quindi i forward static definiti nell'ambito del ricevente, quindi la ricerca passa al padre del ricevente.
     */
	public Forward findForward(String s){
		Forward forward = findHookForward(s);
		if(forward != null)
			return forward;
		if(mapping != null)
			forward = mapping.findForward(s);
		if(forward != null)
			return forward;
		if(parent != null)
			forward = parent.findForward(s);
		return forward;
	}

	protected Forward findHookForward(String name){
		return (Forward)hooks.get(name);
	}
    /**
     * Recupera il business process corrente dai parametri di una HttpRequest.
     */
	public static BusinessProcess getBusinessProcess(HttpServletRequest httpservletrequest){
		BusinessProcess businessprocess = (BusinessProcess)httpservletrequest.getAttribute(it.cnr.jada.action.BusinessProcess.class.getName());
		if(businessprocess == null){
			String s = httpservletrequest.getParameter(it.cnr.jada.action.BusinessProcess.class.getName());
			businessprocess = getBusinessProcess(httpservletrequest, s);
			if(s != null)
				httpservletrequest.setAttribute(it.cnr.jada.action.BusinessProcess.class.getName(), businessprocess);
		}
		return businessprocess;
	}
    /**
     * Recupera il business process corrente dai parametri di una HttpRequest.
     */
	public static BusinessProcess getBusinessProcess(HttpServletRequest httpservletrequest, String bpname){
		if(bpname == null)
			return null;
		int i = bpname.indexOf('$');
		int j = bpname.indexOf('.');
		if(i >= 0){
			HttpSession httpsession = HttpActionContext.getSession(httpservletrequest, false);
			if(httpsession == null || !httpsession.getId().substring(0,httpsession.getId().indexOf('.')==-1?httpsession.getId().length():httpsession.getId().indexOf('.')).equals(bpname.substring(i + 1,j==-1?bpname.length():j)))
				throw new NoSuchSessionException();
			bpname = bpname.substring(0, i);
		}
		BusinessProcess businessprocess = getBusinessProcessRoot(httpservletrequest);
		for(StringTokenizer stringtokenizer = new StringTokenizer(bpname, "/"); businessprocess != null && stringtokenizer.hasMoreTokens();)
			if((businessprocess = businessprocess.getChild(stringtokenizer.nextToken())) == null)
				throw new NoSuchBusinessProcessException("BusinessProcess inesistente ["+bpname+"]", bpname);
		return businessprocess;
	}
    /**
     * Recupera il business process root dalla HttpSession associata ad una HttpRequest.
     */
	public static BusinessProcess getBusinessProcessRoot(HttpServletRequest httpservletrequest){
		return (BusinessProcess)HttpActionContext.getSession(httpservletrequest).getAttribute("rootbp");
	}
    /**
     * Restituisce il figlio del ricevente dal nome specificato,
     */
	public BusinessProcess getChild(String bpname){
		if(children == null)
			return null;
		synchronized(children){
			return (BusinessProcess)children.get(bpname);
		}
	}
    /**
     * Restituisce una enumeration dei figli del ricevente.
     */
	public Enumeration getChildren(){
		return Collections.enumeration(children.values());
	}
    /**
     * Restituisce il nome del ricevente.
     */
	public String getName(){
		return name;
	}
	/**
	 * Ritorna il livello del BusinessProcess corrente 
	 */
	public int getBPLevel(){
		int result = 0;
		BusinessProcess bp = this;
		while(bp.getParent() != null){
			result++;
			bp = bp.getParent();
		}
		return result;
	}
    /**
     * Restituisce il padre del ricevente.
     */
	public BusinessProcess getParent(){
		return parent;
	}
    /**
     * Restituisce una stringa formata dal nome del ricevente pi  quello dei suoi padri. 
     * Il nome del processo root   quello pi  a sinistra. Come separatore viene usato il carattere '/'
     */
	public String getPath(){
		return path;
	}
    /**
     * Restituisce una risorsa associata al ricevente. 
     * Ogni istanza di FormBP possiede un dizionario di risorse locali; 
     * ogni risorsa non trovata localmente viene richiesta tramite
     */
	public String getResource(String name){
		if(resources == null)
			return Config.getHandler().getProperty(getClass(), name);
		else
			return resources.getProperty(name);
	}

	public Properties getResources(){
		if(resources == null)
			return Config.getHandler().getProperties(getClass());
		else
			return resources;
	}
    /**
     * Restituisce il business process root.
     */
	public BusinessProcess getRoot(){
		if(parent == null)
			return this;
		else
			return parent.getRoot();
	}

	public int getTransactionPolicy(){
		return transactionPolicy;
	}
    /**
     * Restituisce il valore della propriet  'userTransaction'
     */
	public UserTransaction getUserTransaction(){
		return userTransaction;
	}
    /**
     * Effettua una gestione standard delle eccezioni.
     */
	public BusinessProcessException handleException(Throwable throwable){
		try{
			throw throwable;
		}catch(BusinessProcessException businessprocessexception){
			return businessprocessexception;
		}catch(Throwable throwable1){
			return new BusinessProcessException(throwable1);
		}
	}
    /**
     * Gestisce una UserTransactionTimeoutException. La gestione standard prevede solamente 
     * la chiusura del ricevente (che non   pi  utilizzabile).
     */
	protected void handleUserTransactionTimeout(ActionContext actioncontext) throws BusinessProcessException{
		actioncontext.closeBusinessProcess(this);
	}
    /**
     * Invocato dall ActionServlet in seguito alla istanziazione di un BusinessProcess.
     */
	protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext) throws BusinessProcessException{
	}

	final void initializeUserTransaction(ActionContext actioncontext) throws BusinessProcessException{
		try{
			switch(transactionPolicy){
			default:
				break;

			case REQUIRES_NEW_TRANSACTION:
				userTransaction = EJBCommonServices.createUserTransaction(actioncontext, this);
				break;

			case REQUIRES_TRANSACTION:
				if(actioncontext.getBusinessProcess().getUserTransaction() == null)
					userTransaction = EJBCommonServices.createUserTransaction(actioncontext, this);
				else
					userTransaction = NestedUserTransaction.createNestedUserTransaction(actioncontext.getBusinessProcess().getUserTransaction());
				break;

			case INHERIT_TRANSACTION:
				userTransaction = NestedUserTransaction.createNestedUserTransaction(actioncontext.getBusinessProcess().getUserTransaction());
				break;
			}
			if (userTransaction != null)
				actioncontext.getUserInfo().setUserTransaction(userTransaction);
		}catch(RemoteException remoteexception){
			throw new BusinessProcessException(remoteexception);
		}catch(EJBException ejbexception){
			throw new BusinessProcessException(ejbexception);
		}
	}

	public synchronized boolean isBusy(){
		if(busy)
			return true;
		for(Iterator iterator = children.values().iterator(); iterator.hasNext();){
			BusinessProcess businessprocess = (BusinessProcess)iterator.next();
			if(businessprocess.isBusy())
				return true;
		}
		return false;
	}
    /**
     * Esegue il forward "default" associato al ricevente.
     */
	public void perform(ActionContext actioncontext){
		actioncontext.findDefaultForward().perform(actioncontext);
	}
    /**
     * Rimuove un business process dall'elenco dei figli del ricevente.
     */
	public BusinessProcess removeChild(ActionContext context, String name) throws BusinessProcessException{
		synchronized(children){
			BusinessProcess businessprocess = getChild(name);
			if(businessprocess != null)
				businessprocess.closed(context);
			children.remove(name);
			return businessprocess;
		}
	}	
    /**
     * Rimuove un business process dall'elenco dei figli del ricevente.
     */
	public BusinessProcess removeChild(String name) throws BusinessProcessException{
		return removeChild(null, name);
	}
    /**
     * Rimuove un hook dall'elenco degli hook del ricevente.
     */
	protected void removeHookForward(String name){
		hooks.remove(name);
	}
    /**
     * Effettua una rollback della UserTransaction associata al ricevente. 
     * Se il ricevente non   in modalit  transazionale esce senza effettuare nulla.
     */
	public void rollbackUserTransaction() throws BusinessProcessException{
		if(getUserTransaction() != null)
			try{
				getUserTransaction().rollback();
			}catch(Throwable throwable){
				throw new BusinessProcessException(throwable);
			}
	}
    /**
     * Imposta il business process corrente all'intern di una HttpRequest.
     */
	public static void setBusinessProcess(HttpServletRequest httpservletrequest, BusinessProcess businessprocess){
		httpservletrequest.setAttribute(it.cnr.jada.action.BusinessProcess.class.getName(), businessprocess);
	}
    /**
     * Imposta il business process root all'interno di una HttpRequest.
     */
	public static void setBusinessProcessRoot(HttpServletRequest httpservletrequest, BusinessProcess businessprocess){
		businessprocess.setRoot();
		HttpActionContext.getSession(httpservletrequest).setAttribute("rootbp", businessprocess);
	}

	public synchronized boolean setBusy(){
		if(isBusy())
			return false;
		else
			return busy = true;
	}
    /**
     * Imposta Il BusinessProcessMapping da cui   stato instanziato il ricevente. 
     * Effettua l'inizializzaione del BusinessProcess.
     */
	protected void setMapping(BusinessProcessMapping businessprocessmapping, ActionContext actioncontext) throws BusinessProcessException{
		name = businessprocessmapping.getName();
		mapping = businessprocessmapping;
		init(businessprocessmapping.getConfig(), actioncontext);
	}

	public void setResource(String name, String value){
		if(resources == null)
			resources = new Properties(Config.getHandler().getProperties(getClass()));
		resources.setProperty(name, value);
	}

	private void setRoot(){
		name = "";
		path = "";
	}
	/**
	 * Inserisce nella Tabella APPLICATION_ERROR gli errori procedurali, ed invia l'E-Mail con l'errore
	 * @param context
	 * @param user
	 * @param esercizio
	 * @param cd_unita_organizzativa
	 * @param stack_trace
	 */
    public void insertError(ActionContext context, String user, Integer esercizio, String cd_unita_organizzativa, String stack_trace){
		String text = "Errore interno del Server Esercizio:"+esercizio+" Utente:"+user+" UO:"+cd_unita_organizzativa;
		SendMail.sendErrorMail(text,getPath()+"<BR>"+stack_trace);
		try {   	
			java.sql.Connection conn = null;
			try {
				conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection(context);				
				it.cnr.jada.persistency.sql.HomeCache homeCache = new it.cnr.jada.persistency.sql.HomeCache(conn);
				Application_errorBulk error = new Application_errorBulk();
				Application_errorHome home = (Application_errorHome)homeCache.getHome(error);
				error.setCd_utente(user);
				error.setEsercizio(esercizio);
				error.setCd_unita_organizzativa(cd_unita_organizzativa);
				home.initializePrimaryKeyForInsert(context.getUserContext(),error);
				error.setUser(context.getUserInfo().getUser());				
				error.setToBeCreated();				
				home.insert(error, context.getUserContext());
				java.io.InputStream is=new java.io.ByteArrayInputStream(stack_trace.getBytes());
				byte[] byteArr = new byte[1024];
				oracle.sql.BLOB blob = (oracle.sql.BLOB)homeCache.getHome(error).getSQLBlob(error,"STACK_TRACE");
				java.io.OutputStream os = new java.io.BufferedOutputStream(blob.getBinaryOutputStream());
				int len;						
				while ((len = is.read(byteArr))>0){
					os.write(byteArr,0,len);
				}
				os.close();
				is.close();
				home.update(error, context.getUserContext());
				conn.commit();			
			} catch (Exception e) {
				if (conn != null)
					conn.rollback();
			}finally{
				if (conn != null){
					conn.close(); 
					conn = null;					
				}
			}
		} catch (SQLException e1) {
		}			
    }
    /**
     * 
     * @return
     */
	public BusinessProcessMapping getMapping() {
		return mapping;
	}
	public Character getFunction() {
		return function;
	}
	public void setFunction(Character function) {
		this.function = function;
	}
	public BusinessProcess getParent(int liv)
	{
		BusinessProcess bp=this;
	 	if(liv!=0) 
			for(int i=1;i<liv;i++){
				bp=bp.getParent();   
			} 
		
		return bp;
	}
	
 }