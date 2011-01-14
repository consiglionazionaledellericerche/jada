package it.cnr.jada.util.jsp;

import it.cnr.jada.util.Introspector;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import javax.servlet.jsp.JspWriter;

public class Button
	implements Serializable
{
	public static final int TOP = 0;
	public static final int BOTTOM = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	private String accessKey;
	private String img;
	private String disabledImg;
	private String label;
	private String style;
	private String href;
	private String enabledProperty;
	private String hiddenProperty;
	private String name;
	private boolean separator;
	private String title;
	private int labelPosition;
	public Button()
	{
		labelPosition = 1;
	}

	public Button(Properties properties, String s)
	{
		labelPosition = 1;
		img = properties.getProperty(s + ".img");
		disabledImg = properties.getProperty(s + ".disabledImg");
		label = properties.getProperty(s + ".label");
		href = properties.getProperty(s + ".href");
		style = properties.getProperty(s + ".style");
		enabledProperty = properties.getProperty(s + ".enabledProperty");
		hiddenProperty = properties.getProperty(s + ".hiddenProperty");
		title = properties.getProperty(s + ".title");
		accessKey = properties.getProperty(s + ".accessKey");
		String s1 = properties.getProperty(s + ".labelPosition");
		if(s1 == null || "bottom".equalsIgnoreCase(s1))
			labelPosition = 1;
		else
		if("top".equalsIgnoreCase(s1))
			labelPosition = 0;
		else
		if("left".equalsIgnoreCase(s1))
			labelPosition = 2;
		else
		if("right".equalsIgnoreCase(s1))
			labelPosition = 3;
		String s2 = properties.getProperty(s + ".separator");
		separator = s2 != null ? Boolean.valueOf(s2).booleanValue() : false;
	}

	public String getDisabledImg()
	{
		return disabledImg;
	}

	public String getEnabledProperty()
	{
		return enabledProperty;
	}

	public String getHiddenProperty()
	{
		return hiddenProperty;
	}

	public String getHref()
	{
		return href;
	}

	public String getImg()
	{
		return img;
	}

	public String getLabel()
	{
		return label;
	}

	public int getLabelPosition()
	{
		return labelPosition;
	}

	public String getName()
	{
		return name;
	}

	public String getStyle()
	{
		return style;
	}

	public String getTitle()
	{
		return title;
	}

	public boolean hasSeparator()
	{
		return separator;
	}

	public boolean isHidden(Object obj)
	{
		return Introspector.getBoolean(obj, hiddenProperty, false);
	}

	public void setDisabledImg(String s)
	{
		disabledImg = s;
	}

	public void setEnabledProperty(String s)
	{
		enabledProperty = s;
	}

	public void setHiddenProperty(String s)
	{
		hiddenProperty = s;
	}

	public void setHref(String s)
	{
		href = s;
	}

	public void setImg(String s)
	{
		img = s;
	}

	public void setLabel(String s)
	{
		label = s;
	}

	public void setLabelPosition(int i)
	{
		labelPosition = i;
	}

	public void setName(String s)
	{
		name = s;
	}

	public void setSeparator(boolean flag)
	{
		separator = flag;
	}

	public void setStyle(String s)
	{
		style = s;
	}

	public void setTitle(String s)
	{
		title = s;
	}

	public void write(JspWriter jspwriter, Object obj)
		throws IOException
	{
		if(!Introspector.getBoolean(obj, hiddenProperty, false))
			write(jspwriter, img, disabledImg, label, labelPosition, href, style, title, enabledProperty != null ? Introspector.getBoolean(obj, enabledProperty, false) : true, accessKey);
	}

	public static void write(JspWriter jspwriter, String s, String s1)
		throws IOException
	{
		write(jspwriter, s, null, s1);
	}

	public static void write(JspWriter jspwriter, String s, String s1, String s2)
		throws IOException
	{
		write(jspwriter, s, s1, s2, null);
	}

	public static void write(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5)
		throws IOException
	{
		write(jspwriter, s, s1, s2, i, s3, s4, s5, ((String) (null)));
	}
	public static void writeWhithAccessKey(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, String accessKey)
		throws IOException
	{
		writeWhithAccessKey(jspwriter, s, s1, s2, i, s3, s4, s5, ((String) (null)), accessKey);
	}
	public static void write(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, 
			String s6)
		throws IOException
	{
		write(jspwriter, s, s1, s1 != null, s2, i, s3, s4, s5, s6);
	}
	public static void writeWhithAccessKey(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, 
			String s6, String accessKey)
		throws IOException
	{
		write(jspwriter, s, s1, s1 != null, s2, i, s3, s4, s5, s6, accessKey);
	}
	public static void write(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, 
			String s6, boolean flag)
		throws IOException
	{
		if(flag)
			write(jspwriter, null, s, s2, i, s3, s4, s5, s6);
		else
			write(jspwriter, null, s1, s2, i, null, s4, s5, s6);
	}

	public static void write(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, 
			boolean flag)
		throws IOException
	{
		if(flag)
			write(jspwriter, null, s, s2, i, s3, s4, s5);
		else
			write(jspwriter, null, s1, s2, i, null, s4, s5);
	}

	public static void writeWithAccessKey(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, String s6, String accesskey)
		throws IOException
	{
		write(jspwriter, s, s1, s1 != null, s2, i, s3, s4, s5, s6, accesskey);
	}
		
	public static void writeWithAccessKey(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, String accesskey)
		throws IOException
	{
		writeWithAccessKey(jspwriter, s, s1, s2, i, s3, s4, s5, ((String) (null)), accesskey);
	}	
	public static void write(JspWriter jspwriter, String s, String s1, String s2, int i, String s3, String s4, String s5, 
			boolean flag, String accesskey)
		throws IOException
	{
		if(flag)
			writeWithAccessKey(jspwriter, null, s, s2, i, s3, s4, s5, accesskey);
		else
			writeWithAccessKey(jspwriter, null, s1, s2, i, null, s4, s5, accesskey);
	}
	public static void write(JspWriter jspwriter, String s, String s1, String s2, String s3)
		throws IOException
	{
		write(jspwriter, null, s, s1, 1, s2, s3, ((String) (null)));
	}
	public static void writeWhithAccessKey(JspWriter jspwriter, String s, String s1, String s2, String s3, String accessKey)
		throws IOException
	{
		writeWhithAccessKey(jspwriter, null, s, s1, 1, s2, s3, ((String) (null)), accessKey);
	}
	public static void write(JspWriter jspwriter, String s, String s1, String s2, String s3, String s4)
		throws IOException
	{
		write(jspwriter, s, s1, s2, 1, s3, s4, ((String) (null)));
	}

	public static void write(JspWriter jspwriter, String s, String s1, String s2, String s3, String s4, String s5, boolean flag)
		throws IOException
	{
		if(flag)
			write(jspwriter, null, s, s2, 1, s3, s4, s5);
		else
			write(jspwriter, null, s1, s2, 1, null, s4, s5);
	}

	public static void write(JspWriter jspwriter, String s, String s1, String s2, String s3, String s4, boolean flag)
		throws IOException
	{
		if(flag)
			write(jspwriter, s, s2, s3, s4);
		else
			write(jspwriter, s1, s2, null, s4);
	}

	public static void write(JspWriter jspwriter, String s, String s1, String s2, String s3, boolean flag)
		throws IOException
	{
		if(flag)
			write(jspwriter, s, s2, s3);
		else
			write(jspwriter, s1, s2, null);
	}

	/* Metodo inserito da Marco Spasiano per gestire l'accessKey*/
	public static void write(JspWriter jspwriter, String s, String s1, boolean flag, String s2, int i, String s3, String s4, 
			String s5, String s6, String s7)
		throws IOException
	{
		jspwriter.print("<button");
		if(flag){
			if(s3 == null)
			  jspwriter.print(" class=\"DisabledButton\"");
			else
			  jspwriter.print(" class=\"Button\"");
		}
		if(s3 == null)
			jspwriter.print(" disabled");
		if(s7 != null)
		{
			jspwriter.print(" accessKey=\"");
			jspwriter.print(s7);
			jspwriter.print('"');
		}		
		if(s != null)
		{
			jspwriter.print(" name=\"");
			jspwriter.print(s);
			jspwriter.print('"');
		}
		if(s3 != null && s3.startsWith("javascript:"))
			s3 = s3.substring(11);
		if(s4 != null)
		{
			jspwriter.print(" style=\"");
			jspwriter.print(s4);
			jspwriter.print('"');
		}
		if(s3 != null)
		{
			jspwriter.print(" onclick=\"cancelBubble(event); if (disableDblClick()) ");
			jspwriter.print(s3);
			jspwriter.print("; return false\"");
		}
		if(flag)
			jspwriter.print(" onMouseOver=\"mouseOver(this)\" onMouseOut=\"mouseOut(this)\" onMouseDown=\"mouseDown(this)\" onMouseUp=\"mouseUp(this)\"");
		if(s5 != null)
		{
			jspwriter.print(" title=\"");
			jspwriter.print(s5);
			jspwriter.print('"');
		}
		if(s6 != null)
		{
			jspwriter.print(' ');
			jspwriter.print(s6);
		}
		jspwriter.print(">");
		if(s1 != null)
		{
			jspwriter.print("<img align=\"middle\" class=\"Button\" src=\"");
			jspwriter.print(s1);
			jspwriter.print("\">");
			if(s2 != null)
				switch(i)
				{
				case 2: // '\002'
					jspwriter.print(" ");
					break;

				case 3: // '\003'
					jspwriter.print(" ");
					break;

				case 1: // '\001'
					jspwriter.print("<br>");
					break;

				default:
					jspwriter.print("<br>");
					break;

				case 0: // '\0'
					break;
				}
		}
		if(s2 != null)
			jspwriter.print(s2);
		jspwriter.print("</button>");

	}
	public static void write(JspWriter jspwriter, String s, String s1, boolean flag, String s2, int i, String s3, String s4, 
			String s5, String s6)
		throws IOException
	{
		write(jspwriter, s, s1, flag, s2, i, s3, s4, s5, s6, null);
	}

	public void write(JspWriter jspwriter, boolean flag)
		throws IOException
	{
		write(jspwriter, img, disabledImg, label, labelPosition, href, style, title, flag);
	}

	public void write(JspWriter jspwriter, boolean flag, String s)
		throws IOException
	{
		write(jspwriter, img, disabledImg, label, labelPosition, s, style, title, flag);
	}

	public void write(JspWriter jspwriter, boolean flag, String s, String s1)
		throws IOException
	{
		write(jspwriter, img, disabledImg, label, labelPosition, s, style, title, s1, flag);
	}

	public void writeToolbarButton(JspWriter jspwriter, boolean flag)
		throws IOException
	{
		jspwriter.print("<td");
		if(separator)
			jspwriter.print(" class=\"VSeparator\"");
		jspwriter.print(">");
		write(jspwriter, img, disabledImg, label, labelPosition, href, style, title, flag);
		jspwriter.print("</td>");
	}

	public void writeWithoutRollover(JspWriter jspwriter, boolean flag)
		throws IOException
	{
		write(jspwriter, null, flag ? img : disabledImg, false, label, labelPosition, flag ? href : null, style, title, ((String) (null)), accessKey);
	}

	/**
	 * @return
	 */
	public String getAccessKey()
	{
		return accessKey;
	}

	/**
	 * @param string
	 */
	public void setAccessKey(String string)
	{
		accessKey = string;
	}

}