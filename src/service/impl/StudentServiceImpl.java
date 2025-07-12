package service.impl;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import entity.*;
import dao.*;
import service.StudentService;

public class StudentServiceImpl implements StudentService {
    private StudentDao studentDao = new StudentDao();
    private FeedbackDao feedbackDao = new FeedbackDao();
    private CourseDao courseDao = new CourseDao();

    @Override
    public boolean updatePersonalInfo(Student student) {
        return studentDao.update(student);
    }

    @Override
    public Student getPersonalInfo(String studentId) {
        return studentDao.load().stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean submitFeedback(Feedback feedback) {
        try {
            feedbackDao.append(feedback);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Course> getClassSchedule(String className) {
        return courseDao.load().stream()
                .filter(c -> c.getClassName().equals(className))
                .collect(Collectors.toList());
    }

    @Override
    public boolean exportSchedule(String className, String filePath) {
        List<Course> courses = getClassSchedule(className);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("课程表 - " + className + "\n");
            writer.write("课程号,课程名,学时,学分,上课时间,教室,教师工号\n");
            for (Course course : courses) {
                writer.write(course.toString() + "\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
