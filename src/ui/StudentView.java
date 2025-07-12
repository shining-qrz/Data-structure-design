package ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.cell.PropertyValueFactory;
import service.StudentService;
import service.impl.StudentServiceImpl;
import entity.*;
import util.ScheduleGridBuilder;
import java.io.File;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentView {
    private StudentService studentService = new StudentServiceImpl();
    private Stage stage;
    private String studentId;
    private Student student;

    public StudentView(Stage stage, String studentId) {
        this.stage = stage;
        this.studentId = studentId;
        this.student = studentService.getPersonalInfo(studentId);
        initialize();
    }

    private void initialize() {
        TabPane tabPane = new TabPane();

        Tab infoTab = new Tab("个人信息");
        infoTab.setContent(createInfoView());

        Tab scheduleTab = new Tab("课程表");
        scheduleTab.setContent(createScheduleView());

        Tab feedbackTab = new Tab("教学反馈");
        feedbackTab.setContent(createFeedbackView());

        tabPane.getTabs().addAll(infoTab, scheduleTab, feedbackTab);

        Scene scene = new Scene(tabPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());

        stage.setTitle("学生系统");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createInfoView() {
        VBox container = new VBox(10);
        container.setStyle("-fx-padding: 20;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // 个人信息字段
        Label idLabel = new Label("学号: " + student.getStudentId());
        TextField nameField = new TextField(student.getName());
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("男", "女");
        genderCombo.setValue(student.getGender());
        TextField birthField = new TextField(student.getBirthDate());
        TextField phoneField = new TextField(student.getPhone());
        Label classLabel = new Label("班级: " + student.getClassName());

        grid.addRow(0, new Label("学号:"), idLabel);
        grid.addRow(1, new Label("姓名:"), nameField);
        grid.addRow(2, new Label("性别:"), genderCombo);
        grid.addRow(3, new Label("出生日期:"), birthField);
        grid.addRow(4, new Label("电话:"), phoneField);
        grid.addRow(5, new Label("班级:"), classLabel);

        Button saveBtn = new Button("保存修改");
        saveBtn.setOnAction(e -> {
            student.setName(nameField.getText());
            student.setGender(genderCombo.getValue());
            student.setBirthDate(birthField.getText());
            student.setPhone(phoneField.getText());
            
            if (studentService.updatePersonalInfo(student)) {
                showInfo("信息更新成功");
            } else {
                showError("信息更新失败");
            }
        });

        container.getChildren().addAll(new Label("个人信息"), grid, saveBtn);
        return container;
    }

    private VBox createScheduleView() {
        VBox container = new VBox(10);
        container.setStyle("-fx-padding: 20;");

        // 获取课程数据
        List<Course> courses = studentService.getClassSchedule(student.getClassName());
        
        // 使用ScheduleGridBuilder创建课表网格
        GridPane scheduleGrid = ScheduleGridBuilder.buildScheduleGrid(courses);
        ScrollPane scrollPane = new ScrollPane(scheduleGrid);
        scrollPane.setFitToWidth(true);
        
        Button exportBtn = new Button("导出课表");
        exportBtn.setOnAction(e -> handleExportSchedule());

        container.getChildren().addAll(new Label("班级课表"), scrollPane, exportBtn);
        return container;
    }

    private VBox createFeedbackView() {
        VBox container = new VBox(10);
        container.setStyle("-fx-padding: 20;");

        // 课程选择
        ComboBox<Course> courseCombo = new ComboBox<>();
        courseCombo.setPromptText("选择课程");
        courseCombo.getItems().addAll(studentService.getClassSchedule(student.getClassName()));
        courseCombo.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });

        TextArea feedbackArea = new TextArea();
        feedbackArea.setPromptText("请输入反馈内容");
        feedbackArea.setPrefRowCount(5);

        Button submitBtn = new Button("提交反馈");
        submitBtn.setOnAction(e -> {
            Course selected = courseCombo.getValue();
            if (selected == null) {
                showError("请选择课程");
                return;
            }
            if (feedbackArea.getText().trim().isEmpty()) {
                showError("请输入反馈内容");
                return;
            }

            Feedback feedback = new Feedback(
                studentId,
                selected.getTeacherId(),
                selected.getCourseId(),
                feedbackArea.getText(),
                new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())
            );

            if (studentService.submitFeedback(feedback)) {
                showInfo("反馈提交成功");
                feedbackArea.clear();
            } else {
                showError("反馈提交失败");
            }
        });

        container.getChildren().addAll(
            new Label("提交教学反馈"),
            courseCombo,
            feedbackArea,
            submitBtn
        );
        return container;
    }

    private void handleExportSchedule() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出课表");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            if (studentService.exportSchedule(student.getClassName(), file.getPath())) {
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
