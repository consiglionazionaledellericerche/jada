package it.cnr.jada.util.jsp;

import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.AbstractDetailCRUDController;
import it.cnr.jada.util.action.AbstractSelezionatoreBP;
import it.cnr.jada.util.action.BulkListBP;
import it.cnr.jada.util.action.Selection;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import java.io.IOException;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.jsp.JspWriter;

// Referenced classes of package it.cnr.jada.util.jsp:
//            Button, TableCustomizer

public class Table
    implements Serializable
{

    public Table(String s)
    {
        multiSelection = false;
        readonly = true;
        status = 5;
        selectable = true;
        editableOnFocus = false;
        name = s;
        checkAllButton = new Button(Config.getHandler().getProperties(it.cnr.jada.util.jsp.Table.class), "checkAllButton");
        checkAllButton.setHref("javascript:selectAll('" + s + "')");
    }

    public Table(String s, Dictionary dictionary)
    {
        this(s);
        columns = dictionary;
    }

    public Button getCheckAllButton()
    {
        return checkAllButton;
    }

    public Dictionary getColumns()
    {
        return columns;
    }

    public TableCustomizer getCustomizer()
    {
        return customizer;
    }

    public final String getName()
    {
        return name;
    }

    public String getOnselect()
    {
        return onselect;
    }

    public String getOnsort()
    {
        return onsort;
    }

    public Orderable getOrderable()
    {
        return orderable;
    }

    public Object getSelectedElement()
    {
        return selectedElement;
    }

    public Selection getSelection()
    {
        return selection;
    }

    public int getStatus()
    {
        return status;
    }

    public boolean isEditableOnFocus()
    {
        return editableOnFocus;
    }

    public boolean isMultiSelection()
    {
        return multiSelection;
    }

    public boolean isReadonly()
    {
        return readonly;
    }

    public boolean isSelectable()
    {
        return selectable;
    }

    public boolean isSingleSelection()
    {
        return singleSelection;
    }

    public void setCheckAllButton(Button button)
    {
        checkAllButton = button;
    }

    public void setColumns(Dictionary dictionary)
    {
        columns = dictionary;
    }

    public void setCustomizer(TableCustomizer tablecustomizer)
    {
        customizer = tablecustomizer;
    }

    public void setEditableOnFocus(boolean flag)
    {
        editableOnFocus = flag;
    }

    public void setMultiSelection(boolean flag)
    {
        multiSelection = flag;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setOnselect(String s)
    {
        onselect = s;
    }

    public void setOnsort(String s)
    {
        onsort = s;
    }

    public void setOrderable(Orderable orderable1)
    {
        orderable = orderable1;
    }

    public void setReadonly(boolean flag)
    {
        readonly = flag;
    }

    public void setRows(Enumeration enumeration)
    {
        rows = enumeration;
    }

    public void setSelectable(boolean flag)
    {
        selectable = flag;
    }

    public void setSelectedElement(Object obj)
    {
        selectedElement = obj;
    }

    public void setSelection(Selection selection1)
    {
        selection = selection1;
    }

    public void setSingleSelection(boolean flag)
    {
        singleSelection = flag;
    }

    public void setStatus(int i)
    {
        status = i;
    }

    public void write(JspWriter jspwriter, FieldValidationMap fieldvalidationmap)
        throws IOException
    {
        write(jspwriter, fieldvalidationmap, 0);
    }
	public void writeTableWithoutColumnHeader(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i) throws IOException{
		writeTableWithoutColumnHeader(null, jspwriter, fieldvalidationmap, i);		
	}
	public void writeTableWithoutColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i) throws IOException{
		writeTableWithoutColumnHeader(bp, jspwriter, fieldvalidationmap, i,false);
	}
	public void writeTableWithoutColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,boolean nascondiColonne) throws IOException{
		writeTableWithoutColumnHeader(bp, jspwriter, fieldvalidationmap, i,false, null);
	}
	public void writeTableWithoutColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,boolean nascondiColonne,Dictionary hiddenColumns) throws IOException{
		writeTableWithoutColumnHeader(bp, jspwriter, fieldvalidationmap, i,false, null, null);
	}
	public void writeTableWithoutColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,boolean nascondiColonne,Dictionary hiddenColumns, String pathBP) throws IOException{
		colonne : for(Enumeration enumeration = columns.elements(); enumeration.hasMoreElements(); jspwriter.println("</td>"))
		{
			ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration.nextElement();
			if (hiddenColumns != null && pathBP != null){
				String key = pathBP.concat("-").concat(columnfieldproperty.getName());
				if (hiddenColumns.get(key) != null)
					continue colonne;
			}
			jspwriter.print("<td");
			columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
			if(columnfieldproperty.isNoWrap())
				jspwriter.print(" nowrap");
		    
			jspwriter.print(">");
			if(onsort != null && orderable != null && orderable.isOrderableBy(columnfieldproperty.getProperty()))
			{
				int k = orderable.getOrderBy(columnfieldproperty.getProperty());
				switch(k)
				{
				case 0: // '\0'
					Button.write(jspwriter, "img/sortable16.gif", null, onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')", "vertical-align:middle;");
					break;

				case 1: // '\001'
					Button.write(jspwriter, "img/sorted_asc16.gif", null, onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')", "vertical-align:middle;");
					break;

				case -1: 
					Button.write(jspwriter, "img/sorted_desc16.gif", null, onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')", "vertical-align:middle;");
					break;
				}
			}
			if (nascondiColonne){
				jspwriter.print("&nbsp;");
				Button.write(jspwriter, null, "img/meno8.gif", null, 1, getOnHiddenColumn() + "('" + name + "','" + columnfieldproperty.getName() + "')", "vertical-align:middle;","Nascondi colonna ("+columnfieldproperty.getLabel()+")");
			}
			columnfieldproperty.writeLabel(bp, jspwriter, null);
		}
		
	}
	public void writeTableWithColumnHeader(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,Hashtable labelHeader) throws IOException{
		writeTableWithColumnHeader(null, jspwriter, fieldvalidationmap, i, labelHeader);
	}	
	public void writeTableWithColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,Hashtable labelHeader) throws IOException{
		writeTableWithColumnHeader(bp, jspwriter, fieldvalidationmap, i,labelHeader,false);
	}
	public void writeTableWithColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,Hashtable labelHeader, boolean nascondiColonne) throws IOException{
		writeTableWithColumnHeader(bp, jspwriter, fieldvalidationmap, i,labelHeader,false, null);
	}	
	public void writeTableWithColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,Hashtable labelHeader, boolean nascondiColonne,Dictionary hiddenColumns) throws IOException{
		writeTableWithColumnHeader(bp, jspwriter, fieldvalidationmap, i,labelHeader,false, null, null);
	}
	public void writeTableWithColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,Hashtable labelHeader, boolean nascondiColonne,Dictionary hiddenColumns,String pathBP) throws IOException{
		colonne : for(Enumeration enumeration = columns.elements(); enumeration.hasMoreElements(); jspwriter.println("</td>"))
		{
			ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration.nextElement();
			if (hiddenColumns != null && pathBP != null){
				String key = pathBP.concat("-").concat(columnfieldproperty.getName());
				if (hiddenColumns.get(key) != null)
					continue colonne;
			}
			if(columnfieldproperty.isNotTableHeader()){
				jspwriter.print("<td");
				columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
				if(columnfieldproperty.isNoWrap())
					jspwriter.print(" nowrap");
					
				jspwriter.print(" valign=center rowspan=2");			    
				jspwriter.print(">");
				if(onsort != null && orderable != null && orderable.isOrderableBy(columnfieldproperty.getProperty()))
				{
					int k = orderable.getOrderBy(columnfieldproperty.getProperty());
					switch(k)
					{
					case 0: // '\0'
						Button.write(jspwriter, "img/sortable16.gif", null, onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')", "vertical-align:middle;");
						break;
	
					case 1: // '\001'
						Button.write(jspwriter, "img/sorted_asc16.gif", null, onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')", "vertical-align:middle;");
						break;
	
					case -1: 
						Button.write(jspwriter, "img/sorted_desc16.gif", null, onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')", "vertical-align:middle;");
						break;
					}
				}
				if (nascondiColonne){
					jspwriter.print("&nbsp;");
					Button.write(jspwriter, "img/meno8.gif", null, getOnHiddenColumn() + "('" + name + "','" + columnfieldproperty.getName() + "')", "vertical-align:middle;");
				}
				columnfieldproperty.writeLabel(bp,jspwriter, null);
			}else{
				if(labelHeader.containsKey(columnfieldproperty.getHeaderLabel())){
					jspwriter.print("<td");
					columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
					jspwriter.print(" align=center colspan="+(Integer)(labelHeader.get(columnfieldproperty.getHeaderLabel()))+">");
					columnfieldproperty.writeHeaderLabel(bp, jspwriter, null,columnfieldproperty.getHeaderLabel());
					jspwriter.println("</td>");					
					labelHeader.remove(columnfieldproperty.getHeaderLabel());
				}
			}
											
		}
		jspwriter.println("</tr>");
		jspwriter.println("<tr valign=\"top\">");
		colonne : for(Enumeration enumeration = columns.elements(); enumeration.hasMoreElements(); jspwriter.println("</td>"))
		{
			ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration.nextElement();
			if (hiddenColumns != null && pathBP != null){
				String key = pathBP.concat("-").concat(columnfieldproperty.getName());
				if (hiddenColumns.get(key) != null)
					continue colonne;
			}
			if(!columnfieldproperty.isNotTableHeader()){
				jspwriter.print("<td");
				columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader");
				if(columnfieldproperty.isNoWrap())
					jspwriter.print(" nowrap");
			    
				jspwriter.print(">");
				if(onsort != null && orderable != null && orderable.isOrderableBy(columnfieldproperty.getProperty()))
				{
					int k = orderable.getOrderBy(columnfieldproperty.getProperty());
					switch(k)
					{
					case 0: // '\0'
						Button.write(jspwriter, "img/sortable16.gif", null, onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')", "vertical-align:middle;");
						break;
	
					case 1: // '\001'
						Button.write(jspwriter, "img/sorted_asc16.gif", null, onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')", "vertical-align:middle;");
						break;
	
					case -1: 
						Button.write(jspwriter, "img/sorted_desc16.gif", null, onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')", "vertical-align:middle;");
						break;
					}
				}
				if (nascondiColonne){
					jspwriter.print("&nbsp;");
					Button.write(jspwriter, "img/meno8.gif", null, getOnHiddenColumn() + "('" + name + "','" + columnfieldproperty.getName() + "')", "vertical-align:middle;");
				}
				columnfieldproperty.writeLabel(bp, jspwriter, null);
			}
		}		
	}
	public void write(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i)
		throws IOException
	{
		write(null, jspwriter, fieldvalidationmap, i);
	}
    public void write(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne,Dictionary hiddenColumns,String pathBP) throws IOException{
		/*
		 * Cerco le headerLabel
		 */ 
		String headerLabel = null;
		boolean presenteHeader = false;
		Hashtable labelHeader = new Hashtable();
		colonne : for(Enumeration enumeration = columns.elements(); enumeration.hasMoreElements();){
			Integer colspan = null;
			ColumnFieldProperty columnfieldproperty = (ColumnFieldProperty)enumeration.nextElement();
			if (hiddenColumns != null && pathBP != null){
				String key = pathBP.concat("-").concat(columnfieldproperty.getName());
				if (hiddenColumns.get(key) != null)
					continue colonne;
			}
			if (columnfieldproperty.getHeaderLabel() != null){
				presenteHeader = true;
				if (labelHeader.containsKey(columnfieldproperty.getHeaderLabel()))
				  colspan = (Integer)labelHeader.get(columnfieldproperty.getHeaderLabel()); 				
				labelHeader.put(columnfieldproperty.getHeaderLabel(),colspan!=null? new Integer(colspan.intValue()+1):new Integer(1));	
			}			
		}
    	
        int j = selection != null ? selection.getFocus() : -1;
        jspwriter.println("<!-- INIZIO TABLE HEADER -->");
        jspwriter.println("<thead>");
        jspwriter.println("<tr valign=\"top\">");
        if(selectable && multiSelection)
        {
			if(!presenteHeader)
              jspwriter.print("<td class=\"TableHeader\" align=\"center\" valign=\"center\">");
            else
			  jspwriter.print("<td rowspan=2 class=\"TableHeader\" align=\"center\" valign=\"center\">");
            checkAllButton.write(jspwriter, true);
            jspwriter.print("</td>");
        }
        if(selectable && singleSelection)
            jspwriter.print("<td class=\"TableHeader\" align=\"center\" valign=\"center\">&nbsp;</td>");
		if(!presenteHeader)			    
		  writeTableWithoutColumnHeader(bp,jspwriter, fieldvalidationmap, i, nascondiColonne,hiddenColumns,pathBP);
		else
		  writeTableWithColumnHeader(bp,jspwriter, fieldvalidationmap, i, labelHeader, nascondiColonne,hiddenColumns,pathBP);  
		
        jspwriter.println("</tr>");
        jspwriter.println("</thead>");
        jspwriter.println("<!-- FINE TABLE HEADER -->");
        jspwriter.println("<tbody>");
        for(Enumeration enumeration1 = rows; enumeration1.hasMoreElements();)
        {
            Object obj = enumeration1.nextElement();
            if(obj.equals(selectedElement))
                j = i;
            boolean flag = selectable && (obj instanceof OggettoBulk) && ((OggettoBulk)obj).isOperabile() && (customizer == null || customizer.isRowEnabled(obj));
            boolean flag1 = readonly && flag && (customizer == null || customizer.isRowReadonly(obj));
            jspwriter.print("<!-- INIZIO RIGA ");
            jspwriter.print(i);
            jspwriter.println(" -->");
            int resto = i % 2;
            String s = name + ".[" + i;
            jspwriter.print("<tr class=\"");
            if(!singleSelection && j == i){
                jspwriter.print((resto == 0?"SelectedTableRow":"SelectedTableRowPar"));
            }else{
            	jspwriter.print((resto == 0?"TableRow":"TableRowPar"));
            }
            jspwriter.print('"');
            if(customizer != null)
            {
                String s1 = customizer.getRowStyle(obj);
                if(s1 != null)
                {
                    jspwriter.print(" style=\"");
                    jspwriter.print(s1);
                    jspwriter.print('"');
                }
            }
            if(j == i)
            {
                jspwriter.print(" id=\"");
                jspwriter.print(Prefix.prependPrefix(s, "selectdTR"));
                jspwriter.print('"');
            }
            if(flag && !singleSelection && onselect != null)
            {
                jspwriter.print(" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" onMouseDown=\"mouseDown(this)\" onMouseUp=\"mouseUp(this)\" onClick=\"");
                jspwriter.print(onselect);
                jspwriter.print("('");
                jspwriter.print(name);
                jspwriter.print("',");
                jspwriter.print(i);
                jspwriter.print(")\"");
            }
            jspwriter.println(">");
            if(selectable && multiSelection)
            {
                jspwriter.print("<td class=\"TableColumn\">");
                if(flag)
                {
                    jspwriter.print("<input type=\"checkbox\" name=\"");
                    jspwriter.print(name);
                    jspwriter.print(".selection\" value=\"");
                    jspwriter.print(i);
                    jspwriter.print("\" onclick=\"cancelBubble(event)\"");
                    if(selection.isSelected(i))
                        jspwriter.print(" checked");
                    jspwriter.println(">");
                } else
                {
                    jspwriter.print("&nbsp;");
                }
                jspwriter.println("</td>");
            }
            if(selectable && singleSelection)
            {
                jspwriter.print("<td class=\"TableColumn\">");
                if(flag)
                {
                    jspwriter.print("<input type=\"radio\" name=\"");
                    jspwriter.print(name);
                    jspwriter.print("\" value=\"");
                    jspwriter.print(i);
                    jspwriter.print("\" onclick=\"cancelBubble();");
                    if(onselect != null)
                        jspwriter.print(onselect);
                    jspwriter.print('"');
                    if(i == j)
                        jspwriter.print(" checked");
                    jspwriter.println(">");
                } else
                {
                    jspwriter.print("&nbsp;");
                }
                jspwriter.println("</td>");
            }
            colonne : for(Enumeration enumeration2 = columns.elements(); enumeration2.hasMoreElements(); jspwriter.println("</td>"))
            {
                ColumnFieldProperty columnfieldproperty1 = (ColumnFieldProperty)enumeration2.nextElement();
    			if (hiddenColumns != null && pathBP != null){
    				String key = pathBP.concat("-").concat(columnfieldproperty1.getName());
    				if (hiddenColumns.get(key) != null)
    					continue colonne;
    			}
                jspwriter.print("<td");
                int l = !readonly && (!editableOnFocus || j == i) ? status : 5;
                columnfieldproperty1.writeColumnStyle(jspwriter, obj, flag1, "TableColumn", l);
                if(columnfieldproperty1.isNoWrap())
                    jspwriter.print(" nowrap");
                jspwriter.print(">");
                columnfieldproperty1.writeInput(jspwriter, obj, flag1, null, null, s, l, fieldvalidationmap);
            }

            jspwriter.println("</tr>");
            if(!singleSelection && j == i)
            {
                jspwriter.print("<script>addOnloadHandler(function() { scrollIntoView('");
                jspwriter.print(Prefix.prependPrefix(s, "selectdTR"));
                jspwriter.print("'); },101)</script>");
            }
            jspwriter.print("<!-- FINE RIGA ");
            jspwriter.print(i);
            jspwriter.println(" -->");
            i++;
        }

        jspwriter.print("<tr height=\"100%\"><td colspan=\"");
        jspwriter.print(columns.size());
        jspwriter.println("\"></td></tr>");
        jspwriter.println("</tbody>");
        if(!singleSelection)
        {
            jspwriter.print("<input type=\"hidden\" name=\"");
            jspwriter.print(name);
            jspwriter.print(".focus");
            if(j >= 0)
            {
                jspwriter.print("\" value=\"");
                jspwriter.print(j);
            }
            jspwriter.println("\">");
        }
    }
    public void write(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne,Dictionary hiddenColumns) throws IOException{
		write(bp, jspwriter, fieldvalidationmap, i,nascondiColonne,null,null);
    }    
    public void write(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne) throws IOException{
		write(bp, jspwriter, fieldvalidationmap, i,nascondiColonne,null);
    }
	public void write(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i) throws IOException{
		write(bp, jspwriter, fieldvalidationmap, i,false);
    }
	

    public void write(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, Enumeration enumeration)
        throws IOException
    {
        setRows(enumeration);
        write(jspwriter, fieldvalidationmap);
    }

    public void write(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, Enumeration enumeration, int i)
        throws IOException
    {
        setRows(enumeration);
        write(jspwriter, fieldvalidationmap, i);
    }

    public void writeScrolledTable(Object bp, JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne,Dictionary hiddenColumns,String pathBP) throws IOException{
    	if(bp instanceof SelezionatoreListaBP)
    		jspwriter.print("<div style=\"background-color:white;border:thin groove;");
    	else
    		jspwriter.print("<div style=\"overflow:auto;background-color:white;border:thin groove;");
    		
        if(s1 != null)
        {
            jspwriter.print("height:");
            jspwriter.print(s1);
            jspwriter.print(';');
        }
        if(s != null)
        {
            jspwriter.print("width:");
            jspwriter.print(s);
            jspwriter.print(';');
        }
        jspwriter.println("\">");
        jspwriter.println("<!-- INIZIO TABLE -->");
        jspwriter.println("\t<table style=\"background:white;width:100%;height:100%\" cellpadding=\"0\" cellspacing=\"0\">");
        write(bp, jspwriter, fieldvalidationmap, i,nascondiColonne,hiddenColumns,pathBP);
        jspwriter.println("\t</table>");
        jspwriter.println("<!-- FINE TABLE -->");
        jspwriter.println("</div>");
    }    
    public void writeScrolledTable(Object bp, JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne,Dictionary hiddenColumns) throws IOException{
    	writeScrolledTable(bp, jspwriter, s, s1, fieldvalidationmap, i, nascondiColonne, null, null);
    }
    public void writeScrolledTable(Object bp, JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne) throws IOException{
    	writeScrolledTable(bp, jspwriter, s, s1, fieldvalidationmap, i, nascondiColonne, null);
    }
    
    public void writeScrolledTable(Object bp, JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i) throws IOException{
    	writeScrolledTable(bp, jspwriter, s, s1, fieldvalidationmap, i,false);
    }
	    
	public void writeScrolledTable(JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i)
		throws IOException
	{
		jspwriter.print("<div style=\"overflow:auto;background-color:white;border:thin groove;");
		if(s1 != null)
		{
			jspwriter.print("height:");
			jspwriter.print(s1);
			jspwriter.print(';');
		}
		if(s != null)
		{
			jspwriter.print("width:");
			jspwriter.print(s);
			jspwriter.print(';');
		}
		jspwriter.println("\">");
		jspwriter.println("<!-- INIZIO TABLE -->");
		jspwriter.println("\t<table style=\"background:white;width:100%;height:100%\" cellpadding=\"0\" cellspacing=\"0\">");
		write(jspwriter, fieldvalidationmap, i);
		jspwriter.println("\t</table>");
		jspwriter.println("<!-- FINE TABLE -->");
		jspwriter.println("</div>");
	}	
    private String name;
    private transient Enumeration rows;
    private Dictionary columns;
    private String onselect;
    private Selection selection;
    private boolean multiSelection;
    private boolean readonly;
    private Button checkAllButton;
    private int status;
    private boolean selectable;
    private boolean editableOnFocus;
    private Object selectedElement;
    private boolean singleSelection;
    private TableCustomizer customizer;
    private Orderable orderable;
    private String onsort;
    private String onHiddenColumn;
	public String getOnHiddenColumn() {
		return onHiddenColumn;
	}

	public void setOnHiddenColumn(String onHiddenColumn) {
		this.onHiddenColumn = onHiddenColumn;
	}    
}