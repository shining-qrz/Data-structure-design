package ui;

import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.AdminService;
import service.impl.AdminServiceImpl;
import ui.view.*;

public class AdminView {
    private AdminService adminService = new AdminServiceImpl();
    private Stage stage;
    
    public AdminView(Stage stage) {
        this.stage = stage;
        initialize();
    }
    
    private void initialize() {
        TabPane tabPane = new TabPane();
        
        Tab teacherTab = new Tab("教师管理");
        teacherTab.setContent(new TeacherTableView(adminService));
        
        Tab studentTab = new Tab("学生管理");
        studentTab.setContent(new StudentTableView(adminService));
        
        Tab courseTab = new Tab("课程管理");
        courseTab.setContent(new CourseTableView(adminService));
        
        Tab scheduleTab = new Tab("排课管理");
        scheduleTab.setContent(new ScheduleView(adminService));
        
        tabPane.getTabs().addAll(teacherTab, studentTab, courseTab, scheduleTab);
        
        Scene scene = new Scene(tabPane, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        stage.setTitle("管理员系统");
        stage.setScene(scene);
        stage.show();
    }
}
