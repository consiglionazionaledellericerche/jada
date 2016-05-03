package it.cnr.jada.action;


public class NoSuchBusinessProcessException extends RuntimeException
{

    public NoSuchBusinessProcessException()
    {
    }

    public NoSuchBusinessProcessException(String s)
    {
        super(s);
    }

    public NoSuchBusinessProcessException(String s, String s1)
    {
        super(s);
        businessProcess = s1;
    }

    public String getBusinessProcess()
    {
        return businessProcess;
    }

    public void setBusinessProcess(String s)
    {
        businessProcess = s;
    }

    private String businessProcess;
}