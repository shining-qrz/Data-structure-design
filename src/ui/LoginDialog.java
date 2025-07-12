package ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import service.*;
import service.impl.*;
import java.util.Map;

public class LoginDialog {
    private String type;
    private String title; // 添加title字段
    private TextField usernameField;
    private PasswordField passwordField;
    
    public LoginDialog(String title, String type) {
        this.type = type;
        this.title = title; // 保存title
        
        VBox content = new VBox(10);
        content.getStyleClass().add("login-dialog");
        
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("dialog-title");
        
        usernameField = new TextField();
        usernameField.setPromptText("用户名");
        
        passwordField = new PasswordField();
        passwordField.setPromptText("密码");
        
        Button loginButton = new Button("登录");
        loginButton.getStyleClass().add("login-button");
        
        content.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton);
    }
    
    public void showAndWait(Stage stage) {
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPrefSize(300, 200);
        
        Label titleLabel = new Label(this.title); // 使用this.title
        titleLabel.getStyleClass().add("dialog-title");
        
        Button loginButton = new Button("登录");
        loginButton.getStyleClass().add("login-button");
        
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (validateLogin(username, password)) {
                stage.close();
                openMainView(username);
            } else {
                showError("登录失败", "用户名或密码错误");
            }
        });
        
        content.getChildren().addAll(titleLabel, usernameField, passwordField, loginButton);
        
        Scene scene = new Scene(content);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        stage.setScene(scene);
        stage.setTitle(title);
        stage.showAndWait();
    }
    
    private boolean validateLogin(String username, String password) {
        // 简单的登录验证逻辑
        switch (type) {
            case "admin":
                return username.equals("admin") && password.equals("admin");
            case "teacher":
                return username.startsWith("T") && password.equals("123456");
            case "student":
                return username.startsWith("2023") && password.equals("123456");
            default:
                return false;
        }
    }
    
    private void openMainView(String username) {
        Stage mainStage = new Stage();
        switch (type) {
            case "admin":
                new AdminView(mainStage);
                break;
            case "teacher":
                new TeacherView(mainStage, username);
                break;
            case "student":
                new StudentView(mainStage, username);
                break;
        }
    }
    
    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
