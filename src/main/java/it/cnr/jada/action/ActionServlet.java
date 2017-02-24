package it.cnr.jada.action;

import it.cnr.jada.util.Log;
import it.cnr.jada.util.servlet.MultipartWrapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet per la gestione di action. 
 * ActionServlet smista le richieste in seguito ad una SUBMIT di una form HTML ad una istanza 
 * di Action in base alla configurazione contenuta in un file XML. 
 * Ad ogni richiesta la servlet controlla se il file di configurazione � stato aggiornato e in tal caso lo rilegge.
 * La vita di una action segue il seguente schema: La richiesta HTML viene convertita in base alla mappatura 
 * nella classe che la implementa. Se la action necessita di una HttpSession gi� attiva e se questa condizione 
 * non � verificata viene cercato nell'ambito della action un forward dal nome sessionExpired e viene eseguito. 
 * All'interno dei parametri della richiesta viene cercato quello relativo al nome del business process in cui 
 * deve essere contestualizzata la action e viene passato all'ActionContext. 
 * La action viene istanziata e ogni property a cui corrisponde un parametro della richiesta viene settata 
 * col valore corrispondente. La action viene eseguita. 
 * Se il forward restituito dalla action � nullo viene cercato un forward dal nome default e viene eseguito. 
 * altrimenti viene eseguito il forward restituito dalla action. 
 * Se in una qualsiasi fase si verifica una eccezione non gestita la servlet cerca il forward dal nome 
 * uncaughtException e lo esegue. 
 * Il file di configurazione definisce tre elementi: 
 * 		Action: definisce una action e la classe che la implementa. 
 * 				� possibile impostare uno o pi� parametri di inizializzazione della action che vengono 
 * 				passati all'istanza che implementa la action prima della sua effettiva esecuzione tramite init(). 
 * 				� possibile richiedere che una action debba avere per forza una una session gi� viva. 
 * 		BusinessProcess: definisce un business process e la classe che lo implementa. 
 * 						Un business process viene mantenuto nella session della servlet ed � possibile 
 * 						associare ogni form html con un particolare business process. 
 * 						� possibile impostare uno o pi� parametri di inizializzazione del bp che vengono 
 * 						passati all'istanza che lo implementa tramite init() 
 * 		Forward: definisce il nome di un forward statico e l'url a cui � associato. 
 * 				I forward possono essere definiti a tre livelli: 
 * 				business process: hanno la precedenza su tutti gli altri. 
 * 				action servlet: se non viene trovato agli altri livelli. 
 * Esempio di un file di configurazione:
 * 	nome del forward path=url /> nome della action actionClass=classe che implementa la action 
 * 	needExistingSession=true|false > nome del parametro value=valore /> 
 * 	nome del forward path=url /> 
 * 	nome del bp className=Classe che lo implementa > 
 * 	nome del parametro value=valore /> 
 * 	nome del forward path=url /> 
 * Per un corretto funzionamento la servlet deve essere dichiarata nel file di configurazione 
 * della web application e associata ad una servlet-path del tipo "*.[estensione]". 
 * In tal modo ogni url terminante nell'estesione specificata viene interpretato dalla servlet. 
 * L'estesione di defaul � ".do".
 * Per default la servlet legge la configurazione dal file denominato actions.xml che si trova 
 * nella directory root della web application a cui appartiene la servlet.
 * � possibile tuttavia utilizzare un altro file di configurazione passadone il nome alla servlet come 
 * parametro di inizializzazione dal nome actions
 * See Also:Serialized Form
 */
public class ActionServlet extends HttpServlet implements Serializable{

    private ActionMappings mappings;
    private String actionExtension;
    private File actionDirFile;    
    private File uploadsTempDir;   
	private static final Log log = Log.getInstance(ActionServlet.class);
	
    public ActionServlet(){
    }
    /**
     * Gestisce una richiesta di action sotto forma di GET.
     */
    protected void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws ServletException, IOException {
    	String s = httpservletrequest.getServletPath();
    	if(!s.endsWith(actionExtension))
    		throw new ServletException("Le actions devono terminare con \".do\"");
    	s = s.substring(0, s.length() - actionExtension.length());
    	ActionMapping actionmapping = mappings.findActionMapping(s);
    	if(actionmapping == null)
    		throw new ServletException("Action not found ["+s+"]");
    	String type = httpservletrequest.getHeader("Content-Type");
    	if (!(type == null || !type.startsWith("multipart/form-data"))) {
    		try{
    			httpservletrequest = new MultipartWrapper(httpservletrequest,getServletContext().getRealPath("/tmp/"));
    		}catch(Exception e){
    			log.error(e, "Errore Multipart :");
    		}
    	} 
    	HttpActionContext httpactioncontext = new HttpActionContext(this, httpservletrequest, httpservletresponse);
    	try{
    		traceRequest(httpactioncontext);
    		httpactioncontext.perform(null, actionmapping, null);
    	}catch(ActionPerformingError actionperformingerror)        {
    		httpactioncontext.forwardUncaughtException(actionperformingerror.getDetail());
    	}catch(RuntimeException runtimeexception){
    		httpactioncontext.forwardUncaughtException(runtimeexception);
    	}

    }
    /**
     * Gestisce una richiesta di action sotto forma di POST.
     */
    protected void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws ServletException, IOException{
        doGet(httpservletrequest, httpservletresponse);
    }

    public File getUploadsTempDir(){
        return uploadsTempDir;
    }
    /**
     * Testa se una ServletRequest possiede un parametro
     */
    public static boolean hasRequestParameter(ServletRequest servletrequest, String s){
        Object obj = (Set)servletrequest.getAttribute("parameterNames");
        if(obj == null){
            int i = 0;
            for(Enumeration<?> enumeration = servletrequest.getParameterNames(); enumeration.hasMoreElements();){
                enumeration.nextElement();
                i++;
            }
            obj = new HashSet(i);
            for(Enumeration enumeration1 = servletrequest.getParameterNames(); enumeration1.hasMoreElements(); ((Set) (obj)).add(enumeration1.nextElement()));
            servletrequest.setAttribute("parameterNames", obj);
        }
        return ((Set) (obj)).contains(s);
    }
    /**
     * Inizializza la servlet leggendo la mappatura.
     */
    public void init() throws ServletException{
    	log.info("EAR del 11/12/2004 19:00");
    	System.setProperty("tmp.dir.SIGLAWeb", getServletContext().getRealPath("/"));
        actionExtension = getServletConfig().getInitParameter("extension");
        if(actionExtension == null)
            actionExtension = ".do";   
		actionDirFile = new File(getServletContext().getRealPath("/actions/"));
		uploadsTempDir = new File(getServletContext().getRealPath("/tmp/"));
        uploadsTempDir.mkdirs();
        try{
            mappings = ActionUtil.reloadActions(actionDirFile);
        }catch(ActionMappingsConfigurationException actionmappingsconfigurationexception){
            throw new ServletException("Action mappings configuration exception", actionmappingsconfigurationexception);
        }
    }
    

    void traceRequest(HttpActionContext httpactioncontext){
    	if (httpactioncontext.getUserContext(false) == null)
    		return;
    	HttpServletRequest httpservletrequest = httpactioncontext.getRequest();
        StringBuffer infoUser = new StringBuffer();
        infoUser.append("User:"+httpactioncontext.getUserContext().getUser());
        infoUser.append(" RemoteHost:"+httpservletrequest.getRemoteAddr());
        String comando = null;
    	log.debug("======INIZIO SUBMIT======");
        for(Enumeration<?> enumeration = httpservletrequest.getParameterNames(); enumeration.hasMoreElements();){
            StringBuffer detailInfoUser = new StringBuffer();
        	String campo = (String)enumeration.nextElement();
        	if (campo.equalsIgnoreCase("comando"))
        		comando = httpservletrequest.getParameter(campo);
        	detailInfoUser.append(" campo:"+campo);
        	detailInfoUser.append(" valore:"+httpservletrequest.getParameter(campo));
            log.debug(detailInfoUser.toString());
        }
    	log.debug("======FINE SUBMIT======");
    	log.info(infoUser.toString()+" URL:"+httpservletrequest.getRequestURI()+ " comando:"+comando);
    }
}