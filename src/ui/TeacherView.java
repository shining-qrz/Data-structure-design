package ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import service.TeacherService;
import service.impl.TeacherServiceImpl;
import entity.*;
import util.ScheduleGridBuilder;
import java.io.File;
import java.util.List;

public class TeacherView {
    private TeacherService teacherService = new TeacherServiceImpl();
    private Stage stage;
    private String teacherId;

    public TeacherView(Stage stage, String teacherId) {
        this.stage = stage;
        this.teacherId = teacherId;
        initialize();
    }

    private void initialize() {
        TabPane tabPane = new TabPane();

        Tab scheduleTab = new Tab("课程表");
        scheduleTab.setContent(createScheduleView());

        Tab gradeTab = new Tab("成绩管理");
        gradeTab.setContent(createGradeView());

        Tab feedbackTab = new Tab("教学反馈");
        feedbackTab.setContent(createFeedbackView());

        tabPane.getTabs().addAll(scheduleTab, gradeTab, feedbackTab);

        Scene scene = new Scene(tabPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        stage.setTitle("教师系统");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createScheduleView() {
        VBox container = new VBox(10);
        container.setStyle("-fx-padding: 20;");

        // 获取课程数据
        List<Course> courses = teacherService.getTeacherSchedule(teacherId);
        
        // 使用ScheduleGridBuilder创建课表网格
        GridPane scheduleGrid = ScheduleGridBuilder.buildScheduleGrid(courses);
        ScrollPane scrollPane = new ScrollPane(scheduleGrid);
        scrollPane.setFitToWidth(true);
        
        Button exportBtn = new Button("导出课表");
        exportBtn.setOnAction(e -> handleExportSchedule());

        container.getChildren().addAll(new Label("个人课表"), scrollPane, exportBtn);
        return container;
    }

    private VBox createGradeView() {
        VBox container = new VBox(10);
        container.setStyle("-fx-padding: 20;");

        // 课程选择
        ComboBox<Course> courseCombo = new ComboBox<>();
        courseCombo.setPromptText("选择课程");
        courseCombo.getItems().addAll(teacherService.getTeacherSchedule(teacherId));
        courseCombo.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName() + " - " + item.getClassName());
            }
        });

        // 成绩列表
        TableView<Grade> gradeTable = new TableView<>();
        TableColumn<Grade, String> studentCol = new TableColumn<>("学号");
        TableColumn<Grade, Double> scoreCol = new TableColumn<>("成绩");
        
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        
        gradeTable.getColumns().addAll(studentCol, scoreCol);

        // 添加成绩输入区域
        HBox inputArea = new HBox(10);
        TextField studentIdField = new TextField();
        studentIdField.setPromptText("学号");
        TextField scoreField = new TextField();
        scoreField.setPromptText("成绩");
        Button addBtn = new Button("录入成绩");

        inputArea.getChildren().addAll(studentIdField, scoreField, addBtn);

        // 事件处理
        courseCombo.setOnAction(e -> {
            Course selected = courseCombo.getValue();
            if (selected != null) {
                List<Grade> grades = teacherService.getGradesByClass(
                    selected.getCourseId(), selected.getClassName());
                gradeTable.getItems().setAll(grades);
            }
        });

        addBtn.setOnAction(e -> {
            Course selected = courseCombo.getValue();
            if (selected == null) {
                showError("请选择课程");
                return;
            }
            try {
                Grade grade = new Grade(selected.getCourseId(),
                    studentIdField.getText(),
                    Double.parseDouble(scoreField.getText()));
                if (teacherService.inputGrade(grade)) {
                    showInfo("成绩录入成功");
                    courseCombo.fireEvent(new ActionEvent()); // 刷新列表
                } else {
                    showError("成绩录入失败");
                }
            } catch (NumberFormatException ex) {
                showError("请输入有效的成绩");
            }
        });

        container.getChildren().addAll(courseCombo, inputArea, gradeTable);
        return container;
    }

    private VBox createFeedbackView() {
        VBox container = new VBox(10);
        container.setStyle("-fx-padding: 20;");

        TableView<Feedback> feedbackTable = new TableView<>();
        
        TableColumn<Feedback, String> courseCol = new TableColumn<>("课程");
        TableColumn<Feedback, String> studentCol = new TableColumn<>("学生");
        TableColumn<Feedback, String> dateCol = new TableColumn<>("日期");
        TableColumn<Feedback, String> contentCol = new TableColumn<>("反馈内容");

        courseCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        studentCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));

        feedbackTable.getColumns().addAll(courseCol, studentCol, dateCol, contentCol);
        
        // 加载数据
        List<Feedback> feedbacks = teacherService.getFeedbacks(teacherId);
        feedbackTable.getItems().setAll(feedbacks);

        container.getChildren().addAll(new Label("教学反馈"), feedbackTable);
        return container;
    }

    private void handleExportSchedule() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出课表");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            if (teacherService.exportSchedule(teacherId, file.getPath())) {
                showInfo("课表导出成功");
            } else {
                showError("课表导出失败");
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("提示");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
