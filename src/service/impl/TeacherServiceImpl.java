package service.impl;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import entity.*;
import dao.*;
import service.TeacherService;

public class TeacherServiceImpl implements TeacherService {
    private GradeDao gradeDao = new GradeDao();
    private FeedbackDao feedbackDao = new FeedbackDao();
    private CourseDao courseDao = new CourseDao();
    private StudentDao studentDao = new StudentDao();

    @Override
    public boolean inputGrade(Grade grade) {
        try {
            gradeDao.append(grade);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Grade> getSortedGrades(String courseId, String className) {
        List<Grade> grades = gradeDao.getGradesByCourse(courseId);
        grades.sort((g1, g2) -> {
            int scoreCompare = g2.getScore().compareTo(g1.getScore());
            return scoreCompare != 0 ? scoreCompare : 
                   g1.getStudentId().compareTo(g2.getStudentId());
        });
        return grades;
    }

    @Override
    public List<Course> getTeacherSchedule(String teacherId) {
        return courseDao.load().stream()
                .filter(c -> c.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateGrade(Grade grade) {
        return gradeDao.update(grade);
    }

    @Override
    public List<Grade> getGradesByClass(String courseId, String className) {
        List<Grade> allGrades = gradeDao.getGradesByCourse(courseId);
        List<String> classStudentIds = studentDao.load().stream()
                .filter(s -> s.getClassName().equals(className))
                .map(Student::getStudentId)
                .collect(Collectors.toList());
        
        return allGrades.stream()
                .filter(g -> classStudentIds.contains(g.getStudentId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Feedback> getFeedbacks(String teacherId) {
        return feedbackDao.getFeedbacksByTeacher(teacherId);
    }

    @Override
    public boolean exportSchedule(String teacherId, String filePath) {
        List<Course> courses = getTeacherSchedule(teacherId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("教师课表 - 工号: " + teacherId + "\n");
            writer.write("课程号,课程名,班级,上课时间,教室\n");
            for (Course course : courses) {
                writer.write(String.format("%s,%s,%s,%s,%s\n",
                    course.getCourseId(), course.getName(), 
                    course.getClassName(), course.getTimeSlot(),
                    course.getClassroom()));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
