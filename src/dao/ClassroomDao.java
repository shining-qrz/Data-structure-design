package dao;

import java.io.*;
import java.util.*;
import entity.Classroom;  // 添加Classroom实体类的导入

public class ClassroomDao implements FileDao<Classroom> {
    private static final String FILE_PATH = "src/data/classrooms.txt";

    @Override
    public void save(List<Classroom> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Classroom classroom : items) {
                writer.write(classroom.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Classroom> load() {
        List<Classroom> classrooms = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                classrooms.add(new Classroom(parts[0], parts[1], Integer.parseInt(parts[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classrooms;
    }

    @Override
    public void append(Classroom item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(item.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Classroom item) {
        List<Classroom> classrooms = load();
        for (int i = 0; i < classrooms.size(); i++) {
            if (classrooms.get(i).getRoomId().equals(item.getRoomId())) {
                classrooms.set(i, item);
                save(classrooms);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        List<Classroom> classrooms = load();
        boolean removed = classrooms.removeIf(c -> c.getRoomId().equals(id));
        if (removed) {
            save(classrooms);
        }
        return removed;
    }
}
