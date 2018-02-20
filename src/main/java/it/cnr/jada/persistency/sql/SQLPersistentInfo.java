package it.cnr.jada.persistency.sql;

import it.cnr.jada.persistency.*;
import it.cnr.jada.util.XMLObjectFiller;
import it.cnr.jada.util.XmlWriter;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// Referenced classes of package it.cnr.jada.persistency.sql:
//            ColumnMap, ColumnMapping

public class SQLPersistentInfo extends SimplePersistentInfo
    implements Serializable
{

    protected SQLPersistentInfo()
    {
        columnMaps = new HashMap();
    }

    public SQLPersistentInfo(Class class1, Introspector introspector)
        throws IntrospectionException
    {   	
        this(class1, introspector,  "/" + class1.getPackage().getName().replace('.', '/') + "/" + class1.getSimpleName() + "PersistentInfo.xml");
    }

    public SQLPersistentInfo(Class class1, Introspector introspector, String inputstream)
        throws IntrospectionException
    {
        super(class1, introspector);
        InputStream inputStream = null;
        try {
    		URL resurl = this.getClass().getResource(inputstream);
    		if (resurl!=null)
    			inputStream = resurl.openStream();
		} catch (IOException e1) {
		}
        if(columnMaps == null)
            columnMaps = new HashMap();
        try
        {
            if(inputStream != null){
				String encoding = System.getProperty("SIGLA_ENCODING","UTF-8");
				readInfo(new InputStreamReader(inputStream,encoding));
            }                
        }
        catch(SAXException saxexception)
        {
            throw new IntrospectionException("Errore XML nella definizione di un SQLPersistentInfo", saxexception);
        }
        catch(IOException ioexception)
        {
            throw new IntrospectionException("Errore di lettura nella definizione di un SQLPersistentInfo", ioexception);
        } catch (ParserConfigurationException e)
        {
			throw new IntrospectionException("Errore XML nella definizione di un SQLPersistentInfo", e);
        }
    }

    public SQLPersistentInfo(Class class1, Introspector introspector, Reader reader)
        throws IntrospectionException
    {
        super(class1, introspector);
        if(columnMaps == null)
            columnMaps = new HashMap();
        try
        {
            if(reader != null)
                readInfo(reader);
        }
        catch(SAXException saxexception)
        {
            throw new IntrospectionException("Errore XML nella definizione di un SQLPersistentInfo", saxexception);
        }
        catch(IOException ioexception)
        {
            throw new IntrospectionException("Errore di lettura nella definizione di un SQLPersistentInfo", ioexception);
        } catch (ParserConfigurationException e)
        {
			throw new IntrospectionException("Errore XML nella definizione di un SQLPersistentInfo", e);
        }
    }

    public void addColumnMap(ColumnMap columnmap)
    {
        ColumnMap columnmap1 = (ColumnMap)columnMaps.get(columnmap.getExtends());
        if(columnmap1 != null)
        {
            columnmap.addNewColumnMappings(columnmap1);
            if(columnmap.getTableName() == null)
                columnmap.setTableName(columnmap1.getTableName());
        }
        columnMaps.put(columnmap.getName(), columnmap);
        columnmap.setPersistentInfo(this);
    }

    public void addDefaultColumnMap(ColumnMap columnmap)
    {
        columnmap.setName("default");
        addColumnMap(columnmap);
    }

    public void clear()
    {
        super.clear();
        columnMaps.clear();
    }

    public ColumnMap getColumnMap(String s)
    {
        if(s == null)
            return getDefaultColumnMap();
        else
            return (ColumnMap)columnMaps.get(s);
    }

	public HashMap getColumnMaps()
	{
		return columnMaps;
	}

    public ColumnMap getDefaultColumnMap()
    {
        return (ColumnMap)columnMaps.get("default");
    }

    public Class getHomeClass()
    {
        return homeClass;
    }

    public String getHomeClassName()
    {
        if(homeClass == null)
            return null;
        else
            return homeClass.getName();
    }

    public String getPersistentClassName()
    {
        if(getPersistentClass() == null)
            return null;
        else
            return getPersistentClass().getName();
    }

    public void initialize()
        throws IntrospectionException
    {
        removeDuplicateProperties();
    }

    protected void initializeFrom(PersistentInfo persistentinfo)
    {
        super.initializeFrom(persistentinfo);
        columnMaps = new HashMap();
        columnMaps.putAll(((SQLPersistentInfo)persistentinfo).columnMaps);
        homeClass = ((SQLPersistentInfo)persistentinfo).homeClass;
    }

    private void readInfo(Reader reader)
        throws IOException, ParserConfigurationException, SAXException
    {
        XMLObjectFiller xmlobjectfiller = new XMLObjectFiller(this);
        xmlobjectfiller.mapElementToClass("SQLPersistentInfo", it.cnr.jada.persistency.sql.SQLPersistentInfo.class);
        xmlobjectfiller.mapElement("columnMap", it.cnr.jada.persistency.sql.ColumnMap.class, "addColumnMap");
        xmlobjectfiller.mapElement("columnMapping", it.cnr.jada.persistency.sql.ColumnMapping.class, "addColumnMapping");
		xmlobjectfiller.mapElement("removeColumnMapping", it.cnr.jada.persistency.sql.RemoveColumnMapping.class, "removeColumnMapping");
		xmlobjectfiller.mapElement("addColumnMapping", it.cnr.jada.persistency.sql.AddColumnMapping.class, "addNewColumnMapping");		
        xmlobjectfiller.mapElement("defaultColumnMap", it.cnr.jada.persistency.sql.ColumnMap.class, "addDefaultColumnMap");
        xmlobjectfiller.mapElement("persistentProperty", it.cnr.jada.persistency.PersistentProperty.class, "addPersistentProperty");
        xmlobjectfiller.mapElement("fetchPolicy", it.cnr.jada.persistency.SimpleFetchPolicy.class, "addFetchPolicy");
        xmlobjectfiller.mapElement("exclude", it.cnr.jada.persistency.ExcludeFetchPattern.class, "addPattern");
        xmlobjectfiller.mapElement("include", it.cnr.jada.persistency.IncludeFetchPattern.class, "addPattern");
        xmlobjectfiller.parse(new InputSource(reader));
    }

    private void removeDuplicatePersistentProperty(Map map, PersistentProperty persistentproperty, String s)
        throws IntrospectionException
    {
        String s1 = Prefix.prependPrefix(s, persistentproperty.getName());
        Class class1 = getIntrospector().getPropertyType(getPersistentClass(), s1);
        if(it.cnr.jada.persistency.KeyedPersistent.class.isAssignableFrom(class1))
        {
            PersistentInfo persistentinfo = getIntrospector().getPersistentInfo(class1);
            for(Iterator iterator = persistentinfo.getOidPersistentProperties().values().iterator(); iterator.hasNext(); removeDuplicatePersistentProperty(map, (PersistentProperty)iterator.next(), s1));
        } else
        if(s == null)
        {
            ColumnMapping columnmapping = getDefaultColumnMap().getMappingForProperty(s1);
            if(columnmapping != null)
            {
                if(!columnmapping.isFetchOnly() && map.get(columnmapping.getColumnName()) != null)
                    removePersistentProperty(s1);
                map.put(columnmapping.getColumnName(), s1);
            }
        } else
        if(persistentproperty.isPartOfOid())
        {
            ColumnMapping columnmapping1 = getDefaultColumnMap().getMappingForProperty(s1);
            if(columnmapping1 != null && !columnmapping1.isFetchOnly())
            {
                String s2 = (String)map.put(columnmapping1.getColumnName(), s1);
                if(s2 != null)
                    removePersistentProperty(s2);
            }
        }
    }

    private void removeDuplicateProperties()
        throws IntrospectionException
    {
        HashMap hashmap = new HashMap(getPersistentProperties().size());
        ArrayList arraylist = new ArrayList(getPersistentProperties().values());
        PersistentProperty persistentproperty;
        for(Iterator iterator = arraylist.iterator(); iterator.hasNext(); removeDuplicatePersistentProperty(hashmap, persistentproperty, null))
            persistentproperty = (PersistentProperty)iterator.next();

    }

	public void setColumnMaps(HashMap hashmap)
	{
		columnMaps = hashmap;
	}

    public void setHomeClass(Class class1)
    {
        homeClass = class1;
    }

    public void setHomeClassName(String s)
    {
        try
        {
            homeClass = getClass().getClassLoader().loadClass(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new IntrospectionError("Home class not found", classnotfoundexception);
        }
    }

    public void setPersistentClassName(String s)
    {
    }

    public String toString()
    {
        StringWriter stringwriter = new StringWriter();
        XmlWriter xmlwriter = new XmlWriter(stringwriter);
        try
        {
            writeTo(xmlwriter);
            xmlwriter.close();
        }
        catch(IOException ioexception)
        {
            return ioexception.toString();
        }
        return stringwriter.getBuffer().toString();
    }

    public void writeTo(XmlWriter xmlwriter)
        throws IOException
    {
        xmlwriter.openTag("SQLPersistentInfo");
        xmlwriter.printAttribute("persistentClassName", getPersistentClassName(), null);
        xmlwriter.printAttribute("keyClassName", getKeyClassName(), null);
        xmlwriter.printAttribute("homeClassName", getHomeClassName(), null);
        for(Iterator iterator = columnMaps.values().iterator(); iterator.hasNext(); ((ColumnMap)iterator.next()).writeTo(xmlwriter));
        for(Iterator iterator1 = getOidPersistentProperties().values().iterator(); iterator1.hasNext(); ((PersistentProperty)iterator1.next()).writeTo(xmlwriter));
        for(Iterator iterator2 = getNotInOidPersistentProperties().values().iterator(); iterator2.hasNext(); ((PersistentProperty)iterator2.next()).writeTo(xmlwriter));
        xmlwriter.closeLastTag();
    }

    private HashMap columnMaps;
    private Class homeClass;
}