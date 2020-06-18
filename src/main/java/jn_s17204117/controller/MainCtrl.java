package jn_s17204117.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import jn_s17204117.service.Servers;
import jn_s17204117.utils.JerryLogger;

/**
 * 界面控制器
 *
 * @author IITII
 */
public class MainCtrl {
    @FXML
    private GridPane root;
    @FXML
    private JFXButton start, stop, forceStop;

    private final Servers servers = new Servers();

    /**
     * 启动服务器
     *
     * @param actionEvent actionEvent
     */
    @FXML
    public void serverStart(ActionEvent actionEvent) {
        try {
            servers.start();
            alert(Alert.AlertType.INFORMATION, "", null, "启动成功");
        } catch (Exception e) {
            alert(Alert.AlertType.ERROR, "", "启动失败", "请检查系统日志");
            JerryLogger logger = new JerryLogger();
            logger.severe(e.getLocalizedMessage());
            logger.close();
            e.printStackTrace();
        }
    }

    /**
     * 关闭服务器
     *
     * @param actionEvent actionEvent
     */
    @FXML
    public void serverStop(ActionEvent actionEvent) {
        if (servers.stop()) {
            alert(Alert.AlertType.INFORMATION, "", null, "关闭成功");
        } else {
            alert(Alert.AlertType.ERROR, "", null, "关闭失败");
        }
    }

    /**
     * 强制关闭
     *
     * @param actionEvent actionEvent
     */
    @FXML
    public void serverForceStop(ActionEvent actionEvent) {
        if (servers.forceStop()) {
            alert(Alert.AlertType.INFORMATION, "", null, "强制关闭成功");
        } else {
            alert(Alert.AlertType.ERROR, "", null, "强制关闭失败");
        }
    }

    /**
     * @param alertType   警告类型
     * @param title       警告窗口标题
     * @param headerText  警告窗口
     * @param contentText 警告窗口内容
     */
    public void alert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle("".equals(title) ? "Jerry" : title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
