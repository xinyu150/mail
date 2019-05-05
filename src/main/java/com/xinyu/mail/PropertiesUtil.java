package com.xinyu.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * @author Sam <br />
 * 读取properties信息 <br />
 * 
 */
public class PropertiesUtil {

    /**
     * Get property by name
     * 
     * @param name
     * @return
     */
    public static String getProperty(String name) {
        return properties.getProperty(name);
    }

    private static Properties properties;

    /****
     * Initialize properties
     */
    static {
        String filePath = "properties.xml";
        InputStream stream = PropertiesUtil.class.getClassLoader()
                .getResourceAsStream(filePath);

        properties = new Properties();

        try {
            properties.loadFromXML(stream);
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}