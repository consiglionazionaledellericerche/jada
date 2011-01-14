package it.cnr.jada.util.action;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

// Referenced classes of package it.cnr.jada.util.action:
//            SelezionatoreListaBP, AbstractSelezionatoreBP, Selection

public class SelezionatoreListaAlberoBP extends SelezionatoreListaBP
    implements Serializable
{

    public SelezionatoreListaAlberoBP()
    {
        history = new Stack();
    }

    public SelezionatoreListaAlberoBP(String s)
    {
        super(s);
        history = new Stack();
    }

    public Button[] createToolbar()
    {
        Button abutton[] = new Button[3];
        int i = 0;
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.bringBack");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.expand");
        abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "Toolbar.back");
        return abutton;
    }

    public RemoteIterator getChildren(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        try
        {
            return EJBCommonServices.openRemoteIterator(actioncontext, remoteBulkTree.getChildren(actioncontext, oggettobulk));
        }
        catch(RemoteException remoteexception)
        {
            throw handleException(remoteexception);
        }
    }

    public final Stack getHistory()
    {
        return history;
    }

    public OggettoBulk getParent(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        try
        {
            return remoteBulkTree.getParent(actioncontext, oggettobulk);
        }
        catch(RemoteException remoteexception)
        {
            throw handleException(remoteexception);
        }
    }

    public OggettoBulk getParentElement()
    {
        return parentElement;
    }

    public RemoteBulkTree getRemoteBulkTree()
    {
        return remoteBulkTree;
    }

    public boolean isBackButtonEnabled()
    {
        return !history.isEmpty();
    }

    public boolean isBringBackButtonEnabled()
    {
        return true;
    }

    public boolean isExpandButtonEnabled()
    {
        return !isLeafElement() && super.selection.getFocus() >= 0;
    }

    public boolean isLeaf(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        try
        {
            return remoteBulkTree.isLeaf(actioncontext, oggettobulk);
        }
        catch(RemoteException remoteexception)
        {
            throw handleException(remoteexception);
        }
    }

    public boolean isLeafElement()
    {
        return leafElement;
    }

    public void setLeafElement(boolean flag)
    {
        leafElement = flag;
    }

    public void setParentElement(OggettoBulk oggettobulk)
    {
        parentElement = oggettobulk;
    }

    public void setRemoteBulkTree(ActionContext actioncontext, RemoteBulkTree remotebulktree)
        throws BusinessProcessException
    {
        try
        {
            if(remotebulktree == null)
                setRemoteBulkTree(actioncontext, null, ((RemoteIterator) (null)));
            else
                setRemoteBulkTree(actioncontext, remotebulktree, remotebulktree.getChildren(actioncontext, null));
        }
        catch(RemoteException remoteexception)
        {
            throw handleException(remoteexception);
        }
    }

    public void setRemoteBulkTree(ActionContext actioncontext, RemoteBulkTree remotebulktree, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        try
        {
            setParentElement(oggettobulk);
            if(remotebulktree == null)
                setRemoteBulkTree(actioncontext, remotebulktree, ((RemoteIterator) (new EmptyRemoteIterator())));
            else
                setRemoteBulkTree(actioncontext, remotebulktree, remotebulktree.getChildren(actioncontext, oggettobulk));
        }
        catch(RemoteException remoteexception)
        {
            throw handleException(remoteexception);
        }
    }

    public void setRemoteBulkTree(ActionContext actioncontext, RemoteBulkTree remotebulktree, RemoteIterator remoteiterator)
        throws BusinessProcessException
    {
        try
        {
            remoteBulkTree = remotebulktree;
            if(remoteiterator == null)
                remoteiterator = new EmptyRemoteIterator();
            history.clear();
            setIterator(actioncontext, remoteiterator);
        }
        catch(RemoteException remoteexception)
        {
            throw handleException(remoteexception);
        }
    }

    public void writeHistory(PageContext pagecontext, String s)
        throws IOException, ServletException
    {
        JspWriter jspwriter = pagecontext.getOut();
        jspwriter.println("<table class=\"Panel\" width=\"100%\">");
        jspwriter.println("<tr width=\"100%\">");
        jspwriter.println("<td>");
        writeHistoryLabel(pagecontext);
        jspwriter.println("</td>");
        jspwriter.println("<td width=\"100%\">");
        writeHistoryField(pagecontext, s);
        jspwriter.println("</td>");
        jspwriter.println("</tr>");
        jspwriter.println("</table>");
    }

    public void writeHistoryField(PageContext pagecontext, String s)
        throws IOException, ServletException
    {
        JspWriter jspwriter = pagecontext.getOut();
        FieldProperty fieldproperty = getBulkInfo().getFieldProperty(s);
        jspwriter.println("<div style=\"border-style: inset;background-color:White;border-width:thin;padding:2px\">");
        jspwriter.print("&nbsp;");
        int i = 0;
        boolean flag = false;
        for(Iterator iterator = getHistory().iterator(); iterator.hasNext();)
        {
            OggettoBulk oggettobulk = (OggettoBulk)iterator.next();
            if(oggettobulk != null)
            {
                if(flag)
                    jspwriter.print(" > ");
                fieldproperty.writeReadonlyText(jspwriter, oggettobulk, "MenuItem", "onclick=\"javascript:submitForm('doGoToLevel(" + i + ")')\" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" onMouseDown=\"mouseDown(this)\" onMouseUp=\"mouseUp(this)\"");
                flag = true;
            }
            i++;
        }

        if(getParentElement() != null)
        {
            if(flag)
                jspwriter.print(" > ");
            fieldproperty.writeReadonlyText(jspwriter, getParentElement(), "MenuItem", "onclick=\"javascript:submitForm('doGoToLevel(" + i + ")')\" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" onMouseDown=\"mouseDown(this)\" onMouseUp=\"mouseUp(this)\"");
        }
        jspwriter.print("</div>");
    }

    public void writeHistoryLabel(PageContext pagecontext)
        throws IOException, ServletException
    {
        JspWriter jspwriter = pagecontext.getOut();
        jspwriter.println("<span class=\"FormLabel\">Percorso: </span>");
    }

    private RemoteBulkTree remoteBulkTree;
    private final Stack history;
    private OggettoBulk parentElement;
    private boolean leafElement;
}