package it.cnr.jada.bulk;

import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.annotation.FormatName;
import it.cnr.jada.bulk.annotation.InputType;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.BeanIntrospector;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.CRUDBP;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class OggettoBulk implements Cloneable, FetchListener, PersistencyListener, Serializable{
	@JsonIgnore
	@it.cnr.jada.bulk.annotation.FieldPropertyAnnotation(name="duva",
			inputType=InputType.ROTEXT,
			formatName=FormatName.timestamp,
			nullable=false,
			label="Data ultimo aggiornamento")
	protected Timestamp duva;
	@JsonIgnore
	@it.cnr.jada.bulk.annotation.FieldPropertyAnnotation(name="dacr",
			inputType=InputType.ROTEXT,
			formatName=FormatName.timestamp,
			nullable=false,
			label="Data di creazione")
    protected Timestamp dacr;
	@JsonIgnore
	@it.cnr.jada.bulk.annotation.FieldPropertyAnnotation(name="utuv",
			inputType=InputType.ROTEXT,
			inputSize=20,
			maxLength=20,
			nullable=false,
			label="Utente ultimo aggiornamento")
    protected String utuv;
	@JsonIgnore
	protected String user;
	@JsonIgnore
	@it.cnr.jada.bulk.annotation.FieldPropertyAnnotation(name="utcr",
			inputType=InputType.ROTEXT,
			inputSize=20,
			maxLength=20,
			nullable=false,
			label="Utente di creazione")
	protected String utcr;
	@JsonIgnore
	@it.cnr.jada.bulk.annotation.FieldPropertyAnnotation(name="pg_ver_rec",
			inputType=InputType.ROTEXT,
			inputSize=20,
			maxLength=20,
			nullable=false,
			label="Progressivo versione Record")
	protected Long pg_ver_rec;
	@JsonIgnore
	protected boolean operabile;
	@JsonIgnore
	private int crudStatus;
    public static final int UNDEFINED = 0;
    public static final int TO_BE_CREATED = 1;
    public static final int TO_BE_UPDATED = 2;
    public static final int TO_BE_DELETED = 3;
    public static final int TO_BE_CHECKED = 4;
    public static final int NORMAL = 5;
    private static final long serialVersionUID = 0x93cc4a6eb3975ff8L;
    @JsonIgnore
    private KeyedPersistent key;

    public OggettoBulk(){
        operabile = true;
        crudStatus = 0;
    }

    public CompoundFindClause buildFindClauses(Boolean boolean1)
    {
        return getBulkInfo().buildFindClausesFrom(this, boolean1);
    }

    protected int calculateKeyHashCode(Object obj)
    {
        if(obj == null)
            return 0;
        else
            return obj.hashCode();
    }

    public Object clone()
    {
        try
        {
            return super.clone();
        }
        catch(CloneNotSupportedException _ex)
        {
            return null;
        }
    }

    protected boolean compareKey(Object obj, Object obj1)
    {
        if(obj != null)
            return obj.equals(obj1);
        return obj1 == null;
    }

    public void deletedUsing(Persister persister, UserContext userContext)
    {
        crudStatus = 0;
    }

    public void deletingUsing(Persister persister, UserContext userContext)
    {
    }

    public boolean equalsByPrimaryKey(Object obj)
    {
        return equals(obj);
    }

    public void fetchedFrom(Broker broker)
        throws IntrospectionException, FetchException
    {
        crudStatus = 5;
    }

    public boolean fillFromActionContext(ActionContext actioncontext, String s, int i, FieldValidationMap fieldvalidationmap)
        throws FillException
    {
        if(actioncontext instanceof HttpActionContext)
            return fillFromHttpRequest(((HttpActionContext)actioncontext).getRequest(), s, i, fieldvalidationmap);
        else
            return false;
    }

    public boolean fillFromHttpRequest(HttpServletRequest httpservletrequest, String s, int i, FieldValidationMap fieldvalidationmap)
        throws FillException
    {
        return fillFromHttpRequest(((ServletRequest) (httpservletrequest)), null, s, i, fieldvalidationmap);
    }

    public boolean fillFromHttpRequest(ServletRequest servletrequest, String s, int i, FieldValidationMap fieldvalidationmap)
        throws FillException
    {
        return fillFromHttpRequest(servletrequest, null, s, i, fieldvalidationmap);
    }

    public boolean fillFromHttpRequest(ServletRequest servletrequest, String s, String s1, int i, FieldValidationMap fieldvalidationmap)
        throws FillException
    {
        boolean flag = false;
        FillException fillexception = null;
        int j = 0;
        for(Enumeration enumeration = getBulkInfo().getFormFieldProperties(s); enumeration.hasMoreElements();)
            try
            {
                if(fillPropertyFromHttpRequest(servletrequest, (FieldProperty)enumeration.nextElement(), s1, i, fieldvalidationmap))
                    flag = true;
            }
            catch(FillException fillexception1)
            {
                fillexception = fillexception1;
                j++;
            }

        if(fillexception != null)
        {
            if(j > 1)
                throw new FillException("Errori su pi\371 campi");
            else
                throw fillexception;
        } else
        {
            return flag;
        }
    }

    public boolean fillPropertyFromHttpRequest(ServletRequest servletrequest, FieldProperty fieldproperty, String s, int i, FieldValidationMap fieldvalidationmap)
        throws FillException
    {
        if(fieldproperty.fillBulkFromRequest(this, servletrequest, s, i, fieldvalidationmap))
        {
            setToBeUpdated();
            return true;
        } else
        {
            return false;
        }
    }
    @JsonIgnore
    public BulkInfo getBulkInfo()
    {
        return BulkInfo.getBulkInfo(getClass());
    }
    @JsonIgnore
    public BulkCollection[] getBulkLists()
    {
        return null;
    }
    @JsonIgnore
    public OggettoBulk[] getBulksForPersistentcy()
    {
        return null;
    }

    public int getCrudStatus()
    {
        return crudStatus;
    }

    public Timestamp getDacr()
    {
        return dacr;
    }

    public Timestamp getDuva()
    {
        return duva;
    }

    public KeyedPersistent getKey()
    {
        return key;
    }

    public Long getPg_ver_rec()
    {
        return pg_ver_rec;
    }

    public String getUser()
    {
        return user;
    }

    public String getUtcr()
    {
        return utcr;
    }

    public String getUtuv()
    {
        return utuv;
    }

    protected OggettoBulk initialize(CRUDBP crudbp, ActionContext actioncontext)
    {
        return this;
    }

    public OggettoBulk initializeForEdit(CRUDBP crudbp, ActionContext actioncontext)
    {
        return this;
    }

    public OggettoBulk initializeForFreeSearch(CRUDBP crudbp, ActionContext actioncontext)
    {
        return initializeForSearch(crudbp, actioncontext);
    }

    public OggettoBulk initializeForInsert(CRUDBP crudbp, ActionContext actioncontext)
    {
        return initialize(crudbp, actioncontext);
    }

    public OggettoBulk initializeForSearch(CRUDBP crudbp, ActionContext actioncontext)
    {
        return initialize(crudbp, actioncontext);
    }

    public void insertedUsing(Persister persister, UserContext userContext)
    {
        crudStatus = 5;
    }

    public void insertingUsing(Persister persister, UserContext userContext)
    {
    }
    @JsonIgnore
    public boolean isNew()
    {
        return crudStatus == 1;
    }
    @JsonIgnore
    public boolean isNotNew()
    {
        return !isNew();
    }
    @JsonIgnore
    public static boolean isNullOrEmpty(String s)
    {
        return s == null || s.length() == 0;
    }
    @JsonIgnore
    public boolean isOperabile()
    {
        return operabile;
    }
    @JsonIgnore
    public boolean isToBeChecked()
    {
        return crudStatus == 4;
    }
    @JsonIgnore
    public boolean isToBeCreated()
    {
        return crudStatus == 1;
    }
    @JsonIgnore
    public boolean isToBeDeleted()
    {
        return crudStatus == 3;
    }
    @JsonIgnore
    public boolean isToBeUpdated()
    {
        return crudStatus == 2;
    }

    public int primaryKeyHashCode()
    {
        return hashCode();
    }

    public void setCrudStatus(int i)
    {
        crudStatus = i;
    }

    public void setDacr(Timestamp timestamp)
    {
        dacr = timestamp;
    }

    public void setDuva(Timestamp timestamp)
    {
        duva = timestamp;
    }

    public void setKey(KeyedPersistent keyedpersistent)
    {
        key = keyedpersistent;
    }

    public void setOperabile(boolean flag)
    {
        operabile = flag;
    }

    public void setPg_ver_rec(Long long1)
    {
        pg_ver_rec = long1;
    }

    public void setToBeChecked()
    {
        if(crudStatus == 5)
            setCrudStatus(4);
    }

    public void setToBeCreated()
    {
        if(crudStatus == 3)
            setCrudStatus(5);
        else
        if(crudStatus == 0)
            setCrudStatus(1);
    }

    public void setToBeDeleted()
    {
        if(crudStatus == 1)
            setCrudStatus(0);
        else
        if(crudStatus != 0)
            setCrudStatus(3);
    }

    public void setToBeUpdated()
    {
        if(crudStatus == 5)
            setCrudStatus(2);
        else
        if(crudStatus == 0)
            setCrudStatus(1);
    }

    public void setUser(String s)
    {
        user = s;
    }

    public void setUtcr(String s)
    {
        utcr = s;
    }

    public void setUtuv(String s)
    {
        utuv = s;
    }

    public String toString()
    {
        if(this instanceof KeyedPersistent)
            try
            {
                return BeanIntrospector.getSQLInstance().getOid((KeyedPersistent)this);
            }
            catch(Throwable _ex) { }
        return super.toString();
    }

    public void updatedUsing(Persister persister, UserContext userContext)
    {
        crudStatus = 5;
    }

    public void updatingUsing(Persister persister, UserContext userContext)
    {
    }

    public void validate()
        throws ValidationException
    {
    }

    public void validate(OggettoBulk oggettobulk)
        throws ValidationException
    {
    }

    public void writeForm(JspWriter jspwriter, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap)
        throws IOException
    {
        writeForm(jspwriter, null, null, null, null, i, fieldvalidationmap, isBootstrap);
    }

    public void writeForm(JspWriter jspwriter, String s, String s1, String s2, String s3, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap)
        throws IOException
    {
        for(Enumeration enumeration = getBulkInfo().getFormFieldProperties(s); enumeration.hasMoreElements(); jspwriter.println("</td></tr>"))
        {
            FieldProperty fieldproperty = (FieldProperty)enumeration.nextElement();
            jspwriter.print("<tr><td>");
            fieldproperty.writeLabel(jspwriter, this, s1);
            jspwriter.print("</td><td>");
            fieldproperty.writeInput(jspwriter, this, false, s2, null, s3, i, fieldvalidationmap, isBootstrap);
        }

    }

    public void writeFormField(JspWriter jspwriter, String s, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap)
        throws IOException
    {
        FieldProperty fieldproperty = getBulkInfo().getFormFieldProperty(null, s);
        if(fieldproperty != null)
        {
            jspwriter.print("<td>");
            fieldproperty.writeLabel(jspwriter, this, null);
            jspwriter.print("</td><td>");
            fieldproperty.writeInput(jspwriter, this, false, null, null, null, i, fieldvalidationmap, isBootstrap);
            jspwriter.print("</td>");
        }
    }

    public void writeFormField(JspWriter jspwriter, String s, String s1, String s2, int i, int j, int k, 
            FieldValidationMap fieldvalidationmap, boolean isBootstrap)
        throws IOException
    {
        FieldProperty fieldproperty = getBulkInfo().getFormFieldProperty(s, s1);
        if(fieldproperty != null)
        {
            jspwriter.print("<td");
            if(i > 1)
            {
                jspwriter.print(" colspan=\"");
                jspwriter.print(i);
                jspwriter.print("\"");
            }
            jspwriter.print(">");
            fieldproperty.writeLabel(jspwriter, this, null);
            jspwriter.print("</td><td");
            if(j > 1)
            {
                jspwriter.print(" colspan=\"");
                jspwriter.print(j);
                jspwriter.print("\"");
            }
            jspwriter.print(">");
            fieldproperty.writeInput(jspwriter, this, false, null, null, s2, k, fieldvalidationmap, isBootstrap);
            jspwriter.print("</td>");
        }
    }

    public void writeFormField(JspWriter jspwriter, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap)
        throws IOException
    {
        writeFormField(jspwriter, s, s1, s2, 1, 1, i, fieldvalidationmap, isBootstrap);
    }

    public void writeFormInput(JspWriter jspwriter, String s, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap)
        throws IOException
    {
        writeFormInput(jspwriter, null, s, false, null, null, null, i, fieldvalidationmap, isBootstrap);
    }

    public void writeFormInput(JspWriter jspwriter, String s, String s1, String s2, int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap)
        throws IOException
    {
        FieldProperty fieldproperty = getBulkInfo().getFormFieldProperty(s, s1);
        if(fieldproperty != null)
            fieldproperty.writeInput(jspwriter, this, false, null, null, s2, i, fieldvalidationmap, isBootstrap);
    }
	public void writeFormInput(JspWriter jspwriter, String s, String s1, boolean flag, String s2, String s3, String s4, 
			int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap) throws IOException{
				writeFormInput(null, jspwriter, s, s1, flag, s2, s3, s4, 
							   i, fieldvalidationmap, isBootstrap);		
	}

    public void writeFormInput(Object bp, JspWriter jspwriter, String s, String s1, boolean flag, String s2, String s3, String s4, 
            int i, FieldValidationMap fieldvalidationmap, boolean isBootstrap)
        throws IOException
    {
        FieldProperty fieldproperty = getBulkInfo().getFormFieldProperty(s, s1);
        if(fieldproperty != null)
            fieldproperty.writeInput(bp, jspwriter, this, flag, s2, s3, s4, i, fieldvalidationmap, isBootstrap);
    }

    public void writeFormLabel(JspWriter jspwriter, String s)
        throws IOException
    {
        writeFormLabel(jspwriter, null, s, null);
    }

    public void writeFormLabel(JspWriter jspwriter, String s, String s1)
        throws IOException
    {
        writeFormLabel(jspwriter, s, s1, null);
    }

    public void writeFormLabel(JspWriter jspwriter, String s, String s1, String s2)
        throws IOException
    {
        FieldProperty fieldproperty = getBulkInfo().getFormFieldProperty(s, s1);
        if(fieldproperty != null)
            fieldproperty.writeLabel(jspwriter, this, s2);
    }
}
