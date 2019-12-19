package com.hyperwallet.android.rule;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExternalResourceManager extends TestWatcher {

    private static final String EMPTY = "";
    private static final String ERRORS = "errors/";
    private ClassLoader classLoader;
    private Logger logger;

    @Override
    protected void starting(Description description) {
        super.starting(description);
        classLoader = description.getTestClass().getClassLoader();
        logger = Logger.getLogger(description.getTestClass().getName());
    }


    public String getResourceContent(final String resourceName) {
        if (resourceName == null) {
            throw new IllegalArgumentException("Parameter resourceName cannot be null");
        }

        return getContent(resourceName);
    }


    public String getResourceContentError(final String resourceName) {
        return getResourceContent(ERRORS + resourceName);
    }


    private String getContent(final String resourceName) {

        URL resource = classLoader.getResource(resourceName);
        InputStream inputStream = null;
        Writer writer = new StringWriter();
        String resourceContent = EMPTY;
        if (resource != null) {
            try {
                inputStream = resource.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = reader.readLine();
                while (line != null) {
                    writer.write(line);
                    line = reader.readLine();
                }
                resourceContent = writer.toString();

            } catch (Exception e) {
                logger.log(Level.WARNING, "There was an error loading an external resource", e);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "There was an error closing input stream", e);
                }
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "There was an error closing writer", e);
                }
            }
        }
        return resourceContent;
    }


}
