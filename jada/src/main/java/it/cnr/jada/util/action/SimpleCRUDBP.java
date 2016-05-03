package it.cnr.jada.util.action;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.Serializable;
import java.util.Dictionary;

public class SimpleCRUDBP extends CRUDBP implements Serializable {

	private String componentSessioneName;
	private Class bulkClass;
	private BulkInfo bulkInfo;
	private String searchResultColumnSet;
	private String freeSearchSet;
	private Class searchBulkClass;
	private BulkInfo searchBulkInfo;
	
	private CompoundFindClause compoundfindclause;
	private OggettoBulk oggettobulk;

	public SimpleCRUDBP() {
	}

	public SimpleCRUDBP(String s) {
		super(s);
	}

	public void create(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			getModel().setToBeCreated();
			setModel(
					actioncontext,
					createComponentSession().creaConBulk(
							actioncontext.getUserContext(), getModel()));
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}

	public CRUDComponentSession createComponentSession()
			throws BusinessProcessException {
		return (CRUDComponentSession) createComponentSession(componentSessioneName);
	}

	public OggettoBulk createEmptyModel(ActionContext actioncontext)
			throws BusinessProcessException {
		return initializeModelForInsert(actioncontext,
				createNewBulk(actioncontext));
	}

	public OggettoBulk createEmptyModelForFreeSearch(ActionContext actioncontext)
			throws BusinessProcessException {
		return initializeModelForFreeSearch(actioncontext,
				createNewSearchBulk(actioncontext));
	}

	public OggettoBulk createEmptyModelForSearch(ActionContext actioncontext)
			throws BusinessProcessException {
		return initializeModelForSearch(actioncontext,
				createNewSearchBulk(actioncontext));
	}

	public OggettoBulk createNewBulk(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			OggettoBulk oggettobulk = (OggettoBulk) bulkClass.newInstance();
			oggettobulk.setUser(actioncontext.getUserInfo().getUserid());
			return oggettobulk;
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}

	public OggettoBulk createNewSearchBulk(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			OggettoBulk oggettobulk = (OggettoBulk) searchBulkClass
					.newInstance();
			oggettobulk.setUser(actioncontext.getUserInfo().getUserid());
			return oggettobulk;
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}

	public void delete(ActionContext actioncontext)
			throws BusinessProcessException {
		int i = getModel().getCrudStatus();
		try {
			getModel().setToBeDeleted();
			createComponentSession().eliminaConBulk(
					actioncontext.getUserContext(), getModel());
			commitUserTransaction();
		} catch (Exception exception) {
			getModel().setCrudStatus(i);
			throw handleException(exception);
		}
	}

	public boolean isLastSearchButtonHidden(){
		return this.compoundfindclause == null && this.oggettobulk == null;
	}
	
	public RemoteIterator lastFind(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			return EJBCommonServices.openRemoteIterator(
					actioncontext,
					createComponentSession().cerca(
							actioncontext.getUserContext(), this.compoundfindclause,
							this.oggettobulk));			
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}
	
	public RemoteIterator find(ActionContext actioncontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		try {
			/**
			 * Salvo l'ultima ricerca effettuata.
			 */
			this.compoundfindclause = compoundfindclause;
			this.oggettobulk = oggettobulk;
			
			return EJBCommonServices.openRemoteIterator(
					actioncontext,
					createComponentSession().cerca(
							actioncontext.getUserContext(), compoundfindclause,
							oggettobulk));			
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}

	public RemoteIterator find(ActionContext actioncontext,
			CompoundFindClause compoundfindclause, OggettoBulk oggettobulk,
			OggettoBulk oggettobulk1, String s) throws BusinessProcessException {
		try {
			return EJBCommonServices.openRemoteIterator(
					actioncontext,
					createComponentSession().cerca(
							actioncontext.getUserContext(), compoundfindclause,
							oggettobulk, oggettobulk1, s));
		} catch (Exception exception) {
			throw new BusinessProcessException(exception);
		}
	}

	public BulkInfo getBulkInfo() {
		if (getModel() == null)
			return bulkInfo;
		else
			return super.getBulkInfo();
	}

	public String getComponentSessioneName() {
		return componentSessioneName;
	}

	public String getFreeSearchSet() {
		return freeSearchSet;
	}

	public Class getSearchBulkClass() {
		return searchBulkClass;
	}

	public BulkInfo getSearchBulkInfo() {
		return searchBulkInfo;
	}

	public Dictionary getSearchResultColumns() {
		if (getSearchResultColumnSet() == null)
			return super.getSearchResultColumns();
		else
			return getModel().getBulkInfo().getColumnFieldPropertyDictionary(
					getSearchResultColumnSet());
	}

	public String getSearchResultColumnSet() {
		return searchResultColumnSet;
	}

	protected void init(Config config, ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			setBulkClassName(config.getInitParameter("bulkClassName"));
			setSearchBulkClassName(config
					.getInitParameter("searchBulkClassName"));
			setComponentSessioneName(config
					.getInitParameter("componentSessionName"));
			setSearchResultColumnSet(config
					.getInitParameter("searchResultColumnSet"));
			setFreeSearchSet(config.getInitParameter("freeSearchSet"));
			if (searchBulkClass == null)
				setSearchBulkClass(bulkClass);
		} catch (ClassNotFoundException _ex) {
			throw new RuntimeException("Non trovata la classe bulk");
		}
		super.init(config, actioncontext);
	}

	public OggettoBulk initializeModelForEdit(ActionContext actioncontext,
			OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			return createComponentSession().inizializzaBulkPerModifica(
					actioncontext.getUserContext(),
					super.initializeModelForEdit(actioncontext, oggettobulk));
		} catch (Throwable throwable) {
			throw new BusinessProcessException(throwable);
		}
	}

	public OggettoBulk initializeModelForFreeSearch(
			ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {
		try {
			return createComponentSession().inizializzaBulkPerRicercaLibera(
					actioncontext.getUserContext(),
					super.initializeModelForFreeSearch(actioncontext,
							oggettobulk));
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}

	public OggettoBulk initializeModelForInsert(ActionContext actioncontext,
			OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			return createComponentSession().inizializzaBulkPerInserimento(
					actioncontext.getUserContext(),
					super.initializeModelForInsert(actioncontext, oggettobulk));
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}

	public OggettoBulk initializeModelForSearch(ActionContext actioncontext,
			OggettoBulk oggettobulk) throws BusinessProcessException {
		try {
			return createComponentSession().inizializzaBulkPerRicerca(
					actioncontext.getUserContext(),
					super.initializeModelForSearch(actioncontext, oggettobulk));
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}

	private void setBulkClass(Class class1) throws ClassNotFoundException {
		bulkInfo = BulkInfo.getBulkInfo(bulkClass = class1);
	}

	public void setBulkClassName(String s) throws ClassNotFoundException {
		setBulkClass(getClass().getClassLoader().loadClass(s));
	}

	public void setComponentSessioneName(String s) {
		componentSessioneName = s;
	}

	public void setFreeSearchSet(String s) {
		freeSearchSet = s;
	}

	private void setSearchBulkClass(Class class1) {
		searchBulkInfo = BulkInfo.getBulkInfo(searchBulkClass = class1);
	}

	public void setSearchBulkClassName(String s) throws ClassNotFoundException {
		if (s != null)
			setSearchBulkClass(getClass().getClassLoader().loadClass(s));
	}

	public void setSearchResultColumnSet(String s) {
		searchResultColumnSet = s;
	}

	/**
	 * Metodo da sovrascrivere nelle classi Bulk figlie che implementeranno la
	 * gestione PostIt 1. Progetti
	 */
	public it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagliPostIt() {
		return null;
	}

	/**
	 * Metodo da sovrascrivere nelle classi Bulk figlie che implementeranno la
	 * gestione PostIt 1. Progetti
	 */
	public boolean isActive(OggettoBulk bulk, int sel) {
		return true;
	}

	public void update(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			getModel().setToBeUpdated();
			setModel(
					actioncontext,
					createComponentSession().modificaConBulk(
							actioncontext.getUserContext(), getModel()));
		} catch (Exception exception) {
			throw handleException(exception);
		}
	}
}