package it.cnr.jada.action;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.UserInfo;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.IntrospectionError;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.XMLObjectFiller;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.JSPUtils;
import it.cnr.jada.util.servlet.MultipartWrapper;
import it.cnr.jada.util.upload.UploadedFile;

import java.beans.IntrospectionException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HttpActionContext
	implements Serializable, ActionContext
{

    public static final String CONTEXT_FOCUSED_ELEMENT = "it.cnr.jada.action.HttpActionContext.focusedElement";
    public static final String USER_CONTEXT = "UserContext";
    public static final String CURRENT_USER = "CurrentUser";
    private File actionDirFile;
	private HttpServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ActionMapping actionMapping;
	private String command;
	private BusinessProcess businessProcess;
	private Forward caller;
	private static final String requestTracingUserAttributeName = "it.cnr.jada.action.HttpActionContext.requestTracingUsers";
	private static final String tracingSessionDescriptionAttributeName = "it.cnr.jada.action.HttpActionContext.tracingSessionDescription";
	
	public void setActionMapping(ActionMapping actionMapping) {
		this.actionMapping = actionMapping;
	}

	public HttpActionContext(HttpServlet actionservlet, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
		throws ServletException
	{
		request = httpservletrequest;
		response = httpservletresponse;
		servlet = actionservlet;
		actionDirFile = new File(servlet.getServletContext().getRealPath("/actions/"));
	}

	public BusinessProcess addBusinessProcess(BusinessProcess businessprocess)
		throws BusinessProcessException
	{
		try {
			Integer liv = 1;
			int result=0;
			if(getBusinessProcess() instanceof ConsultazioniBP){
				BusinessProcess bp2=getBusinessProcess();
				BusinessProcess bp = getBusinessProcess();
				while(bp.getParent() != null && bp.getParent() instanceof ConsultazioniBP && result<liv){
					result++;
					bp = bp.getParent();
				}
				// chiudo il bp n livelli sopra senza cancellare i figli
				if(result==liv){
					closeBusinessProcess(bp,false);
				// aggiungo gli ultimi n-1 livelli, richiamando un nuovo addChild con parametro
					for(int i=liv;i>1;i--){
						getBusinessProcess().addChild(bp2.getParent(i),false);
						setBusinessProcess(bp2.getParent(i));
					}
					getBusinessProcess().addChild(bp2,false);
					setBusinessProcess(bp2);
				}
			}
		} catch (Exception e) {
			throw new BusinessProcessException(e);
		}
		getBusinessProcess().addChild(businessprocess, this);
		setBusinessProcess(businessprocess);
		return businessprocess;
	}

	public HookForward addHookForward(BusinessProcess businessprocess, String s, Action action)
	{
		return businessprocess.addHookForward(s, new ActionForward(action, actionMapping));
	}

	public HookForward addHookForward(BusinessProcess businessprocess, String s, Action action, String s1)
	{
		return businessprocess.addHookForward(s, new ActionForward(action, actionMapping, s1));
	}

	public HookForward addHookForward(String s, Action action)
	{
		return addHookForward(getBusinessProcess(), s, action);
	}

	public HookForward addHookForward(String s, Action action, String s1)
	{
		return addHookForward(getBusinessProcess(), s, action, s1);
	}

	public HookForward addHookForward(String s, Forward forward)
	{
		return getBusinessProcess().addHookForward(s, forward);
	}

	public void addRequestTracingUser(String s)
	{
		if(s == null)
			return;
		Object obj = (Set)servlet.getServletContext().getAttribute("it.cnr.jada.action.HttpActionContext.requestTracingUsers");
		if(obj == null)
			servlet.getServletContext().setAttribute("it.cnr.jada.action.HttpActionContext.requestTracingUsers", obj = new TreeSet());
		((Set) (obj)).add(s);
	}

	public boolean checkActionCounter()
	{
		String s = request.getParameter("actionCounter");
		if(s == null)
			return true;
		int i = s.indexOf('-');
		String s1 = "it.cnr.jada.action.HttpActionContext.actionCounter";
		if(i >= 0)
			s1 = s1 + '.' + s.substring(0, i);
		Long long1 = (Long)getSessionValue(s1);
		if(long1 == null)
			return false;
		long l = long1.longValue();
		if(Long.parseLong(s.substring(i + 1)) != l)
		{
			return false;
		} else
		{
			getSession().setAttribute(s1, new Long(++l));
			return true;
		}
	}

	public BusinessProcess closeBusinessProcess()
		throws BusinessProcessException
	{
		return closeBusinessProcess(getBusinessProcess(false));
	}

	public BusinessProcess closeBusinessProcess(BusinessProcess businessprocess)
		throws BusinessProcessException
	{
		if(businessprocess == null || businessprocess.getParent() == null)
		{
			return null;
		} else
		{
			setBusinessProcess(businessprocess.getParent());
			businessprocess.getParent().removeChild(this, businessprocess.getName());
			return businessprocess;
		}
	}
	public BusinessProcess closeBusinessProcess(BusinessProcess businessprocess,Boolean flag)
		throws BusinessProcessException
	{
		// RP Per limitare il numero dei livelli delle consultazioni viene chiuso un livello intermedio
		// e non bisogna chiudere i figli cosa che fa il closeBusinessProcess(businessprocess)
		if(flag)
			return closeBusinessProcess(businessprocess);
		else
		{
			if(businessprocess == null || businessprocess.getParent() == null){
				return null;
			} 
			else{
				setBusinessProcess(businessprocess.getParent());
				// non pu essere richiamato il closed perch chiude anche i figli
		        if (businessprocess instanceof SelezionatoreListaBP)
					try
					{
						EJBCommonServices.closeRemoteIterator(this,((SelezionatoreListaBP)businessprocess).getIterator());
					}
					catch(RemoteException remoteexception)
					{			
						throw new BusinessProcessException();
					}
				return businessprocess;
			}	
		}
	}
	public BusinessProcess createBusinessProcess(String s)
		throws BusinessProcessException
	{
		if (actionMapping == null) {
			try {
				actionMapping = ActionUtil.reloadActions(actionDirFile).findActionMapping("/Login");
			} catch (ActionMappingsConfigurationException e) {				
			}
		}
		return actionMapping.createBusinessProcess(s, this);
	}

	public BusinessProcess createBusinessProcess(String s, Object aobj[])
		throws BusinessProcessException
	{
		return actionMapping.createBusinessProcess(s, this, aobj);
	}

	public static void encodeActionCounter(PageContext pagecontext)
		throws IOException
	{
		pagecontext.getOut().print("<INPUT TYPE=HIDDEN NAME=\"actionCounter\" VALUE=\"");
		HttpServletRequest httpservletrequest = (HttpServletRequest)pagecontext.getRequest();
		HttpSession httpsession = getSession(httpservletrequest);
		if (httpsession != null) {
			String s = httpservletrequest.getParameter("actionCounter");
			if(s == null) {
				Long long1 = (Long)httpsession.getAttribute("it.cnr.jada.action.HttpActionContext.actionCounter.frame");
				if(long1 == null)
					long1 = new Long(0L);
				else
					long1 = new Long(long1.longValue() + 1L);
				httpsession.setAttribute("it.cnr.jada.action.HttpActionContext.actionCounter.frame", long1);
				httpsession.setAttribute("it.cnr.jada.action.HttpActionContext.actionCounter." + long1, new Long(0L));
				pagecontext.getOut().print(long1.toString() + "-0");
			} else {
				int i = s.indexOf('-');
				String s1 = "it.cnr.jada.action.HttpActionContext.actionCounter";
				if(i >= 0)
					s1 = s1 + '.' + s.substring(0, i);
				Long long2 = (Long)httpsession.getAttribute(s1);
				long l = -1L;
				if(long2 != null)
					l = long2.longValue();
				if(i >= 0)
					s = s.substring(0, i + 1) + l;
				else
					s = String.valueOf(l);
				pagecontext.getOut().print(s);
			}			
		}
		pagecontext.getOut().println("\">");
	}

	public boolean fill(Object obj)
		throws ParseException
	{
		return fill(obj, null);
	}

	public boolean fill(Object obj, String s)
		throws ParseException
	{
		if(obj == null)
			return false;
		try
		{
			String as[] = Introspector.getReadWriteProperties(obj.getClass());
			for(int i = 0; i < as.length; i++)
			{
				String s1 = as[i];
				Class class1 = Introspector.getPropertyType(obj.getClass(), s1);
				if(class1.isArray())
				{
					class1 = class1.getComponentType();
					String as1[] = request.getParameterValues(FieldProperty.mergePrefix(s, as[i]));
					if(as1 != null)
					{
						String as2[];
						if(class1 == java.lang.String.class)
						{
							as2 = as1;
						} else
						{
							as2 = ((String []) (Array.newInstance(class1, as1.length)));
							for(int j = 0; j < as1.length; j++)
								Array.set(as2, j, Introspector.standardParse(as1[j], class1));

						}
						Introspector.setPropertyValue(obj, s1, as2);
					}
				} else
				{
					String s2 = FieldProperty.mergePrefix(s, as[i]);
					if(it.cnr.jada.util.Config.hasRequestParameter(request, s2))
					{
						String s3 = request.getParameter(s2);
						if(s3.length() == 0)
							s3 = null;
						Introspector.setPropertyValue(obj, as[i], s3);
					}
				}
			}

		}
		catch(InvocationTargetException invocationtargetexception)
		{
			throw new IntrospectionError(invocationtargetexception);
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
		return true;
	}

	public boolean fillProperty(Object obj, String s, String s1)
		throws ParseException
	{
		if(obj == null)
			return false;
		try
		{
			Class class1 = Introspector.getPropertyType(obj.getClass(), s);
			if(class1.isArray())
			{
				class1 = class1.getComponentType();
				String as[] = request.getParameterValues(FieldProperty.mergePrefix(s1, s));
				if(as == null)
					return false;
				String as1[];
				if(class1 == java.lang.String.class)
				{
					as1 = as;
				} else
				{
					as1 = ((String []) (Array.newInstance(class1, as.length)));
					for(int i = 0; i < as.length; i++)
						Array.set(as1, i, Introspector.standardParse(as[i], class1));

				}
				Introspector.setPropertyValue(obj, s, as1);
			} else
			{
				String s2 = FieldProperty.mergePrefix(s1, s);
				if(!it.cnr.jada.util.Config.hasRequestParameter(request, s2))
					return false;
				String s3 = request.getParameter(s2);
				if(s3.length() == 0)
					s3 = null;
				Introspector.setPropertyValue(obj, s, s3);
			}
		}
		catch(InvocationTargetException invocationtargetexception)
		{
			throw new IntrospectionError(invocationtargetexception);
		}
		catch(IntrospectionException introspectionexception)
		{
			throw new IntrospectionError(introspectionexception);
		}
		return true;
	}

	public Forward findActionForward(String s)
	{
		return actionMapping.findActionForward(s);
	}

	public Forward findDefaultForward()
	{
		Forward forward = null;
		if(businessProcess != null)
			forward = businessProcess.findDefaultForward();
		if(forward != null)
			return forward;
		else
			return actionMapping.findForward("default");
	}

	public Forward findForward(String s)
	{
		Forward forward = null;
		if(businessProcess != null)
			forward = businessProcess.findForward(s);
		if(forward != null)
			return forward;
		else
			return actionMapping.findForward(s);
	}

	public void forwardUncaughtException(Throwable throwable)
		throws ServletException
	{
		try
		{
			request.setAttribute("uncaughtException", throwable);
			findForward("uncaughtException").perform(this);
		}
		catch(RuntimeException _ex)
		{
			throw new ServletException(throwable);
		}
	}

	private Action getActionInstance(ActionMapping actionmapping, ServletRequest servletrequest)
	{
		try
		{
			Action action = actionmapping.getActionInstance();
			fill(action);
			return action;
		}
		catch(InstantiationException instantiationexception)
		{
			throw new ActionPerformingError(instantiationexception);
		}
		catch(ParseException parseexception)
		{
			throw new ActionPerformingError(parseexception);
		}
	}

	public String getApplicationId()
	{
		return JSPUtils.getApplicationId(request);
	}

	public BusinessProcess getBusinessProcess()
	{
		if(businessProcess == null)
			setBusinessProcess(getBusinessProcessRoot(true));
		return businessProcess;
	}

	public BusinessProcess getBusinessProcess(String s)
	{
		return BusinessProcess.getBusinessProcess(request, s);
	}

	private BusinessProcess getBusinessProcess(boolean flag)
	{
		if(businessProcess == null && flag)
			setBusinessProcess(getBusinessProcessRoot(flag));
		return businessProcess;
	}

	public BusinessProcess getBusinessProcessRoot(boolean flag)
	{
		BusinessProcess businessprocess = BusinessProcess.getBusinessProcessRoot(request);
		if(businessprocess != null)
		{
			if(businessProcess == null)
				setBusinessProcess(businessprocess);
		} else
		if(flag)
			try
			{
				BusinessProcess.setBusinessProcessRoot(request, businessprocess = createBusinessProcess("root"));
				setBusinessProcess(businessprocess);
			}
			catch(BusinessProcessException _ex) { }
		return businessprocess;
	}

	public Forward getCaller()
	{
		return caller;
	}

	public String getCurrentCommand()
	{
		if(command == null)
		{
			command = request.getParameter("comando");
			if(command == null || command.trim().length() == 0)
			{
				for(Enumeration enumeration = request.getParameterNames(); enumeration.hasMoreElements();)
				{
					String s = enumeration.nextElement().toString();
					if(s.startsWith("comando."))
						return command = s.substring("comando.".length());
				}

				command = "doDefault";
			}
		}
		return command;
	}

	public UploadedFile getMultipartParameter(String s)
	{
		if(request instanceof MultipartWrapper)
			return ((MultipartWrapper)request).getFile(s);
		else
			return null;
	}

	public String getParameter(String s)
	{
		return request.getParameter(s);
	}

	public InputStream getParameterInputStream(String s)
	{
		UploadedFile multipartparameter = getMultipartParameter(s);
		if(multipartparameter != null)
			try
			{
				return new FileInputStream(multipartparameter.getFile());
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		String s1 = getParameter(s);
		if(s1 != null)
			return new ByteArrayInputStream(s1.getBytes());
		else
			return null;
	}

	public Reader getParameterReader(String s)
	{
		File multipartparameter = getMultipartParameter(s).getFile();
		if(multipartparameter != null)
			try
			{
				return new InputStreamReader(new FileInputStream(multipartparameter));
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		String s1 = getParameter(s);
		if(s1 != null)
			return new StringReader(s1);
		else
			return null;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public Enumeration getRequestTracingUsers()
	{
		Set set = (Set)servlet.getServletContext().getAttribute("it.cnr.jada.action.HttpActionContext.requestTracingUsers");
		if(set == null)
			return null;
		else
			return Collections.enumeration(set);
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	public HttpServlet getServlet()
	{
		return servlet;
	}

	public HttpSession getSession()
	{
		return getSession(true);
	}

	public static HttpSession getSession(HttpServletRequest httpservletrequest)
	{
		return getSession(httpservletrequest, false);
	}

	public static HttpSession getSession(HttpServletRequest httpservletrequest, boolean flag)
	{
		HttpSession httpsession = httpservletrequest.getSession(false);
		if(httpsession == null && flag)
		{
			httpsession = httpservletrequest.getSession(true);
		}
		return httpsession;
	}

	public HttpSession getSession(boolean flag)
	{
		return getSession(request, flag);
	}

	public String getSessionId()
	{
		if(getSession(false) == null)
			return null;
		else
			return getSession().getId();
	}

	private Object getSessionValue(String s)
	{
		return getSession().getAttribute(s);
	}

	public String getTracingSessionDescription()
	{
		return (String)getSession().getAttribute("it.cnr.jada.action.HttpActionContext.tracingSessionDescription");
	}

	public UserContext getUserContext(boolean createSession)
	{
		return getUserContext(getSession(false));
	}
	
	public UserContext getUserContext()
	{
	    return Optional.ofNullable(getSession(false))
                .map(session -> getUserContext(session))
                .orElseGet(() ->
                    Optional.ofNullable(getRequest().getAttribute(USER_CONTEXT))
                        .filter(UserContext.class::isInstance)
                        .map(UserContext.class::cast)
                        .orElseGet(() -> getUserContext(getSession()))
                );
	}

	public static UserContext getUserContext(HttpSession httpsession)
	{
		return Optional.ofNullable(httpsession).map(session -> (UserContext)session.getAttribute(USER_CONTEXT)).orElse(null);
	}

	public UserInfo getUserInfo()
	{
        return Optional.ofNullable(getSession(false))
                .map(session -> getUserInfo(session))
                .orElseGet(() ->
                        Optional.ofNullable(getRequest().getAttribute(CURRENT_USER))
                                .filter(UserInfo.class::isInstance)
                                .map(UserInfo.class::cast)
                                .orElseGet(() -> getUserInfo(getSession()))
                );
	}

    public static UserInfo getUserInfo(HttpSession httpSession)
    {
        return (UserInfo)httpSession.getAttribute(CURRENT_USER);
    }

	public static UserInfo getUserInfo(HttpServletRequest httpservletrequest)
	{
		return (UserInfo)getSession(httpservletrequest).getAttribute(CURRENT_USER);
	}

	public void initialize()
		throws NoSuchBusinessProcessException
	{
		businessProcess = BusinessProcess.getBusinessProcess(request);
	}

	public void invalidateSession()
	{
		HttpSession httpsession = getSession(false);
		if(httpsession != null)
			httpsession.invalidate();
	}

	public boolean isRequestTracingUser()
	{
		Set set = (Set)servlet.getServletContext().getAttribute("it.cnr.jada.action.HttpActionContext.requestTracingUsers");
		if(set == null)
			return false;
		UserInfo userinfo = getUserInfo();
		if(userinfo == null)
			return false;
		else
			return set.contains(userinfo.getUserid());
	}

	public boolean isRequestTracingUser(String s)
	{
		Set set = (Set)servlet.getServletContext().getAttribute("it.cnr.jada.action.HttpActionContext.requestTracingUsers");
		if(set == null)
			return false;
		else
			return set.contains(s);
	}

	public void perform(Action action, ActionMapping actionmapping, String s)
	{
		if(s != null)
			command = s;
		if(actionmapping != null)
			actionMapping = actionmapping;
		Forward forward = null;
		if(actionmapping.needExistingSession())
		{
			if(businessProcess == null)
				try
				{
					businessProcess = BusinessProcess.getBusinessProcess(request);
				}
				catch(NoSuchBusinessProcessException _ex)
				{
					forward = actionmapping.findForward("pageExpired");
				}
				catch(NoSuchSessionException _ex)
				{
					forward = actionmapping.findForward("sessionExpired");
				}
			if(getBusinessProcessRoot(false) == null)
				forward = actionmapping.findForward("sessionExpired");
			else
			if(caller == null && !checkActionCounter())
				forward = actionmapping.findForward("pageExpired");
		} else
		{
			try
			{
				businessProcess = BusinessProcess.getBusinessProcess(request);
			}
			catch(NoSuchBusinessProcessException _ex) { }
			catch(NoSuchSessionException _ex) { }
		}
		BusinessProcess businessprocess = businessProcess;
		if(forward == null)
		{
			if(action == null)
				action = getActionInstance(actionmapping, request);
			boolean flag = businessprocess == null || action.isThreadsafe(this);
			if(!flag && !businessprocess.setBusy())
				forward = findForward("businessProcessBusy");
			else
				try{
                    saveFocusedElement();
					forward = action.perform(this);
				}finally{
					if(!flag)
						businessprocess.clearBusy();
				}
		}
		if(forward == null)
			forward = findForward("default");
		caller = forward;
		if(forward != null)
			forward.perform(this);
	}

	public void removeHookForward(String s)
	{
		getBusinessProcess().removeHookForward(s);
	}

	public void removeRequestTracingUser(String s)
	{
		if(s == null)
			return;
		Set set = (Set)servlet.getServletContext().getAttribute("it.cnr.jada.action.HttpActionContext.requestTracingUsers");
		if(set == null)
			return;
		set.remove(s);
		if(set.isEmpty())
			servlet.getServletContext().removeAttribute("it.cnr.jada.action.HttpActionContext.requestTracingUsers");
	}
	public synchronized void reloadActions()
		 throws ActionMappingsConfigurationException
	 {
		 ActionMappings actionmappings = new ActionMappings();
		 File afile[] = actionDirFile.listFiles();
		 try
		 {
			 XMLObjectFiller xmlobjectfiller = new XMLObjectFiller(actionmappings);
			 xmlobjectfiller.mapElementToClass("action-mappings", it.cnr.jada.action.ActionMappings.class);
			 xmlobjectfiller.mapElement("action", it.cnr.jada.action.ActionMapping.class, "addActionMapping");
			 xmlobjectfiller.mapElement("businessProcess", it.cnr.jada.action.BusinessProcessMapping.class, "addBusinessProcessMapping");
			 xmlobjectfiller.mapElement("forward", it.cnr.jada.action.StaticForward.class, "addForward");
			 xmlobjectfiller.mapElement("init-param", it.cnr.jada.action.InitParameter.class, "addInitParameter");
			 for(int i = 0; i < afile.length; i++)
				 if(afile[i].isFile() && afile[i].getName().endsWith(".xml") && afile[i].canRead()){                
					 //xmlobjectfiller.parse(new InputSource(new FileInputStream(afile[i])));                  
					 String encoding = System.getProperty("SIGLA_ENCODING","UTF-8");
					 xmlobjectfiller.parse(new InputSource(new InputStreamReader(new FileInputStream(afile[i]),encoding)));
				 }
		 }
		 catch(SAXException saxexception)
		 {
			 throw new ActionMappingsConfigurationException(saxexception);
		 }
		 catch(ParserConfigurationException parserConfigurationException)
		 {
			 throw new ActionMappingsConfigurationException(parserConfigurationException);
		 }
        
		 catch(IOException ioexception)
		 {
			 throw new ActionMappingsConfigurationException(ioexception);
		 }
		 //mappings = actionmappings;
	 }
	public void resetActionMappings()
		throws BusinessProcessException
	{
		try
		{
			reloadActions();
		}
		catch(ActionMappingsConfigurationException actionmappingsconfigurationexception)
		{
			throw new BusinessProcessException(actionmappingsconfigurationexception);
		}
	}

	private void setActionProperty(Action action, String s, String s1)
		throws ParseException, IntrospectionException, InvocationTargetException
	{
		Object obj = null;
		Class class1 = Introspector.getPropertyType(action.getClass(), s);
		if(class1 == java.lang.String.class)
			obj = s1;
		else
		if(s1 == null)
		{
			if(class1.isPrimitive())
				throw new ParseException("Can't convert null String to a primitive type", 0);
			obj = null;
		} else
		if(class1 == java.lang.Integer.class || class1 == Integer.TYPE)
			obj = Integer.valueOf(s1);
		else
		if(class1 == java.lang.Float.class || class1 == Float.TYPE)
			obj = Float.valueOf(s1);
		else
		if(class1 == java.lang.Double.class || class1 == Double.TYPE)
			obj = Double.valueOf(s1);
		else
		if(class1 == java.lang.Long.class || class1 == Long.TYPE)
			obj = Long.valueOf(s1);
		else
		if(class1 == java.lang.Short.class || class1 == Short.TYPE)
			obj = Short.valueOf(s1);
		else
		if(class1 == java.lang.Character.class || class1 == Character.TYPE)
			obj = new Character(s1.length() != 0 ? s1.charAt(0) : '\0');
		else
		if(class1 == java.lang.Boolean.class || class1 == Boolean.TYPE)
		{
			s1 = s1.toUpperCase();
			obj = new Boolean(s1.startsWith("Y") || s1.startsWith("T"));
		}
		if(class1.isPrimitive() && obj == null)
		{
			throw new ParseException("The conversion of \"" + s1 + "\" is a null Object, but the property is primitive", 0);
		} else
		{
			Introspector.setPropertyValue(action, s, obj);
			return;
		}
	}

	public void setBusinessProcess(BusinessProcess businessprocess)
	{
		businessProcess = businessprocess;
		BusinessProcess.setBusinessProcess(request, businessProcess);
	}

	public void setTracingSessionDescription(String s)
	{
		getSession().setAttribute("it.cnr.jada.action.HttpActionContext.tracingSessionDescription", s);
	}

	public void setUserContext(UserContext usercontext)
	{
		setUserContext(usercontext, true);
	}

	public void setUserContext(UserContext usercontext, boolean onHttpSession)
	{
		if (onHttpSession)
			getSession().setAttribute(USER_CONTEXT, usercontext);
		else
			getRequest().setAttribute(USER_CONTEXT, usercontext);
	}

	public void setUserInfo(UserInfo userinfo)
	{
		setUserInfo(userinfo, true);
	}

    public void setUserInfo(UserInfo userinfo, boolean onHttpSession)
    {
        if (onHttpSession)
            getSession().setAttribute(CURRENT_USER, userinfo);
        else
            getRequest().setAttribute(CURRENT_USER, userinfo);
    }

    public void traceException(Throwable throwable)
	{
		request.setAttribute("it.cnr.jada.action.HttpActionContext.traceException", throwable);
	}

    public void saveFocusedElement() {
        Optional.ofNullable(request)
                .ifPresent(httpServletRequest ->  Optional.ofNullable(httpServletRequest.getSession(false))
                        .ifPresent(httpSession -> {
                            String scrollx = httpServletRequest.getParameter("scrollx");
                            String scrolly = httpServletRequest.getParameter("scrolly");
                            Map<String, String> focusedElement = Optional.ofNullable(httpSession.getAttribute(CONTEXT_FOCUSED_ELEMENT))
                                    .filter(Map.class::isInstance)
                                    .map(Map.class::cast)
                                    .orElseGet(() -> new HashMap());
                            Optional.ofNullable(httpServletRequest.getParameter("requestor"))
                                    .filter(requestor -> requestor.length() > 0)
                                    .ifPresent(requestor -> {
                                        focusedElement.put(requestor, scrollx.concat(",").concat(scrolly));
                                        httpSession.setAttribute(CONTEXT_FOCUSED_ELEMENT,focusedElement);
                                    });
                        })
                );
    }

    public static boolean isFromBootstrap(PageContext pagecontext) {
		Optional<UserContext> optContext = Optional.ofNullable(pagecontext.getSession())
				.map(session -> (UserContext)session.getAttribute(USER_CONTEXT));
		if (optContext.isPresent()) 
			return (boolean) optContext.get().getAttributes().getOrDefault("bootstrap", false);		
		return false;
	}
}