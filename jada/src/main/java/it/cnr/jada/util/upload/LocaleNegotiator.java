package it.cnr.jada.util.upload;
import java.util.*;

public class LocaleNegotiator
{

    public LocaleNegotiator(String bundleName, String languages, String charsets)
    {
        Locale defaultLocale = new Locale("en", "US");
        String defaultCharset = "ISO-8859-1";
        ResourceBundle defaultBundle = null;
        try
        {
            defaultBundle = ResourceBundle.getBundle(bundleName, defaultLocale);
        }
        catch(MissingResourceException _ex) { }
        if(languages == null)
        {
            chosenLocale = defaultLocale;
            chosenCharset = defaultCharset;
            chosenBundle = defaultBundle;
            return;
        }
        for(StringTokenizer tokenizer = new StringTokenizer(languages, ","); tokenizer.hasMoreTokens();)
        {
            String lang = tokenizer.nextToken();
            Locale loc = getLocaleForLanguage(lang);
            ResourceBundle bundle = getBundleNoFallback(bundleName, loc);
            if(bundle != null)
            {
                String charset = getCharsetForLocale(loc, charsets);
                if(charset != null)
                {
                    chosenLocale = loc;
                    chosenBundle = bundle;
                    chosenCharset = charset;
                    return;
                }
            }
        }

        chosenLocale = defaultLocale;
        chosenCharset = defaultCharset;
        chosenBundle = defaultBundle;
    }

    public ResourceBundle getBundle()
    {
        return chosenBundle;
    }

    private ResourceBundle getBundleNoFallback(String bundleName, Locale loc)
    {
        ResourceBundle fallback = null;
        try
        {
            fallback = ResourceBundle.getBundle(bundleName, new Locale("bogus", ""));
        }
        catch(MissingResourceException _ex) { }
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleName, loc);
            if(bundle != fallback)
                return bundle;
            if(bundle == fallback && loc.getLanguage().equals(Locale.getDefault().getLanguage()))
                return bundle;
        }
        catch(MissingResourceException _ex) { }
        return null;
    }

    public String getCharset()
    {
        return chosenCharset;
    }

    protected String getCharsetForLocale(Locale loc, String charsets)
    {
        return LocaleToCharsetMap.getCharset(loc);
    }

    public Locale getLocale()
    {
        return chosenLocale;
    }

    private Locale getLocaleForLanguage(String lang)
    {
        int semi;
        if((semi = lang.indexOf(';')) != -1)
            lang = lang.substring(0, semi);
        lang = lang.trim();
        Locale loc;
        int dash;
        if((dash = lang.indexOf('-')) == -1)
            loc = new Locale(lang, "");
        else
            loc = new Locale(lang.substring(0, dash), lang.substring(dash + 1));
        return loc;
    }

    private ResourceBundle chosenBundle;
    private Locale chosenLocale;
    private String chosenCharset;
}