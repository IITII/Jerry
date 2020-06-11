package utils;

/**
 * 利用java 反射机制，根据传入的字符串，动态创建对应对象
 *
 * @author IITII
 */
public class GetObjectByStringValue {
    private final String objectName;

    public GetObjectByStringValue(String objectName) {
        this.objectName = objectName;
    }

    public Object getNewInstance() {
        try {
            Class<?> aClass = Class.forName(objectName);
            return aClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
