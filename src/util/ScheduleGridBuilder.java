package util;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import entity.Course;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class ScheduleGridBuilder {
    private static final String[] DAYS = {"周一", "周二", "周三", "周四", "周五"};
    private static final String[] TIMES = {"1-2节", "3-4节", "5-6节", "7-8节", "9-10节"};
    private static final Border CELL_BORDER = new Border(
        new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)
    );

    public static GridPane buildScheduleGrid(List<Course> courses) {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setPadding(new Insets(10));
        grid.getStyleClass().add("schedule-grid");

        // 添加表头
        for (int i = 0; i < DAYS.length; i++) {
            addHeaderCell(grid, DAYS[i], i + 1, 0);
        }
        for (int i = 0; i < TIMES.length; i++) {
            addHeaderCell(grid, TIMES[i], 0, i + 1);
        }

        // 添加网格单元格
        for (int i = 1; i <= DAYS.length; i++) {
            for (int j = 1; j <= TIMES.length; j++) {
                addEmptyCell(grid, i, j);
            }
        }

        // 填充课程信息
        for (Course course : courses) {
            if (course.getTimeSlot() != null) {
                TimeSlot slot = TimeSlot.fromString(course.getTimeSlot());
                if (slot != null) {
                    addCourseCell(grid, course, slot);
                }
            }
        }

        return grid;
    }

    private static void addHeaderCell(GridPane grid, String text, int col, int row) {
        Label label = new Label(text);
        StackPane cell = new StackPane(label);
        cell.getStyleClass().add("header-cell");
        grid.add(cell, col, row);
    }

    private static void addEmptyCell(GridPane grid, int col, int row) {
        StackPane cell = new StackPane();
        cell.setBorder(CELL_BORDER);
        cell.setMinSize(100, 50);
        grid.add(cell, col, row);
    }

    private static void addCourseCell(GridPane grid, Course course, TimeSlot slot) {
        Label courseLabel = new Label(String.format("%s\n%s\n%s",
            course.getName(),
            course.getClassroom(),
            course.getClassName()));
        courseLabel.setWrapText(true);
        courseLabel.setAlignment(Pos.CENTER);
        
        StackPane cell = new StackPane(courseLabel);
        cell.getStyleClass().add("course-cell");
        
        grid.add(cell, slot.getDayOfWeek(), (slot.getStartPeriod() + 1) / 2);
    }
}
