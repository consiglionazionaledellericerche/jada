package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.FieldValidationMap;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
/**
 * Un BusinessProcess che permette di simulare una finestra di una GUI.
 */
public class FormBP extends BusinessProcess implements Serializable{

    private String message;
    private Map tabs;
    private int messageStatus;
    public static final int ERROR_MESSAGE = 1;
    public static final int WARNING_MESSAGE = 2;
    public static final int QUESTION_MESSAGE = 3;
    private String defaultAction;
    private Button toolbar[];
    private static Button closeButton;
    private static Button helpButton;
    private static Button maximizeButton;
    private static Button preferitiButton;
    private String actionTarget;
    private final FieldValidationMap fieldValidationMap;

    protected FormBP(){
        message = null;
        tabs = new HashMap();
        fieldValidationMap = new FieldValidationMap();
    }

    protected FormBP(String function){
        super(function);
        message = null;
        tabs = new HashMap();
        fieldValidationMap = new FieldValidationMap();
    }
    /**
     * Cancella lo UserMessage corrente.
     */
    public void clearMessage(){
        message = null;
    }

    public void closeForm(PageContext pagecontext)
        throws IOException, ServletException
    {
        if (!this.getParentRoot().isBootstrap())
        	pagecontext.getOut().println("</table>");
        pagecontext.getOut().println("</form>");
        Throwable throwable = (Throwable)pagecontext.getRequest().getAttribute("it.cnr.jada.action.HttpActionContext.traceException");
        if(throwable != null)
        {
            new StringWriter();
            pagecontext.getOut().println("<!--");
            if(throwable != null)
                throwable.printStackTrace(new PrintWriter(pagecontext.getOut()));
            pagecontext.getOut().println("-->");
        }
    }
    /**
     * Completa il disegno della FORM iniziato da openForm
     */
    public void closeFormWindow(PageContext pagecontext) throws IOException, ServletException{
        pagecontext.getOut().println("<!-- FINE FORM BODY -->");
        if (!this.getParentRoot().isBootstrap())
        	pagecontext.getOut().println("</td></tr>");
        closeForm(pagecontext);
        if (!this.getParentRoot().isBootstrap())
        	pagecontext.getOut().println("<script>initializeWindow('mainWindow')</script>");
    }
    /**
     * Completa il disegno della TOOLBAR iniziato da openToolbar
     */
    public void closeToolbar(JspWriter jspwriter) throws IOException, ServletException{
        jspwriter.println("</td></tr>");
        jspwriter.println("<!-- FINE TOOLBAR -->");
    }

    protected final Button createButton(String name){
        return new Button(getResources(), name);
    }
    /**
     * Crea la toolbar associata al ricevente
     */
    protected Button[] createToolbar(){
        return null;
    }
    /**
     * Restituisce il target HTML da usare come parametro della FORM HTML associata al ricevente
     */
    public String getActionTarget(){
        return actionTarget;
    }
    /**
     * Restituisce lo user message corrente e lo cancella.
     */
    public String getAndClearMessage(){
        String s = message;
        clearMessage();
        return s;
    }
    /**
     * Restituisce di chiusura il bottone da visualizzare nella barra del titolo del ricevente
     */
    protected Button getCloseButton(){
        if(closeButton == null)
            closeButton = new Button(Config.getHandler().getProperties(getClass()), "TitleBar.close");
        return closeButton;
    }
    /**
     * Restituisce l'azione da usare nella FORM del ricevente
     */
    public String getDefaultAction(){
        return defaultAction;
    }

    public final FieldValidationMap getFieldValidationMap(){
        return fieldValidationMap;
    }

    public String getFormTitle(){
        return "<script>document.write(document.title)</script>";
    }
    /**
     * Restituisce di chiusura il bottone da visualizzare nella barra del titolo del ricevente
     */
    protected Button getHelpButton(){
        if(helpButton == null)
            helpButton = new Button(Config.getHandler().getProperties(getClass()), "TitleBar.help");
        return helpButton;
    }
    /**
     * Restituisce il bottone di massimizzazione da visualizzare nella barra del titolo del ricevente
     */
    public Button getMaximizeButton(){
        if(maximizeButton == null)
            maximizeButton = new Button(Config.getHandler().getProperties(getClass()), "TitleBar.maximize");
        return maximizeButton;
    }
    /**
     * Restituisce lo user message corrente
     */
    public String getMessage(){
        return message;
    }
    /**
     * Restituisce lo stato associato allo user message corrente. 
     * Gli stati possibili sono: 
     * WARNING_MESSAGE: messaggio di avvertimento; 
     * QUESTION_MESSAGE: messaggio interrogativo; 
     * ERROR_MESSAGE: messaggio di errore.
     */
    public int getMessageStatus(){
        return messageStatus;
    }

    protected String getStandardAction(){
        return "FormAction";
    }
    /**
     * Restituisce la pagina attiva di un controllo "tabbed"
     */
    public String getTab(String tabName){
        return (String)tabs.get(tabName);
    }
    /**
     * Restituisce la toolbar del ricevente
     */
    public Button[] getToolbar(){
        if(toolbar == null)
            toolbar = createToolbar();
        return toolbar;
    }
    /**
     * Description copied from class:
     * Invocato dall ActionServlet in seguito alla istanziazione di un BusinessProcess.
     */
    protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext) throws BusinessProcessException{
        super.init(config, actioncontext);
        setDefaultAction(config.getInitParameter("defaultAction"));
        setActionTarget(config.getInitParameter("actionTarget"));
    }
    /**
     * Disegna una FORM HTML che contiene le seguenti parti: un input hidden "comando" che contiene il nome 
     * del comando da eseguire sulla action; un input hidden "businessProcess" che contiene il nome 
     * del BusinessProcess contestuale all'azione. una serie di input hidden necessari per conservare 
     * la posizione di scoll sul browser alla risposta. 
     * Per completare il disegno della FORM   necessario invocare closeForm
     */
    public void openForm(PageContext pagecontext) throws IOException, ServletException{
        openForm(pagecontext, getDefaultAction(), getActionTarget(), null);
    }
    /**
     * Disegna una FORM HTML che contiene le seguenti parti: un input hidden "comando" che contiene il nome 
     * del comando da eseguire sulla action; un input hidden "businessProcess" che contiene il nome 
     * del BusinessProcess contestuale all'azione. una serie di input hidden necessari per conservare 
     * la posizione di scoll sul browser alla risposta. 
     * Per completare il disegno della FORM   necessario invocare closeForm
     */
    public void openForm(PageContext pagecontext, String action, String target) throws IOException, ServletException{
        openForm(pagecontext, action, target, null);
    }
    /**
     * Disegna una FORM HTML che contiene le seguenti parti: un input hidden "comando" che contiene il nome 
     * del comando da eseguire sulla action; un input hidden "businessProcess" che contiene il nome 
     * del BusinessProcess contestuale all'azione. una serie di input hidden necessari per conservare 
     * la posizione di scoll sul browser alla risposta. 
     * Per completare il disegno della FORM   necessario invocare closeForm
     */
    public void openForm(PageContext pagecontext, String action, String target, String encType) throws IOException, ServletException{
        pagecontext.getOut().print("<form name=\"");
        pagecontext.getOut().print(getResource("mainFormName"));
        pagecontext.getOut().print("\" action=\"");
        if(action == null)
            pagecontext.getOut().print(getStandardAction());
        else
            pagecontext.getOut().print(action);
        pagecontext.getOut().print(".do\"");
        if(target != null){
            pagecontext.getOut().print(" target=\"");
            pagecontext.getOut().print(target);
            pagecontext.getOut().print('"');
        }
        if(encType != null){
            pagecontext.getOut().print(" enctype=\"");
            pagecontext.getOut().print(encType);
            pagecontext.getOut().print('"');
        }
        if (HttpActionContext.isFromBootstrap(pagecontext)) {
            pagecontext.getOut().println(" method=post>");        	        	
        } else {
            pagecontext.getOut().println(" method=post onSubmit=\"return disableDblClick()\">");        	
        }
        pagecontext.getOut().println("<input type=hidden name=\"comando\">");
        BusinessProcess.encode(this, pagecontext);
        HttpActionContext.encodeActionCounter(pagecontext);
        JSPUtils.scrollSupport(pagecontext);
        if (HttpActionContext.isFromBootstrap(pagecontext)) {
            String s = getAndClearMessage();
            String s1 = null;
            if(s != null) {
                switch(getMessageStatus()){
	                case WARNING_MESSAGE:
	                    s1 = "alert-warning";
	                    break;
	
	                case ERROR_MESSAGE:
	                    s1 = "alert-danger";
	                    break;
	
	                case QUESTION_MESSAGE:
	                    s1 = "alert-info";
	                    break;
                }
                pagecontext.getOut().println("<div class=\"alert-message-bp h4 alert " + s1 + " alert-dismissible fade show\" role=\"alert\">");
                pagecontext.getOut().println("<button type=\"button\" onclick=\"hideAlert(this);\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">");
                pagecontext.getOut().println("<span aria-hidden=\"true\">&times;</span>");
                pagecontext.getOut().println("</button>");
                pagecontext.getOut().println(s);
                pagecontext.getOut().println("</div>");
            }            
        } else {
            writeMessage(pagecontext.getOut());        	
        }
    }
    /**
     * Disegna una FORM e una tabella HTML con la barra del titolo e la toolbar. 
     * Per completare il disegno della tabella   necessario invocare closeFormWindow
     */
    public void openFormWindow(PageContext pagecontext) throws IOException, ServletException{
        openFormWindow(pagecontext, getDefaultAction(), getActionTarget());
    }
    /**
     * Disegna una FORM e una tabella HTML con la barra del titolo e la toolbar. 
     * Per completare il disegno della tabella   necessario invocare closeFormWindow
     */
    public void openFormWindow(PageContext pagecontext, String action, String target) throws IOException, ServletException{
        openForm(pagecontext, action, target);
        if (!this.getParentRoot().isBootstrap()){
        	pagecontext.getOut().println("<table id=\"mainWindow\" class=\"Form\" width=\"100%\" height=\"100%\" cellspacing=\"0\" cellpadding=\"2\">");
        }
        writeTitleBar(pagecontext);
        writeToolbar(pagecontext);
        if (!this.getParentRoot().isBootstrap()){        
	        pagecontext.getOut().println("<!-- FORM BODY -->");
	        pagecontext.getOut().println("<tr height=\"100%\" valign=\"top\"><td>");
        }
    }
    /**
     * Disegna latoolbar HTML del ricevente. Per completare il disegno   necessario invocare closeToolbar
     */
    public void openToolbar(JspWriter jspwriter) throws IOException, ServletException{
        jspwriter.println("<!-- TOOLBAR -->");
        jspwriter.println("<tr><td>");
    }

    protected void resetTabs(ActionContext actioncontext){
    }

    public void setActionTarget(String s){
        actionTarget = s;
    }

    public void setDefaultAction(String s){
        defaultAction = s;
    }

    public void setErrorMessage(String newMessage){
        setMessage(1, newMessage);
    }
    /**
     * Imposta lo user message corrente.
     */
    public void setMessage(int status, String s){
        messageStatus = status;
        if(s == null)
            message = "";
        else
            message = s;
    }
    /**
     * Imposta lo user message corrente con stato WARNING_MESSAGE.
     */
    public void setMessage(String newMessage){
        setMessage(WARNING_MESSAGE, newMessage);
    }
    /**
     * Imposta lo stato dello user message corrente.
     */
    public void setMessageStatus(int i){
        messageStatus = i;
    }
    /**
     * Imposta lo user message corrente con stato QUESTION_MESSAGE.
     */
    public void setQuestionMessage(String newMessage){
        setMessage(QUESTION_MESSAGE, newMessage);
    }
    /**
     * Imposta la pagina corrente di un controllo tabbed
     */
    public void setTab(String tabName, String pageName){
        tabs.put(tabName, pageName);
    }
    /**
     * Disegna la messageBar del ricevente. L'implementazione attuale invoca un javascript:setMessage
     */
    public void writeMessage(JspWriter jspwriter) throws IOException, ServletException{
        String s = getAndClearMessage();
        String s1 = null;
        if(s != null)
            switch(getMessageStatus()){
            case WARNING_MESSAGE:
                s1 = "img/warningmsg.gif";
                break;

            case ERROR_MESSAGE:
                s1 = "img/errormsg.gif";
                break;

            case QUESTION_MESSAGE:
                s1 = "img/questionmsg.gif";
                break;
            }
        jspwriter.print("<script>showMessage(");
        jspwriter.print(getMessageStatus());
        jspwriter.print(',');
        if(s1 != null){
            jspwriter.print('"');
            jspwriter.print(s1);
            jspwriter.print('"');
        }else{
            jspwriter.print("null");
        }
        jspwriter.print(',');
        JSPUtils.printJavascriptString(jspwriter, s);
        jspwriter.println(")</script>");
    }

    protected void writeTabbed(PageContext pagecontext, String name, String pages[][], String page, String align, String width, String height) throws IOException, ServletException{
        JSPUtils.tabbed(pagecontext, name, pages, page, align, width, height, true);
    }

    protected void writeTabbed(PageContext pagecontext, String name, String pages[][], String page, String align, String width, String height, boolean enabled) throws IOException, ServletException{
    	JSPUtils.tabbed(pagecontext, name, pages, page, align, width, height, enabled);
    }

    public void writeTitleBar(JspWriter jspwriter, boolean isBootstrap) throws IOException, ServletException{
        jspwriter.println("<tr><td class=\"FormTitle\">");
        getMaximizeButton().writeWithoutRollover(jspwriter, true, isBootstrap);
        getCloseButton().writeWithoutRollover(jspwriter, true, isBootstrap);
        jspwriter.print("&nbsp;");
        jspwriter.print(getFormTitle());
        jspwriter.println("</td>");
        jspwriter.println("</tr>");
    }

    public void writeTitleBar(PageContext pagecontext) throws IOException, ServletException{
        JspWriter jspwriter = pagecontext.getOut();
        HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(pagecontext.getRequest().getScheme());
        stringbuffer.append("://");
        stringbuffer.append(pagecontext.getRequest().getServerName());
        stringbuffer.append(':');
        stringbuffer.append(pagecontext.getRequest().getServerPort());
        stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
        stringbuffer.append("help");
        stringbuffer.append(httpservletrequest.getServletPath());
        getHelpButton().setHref("javascript:doHelp('" + stringbuffer.toString() + "')");
        jspwriter.println("<!-- TITLEBAR -->");
        if (HttpActionContext.isFromBootstrap(pagecontext)) {
            List<Button> buttons = new ArrayList<Button>();
            buttons.add(getHelpButton());
            if (getParent() != null){
                String isPreferitiButtonHiddenParent = (String)getParent().getMapping().getConfig().getInitParameter("preferitiButtonHidden");
                String isPreferitiButtonHidden = (String)getMapping().getConfig().getInitParameter("preferitiButtonHidden");
                if (isPreferitiButtonHiddenParent != null && isPreferitiButtonHiddenParent.equalsIgnoreCase("false")){
                    if (isPreferitiButtonHidden == null || (isPreferitiButtonHidden != null && isPreferitiButtonHidden.equalsIgnoreCase("false"))){
                        buttons.add(getPreferitiButton());
                    }
                }
            }
            buttons.add(getCloseButton());
            jspwriter.println("<div class=\"title-bar bg-primary text-white\">");
            jspwriter.print("<sigla-page-title class=\"h6\">");
            jspwriter.print(getFormTitle().replace("<script>document.write(document.title)</script>", ""));
            jspwriter.print("</sigla-page-title>");
            jspwriter.println("<div id=\"titleToolbar\" class=\"btn-toolbar pull-right\" role=\"toolbar\" aria-label=\"Toolbar with button groups\">");
                JSPUtils.toolbarBootstrap(jspwriter, buttons, this);
            jspwriter.println("</div>");
            jspwriter.println("</div>");
        } else {
            jspwriter.println("<tr><td>");
            jspwriter.println("<table class=\"Form\" width=\"100%\" cellspacing=\"0\" cellpadding=\"2\">");
            jspwriter.println("<tr><td class=\"FormTitle\">");
            getHelpButton().writeWithoutRollover(jspwriter, true, HttpActionContext.isFromBootstrap(pagecontext));
            getMaximizeButton().writeWithoutRollover(jspwriter, true, HttpActionContext.isFromBootstrap(pagecontext));
            getCloseButton().writeWithoutRollover(jspwriter, true, HttpActionContext.isFromBootstrap(pagecontext));
            jspwriter.print("&nbsp;");
            jspwriter.print(getFormTitle());
            jspwriter.println("</td>");
            if (getParent() != null){
                String isPreferitiButtonHiddenParent = (String)getParent().getMapping().getConfig().getInitParameter("preferitiButtonHidden");
                String isPreferitiButtonHidden = (String)getMapping().getConfig().getInitParameter("preferitiButtonHidden");
                if (isPreferitiButtonHiddenParent != null && isPreferitiButtonHiddenParent.equalsIgnoreCase("false")){
                    if (isPreferitiButtonHidden == null || (isPreferitiButtonHidden != null && isPreferitiButtonHidden.equalsIgnoreCase("false"))){
                        jspwriter.println("<td class=\"FormTitle\" align=\"right\">");
                        getPreferitiButton().writeWithoutRollover(jspwriter, true, HttpActionContext.isFromBootstrap(pagecontext));
                        jspwriter.println("</td>");
                    }
                }
            }
            jspwriter.println("</tr>");
            jspwriter.println("</table>");
            jspwriter.println("</td></tr>");
            jspwriter.println("<!-- FINE TITLEBAR -->");
        }

    }

    public void writeToolbar(JspWriter jspwriter) throws IOException, ServletException{
        Button abutton[] = getToolbar();
        if(abutton != null)
            writeToolbar(jspwriter, abutton);
    }

    protected void writeToolbar(JspWriter jspwriter, Button abutton[]) throws IOException, ServletException{
    	if (this.getParentRoot().isBootstrap()) {
            jspwriter.println("<!-- TOOLBAR BOOTSTRAP -->");
            jspwriter.println("<div id=\"crudToolbar\" class=\"btn-toolbar\" role=\"toolbar\" aria-label=\"Toolbar with button groups\">");
            JSPUtils.toolbarBootstrap(jspwriter,
                    Arrays.stream(abutton).collect(Collectors.toList()), this);
            jspwriter.println("</div>");
            jspwriter.println("<!-- FINE TOOLBAR BOOTSTRAP -->");    		
    	} else {
            openToolbar(jspwriter);
            JSPUtils.toolbar(jspwriter, abutton, this, this.getParentRoot().isBootstrap());
            closeToolbar(jspwriter);    		
    	}
    }
    
    public void writeToolbar(PageContext pagecontext) throws IOException, ServletException{
        writeToolbar(pagecontext.getOut());
    }

	public Button getPreferitiButton() {
        if(preferitiButton == null)
        	preferitiButton = new Button(Config.getHandler().getProperties(getClass()), "TitleBar.preferiti");
        return preferitiButton;
	}
}