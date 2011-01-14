package it.cnr.jada.comp;

import it.cnr.jada.bulk.OggettoBulk;

import java.io.Serializable;

// Referenced classes of package it.cnr.jada.comp:
//            CRUDException

public class CRUDDuplicateKeyException extends CRUDException
    implements Serializable
{

    public CRUDDuplicateKeyException()
    {
    }

    public CRUDDuplicateKeyException(String s)
    {
        super(s);
    }

    public CRUDDuplicateKeyException(String s, OggettoBulk oggettobulk, OggettoBulk oggettobulk1)
    {
        super(s, null, oggettobulk);
        duplicateBulk = oggettobulk1;
    }

    public CRUDDuplicateKeyException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public CRUDDuplicateKeyException(String s, Throwable throwable, OggettoBulk oggettobulk, OggettoBulk oggettobulk1)
    {
        super(s, throwable, oggettobulk);
        duplicateBulk = oggettobulk1;
    }

    public CRUDDuplicateKeyException(Throwable throwable)
    {
        super(throwable);
    }

    public CRUDDuplicateKeyException(Throwable throwable, OggettoBulk oggettobulk, OggettoBulk oggettobulk1)
    {
        super(throwable, oggettobulk);
        duplicateBulk = oggettobulk1;
    }

    public OggettoBulk getDuplicateBulk()
    {
        return duplicateBulk;
    }

    public void setDuplicateBulk(OggettoBulk oggettobulk)
    {
        duplicateBulk = oggettobulk;
    }

    private OggettoBulk duplicateBulk;
}