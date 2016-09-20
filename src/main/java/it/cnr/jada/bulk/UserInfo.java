package it.cnr.jada.bulk;

import it.cnr.jada.UserTransaction;
import it.cnr.jada.action.*;

import java.io.Serializable;

public class UserInfo extends OggettoBulk implements Serializable {

	private static final long serialVersionUID = 1L;
	private UserTransaction userTransaction;
	private String userid;

	public UserInfo() {
	}

	public BusinessProcess createBusinessProcess(ActionContext actioncontext,
			String s) throws BusinessProcessException {
		return actioncontext.createBusinessProcess(s);
	}

	public BusinessProcess createBusinessProcess(ActionContext actioncontext,
			String s, Object aobj[]) throws BusinessProcessException {
		return actioncontext.createBusinessProcess(s, aobj);
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String s) {
		userid = s;
	}

	public UserTransaction getUserTransaction() {
		return userTransaction;
	}

	public void setUserTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}

}