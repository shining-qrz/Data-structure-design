package ui.view;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import service.AdminService;

public abstract class BaseTableView extends VBox {
    protected AdminService adminService;
    
    public BaseTableView(AdminService adminService) {
        super(10);
        this.adminService = adminService;
        setStyle("-fx-padding: 20;");
    }
    
    protected void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
