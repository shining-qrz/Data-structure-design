package dao;
import java.io.*;
import java.util.*;
import entity.Student;

public class StudentDao implements FileDao<Student> {
    private static final String FILE_PATH = "src/data/students.txt";

    @Override
    public void save(List<Student> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student student : items) {
                writer.write(student.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> load() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                students.add(new Student(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public void append(Student item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(item.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Student item) {
        List<Student> students = load();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(item.getStudentId())) {
                students.set(i, item);
                save(students);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        List<Student> students = load();
        boolean removed = students.removeIf(s -> s.getStudentId().equals(id));
        if (removed) {
            save(students);
        }
        return removed;
    }
}
