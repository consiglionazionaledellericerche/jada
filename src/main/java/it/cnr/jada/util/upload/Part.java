package it.cnr.jada.util.upload;

public abstract class Part
{

    Part(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean isFile()
    {
        return false;
    }

    public boolean isParam()
    {
        return false;
    }

    private String name;
}