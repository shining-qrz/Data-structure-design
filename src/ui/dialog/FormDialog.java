package ui.dialog;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;  // 添加这行导入
import java.util.Map;

public class FormDialog<T> {
    private Stage stage;
    private Map<String, String> fields;
    private String title;
    private FormCallback<T> callback;

    public interface FormCallback<T> {
        T createFromFields(Map<String, String> fields);
    }

    public FormDialog(String title, Map<String, String> fields, FormCallback<T> callback) {
        this.title = title;
        this.fields = fields;
        this.callback = callback;
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    public T showAndWait() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Map<String, TextField> textFields = new java.util.HashMap<>();
        int row = 0;
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            Label label = new Label(entry.getKey() + ":");
            TextField field = new TextField();
            field.setPromptText(entry.getValue());
            // 如果值不为null，则设置为当前值
            if (entry.getValue() != null && !entry.getValue().startsWith("请输入")) {
                field.setText(entry.getValue());
            }
            textFields.put(entry.getKey(), field);
            grid.add(label, 0, row);
            grid.add(field, 1, row);
            row++;
        }

        Button submitBtn = new Button("确定");
        Button cancelBtn = new Button("取消");
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(submitBtn, cancelBtn);

        final T[] result = (T[]) new Object[1];
        submitBtn.setOnAction(e -> {
            Map<String, String> values = new java.util.HashMap<>();
            for (Map.Entry<String, TextField> entry : textFields.entrySet()) {
                String value = entry.getValue().getText();
                // 只保存修改过的字段
                if (!value.isEmpty()) {
                    values.put(entry.getKey(), value);
                } else if (fields.get(entry.getKey()) != null && 
                          !fields.get(entry.getKey()).startsWith("请输入")) {
                    values.put(entry.getKey(), fields.get(entry.getKey()));
                }
            }
            result[0] = callback.createFromFields(values);
            stage.close();
        });

        cancelBtn.setOnAction(e -> stage.close());

        content.getChildren().addAll(new Label(title), grid, buttonBox);

        Scene scene = new Scene(content);
        scene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
        
        stage.setScene(scene);
        stage.setTitle(title);
        stage.showAndWait();

        return result[0];
    }
}
