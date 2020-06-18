package jn_s17204117.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 主界面
 * 目录格式跟随 resource 下 views 文件夹
 *
 * @author IITII
 */
public class MainStage extends Application {

    final String title = "Jerry";
    final int defaultWidth = 500;
    final int defaultHeight = 240;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));
        primaryStage.setTitle(title);
        primaryStage.getIcons().add(new Image(MainStage.class.getResourceAsStream("/images/icon.png")));
        Scene scene = new Scene(root, defaultWidth, defaultHeight);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
