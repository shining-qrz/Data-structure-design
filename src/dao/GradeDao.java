package dao;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import entity.Grade;

public class GradeDao implements FileDao<Grade> {
    private static final String FILE_PATH = "src/data/grades.txt";

    @Override
    public void save(List<Grade> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Grade grade : items) {
                writer.write(grade.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Grade> load() {
        List<Grade> grades = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                grades.add(new Grade(parts[0], parts[1], Double.parseDouble(parts[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public void append(Grade item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(item.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Grade item) {
        List<Grade> grades = load();
        for (int i = 0; i < grades.size(); i++) {
            if (grades.get(i).getCourseId().equals(item.getCourseId()) && 
                grades.get(i).getStudentId().equals(item.getStudentId())) {
                grades.set(i, item);
                save(grades);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        // 由于成绩需要courseId和studentId两个标识，这里不实现单一id的删除
        return false;
    }

    // 获取指定课程的所有成绩
    public List<Grade> getGradesByCourse(String courseId) {
        return load().stream()
                .filter(g -> g.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }
}
