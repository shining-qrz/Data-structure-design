package ui.view;

import entity.Teacher;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import service.AdminService;
import ui.dialog.FormDialog;
import java.util.*;

public class TeacherTableView extends BaseTableView {
    private TableView<Teacher> tableView;
    
    public TeacherTableView(AdminService adminService) {
        super(adminService);
        initialize();
    }
    
    private void initialize() {
        tableView = createTableView();
        HBox buttonBox = createButtonBox();
        getChildren().addAll(buttonBox, tableView);
        loadData();
    }
    
    private TableView<Teacher> createTableView() {
        TableView<Teacher> table = new TableView<>();
        
        TableColumn<Teacher, String> idCol = new TableColumn<>("工号");
        TableColumn<Teacher, String> nameCol = new TableColumn<>("姓名");
        TableColumn<Teacher, String> genderCol = new TableColumn<>("性别");
        TableColumn<Teacher, String> birthCol = new TableColumn<>("出生日期");
        TableColumn<Teacher, String> phoneCol = new TableColumn<>("电话");

        idCol.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        table.getColumns().addAll(idCol, nameCol, genderCol, birthCol, phoneCol);
        return table;
    }
    
    private HBox createButtonBox() {
        HBox buttonBox = new HBox(10);
        Button addBtn = new Button("添加");
        Button editBtn = new Button("编辑");
        Button deleteBtn = new Button("删除");
        
        addBtn.setOnAction(e -> handleAdd());
        editBtn.setOnAction(e -> handleEdit());
        deleteBtn.setOnAction(e -> handleDelete());
        
        buttonBox.getChildren().addAll(addBtn, editBtn, deleteBtn);
        return buttonBox;
    }
    
    private void handleAdd() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("工号", "请输入工号");
        fields.put("姓名", "请输入姓名");
        fields.put("性别", "请输入性别");
        fields.put("出生日期", "请输入出生日期(yyyy-MM-dd)");
        fields.put("电话", "请输入电话");

        FormDialog<Teacher> dialog = new FormDialog<>("添加教师", fields, 
            values -> new Teacher(
                values.get("工号"),
                values.get("姓名"),
                values.get("性别"),
                values.get("出生日期"),
                values.get("电话")
            ));

        Teacher teacher = dialog.showAndWait();
        if (teacher != null && adminService.addTeacher(teacher)) {
            showInfo("添加成功");
            loadData();
        } else {
            showError("添加失败");
        }
    }
    
    private void handleEdit() {
        Teacher selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("请选择要编辑的教师");
            return;
        }

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("工号", selected.getTeacherId());
        fields.put("姓名", selected.getName());
        fields.put("性别", selected.getGender());
        fields.put("出生日期", selected.getBirthDate());
        fields.put("电话", selected.getPhone());

        FormDialog<Teacher> dialog = new FormDialog<>("编辑教师", fields, 
            values -> new Teacher(
                values.getOrDefault("工号", selected.getTeacherId()),
                values.getOrDefault("姓名", selected.getName()),
                values.getOrDefault("性别", selected.getGender()),
                values.getOrDefault("出生日期", selected.getBirthDate()),
                values.getOrDefault("电话", selected.getPhone())
            ));

        Teacher teacher = dialog.showAndWait();
        if (teacher != null && adminService.updateTeacher(teacher)) {
            showInfo("修改成功");
            loadData();
        } else {
            showError("修改失败");
        }
    }
    
    private void handleDelete() {
        Teacher selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("请选择要删除的教师");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认删除");
        alert.setContentText("确定要删除该教师吗？");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (adminService.deleteTeacher(selected.getTeacherId())) {
                    showInfo("删除成功");
                    loadData();
                } else {
                    showError("删除失败");
                }
            }
        });
    }
    
    public void loadData() {
        tableView.getItems().setAll(adminService.getAllTeachers());
    }
}
