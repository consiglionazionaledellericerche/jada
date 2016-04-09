package it.cnr.jada.action;

import it.cnr.jada.action.ActionMapping;
import it.cnr.jada.action.ActionMappings;
import it.cnr.jada.action.ActionMappingsConfigurationException;
import it.cnr.jada.action.ActionPerformingError;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.ejb.AdminSession;
import it.cnr.jada.firma.FirmaInfos;
import it.cnr.jada.firma.bp.CRUDFirmaBP;
import it.cnr.jada.util.*;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.JSPUtils;
import it.cnr.jada.util.servlet.*;

import java.io.*;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;

import javax.ejb.EJBException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 * Servlet per la gestione di action. 
 * ActionServlet smista le richieste in seguito ad una SUBMIT di una form HTML ad una istanza 
 * di Action in base alla configurazione contenuta in un file XML. 
 * Ad ogni richiesta la servlet controlla se il file di configurazione è stato aggiornato e in tal caso lo rilegge.
 * La vita di una action segue il seguente schema: La richiesta HTML viene convertita in base alla mappatura 
 * nella classe che la implementa. Se la action necessita di una HttpSession già attiva e se questa condizione 
 * non è verificata viene cercato nell'ambito della action un forward dal nome sessionExpired e viene eseguito. 
 * All'interno dei parametri della richiesta viene cercato quello relativo al nome del business process in cui 
 * deve essere contestualizzata la action e viene passato all'ActionContext. 
 * La action viene istanziata e ogni property a cui corrisponde un parametro della richiesta viene settata 
 * col valore corrispondente. La action viene eseguita. 
 * Se il forward restituito dalla action è nullo viene cercato un forward dal nome default e viene eseguito. 
 * altrimenti viene eseguito il forward restituito dalla action. 
 * Se in una qualsiasi fase si verifica una eccezione non gestita la servlet cerca il forward dal nome 
 * uncaughtException e lo esegue. 
 * Il file di configurazione definisce tre elementi: 
 * 		Action: definisce una action e la classe che la implementa. 
 * 				È possibile impostare uno o più parametri di inizializzazione della action che vengono 
 * 				passati all'istanza che implementa la action prima della sua effettiva esecuzione tramite init(). 
 * 				È possibile richiedere che una action debba avere per forza una una session già viva. 
 * 		BusinessProcess: definisce un business process e la classe che lo implementa. 
 * 						Un business process viene mantenuto nella session della servlet ed è possibile 
 * 						associare ogni form html con un particolare business process. 
 * 						È possibile impostare uno o più parametri di inizializzazione del bp che vengono 
 * 						passati all'istanza che lo implementa tramite init() 
 * 		Forward: definisce il nome di un forward statico e l'url a cui è associato. 
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
 * L'estesione di defaul è ".do".
 * Per default la servlet legge la configurazione dal file denominato actions.xml che si trova 
 * nella directory root della web application a cui appartiene la servlet.
 * È possibile tuttavia utilizzare un altro file di configurazione passadone il nome alla servlet come 
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

	    String signedData;
	    String ellipsApplet = httpservletrequest.getParameter("EllipsApplet");
	    String signFilename = httpservletrequest.getParameter("sign_nome_file");
	    String signError = httpservletrequest.getParameter("Error");
	    String sessionId=null;
	    if (httpservletrequest.getSession()!=null)
	    	sessionId=httpservletrequest.getSession().getId();
        String pathFirmaTempDir = getServletContext().getRealPath("/tmp/"+httpservletrequest.getSession().getId());
	    if (ellipsApplet!=null) {
		    // Check that we have a file upload request
		    boolean isMultipart = ServletFileUpload.isMultipartContent(httpservletrequest);
		    
	    	if (type != null && type.startsWith("application/x-www-form-urlencoded")) {
	    		if (isMultipart) {
			    	try{
			    		/*
			    		 * per ora questa parte non è gestita dato che il file 
			    		 * firmato non viene trasferito come multipart
			    		 */
			    		// Create a new file upload handler
				    	ServletFileUpload upload = new ServletFileUpload();
				        
				    	// Parse the request
				    	FileItemIterator iter = upload.getItemIterator(httpservletrequest);
				    	while (iter.hasNext()) {
				    	    FileItemStream item = iter.next();
				    	    String name = item.getFieldName();
				    	    InputStream stream = item.openStream();
				    	    if (item.isFormField()) {
				    	    	log.info("Form field " + name + " with value "
				    	            + Streams.asString(stream) + " detected.");
				    	    } else {
				    	    	log.info("File field " + name + " with file name "
				    	            + item.getName() + " detected.");
				    	        // Process the input stream
				    	    }
				    	}
			    	}catch(Exception e){
			    		log.error(e, "Errore Form urlencoded :");
					}
			    }
		    	else {
			        signedData = httpservletrequest.getParameter("Signature");
			        if (signedData!=null) {
			        	//Utility.saveFile(signFilename+".p7m", pathFirmaTempDir, signedData);
			        	Utility.decodeAndSaveFile(signFilename+".p7m", pathFirmaTempDir, signedData);
			        }

		    	}
	    		
	    		PrintWriter out = httpservletresponse.getWriter();
	    		String path = httpservletrequest.getServletPath();
	    		String url = JSPUtils.buildAbsoluteUrl(httpservletrequest, null, path);
			    String signBPPath = httpservletrequest.getParameter("sign_bp_path");
	    		String params=null;
	    		if (signError==null) {
		    		params = "sign_file_ricevuto="+signFilename+".p7m"+"&sign_bp_path="+signBPPath;
	    		}
	    		else {
		    		params = "sign_file_errore=Y"+"&sign_bp_path="+signBPPath;
	    		}
	    		out.println("Location: "+url+"?"+params);
			}
	    }
	    else {

		    String signFileRicevuto = httpservletrequest.getParameter("sign_file_ricevuto");
		    String signFileErrore = httpservletrequest.getParameter("sign_file_errore");
		    String signBPPath = httpservletrequest.getParameter("sign_bp_path");
	    	if (signFileErrore!=null) {

	    		HttpActionContext httpactioncontext = new HttpActionContext(this, httpservletrequest, httpservletresponse);

		    	BusinessProcess bp = httpactioncontext.getBusinessProcess(signBPPath);
		    	BusinessProcess.setBusinessProcess(httpservletrequest, bp);
	        	httpactioncontext.setBusinessProcess(bp);
	        	httpactioncontext.setActionMapping(actionmapping);

	        	try{
		            traceRequest(httpactioncontext);
		            httpactioncontext.perform(null, actionmapping, null);
		        }catch(ActionPerformingError actionperformingerror)        {
		            httpactioncontext.forwardUncaughtException(actionperformingerror.getDetail());
		        }catch(RuntimeException runtimeexception){
		            httpactioncontext.forwardUncaughtException(runtimeexception);
		        }
	    	}
	    	else if (signFileRicevuto!=null) {

		    	HttpActionContext httpactioncontext = new HttpActionContext(this, httpservletrequest, httpservletresponse);

		    	BusinessProcess bp = httpactioncontext.getBusinessProcess(signBPPath);
		    	BusinessProcess.setBusinessProcess(httpservletrequest, bp);
	        	httpactioncontext.setBusinessProcess(bp);
	        	httpactioncontext.setActionMapping(actionmapping);
	        	String fileNameCompleto = pathFirmaTempDir+"/"+signFileRicevuto;
	        	
		    	CRUDFirmaBP firmabp=null;
		    	boolean bTipoPersistenzaEsterna=false;
		    	if (bp instanceof FirmaInfos) {
		    		if (((FirmaInfos)bp).tipoPersistenza().equals(FirmaInfos.TIPO_PERSISTENZA_ESTERNA))
		    			bTipoPersistenzaEsterna=true;
		    	}
		        try {
		        	if (bTipoPersistenzaEsterna) {
		                traceRequest(httpactioncontext);
			            httpactioncontext.perform(null, actionmapping, "doPersist");
		        	} else {
		                firmabp = (CRUDFirmaBP)
	                	httpactioncontext.createBusinessProcess
	                	("CRUDFirmaBP", new Object[] {"M", bp, fileNameCompleto});
		                httpactioncontext.addBusinessProcess(firmabp);

		                traceRequest(httpactioncontext);
			            httpactioncontext.perform(null, actionmapping, null);
		        	}

		        }catch(ActionPerformingError actionperformingerror)        {
		            httpactioncontext.forwardUncaughtException(actionperformingerror.getDetail());
		        }catch(RuntimeException runtimeexception){
		            httpactioncontext.forwardUncaughtException(runtimeexception);
		        } catch(Throwable throwable) {
		            httpactioncontext.forwardUncaughtException(throwable);
		        }
	    	}
		    else {
		    	
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
            for(Enumeration enumeration = servletrequest.getParameterNames(); enumeration.hasMoreElements();){
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
			if (System.getProperty("it.cnr.readBulkInfos","Y").equalsIgnoreCase("Y"))
				loadPersistentInfos();
        }catch(ActionMappingsConfigurationException actionmappingsconfigurationexception){
            throw new ServletException("Action mappings configuration exception", actionmappingsconfigurationexception);
        }
    }
    
	public synchronized void loadPersistentInfos() throws ServletException{    
		new ReadClass(getServletContext().getRealPath("/bulkinfos/BulkClassList.xml"));		
	}

    void traceRequest(HttpActionContext httpactioncontext){
    	if (httpactioncontext.getUserContext() == null)
    		return;
    	HttpServletRequest httpservletrequest = httpactioncontext.getRequest();
        StringBuffer infoUser = new StringBuffer();
        infoUser.append("User:"+httpactioncontext.getUserContext().getUser());
        infoUser.append(" RemoteHost:"+httpservletrequest.getRemoteAddr());
        String comando = null;
    	log.debug("======INIZIO SUBMIT======");
        for(Enumeration enumeration = httpservletrequest.getParameterNames(); enumeration.hasMoreElements();){
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