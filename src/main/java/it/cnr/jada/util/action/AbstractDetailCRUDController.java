package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.*;
import it.cnr.jada.util.jsp.*;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

// Referenced classes of package it.cnr.jada.util.action:
//            NestedFormController, CRUDController, Selection, ModelController, 
//            FormField, SelectionIterator, FormController

public abstract class AbstractDetailCRUDController extends NestedFormController
    implements Serializable, CRUDController, Orderable
{

    public AbstractDetailCRUDController(String s, FormController formcontroller)
    {
        this(s, formcontroller, true);
    }

    public AbstractDetailCRUDController(String s, FormController formcontroller, boolean flag)
    {
        super(s, formcontroller);
        modelIndex = -1;
        currentPage = 0;
        pageSize = 10;
        pageFrameSize = 10;
        currentPageFrame = 0;
        enabled = true;
        try
        {
            pageSize = Integer.parseInt(Config.getHandler().getProperty(getClass(), "pageSize"));
        }
        catch(NumberFormatException _ex) { }
        try
        {
            pageFrameSize = Integer.parseInt(Config.getHandler().getProperty(getClass(), "pageFrameSize"));
        }
        catch(NumberFormatException _ex) { }
        selection = new Selection();
        table = new Table(getInputPrefix());
        table.setMultiSelection(flag);
        table.setOnselect("javascript:select");
        table.setReadonly(true);
        table.setStatus(2);
        table.setEditableOnFocus(true);
        table.setOrderable(this);
        table.setOnsort("javascript:sort");
    }

    public void add(ActionContext actioncontext)
        throws BusinessProcessException
    {
        add(actioncontext, createEmptyModel(actioncontext));
    }

    public void add(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
        if(getParentModel() == null)
            return;
        if(getModel() != null)
            try
            {
                validate(actioncontext);
                save(actioncontext);
            }
            catch(ValidationException validationexception)
            {
                throw new BusinessProcessException(validationexception);
            }
        int i = addDetail(oggettobulk);
        getParentController().setDirty(true);
        setModelIndex(actioncontext, i);
    }

    public abstract int addDetail(OggettoBulk oggettobulk)
        throws BusinessProcessException;

    protected boolean basicFillModel(ActionContext actioncontext)
        throws FillException
    {
        boolean flag = super.basicFillModel(actioncontext);
        if(getModel() != null && !isReadonly())
            flag = getModel().fillFromActionContext(actioncontext, getInputPrefix() + ".[" + getModelIndex(), 2, getFieldValidationMap()) || flag;
        return flag;
    }

    protected void basicReset(ActionContext actioncontext)
    {
        setModel(actioncontext, null);
        basicSetModelIndex(-1);
        selection.clear();
        resetChildren(actioncontext);
    }

    protected void basicSetModelIndex(int i)
    {
        modelIndex = i;
        selection.setFocus(modelIndex);
        if(modelIndex < 0)
        {
            currentPage = currentPageFrame = 0;
        } else
        {
            currentPage = modelIndex / pageSize;
            currentPageFrame = currentPageFrame = currentPage / pageFrameSize;
        }
    }

    protected Selection basicSetSelection(ActionContext actioncontext)
        throws ValidationException
    {
        selection.setFocus(actioncontext, getInputPrefix());
        if(paged)
            selection.setSelection(actioncontext, getInputPrefix(), currentPage * pageSize, pageSize);
        else
            selection.setSelection(actioncontext, getInputPrefix());
        setModelIndex(actioncontext, selection.getFocus());
        return selection;
    }

    protected int calcPageCount(int i)
    {
        return (i - 1) / pageSize + 1;
    }

    protected void clearFilter(ActionContext actioncontext)
    {
    }

    public abstract int countDetails();

    protected Button[] createCRUDToolbar()
    {
        Button abutton[] = new Button[5];
        abutton[0] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.add");
        abutton[1] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.filter");
        abutton[2] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.remove");
        abutton[3] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.removeAll");
        abutton[4] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.removeFilter");
        return abutton;
    }

    public abstract OggettoBulk createEmptyModel(ActionContext actioncontext);

    public boolean fillModel(ActionContext actioncontext)
        throws FillException
    {
        boolean flag = super.fillModel(actioncontext);
        if(paged)
            selection.setSelection(actioncontext, getInputPrefix(), currentPage * pageSize, pageSize);
        else
            selection.setSelection(actioncontext, getInputPrefix());
        return flag;
    }

    protected Button[] getCRUDToolbar()
    {
        if(crudToolbar == null)
            crudToolbar = createCRUDToolbar();
        return crudToolbar;
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    protected abstract OggettoBulk getDetail(int i);

    public abstract List getDetails();

    protected abstract List getDetailsPage();

    public Enumeration getElements()
    {
        return Collections.enumeration(getDetails());
    }

    public FormField getFormField(String s)
    {
        int i = s.indexOf('.');
        if(s.charAt(0) == '[')
        {
            int j = Integer.parseInt(s.substring(1, i));
            return new FormField(this, getBulkInfo().getFieldProperty(s.substring(i + 1)), getDetail(j));
        } else
        {
            return super.getFormField(s);
        }
    }

    public int getModelIndex()
    {
        return modelIndex;
    }

    protected Button[] getNavigatorToolbar()
    {
        if(navigatorToolbar == null)
        {
            navigatorToolbar = new Button[4];
            navigatorToolbar[0] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.previousPageFrame");
            navigatorToolbar[0].setSeparator(true);
            navigatorToolbar[1] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.previousPage");
            navigatorToolbar[2] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.nextPage");
            navigatorToolbar[3] = new Button(Config.getHandler().getProperties(getClass()), "Navigator.nextPageFrame");
        }
        return navigatorToolbar;
    }

    public int getPageFrameSize()
    {
        return pageFrameSize;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public List getSelectedModels(ActionContext actioncontext)
        throws ValidationException, BusinessProcessException
    {
        List list = getDetails();
        if(list == null)
        {
            return Collections.EMPTY_LIST;
        } else
        {
            setSelection(actioncontext);
            return selection.select(list);
        }
    }

    public int[] getSelectedRows(ActionContext actioncontext)
    {
        Selection selection1 = new Selection(actioncontext, getInputPrefix());
        int ai[] = new int[selection1.size()];
        int i = 0;
        for(SelectionIterator selectioniterator = selection1.iterator(); selectioniterator.hasNext();)
            ai[i++] = selectioniterator.nextIndex();

        return ai;
    }

    public Selection getSelection()
    {
        return selection;
    }

    public Selection getSelection(ActionContext actioncontext)
    {
        return new Selection(actioncontext, getInputPrefix());
    }

    public int getStatus()
    {
        switch(getParentController().getStatus())
        {
        case 5: // '\005'
            return 5;
        }
        return 2;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public boolean isFiltered()
    {
        return false;
    }

    public boolean isGrowable()
    {
        return !getParentController().isInputReadonly() && getStatus() != 5;
    }

    public boolean isOrderableBy(String s)
    {
        if(getBulkInfo() == null)
            return false;
        try
        {
            Class class1 = Introspector.getPropertyType(getBulkInfo().getBulkClass(), s);
            return java.lang.Comparable.class.isAssignableFrom(class1);
        }
        catch(IntrospectionException _ex)
        {
            return false;
        }
    }

    public boolean isPaged()
    {
        return paged;
    }

    public boolean isShrinkable()
    {
        return !getParentController().isInputReadonly() && getStatus() != 5;
    }

    public Iterator iterator()
    {
        if(selection.size() > 0)
            return selection.iterator(getDetails());
        if(getModel() == null)
            return Collections.EMPTY_LIST.iterator();
        else
            return Collections.singleton(getModel()).iterator();
    }

    public void remove(ActionContext actioncontext)
        throws ValidationException, BusinessProcessException
    {
        basicSetSelection(actioncontext);
        if(paged)
        {
            List list = getDetailsPage();
            BitSet bitset = selection.getSelection(currentPage * pageSize, pageSize);
            if(bitset.length() == 0)
            {
                for(int i = 0; i < pageSize; i++)
                    if(bitset.get(i))
                        validateForDelete(actioncontext, (OggettoBulk)list.get(i));

                for(int j = pageSize - 1; j > 0; j--)
                    if(bitset.get(j))
                        removeDetail((OggettoBulk)list.get(j), j);

            } else
            if(selection.getFocus() >= 0)
            {
                OggettoBulk oggettobulk1 = getDetail(selection.getFocus());
                validateForDelete(actioncontext, oggettobulk1);
                removeDetail(oggettobulk1, selection.getFocus());
            }
        } else
        {
            List list1 = getDetails();
            if(selection.size() > 0)
            {
                OggettoBulk oggettobulk2;
                for(Iterator iterator1 = selection.iterator(list1); iterator1.hasNext(); validateForDelete(actioncontext, oggettobulk2))
                    oggettobulk2 = (OggettoBulk)iterator1.next();

                int k;
                OggettoBulk oggettobulk3;
                for(SelectionIterator selectioniterator = selection.reverseIterator(); selectioniterator.hasNext(); removeDetail(oggettobulk3, k))
                {
                    k = selectioniterator.nextIndex();
                    oggettobulk3 = (OggettoBulk)list1.get(k);
                }

            } else
            if(selection.getFocus() >= 0)
            {
                OggettoBulk oggettobulk = getDetail(selection.getFocus());
                validateForDelete(actioncontext, oggettobulk);
                removeDetail(oggettobulk, selection.getFocus());
            }
        }
        getParentController().setDirty(true);
        reset(actioncontext);
    }

    public void removeAll(ActionContext actioncontext)
        throws ValidationException, BusinessProcessException
    {
        List list = getDetails();
        OggettoBulk oggettobulk;
        for(Iterator iterator1 = list.iterator(); iterator1.hasNext(); validateForDelete(actioncontext, oggettobulk))
            oggettobulk = (OggettoBulk)iterator1.next();

        for(int i = list.size() - 1; i >= 0; i--)
        {
            OggettoBulk oggettobulk1 = (OggettoBulk)list.get(i);
            removeDetail(oggettobulk1, i);
        }

        getParentController().setDirty(true);
        reset(actioncontext);
    }

    protected abstract OggettoBulk removeDetail(OggettoBulk oggettobulk, int i);

    public void reset(ActionContext actioncontext)
    {
        clearFilter(actioncontext);
        basicReset(actioncontext);
    }

    public void resync(ActionContext actioncontext)
        throws BusinessProcessException
    {
        try
        {
            setModel(actioncontext, getDetail(modelIndex));
        }
        catch(IndexOutOfBoundsException _ex)
        {
            basicSetModelIndex(-1);
            setModel(actioncontext, null);
        }
        super.resync(actioncontext);
    }

    public void save(ActionContext actioncontext)
        throws BusinessProcessException
    {
        if(getModel() != null && isDirty())
            save(actioncontext, getModel());
        for(Enumeration enumeration = getChildrenController(); enumeration.hasMoreElements();)
        {
            Object obj = enumeration.nextElement();
            if(obj instanceof CRUDController)
                ((CRUDController)obj).save(actioncontext);
        }

    }

    public void save(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws BusinessProcessException
    {
    }

    public void setEnabled(boolean flag)
    {
        enabled = flag;
    }

    public abstract void setFilter(ActionContext actioncontext, CompoundFindClause compoundfindclause);

    public void setModelIndex(ActionContext actioncontext, int i)
    {
        OggettoBulk oggettobulk;
        try
        {
            oggettobulk = getDetail(i);
        }
        catch(IndexOutOfBoundsException _ex)
        {
            oggettobulk = null;
            i = -1;
        }
        if(modelIndex != i || getModel() != oggettobulk)
        {
            basicSetModelIndex(i);
            setModel(actioncontext, oggettobulk);
            resetChildren(actioncontext);
        }
    }

    public void setMultiSelection(boolean flag)
    {
        table.setMultiSelection(flag);
    }

    public void setPaged(boolean flag)
    {
        paged = flag;
    }

    public void setPageIndex(ActionContext actioncontext, int i)
        throws ValidationException, BusinessProcessException
    {
        setSelection(actioncontext);
        int j = calcPageCount(countDetails());
        if(i < 0 || i >= j)
        {
            return;
        } else
        {
            setModelIndex(actioncontext, -1);
            currentPage = i;
            currentPageFrame = currentPageFrame = currentPage / pageFrameSize;
            return;
        }
    }

    public void setPageSize(int i)
    {
        pageSize = i;
    }

    public Selection setSelection(ActionContext actioncontext)
        throws ValidationException, BusinessProcessException
    {
        validate(actioncontext);
        save(actioncontext);
        return basicSetSelection(actioncontext);
    }

    public Selection setSelection(ActionContext actioncontext, Selection selection1)
        throws ValidationException, BusinessProcessException
    {
        validate(actioncontext);
        save(actioncontext);
        selection = selection1;
        setModelIndex(actioncontext, selection1.getFocus());
        return selection1;
    }

    protected abstract List sortDetailsBy(Comparator comparator);

    public void validateForDelete(ActionContext actioncontext, OggettoBulk oggettobulk)
        throws ValidationException
    {
    }

    protected final void writeCRUDToolbar(PageContext pagecontext, boolean flag, boolean flag1, boolean flag2)
        throws IOException, ServletException
    {
        getCRUDToolbar();
        if(flag)
        {
            Button button = crudToolbar[0];
            button.setHref("javascript:submitForm('doAddToCRUD(" + getInputPrefix() + ")')");
            button.writeToolbarButton(pagecontext.getOut(), isGrowable());
        }
        if(flag1)
        {
            Button button1;
            if(isFiltered())
            {
                button1 = crudToolbar[4];
                button1.setHref("javascript:submitForm('doRemoveFilterCRUD(" + getInputPrefix() + ")')");
            } else
            {
                button1 = crudToolbar[1];
                button1.setHref("javascript:submitForm('doFilterCRUD(" + getInputPrefix() + ")')");
            }
            button1.writeToolbarButton(pagecontext.getOut(), true);
        }
        if(flag2)
        {
            Button button2 = crudToolbar[2];
            button2.setHref("javascript:submitForm('doRemoveFromCRUD(" + getInputPrefix() + ")')");
            button2.writeToolbarButton(pagecontext.getOut(), isShrinkable());
        }
        if(flag2 && paged)
        {
            Button button3 = crudToolbar[3];
            button3.setHref("javascript:submitForm('doRemoveAllFromCRUD(" + getInputPrefix() + ")')");
            button3.writeToolbarButton(pagecontext.getOut(), isShrinkable());
        }
    }

    public void writeHTMLNavigator(PageContext pagecontext, int i)
        throws IOException, ServletException
    {
        boolean flag = currentPage > 0;
        boolean flag1 = currentPage < i - 1;
        JspWriter jspwriter = pagecontext.getOut();
        getNavigatorToolbar();
        Button button = navigatorToolbar[0];
        button.setHref("javascript:doNavigate('" + getInputPrefix() + "'," + ((currentPage - 1) / pageFrameSize) * pageFrameSize + ")");
        button.writeToolbarButton(jspwriter, flag);
        button = navigatorToolbar[1];
        button.setHref("javascript:doNavigate('" + getInputPrefix() + "'," + (currentPage - 1) + ")");
        button.writeToolbarButton(jspwriter, flag);
        int j = currentPageFrame * pageFrameSize;
        for(int k = 0; k < pageFrameSize && j < i; j++)
        {
            jspwriter.print("<td>");
			it.cnr.jada.util.jsp.JSPUtils.toolbarButton(pagecontext, null, String.valueOf(j + 1), j == currentPage ? null : "javascript:doNavigate('" + getInputPrefix() + "'," + j + ")", false);
            jspwriter.print("</td>");
            k++;
        }

        button = navigatorToolbar[2];
        button.setHref("javascript:doNavigate('" + getInputPrefix() + "'," + (currentPage + 1) + ")");
        button.writeToolbarButton(jspwriter, flag1);
        button = navigatorToolbar[3];
        button.setHref("javascript:doNavigate('" + getInputPrefix() + "'," + Math.min(((currentPage + pageFrameSize + 1) / pageFrameSize) * pageFrameSize - 1, i - 1) + ")");
        button.writeToolbarButton(jspwriter, flag);
    }

    protected void writeHTMLPagedTable(PageContext pagecontext, String s, boolean flag, boolean flag1, boolean flag2, String s1, String s2, 
            boolean flag3, TableCustomizer tablecustomizer, List list, int i)
        throws IOException, ServletException
    {
        JspWriter jspwriter = pagecontext.getOut();
        table.setSelection(selection);
        table.setColumns(getBulkInfo().getColumnFieldPropertyDictionary(s));
        table.setSelectable(enabled);
        table.setReadonly(flag3 || !enabled);
        table.setCustomizer(tablecustomizer);
        table.setRows(Collections.enumeration(list != null ? ((java.util.Collection) (list)) : ((java.util.Collection) (Collections.EMPTY_LIST))));
        boolean flag4 = "100%".equals(s2) && "100%".equals(s1);
        if(flag4)
        {
            jspwriter.println("<table class=\"Panel\" style=\"width:100%;height=100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">");
            jspwriter.println("<tr style=\"height:" + s2 + "\"><td style=\"width:" + s1 + "\">");
            table.writeScrolledTable(this, pagecontext.getOut(), "100%", "100%", getFieldValidationMap(), currentPage * pageSize);
            jspwriter.println("</td></tr>");
        } else
        {
            table.writeScrolledTable(this, pagecontext.getOut(), s1, s2, getFieldValidationMap(), currentPage * pageSize);
        }
        if(flag4)
            jspwriter.println("<tr><td style=\"width:" + s1 + "\">");
        jspwriter.println("<table class=\"Toolbar\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:" + s1 + "\"><tr>");
        if(enabled)
            writeHTMLToolbar(pagecontext, flag, flag1, flag2);
        writeHTMLNavigator(pagecontext, i);
        jspwriter.println("<td style=\"width:100%\">&nbsp;</td></tr></table>");
        if(flag4)
            jspwriter.println("</td></tr>");
        if(flag4)
            jspwriter.println("</table>");
    }

    public void writeHTMLTable(PageContext pagecontext, String s, boolean flag, boolean flag1, boolean flag2, String s1, String s2)
        throws IOException, ServletException
    {
        writeHTMLTable(pagecontext, s, flag, flag1, flag2, s1, s2, true);
    }

    public void writeHTMLTable(PageContext pagecontext, String s, boolean flag, boolean flag1, boolean flag2, String s1, String s2, 
            boolean flag3)
        throws IOException, ServletException
    {
        if(this instanceof TableCustomizer)
            writeHTMLTable(pagecontext, s, flag, flag1, flag2, s1, s2, flag3, (TableCustomizer)this);
        else
            writeHTMLTable(pagecontext, s, flag, flag1, flag2, s1, s2, flag3, null);
    }

    public abstract void writeHTMLTable(PageContext pagecontext, String s, boolean flag, boolean flag1, boolean flag2, String s1, String s2, 
            boolean flag3, TableCustomizer tablecustomizer)
        throws IOException, ServletException;

    protected void writeHTMLTable(PageContext pagecontext, String s, boolean flag, boolean flag1, boolean flag2, String s1, String s2, 
            boolean flag3, TableCustomizer tablecustomizer, List list)
        throws IOException, ServletException
    {
        JspWriter jspwriter = pagecontext.getOut();
        table.setSelection(selection);
        table.setColumns(getBulkInfo().getColumnFieldPropertyDictionary(s));
        table.setSelectable(enabled);
        table.setReadonly(isInputReadonly() || flag3 || !enabled);
        table.setCustomizer(tablecustomizer);
        table.setRows(Collections.enumeration(list != null ? ((java.util.Collection) (list)) : ((java.util.Collection) (Collections.EMPTY_LIST))));
        boolean flag4 = "100%".equals(s2) && "100%".equals(s1);
        if(flag4)
        {
            jspwriter.println("<table class=\"Panel\" style=\"width:100%;height=100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">");
            jspwriter.println("<tr style=\"height:" + s2 + "\"><td style=\"width:" + s1 + "\">");
            table.writeScrolledTable(this,pagecontext.getOut(), "100%", "100%", getFieldValidationMap(), 0);
            jspwriter.println("</td></tr>");
        } else
        {
            table.writeScrolledTable(this,pagecontext.getOut(), s1, s2, getFieldValidationMap(), 0);
        }
        if(enabled)
        {
            if(flag4)
                jspwriter.println("<tr><td style=\"width:" + s1 + "\">");
            jspwriter.println("<table class=\"Toolbar\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:" + s1 + "\"><tr>");
            writeHTMLToolbar(pagecontext, flag, flag1, flag2);
            jspwriter.println("<td style=\"width:100%\">&nbsp;</td></tr></table>");
            if(flag4)
                jspwriter.println("</td></tr>");
        }
        if(flag4)
            jspwriter.println("</table>");
    }

    public void writeHTMLToolbar(PageContext pagecontext, boolean flag, boolean flag1, boolean flag2)
        throws IOException, ServletException
    {
        writeCRUDToolbar(pagecontext, flag, flag1, flag2);
    }

    public void writeHTMLToolbar(PageContext pagecontext, boolean flag, boolean flag1, boolean flag2, List list)
        throws IOException, ServletException
    {
        writeHTMLToolbar(pagecontext, flag, flag1, flag2);
    }

    public abstract int getOrderBy(String s);

    public abstract void setOrderBy(ActionContext actioncontext, String s, int i);

    protected int modelIndex;
    private Table table;
    protected boolean paged;
    private int currentPage;
    private int pageSize;
    private int pageFrameSize;
    private int currentPageFrame;
    protected Selection selection;
    private boolean enabled;
    private Button crudToolbar[];
    private Button navigatorToolbar[];
}