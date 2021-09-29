package com.appleisle.tincase.config;


import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class YamlMessageSource extends ResourceBundleMessageSource {

    protected ResourceBundle doGetBundle(String basename, Locale locale)
            throws MissingResourceException
    {
        return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
    }

}
