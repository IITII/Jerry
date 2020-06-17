package jn_s17204117.models;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * 允许的 properties 参数名称
 *
 * @author IITII
 */
public class CheckProperties {
    final String allProp = "address port root autoindex ssl.file ssl.password ssl.type ssl.protocol";
    final String necessaryProp = "address port root";

    public String[] getAllProp() {
        return allProp.split("\\s");
    }

    public ArrayList<String> getAllPropArrayList() {
        return new ArrayList<>(Arrays.asList(getAllProp()));
    }

    public String[] getNecessaryProp() {
        return necessaryProp.split("\\s");
    }

    public ArrayList<String> getNecessaryPropArrayList() {
        return new ArrayList<>(Arrays.asList(getNecessaryProp()));
    }

    /**
     * 检查配置文件
     *
     * @param properties properties 变量
     * @return true：验证无误， false：验证失败
     */
    public Boolean checkProp(Properties properties) {
        boolean flag = true;
        if (!
                (
                        properties.keySet().containsAll(getNecessaryPropArrayList())
                                && getAllPropArrayList().containsAll(properties.keySet())
                )
        ) {
            flag = false;
        }
        // 校验其他参数
        for (Object s : properties.keySet()) {
            switch ((String) s) {
                case "ssl.file":
                case "root":
                    String root = properties.getProperty("root");
                    //非空
                    if (root == null) {
                        flag = false;
                        break;
                    }

                    //必须为绝对路径
                    Path path = Paths.get(root);
                    if (!(
                            path.isAbsolute() && Files.exists(path)
                    )) {
                        flag = false;
                    }
                    break;
                default:
                    break;
            }
        }
        return flag;
    }
}
