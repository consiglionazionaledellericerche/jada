package it.cnr.jada.util;

import java.io.*;
import java.util.Stack;

// Referenced classes of package it.cnr.jada.util:
//            XmlWriteable

public class XmlWriter extends PrintWriter
    implements Serializable
{

    public XmlWriter(OutputStream outputstream)
    {
        super(outputstream);
        tags = new Stack();
        startTagClosed = true;
        inline = true;
        tagHasAttributes = false;
    }

    public XmlWriter(OutputStream outputstream, boolean flag)
    {
        super(outputstream, flag);
        tags = new Stack();
        startTagClosed = true;
        inline = true;
        tagHasAttributes = false;
    }

    public XmlWriter(Writer writer)
    {
        super(writer);
        tags = new Stack();
        startTagClosed = true;
        inline = true;
        tagHasAttributes = false;
    }

    public XmlWriter(Writer writer, boolean flag)
    {
        super(writer, flag);
        tags = new Stack();
        startTagClosed = true;
        inline = true;
        tagHasAttributes = false;
    }

    public void closeLastTag()
        throws IOException
    {
        String s = (String)tags.pop();
        level--;
        if(startTagClosed)
        {
            printLevelTabs();
            print("</");
            print(s);
            print(">\n");
        } else
        {
            closeStartTag(true);
        }
        startTagClosed = true;
    }

    public void closeStartTag(boolean flag)
    {
        if(startTagClosed)
            return;
        startTagClosed = true;
        if(tagHasAttributes)
            print(' ');
        if(flag)
            print('/');
        print(">\n");
    }

    public void openInlineTag(String s)
        throws IOException
    {
        openTag(s);
        inline = true;
    }

    public void openTag(String s)
        throws IOException
    {
        closeStartTag(false);
        printLevelTabs();
        print('<');
        print(s);
        tags.push(s);
        startTagClosed = false;
        inline = false;
        tagHasAttributes = false;
        level++;
    }

    public void printAttribute(String s, int i, int j)
    {
        if(i == j)
        {
            return;
        } else
        {
            printAttributeName(s);
            print("=\"");
            print(i);
            print("\"");
            return;
        }
    }

    public void printAttribute(String s, String s1, String s2)
    {
        if(s1 == null)
            return;
        if(s1.equals(s2))
        {
            return;
        } else
        {
            printAttributeName(s);
            print("=\"");
            print(s1);
            print("\"");
            return;
        }
    }

    public void printAttribute(String s, boolean flag, boolean flag1)
    {
        if(flag == flag1)
        {
            return;
        } else
        {
            printAttributeName(s);
            print("=\"");
            print(flag);
            print("\"");
            return;
        }
    }

    public void printAttributeName(String s)
    {
        tagHasAttributes = true;
        if(inline)
        {
            print(' ');
        } else
        {
            print('\n');
            printLevelTabs();
        }
        print(s);
    }

    protected void printLevelTabs()
    {
        for(int i = 0; i < level; i++)
            print('\t');

    }

    public static String toString(XmlWriteable xmlwriteable)
    {
        try
        {
            StringWriter stringwriter = new StringWriter();
            XmlWriter xmlwriter = new XmlWriter(stringwriter);
            xmlwriteable.writeTo(xmlwriter);
            xmlwriter.close();
            return stringwriter.getBuffer().toString();
        }
        catch(IOException _ex)
        {
            return null;
        }
    }

    protected int level;
    protected Stack tags;
    protected boolean startTagClosed;
    protected boolean inline;
    protected boolean tagHasAttributes;
}