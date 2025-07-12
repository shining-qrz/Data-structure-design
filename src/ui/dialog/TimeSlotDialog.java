package ui.dialog;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import util.TimeSlot;

public class TimeSlotDialog {
    private Stage stage;
    private TimeSlot result;
    private VBox content = new VBox(10);

    public TimeSlotDialog(String title) {
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
    }

    public TimeSlot showAndWait() {
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        // 创建输入控件
        ComboBox<Integer> weekStartCombo = new ComboBox<>();
        ComboBox<Integer> weekEndCombo = new ComboBox<>();
        ComboBox<Integer> dayCombo = new ComboBox<>();
        ComboBox<Integer> periodStartCombo = new ComboBox<>();
        ComboBox<Integer> periodEndCombo = new ComboBox<>();

        // 设置可选值
        for (int i = 1; i <= 18; i++) {
            weekStartCombo.getItems().add(i);
            weekEndCombo.getItems().add(i);
        }
        for (int i = 1; i <= 5; i++) {
            dayCombo.getItems().add(i);
        }
        for (int i = 1; i <= 10; i++) {
            periodStartCombo.getItems().add(i);
            periodEndCombo.getItems().add(i);
        }

        // 设置默认值
        weekStartCombo.setValue(1);
        weekEndCombo.setValue(18);
        dayCombo.setValue(1);
        periodStartCombo.setValue(1);
        periodEndCombo.setValue(2);

        // 创建输入表单
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("起始周:"), 0, 0);
        grid.add(weekStartCombo, 1, 0);
        grid.add(new Label("结束周:"), 0, 1);
        grid.add(weekEndCombo, 1, 1);
        grid.add(new Label("星期:"), 0, 2);
        grid.add(dayCombo, 1, 2);
        grid.add(new Label("起始节:"), 0, 3);
        grid.add(periodStartCombo, 1, 3);
        grid.add(new Label("结束节:"), 0, 4);
        grid.add(periodEndCombo, 1, 4);

        // 添加按钮
        Button submitBtn = new Button("确定");
        Button cancelBtn = new Button("取消");
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(submitBtn, cancelBtn);

        submitBtn.setOnAction(e -> {
            // 创建时间段，确保格式为"周数;星期;节数"
            result = new TimeSlot(
                weekStartCombo.getValue(),
                weekEndCombo.getValue(),
                dayCombo.getValue(),
                periodStartCombo.getValue(),
                periodEndCombo.getValue()
            );
            stage.close();
        });

        cancelBtn.setOnAction(e -> {
            result = null;
            stage.close();
        });

        content.getChildren().addAll(grid, buttonBox);

        Scene scene = new Scene(content);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        stage.setScene(scene);
        stage.showAndWait();

        return result;
    }
}
