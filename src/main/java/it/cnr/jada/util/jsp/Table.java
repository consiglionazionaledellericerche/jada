package it.cnr.jada.util.jsp;

import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.FieldValidationMap;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.Orderable;
import it.cnr.jada.util.Prefix;
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

    public void write(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, boolean isBootstrap)
        throws IOException
    {
        write(jspwriter, fieldvalidationmap, 0, isBootstrap);
    }
	public void writeTableWithoutColumnHeader(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean isBootstrap) throws IOException{
		writeTableWithoutColumnHeader(null, jspwriter, fieldvalidationmap, i, isBootstrap);		
	}
	public void writeTableWithoutColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean isBootstrap) throws IOException{
		writeTableWithoutColumnHeader(bp, jspwriter, fieldvalidationmap, i,false, isBootstrap);
	}
	public void writeTableWithoutColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,boolean nascondiColonne, boolean isBootstrap) throws IOException{
		writeTableWithoutColumnHeader(bp, jspwriter, fieldvalidationmap, i,false, null, isBootstrap);
	}
	public void writeTableWithoutColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,boolean nascondiColonne,Dictionary hiddenColumns, boolean isBootstrap) throws IOException{
		writeTableWithoutColumnHeader(bp, jspwriter, fieldvalidationmap, i,false, null, null, isBootstrap);
	}
	public void writeTableWithoutColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,boolean nascondiColonne,Dictionary hiddenColumns, String pathBP, boolean isBootstrap) throws IOException{
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
			writeButtonOnTableHeader(jspwriter, columnfieldproperty, nascondiColonne, isBootstrap);
			columnfieldproperty.writeLabel(bp, jspwriter, null, isBootstrap);
		}
		
	}
	public void writeTableWithColumnHeader(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,Hashtable labelHeader, boolean isBootstrap) throws IOException{
		writeTableWithColumnHeader(null, jspwriter, fieldvalidationmap, i, labelHeader, isBootstrap);
	}	
	public void writeTableWithColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,Hashtable labelHeader, boolean isBootstrap) throws IOException{
		writeTableWithColumnHeader(bp, jspwriter, fieldvalidationmap, i,labelHeader,false, isBootstrap);
	}
	public void writeTableWithColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,Hashtable labelHeader, boolean nascondiColonne, boolean isBootstrap) throws IOException{
		writeTableWithColumnHeader(bp, jspwriter, fieldvalidationmap, i,labelHeader,false, null, isBootstrap);
	}	
	
	public void writeTableWithColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i,Hashtable labelHeader, boolean nascondiColonne,Dictionary hiddenColumns, boolean isBootstrap) throws IOException{
		writeTableWithColumnHeader(bp, jspwriter, fieldvalidationmap, i,labelHeader,false, null, null, isBootstrap);
	}
	
	public void writeButtonOnTableHeader(JspWriter jspwriter, ColumnFieldProperty columnfieldproperty, boolean nascondiColonne, boolean isBootstrap) throws IOException {
		Button sort = new Button();
		sort.setHref( onsort + "('" + name + "','" + columnfieldproperty.getProperty() + "')");
		sort.setImg("img/sortable16.gif");
		sort.setStyle("vertical-align:middle;");
		sort.setIconClass("fa fa-exchange fa-rotate-90");
		sort.setButtonClass("btn-sm btn-link");

		Button sortasc = sort.clone();
		sortasc.setImg("img/sorted_asc16.gif");
		sortasc.setIconClass("fa fa-sort-alpha-asc");
		
		Button sortdesc = sort.clone();
		sortdesc.setImg("img/sorted_desc16.gif");
		sortdesc.setIconClass("fa fa-sort-alpha-desc");
		if (isBootstrap) {
			jspwriter.write("<div class=\"btn-group float-right\" role=\"group\">");
		}
			
		if(onsort != null && orderable != null && orderable.isOrderableBy(columnfieldproperty.getProperty()))
		{
			int k = orderable.getOrderBy(columnfieldproperty.getProperty());
			switch(k){
				case 0: // '\0'
					sort.write(jspwriter, true, isBootstrap);
					break;
	
				case 1: // '\001'
					sortasc.write(jspwriter, true, isBootstrap);
					break;
	
				case -1: 
					sortdesc.write(jspwriter, true, isBootstrap);
					break;
			}
		}
		if (nascondiColonne){
			if (!isBootstrap)
				jspwriter.print("&nbsp;");
			Button.write(jspwriter, 
					null, 
					isBootstrap ? "fa fa-angle-left" : "img/meno8.gif", 
					null, 
					1, 
					getOnHiddenColumn() + "('" + name + "','" + columnfieldproperty.getName() + "')", 
					isBootstrap ? "btn-sm btn-link" : "vertical-align:middle;",
					"Nascondi colonna ("+columnfieldproperty.getLabel()+")", 
					isBootstrap);
		}
		if (isBootstrap) {
			jspwriter.write("</div>");
		}		
	}
	
	public void writeTableWithColumnHeader(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, 
			Hashtable labelHeader, boolean nascondiColonne,Dictionary hiddenColumns,String pathBP, boolean isBootstrap) throws IOException{
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
				writeButtonOnTableHeader(jspwriter, columnfieldproperty, nascondiColonne, isBootstrap);
				columnfieldproperty.writeLabel(bp,jspwriter, null, isBootstrap);
			}else{
				if(labelHeader.containsKey(columnfieldproperty.getHeaderLabel())){
					jspwriter.print("<td");
					columnfieldproperty.writeHeaderStyle(jspwriter, null, "TableHeader text-primary font-weight-bold");
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
				writeButtonOnTableHeader(jspwriter, columnfieldproperty, nascondiColonne, isBootstrap);
				columnfieldproperty.writeLabel(bp, jspwriter, null, isBootstrap);
			}
		}		
	}
	public void write(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean isBootstrap)
		throws IOException
	{
		write(null, jspwriter, fieldvalidationmap, i, isBootstrap);
	}
	
    public void write(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne, 
    		Dictionary hiddenColumns,String pathBP, boolean isBootstrap) throws IOException{
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
        jspwriter.println("<thead class=\"table-info\">");
        jspwriter.println("<tr valign=\"top\">");
        if(selectable && multiSelection)
        {
			if(!presenteHeader)
              jspwriter.print("<td class=\"TableHeader\" align=\"center\" valign=\"center\">");
            else
			  jspwriter.print("<td rowspan=2 class=\"TableHeader\" align=\"center\" valign=\"center\">");

	        checkAllButton.write(jspwriter, true, isBootstrap);				
            jspwriter.print("</td>");
        }
        if(selectable && singleSelection)
            jspwriter.print("<td class=\"TableHeader\" align=\"center\" valign=\"center\">&nbsp;</td>");
		if(!presenteHeader)			    
		  writeTableWithoutColumnHeader(bp,jspwriter, fieldvalidationmap, i, nascondiColonne,hiddenColumns,pathBP, isBootstrap);
		else
		  writeTableWithColumnHeader(bp,jspwriter, fieldvalidationmap, i, labelHeader, nascondiColonne,hiddenColumns,pathBP, isBootstrap);  
		
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
                jspwriter.print((resto == 0?"SelectedTableRow bg-primary text-white":"SelectedTableRowPar bg-primary text-white"));
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
                    jspwriter.print("\" ");
                    if (onselect != null){
                    	jspwriter.print(" onclick=\"");
                    	jspwriter.print(onselect);
                        jspwriter.print("('");
                        jspwriter.print(name);
                        jspwriter.print("',");
                        jspwriter.print(i);
                        jspwriter.print(");\"");
                    }else{
                    	jspwriter.print("onclick=\"cancelBubble(event)\" ");
                    }
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
                columnfieldproperty1.writeInput(jspwriter, obj, flag1, null, null, s, l, fieldvalidationmap, isBootstrap);
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
        if (!isBootstrap) {
            jspwriter.print("<tr height=\"100%\"><td colspan=\"");
            jspwriter.print(columns.size());
            jspwriter.println("\"></td></tr>");        	
        }
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
    public void write(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne,Dictionary hiddenColumns, boolean isBootstrap) throws IOException{
		write(bp, jspwriter, fieldvalidationmap, i, nascondiColonne, null, null, isBootstrap);
    }    
    public void write(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne, boolean isBootstrap) throws IOException{
		write(bp, jspwriter, fieldvalidationmap, i,nascondiColonne,null, isBootstrap);
    }
	public void write(Object bp, JspWriter jspwriter, FieldValidationMap fieldvalidationmap, int i, boolean isBootstrap) throws IOException{
		write(bp, jspwriter, fieldvalidationmap, i,false, isBootstrap);
    }
	

    public void write(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, Enumeration enumeration, boolean isBootstrap)
        throws IOException
    {
        setRows(enumeration);
        write(jspwriter, fieldvalidationmap, isBootstrap);
    }

    public void write(JspWriter jspwriter, FieldValidationMap fieldvalidationmap, Enumeration enumeration, int i, boolean isBootstrap)
        throws IOException
    {
        setRows(enumeration);
        write(jspwriter, fieldvalidationmap, i, isBootstrap);
    }

    public void writeScrolledTable(Object bp, JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne,
    			Dictionary hiddenColumns,String pathBP, boolean isBootstrap) throws IOException{
    	if (!isBootstrap) {
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
	        write(bp, jspwriter, fieldvalidationmap, i,nascondiColonne,hiddenColumns,pathBP,isBootstrap);
	        jspwriter.println("\t</table>");
	        jspwriter.println("<!-- FINE TABLE -->");
            jspwriter.println("</div>");    		
    	} else {
    		jspwriter.print("<div class=\"div-sigla-table table-responsive ");
	        if(s1 != null && !s1.equalsIgnoreCase("100%")) {
	        	jspwriter.print("\" style=\"height:");
	            jspwriter.print(s1);
	            jspwriter.print(";\"");
	        } else {
	        	jspwriter.print("col-sm-12\"");
	        }
	        jspwriter.println(">");
    		
		    jspwriter.println("<!-- INIZIO TABLE -->");
	        jspwriter.println("<table class=\"sigla-table table table-bordered table-hover table-striped table-sm\">");
	        write(bp, jspwriter, fieldvalidationmap, i,nascondiColonne,hiddenColumns,pathBP, isBootstrap);
	        jspwriter.println("</table>");
	        jspwriter.println("<!-- FINE TABLE -->"); 
            jspwriter.println("</div>");    		

    	}
    }    
    public void writeScrolledTable(Object bp, JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne,Dictionary hiddenColumns, boolean isBootstrap) throws IOException{
    	writeScrolledTable(bp, jspwriter, s, s1, fieldvalidationmap, i, nascondiColonne, null, null, isBootstrap);
    }
    public void writeScrolledTable(Object bp, JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i, boolean nascondiColonne, boolean isBootstrap) throws IOException{
    	writeScrolledTable(bp, jspwriter, s, s1, fieldvalidationmap, i, nascondiColonne, null, isBootstrap);
    }
    
    public void writeScrolledTable(Object bp, JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i, boolean isBootstrap) throws IOException{
    	writeScrolledTable(bp, jspwriter, s, s1, fieldvalidationmap, i,false, isBootstrap);
    }
	    
	public void writeScrolledTable(JspWriter jspwriter, String s, String s1, FieldValidationMap fieldvalidationmap, int i, boolean isBootstrap)
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
		write(jspwriter, fieldvalidationmap, i, isBootstrap);
		jspwriter.println("\t</table>");
		jspwriter.println("<!-- FINE TABLE -->");
		jspwriter.println("</div>");
	}	

	public String getOnHiddenColumn() {
		return onHiddenColumn;
	}

	public void setOnHiddenColumn(String onHiddenColumn) {
		this.onHiddenColumn = onHiddenColumn;
	}    
}