package service.impl;
import java.util.*;
import entity.*;
import dao.*;
import service.AdminService;
import util.CourseScheduler;
import util.TimeSlot;

public class AdminServiceImpl implements AdminService {
    private TeacherDao teacherDao = new TeacherDao();
    private StudentDao studentDao = new StudentDao();
    private CourseDao courseDao = new CourseDao();
    private ClassroomDao classroomDao = new ClassroomDao();

    @Override
    public boolean addTeacher(Teacher teacher) {
        try {
            teacherDao.append(teacher);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean arrangeCourses() {
        List<Course> courses = courseDao.load();
        List<Classroom> classrooms = classroomDao.load();
        
        // 使用CourseScheduler生成课表
        Map<String, List<TimeSlot>> scheduleMap = CourseScheduler.generateSchedule(courses, classrooms);
        
        // 保存排课结果
        boolean success = true;
        for (Course course : courses) {
            if (!courseDao.update(course)) {
                success = false;
                break;
            }
        }
        
        return success;
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        return teacherDao.update(teacher);
    }

    @Override
    public boolean deleteTeacher(String teacherId) {
        return teacherDao.delete(teacherId);
    }

    @Override
    public Teacher getTeacher(String teacherId) {
        return teacherDao.load().stream()
                .filter(t -> t.getTeacherId().equals(teacherId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherDao.load();
    }

    @Override
    public boolean validateSchedule() {
        List<String> conflicts = CourseScheduler.validateSchedule(courseDao.load());
        return conflicts.isEmpty();
    }

    @Override
    public List<String> getConflicts() {
        return CourseScheduler.validateSchedule(courseDao.load());
    }

    @Override
    public boolean modifySchedule(Course course) {
        return courseDao.update(course);
    }

    // 添加学生管理方法实现
    @Override
    public boolean addStudent(Student student) {
        try {
            studentDao.append(student);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateStudent(Student student) {
        return studentDao.update(student);
    }

    @Override
    public boolean deleteStudent(String studentId) {
        return studentDao.delete(studentId);
    }

    @Override
    public Student getStudent(String studentId) {
        return studentDao.load().stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDao.load();
    }

    // 添加课程管理方法实现
    @Override
    public boolean addCourse(Course course) {
        try {
            courseDao.append(course);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateCourse(Course course) {
        return courseDao.update(course);
    }

    @Override
    public boolean deleteCourse(String courseId) {
        return courseDao.delete(courseId);
    }

    @Override
    public Course getCourse(String courseId) {
        return courseDao.load().stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.load();
    }

}
