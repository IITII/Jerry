package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * 允许的 properties 参数名称
 *
 * @author IITII
 */
public class CheckProperties {
    final String allProp = "address port root autoindex ssl.key ssl.crt";
    final String necessaryProp = "address port root";

    public String[] getAllProp() {
        return allProp.split("\\s");
    }

    public ArrayList<String> getAllPropArrayList() {
        return (ArrayList<String>) Arrays.asList(getAllProp());
    }

    public String[] getNecessaryProp() {
        return necessaryProp.split("\\s");
    }

    public ArrayList<String> getNecessaryPropArrayList() {
        return (ArrayList<String>) Arrays.asList(getNecessaryProp());
    }

    public Boolean checkProp(Properties properties) {
        return properties.keySet().containsAll(getNecessaryPropArrayList())
                && getAllPropArrayList().containsAll(properties.keySet());
    }
}
