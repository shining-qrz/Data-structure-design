package service;

import java.util.List;
import entity.Teacher;
import entity.Student;
import entity.Course;

public interface AdminService {
    // 教师管理
    boolean addTeacher(Teacher teacher);
    boolean updateTeacher(Teacher teacher);
    boolean deleteTeacher(String teacherId);
    Teacher getTeacher(String teacherId);
    List<Teacher> getAllTeachers();
    
    // 学生管理
    boolean addStudent(Student student);
    boolean updateStudent(Student student);
    boolean deleteStudent(String studentId);
    Student getStudent(String studentId);
    List<Student> getAllStudents();
    
    // 课程管理
    boolean addCourse(Course course);
    boolean updateCourse(Course course);
    boolean deleteCourse(String courseId);
    Course getCourse(String courseId);
    List<Course> getAllCourses();
    
    // 排课相关
    boolean arrangeCourses();
    boolean validateSchedule();
    List<String> getConflicts();
    boolean modifySchedule(Course course);
}
