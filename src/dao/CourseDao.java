package dao;
import java.io.*;
import java.util.*;
import entity.Course;

public class CourseDao implements FileDao<Course> {
    private static final String FILE_PATH = "src/data/courses.txt";

    @Override
    public void save(List<Course> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Course course : items) {
                // 确保数据格式统一
                String line = String.format("%s,%s,%d,%.1f,%s,%s,%s,%s",
                    course.getCourseId(),
                    course.getName(),
                    course.getHours(),
                    course.getCredits(),
                    course.getTimeSlot() != null ? course.getTimeSlot() : "",
                    course.getClassroom() != null ? course.getClassroom() : "",
                    course.getTeacherId() != null ? course.getTeacherId() : "",
                    course.getClassName() != null ? course.getClassName() : "");
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> load() {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Course course = new Course(parts[0], parts[1], 
                    Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
                // 修改数据解析逻辑
                if (parts.length > 4) {
                    StringBuilder timeSlot = new StringBuilder(parts[4]);
                    // 如果存在时间信息，合并为标准格式
                    if (parts.length > 5 && parts[5].startsWith("周")) {
                        timeSlot.append(";").append(parts[5]);
                        if (parts.length > 6 && parts[6].contains("节")) {
                            timeSlot.append(";").append(parts[6]);
                        }
                    }
                    course.setTimeSlot(timeSlot.toString());
                    
                    // 根据实际数据位置读取其他信息
                    for (int i = 5; i < parts.length; i++) {
                        if (parts[i].startsWith("R")) {
                            course.setClassroom(parts[i]);
                        } else if (parts[i].startsWith("T")) {
                            course.setTeacherId(parts[i]);
                        } else if (parts[i].startsWith("计科")) {
                            course.setClassName(parts[i]);
                        }
                    }
                }
                courses.add(course);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override 
    public void append(Course item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(item.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Course item) {
        List<Course> courses = load();
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId().equals(item.getCourseId())) {
                courses.set(i, item);
                save(courses);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        List<Course> courses = load();
        boolean removed = courses.removeIf(c -> c.getCourseId().equals(id));
        if (removed) {
            save(courses);
        }
        return removed;
    }
}
