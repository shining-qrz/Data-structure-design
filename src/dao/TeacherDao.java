package dao;
import java.io.*;
import java.util.*;
import entity.Teacher;

public class TeacherDao implements FileDao<Teacher> {
    private static final String FILE_PATH = "src/data/teachers.txt";

    @Override
    public void save(List<Teacher> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Teacher teacher : items) {
                writer.write(teacher.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Teacher> load() {
        List<Teacher> teachers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                teachers.add(new Teacher(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teachers;
    }

    @Override
    public void append(Teacher item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(item.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Teacher item) {
        List<Teacher> teachers = load();
        for (int i = 0; i < teachers.size(); i++) {
            if (teachers.get(i).getTeacherId().equals(item.getTeacherId())) {
                teachers.set(i, item);
                save(teachers);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        List<Teacher> teachers = load();
        boolean removed = teachers.removeIf(t -> t.getTeacherId().equals(id));
        if (removed) {
            save(teachers);
        }
        return removed;
    }
}
