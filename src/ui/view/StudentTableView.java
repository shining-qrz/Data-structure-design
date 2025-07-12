package ui.view;

import entity.Student;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import service.AdminService;
import ui.dialog.FormDialog;
import java.util.*;

public class StudentTableView extends BaseTableView {
    private TableView<Student> tableView;
    private TextField searchField;
    
    public StudentTableView(AdminService adminService) {
        super(adminService);
        initialize();
    }
    
    private void initialize() {
        tableView = createTableView();
        HBox controlBox = createControlBox();
        getChildren().addAll(controlBox, tableView);
        loadData();
    }
    
    private TableView<Student> createTableView() {
        TableView<Student> table = new TableView<>();
        
        TableColumn<Student, String> idCol = new TableColumn<>("学号");
        TableColumn<Student, String> nameCol = new TableColumn<>("姓名");
        TableColumn<Student, String> genderCol = new TableColumn<>("性别");
        TableColumn<Student, String> birthCol = new TableColumn<>("出生日期");
        TableColumn<Student, String> phoneCol = new TableColumn<>("电话");
        TableColumn<Student, String> classCol = new TableColumn<>("班级");

        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        classCol.setCellValueFactory(new PropertyValueFactory<>("className"));

        table.getColumns().addAll(idCol, nameCol, genderCol, birthCol, phoneCol, classCol);
        return table;
    }
    
    private HBox createControlBox() {
        HBox controlBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("输入学号或姓名搜索");
        Button searchBtn = new Button("搜索");
        Button addBtn = new Button("添加");
        Button editBtn = new Button("编辑");
        Button deleteBtn = new Button("删除");
        
        searchBtn.setOnAction(e -> handleSearch());
        addBtn.setOnAction(e -> handleAdd());
        editBtn.setOnAction(e -> handleEdit());
        deleteBtn.setOnAction(e -> handleDelete());
        
        controlBox.getChildren().addAll(searchField, searchBtn, addBtn, editBtn, deleteBtn);
        return controlBox;
    }

    private void handleSearch() {
        String keyword = searchField.getText();
        if (keyword.isEmpty()) {
            loadData();
        } else {
            List<Student> students = adminService.getAllStudents();
            tableView.getItems().setAll(students.stream()
                .filter(s -> s.getStudentId().contains(keyword) || 
                           s.getName().contains(keyword))
                .toList());
        }
    }

    private void handleAdd() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("学号", "请输入学号");
        fields.put("姓名", "请输入姓名");
        fields.put("性别", "请输入性别");
        fields.put("出生日期", "请输入出生日期(yyyy-MM-dd)");
        fields.put("电话", "请输入电话");
        fields.put("班级", "请输入班级");

        FormDialog<Student> dialog = new FormDialog<>("添加学生", fields, 
            values -> new Student(
                values.get("学号"),
                values.get("姓名"),
                values.get("性别"),
                values.get("出生日期"),
                values.get("电话"),
                values.get("班级")
            ));

        Student student = dialog.showAndWait();
        if (student != null && adminService.addStudent(student)) {
            showInfo("添加成功");
            loadData();
        } else {
            showError("添加失败");
        }
    }

    private void handleEdit() {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("请选择要编辑的学生");
            return;
        }

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("学号", selected.getStudentId());
        fields.put("姓名", selected.getName());
        fields.put("性别", selected.getGender());
        fields.put("出生日期", selected.getBirthDate());
        fields.put("电话", selected.getPhone());
        fields.put("班级", selected.getClassName());

        FormDialog<Student> dialog = new FormDialog<>("编辑学生", fields, 
            values -> new Student(
                values.getOrDefault("学号", selected.getStudentId()),
                values.getOrDefault("姓名", selected.getName()),
                values.getOrDefault("性别", selected.getGender()),
                values.getOrDefault("出生日期", selected.getBirthDate()),
                values.getOrDefault("电话", selected.getPhone()),
                values.getOrDefault("班级", selected.getClassName())
            ));

        Student student = dialog.showAndWait();
        if (student != null && adminService.updateStudent(student)) {
            showInfo("修改成功");
            loadData();
        } else {
            showError("修改失败");
        }
    }

    private void handleDelete() {
        Student selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("请选择要删除的学生");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认删除");
        alert.setContentText("确定要删除该学生吗？");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (adminService.deleteStudent(selected.getStudentId())) {
                    showInfo("删除成功");
                    loadData();
                } else {
                    showError("删除失败");
                }
            }
        });
    }

    private void loadData() {
        tableView.getItems().setAll(adminService.getAllStudents());
    }
}
