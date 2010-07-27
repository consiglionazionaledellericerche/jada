package it.cnr.jada.ejb.session;

import it.cnr.jada.bulk.OggettoBulk;

import javax.ejb.Remote;
@Remote
public interface CRUDComponentSessionRemote<T extends OggettoBulk> extends CRUDComponentSession<T> {

}
