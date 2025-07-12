package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.DataFolderInitializer;
import util.DataInitializer;

public class MainApplication extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // 初始化数据
        DataFolderInitializer.initializeDataFolder();
        DataInitializer.initializeData();
        
        // 创建主界面
        VBox root = new VBox(10);
        root.getStyleClass().add("main-container");
        
        // 创建标题
        Label title = new Label("教务管理系统");
        title.getStyleClass().add("title");
        
        // 创建按钮
        Button adminBtn = createMenuButton("管理员登录");
        Button teacherBtn = createMenuButton("教师登录");
        Button studentBtn = createMenuButton("学生登录");
        
        // 添加事件处理
        adminBtn.setOnAction(e -> showLoginDialog("管理员登录", "admin"));
        teacherBtn.setOnAction(e -> showLoginDialog("教师登录", "teacher"));
        studentBtn.setOnAction(e -> showLoginDialog("学生登录", "student"));
        
        root.getChildren().addAll(title, adminBtn, teacherBtn, studentBtn);
        
        // 设置场景
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        // 设置窗口
        primaryStage.setTitle("教务管理系统");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("menu-button");
        return button;
    }
    
    private void showLoginDialog(String title, String type) {
        Stage loginStage = new Stage();
        LoginDialog dialog = new LoginDialog(title, type);
        dialog.showAndWait(loginStage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
