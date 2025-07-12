package dao;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import entity.Feedback;

public class FeedbackDao implements FileDao<Feedback> {
    private static final String FILE_PATH = "src/data/feedbacks.txt";

    @Override
    public void save(List<Feedback> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Feedback feedback : items) {
                writer.write(feedback.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Feedback> load() {
        List<Feedback> feedbacks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                feedbacks.add(new Feedback(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }

    @Override
    public void append(Feedback item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(item.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(Feedback item) {
        List<Feedback> feedbacks = load();
        for (int i = 0; i < feedbacks.size(); i++) {
            if (feedbacks.get(i).getStudentId().equals(item.getStudentId()) &&
                feedbacks.get(i).getCourseId().equals(item.getCourseId())) {
                feedbacks.set(i, item);
                save(feedbacks);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        // 由于反馈需要studentId和courseId两个标识，这里不实现单一id的删除
        return false;
    }


    // 获取教师的所有反馈
    public List<Feedback> getFeedbacksByTeacher(String teacherId) {
        return load().stream()
                .filter(f -> f.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }
}
