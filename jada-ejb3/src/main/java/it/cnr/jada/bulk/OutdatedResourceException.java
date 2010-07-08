/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bulk;

import it.cnr.jada.ejb.session.ComponentException;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class OutdatedResourceException extends ComponentException {
	private static final long serialVersionUID = -2828685986817528196L;
	private OggettoBulk bulk;
    public OutdatedResourceException(){
    }

    public OutdatedResourceException(OggettoBulk oggettobulk){
        bulk = oggettobulk;
    }

    public OutdatedResourceException(String s){
        super(s);
    }

    public OutdatedResourceException(String s, OggettoBulk oggettobulk){
        super(s);
        bulk = oggettobulk;
    }

    public OggettoBulk getBulk(){
        return bulk;
    }

    public void setBulk(OggettoBulk oggettobulk){
        bulk = oggettobulk;
    }    
}
