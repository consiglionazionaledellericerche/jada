package it.cnr.jada.util;


public abstract class PlatformObjectFactory
{

    private PlatformObjectFactory()
    {
    }

    public static final Object createInstance(Class class1, String s)
    {
        int i = class1.getName().lastIndexOf('.');
        try
        {
            return class1.getClassLoader().loadClass(class1.getName().substring(0, i) + "." + s + class1.getName().substring(i)).newInstance();
        }
        catch(ClassNotFoundException _ex)
        {
            return null;
        }
        catch(InstantiationException instantiationexception)
        {
            throw new InstantiationError(instantiationexception.getMessage());
        }
        catch(IllegalAccessException illegalaccessexception)
        {
            throw new IllegalAccessError(illegalaccessexception.getMessage());
        }
    }
}