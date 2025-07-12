package ui.view;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import service.AdminService;

import java.util.List;

public class ScheduleView extends BaseTableView {
    private TextArea conflictArea;
    
    public ScheduleView(AdminService adminService) {
        super(adminService);
        initialize();
    }
    
    private void initialize() {
        Button checkBtn = new Button("检查课表冲突");
        conflictArea = new TextArea();
        conflictArea.setEditable(false);
        conflictArea.setPrefRowCount(10);

        checkBtn.setOnAction(e -> checkConflicts());

        getChildren().addAll(checkBtn, conflictArea);
    }
    
    private void checkConflicts() {
        List<String> conflicts = adminService.getConflicts();
        if (conflicts.isEmpty()) {
            conflictArea.setText("没有发现课程冲突。");
        } else {
            conflictArea.setText("发现以下冲突：\n" + 
                String.join("\n", conflicts));
        }
    }
}
