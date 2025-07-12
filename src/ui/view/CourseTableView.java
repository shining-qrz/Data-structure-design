package ui.view;

import entity.Course;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import service.AdminService;
import ui.dialog.*;
import util.TimeSlot;
import java.util.*;

public class CourseTableView extends BaseTableView {
    private TableView<Course> tableView;
    
    public CourseTableView(AdminService adminService) {
        super(adminService);
        initialize();
    }
    
    private void initialize() {
        tableView = createTableView();
        HBox controlBox = createControlBox();
        getChildren().addAll(controlBox, tableView);
        loadData();
    }
    
    private TableView<Course> createTableView() {
        TableView<Course> table = new TableView<>();
        
        TableColumn<Course, String> idCol = new TableColumn<>("课程号");
        TableColumn<Course, String> nameCol = new TableColumn<>("课程名称");
        TableColumn<Course, Number> hoursCol = new TableColumn<>("学时");
        TableColumn<Course, Number> creditsCol = new TableColumn<>("学分");
        TableColumn<Course, String> timeCol = new TableColumn<>("时间");
        TableColumn<Course, String> roomCol = new TableColumn<>("教室");
        TableColumn<Course, String> teacherCol = new TableColumn<>("教师工号");
        TableColumn<Course, String> classCol = new TableColumn<>("班级");

        idCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hoursCol.setCellValueFactory(new PropertyValueFactory<>("hours"));
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("credits"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        roomCol.setCellValueFactory(new PropertyValueFactory<>("classroom"));      // 修正：显示教室号
        teacherCol.setCellValueFactory(new PropertyValueFactory<>("teacherId"));
        classCol.setCellValueFactory(new PropertyValueFactory<>("className"));     // 修正：显示班级

        // 设置时间列的宽度和换行
        timeCol.setPrefWidth(200);  // 增加宽度以容纳更多文本
        timeCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-wrap-text: true;");

        table.getColumns().addAll(idCol, nameCol, hoursCol, creditsCol, 
                                timeCol, roomCol, teacherCol, classCol);
        return table;
    }
    
    private HBox createControlBox() {
        HBox controlBox = new HBox(10);
        
        // 添加搜索功能
        TextField searchField = new TextField();
        searchField.setPromptText("输入课程号或名称搜索");
        Button searchBtn = new Button("搜索");
        searchBtn.setOnAction(e -> handleSearch(searchField.getText()));

        // 创建管理按钮
        Button addBtn = new Button("添加课程");
        Button editBtn = new Button("编辑课程");
        Button deleteBtn = new Button("删除课程");
        Button arrangeBtn = new Button("自动排课");
        Button modifyTimeBtn = new Button("修改时间");

        addBtn.setOnAction(e -> handleAdd());
        editBtn.setOnAction(e -> handleEdit());
        deleteBtn.setOnAction(e -> handleDelete());
        arrangeBtn.setOnAction(e -> handleArrange());
        modifyTimeBtn.setOnAction(e -> handleModifyTime());
        
        // 创建筛选功能
        ComboBox<String> classFilter = new ComboBox<>();
        classFilter.setPromptText("按班级筛选");
        classFilter.getItems().addAll(getDistinctClasses());
        classFilter.setOnAction(e -> handleFilter(classFilter.getValue()));
        
        controlBox.getChildren().addAll(
            searchField, 
            searchBtn, 
            new Separator(Orientation.VERTICAL),
            addBtn, 
            editBtn, 
            deleteBtn, 
            arrangeBtn, 
            modifyTimeBtn,
            new Separator(Orientation.VERTICAL),
            classFilter
        );
        return controlBox;
    }

    private void handleSearch(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadData();
            return;
        }
        
        List<Course> courses = adminService.getAllCourses();
        tableView.getItems().setAll(courses.stream()
            .filter(c -> c.getCourseId().contains(keyword) || 
                        c.getName().contains(keyword))
            .toList());
    }

    private void handleFilter(String className) {
        if (className == null) {
            loadData();
            return;
        }
        
        List<Course> courses = adminService.getAllCourses();
        tableView.getItems().setAll(courses.stream()
            .filter(c -> c.getClassName().equals(className))
            .toList());
    }

    private List<String> getDistinctClasses() {
        return adminService.getAllCourses().stream()
            .map(Course::getClassName)
            .distinct()
            .sorted()
            .toList();
    }

    private void handleArrange() {
        if (adminService.arrangeCourses()) {
            showInfo("排课成功");
            loadData();
        } else {
            showError("排课失败");
        }
    }

    private void handleModifyTime() {
        Course selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("请选择要修改时间的课程");
            return;
        }
        
        TimeSlotDialog dialog = new TimeSlotDialog("修改课程时间");
        TimeSlot timeSlot = dialog.showAndWait();
        if (timeSlot != null) {
            // 确保时间信息只存储在timeSlot字段中
            selected.setTimeSlot(timeSlot.toString());
            if (adminService.updateCourse(selected)) {
                showInfo("修改时间成功");
                loadData();
            } else {
                showError("修改时间失败");
            }
        }
    }

    private void handleAdd() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("课程号", "请输入课程号");
        fields.put("课程名称", "请输入课程名称");
        fields.put("学时", "请输入学时数");
        fields.put("学分", "请输入学分数");
        fields.put("教师工号", "请输入教师工号");
        fields.put("班级", "请输入班级");
        fields.put("教室", "请输入教室号");

        FormDialog<Course> dialog = new FormDialog<>("添加课程", fields, 
            values -> new Course(
                values.get("课程号"),
                values.get("课程名称"),
                Integer.parseInt(values.get("学时")),
                Double.parseDouble(values.get("学分"))
            ) {{
                setTeacherId(values.get("教师工号"));
                setClassName(values.get("班级"));
                setClassroom(values.get("教室"));
            }});

        Course course = dialog.showAndWait();
        if (course != null && adminService.addCourse(course)) {
            showInfo("添加成功");
            loadData();
        } else {
            showError("添加失败");
        }
    }

    private void handleEdit() {
        Course selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("请选择要编辑的课程");
            return;
        }

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("课程号", selected.getCourseId());
        fields.put("课程名称", selected.getName());
        fields.put("学时", String.valueOf(selected.getHours()));
        fields.put("学分", String.valueOf(selected.getCredits()));
        fields.put("教师工号", selected.getTeacherId());
        fields.put("班级", selected.getClassName());

        FormDialog<Course> dialog = new FormDialog<>("编辑课程", fields, 
            values -> new Course(
                values.get("课程号"),
                values.get("课程名称"),
                Integer.parseInt(values.get("学时")),
                Double.parseDouble(values.get("学分"))
            ) {{
                setTeacherId(values.get("教师工号"));
                setClassName(values.get("班级"));
                setTimeSlot(selected.getTimeSlot());
                setClassroom(selected.getClassroom());
            }});

        Course course = dialog.showAndWait();
        if (course != null && adminService.updateCourse(course)) {
            showInfo("修改成功");
            loadData();
        } else {
            showError("修改失败");
        }
    }

    private void handleDelete() {
        Course selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("请选择要删除的课程");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("确认删除");
        alert.setContentText("确定要删除该课程吗？");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (adminService.deleteCourse(selected.getCourseId())) {
                    showInfo("删除成功");
                    loadData();
                } else {
                    showError("删除失败");
                }
            }
        });
    }

    private void loadData() {
        tableView.getItems().setAll(adminService.getAllCourses());
    }
}
