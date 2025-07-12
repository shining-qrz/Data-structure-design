package util;
import java.util.*;
import java.io.*;
import entity.*;
import dao.*;

public class DataInitializer {
    public static void initializeData() {
        // 如果所有数据文件都不为空，则不进行初始化
        if (!needInitialization()) {
            System.out.println("检测到已存在数据，跳过初始化。");
            return;
        }
        
        initializeTeachers();
        initializeStudents();
        initializeClassrooms();
        initializeCourses();
        System.out.println("数据初始化完成。");
    }

    private static boolean needInitialization() {
        TeacherDao teacherDao = new TeacherDao();
        StudentDao studentDao = new StudentDao();
        CourseDao courseDao = new CourseDao();
        ClassroomDao classroomDao = new ClassroomDao();
        
        // 检查各数据文件是否为空
        return teacherDao.load().isEmpty() &&
               studentDao.load().isEmpty() &&
               courseDao.load().isEmpty() &&
               classroomDao.load().isEmpty();
    }

    private static void initializeTeachers() {
        TeacherDao teacherDao = new TeacherDao();
        List<Teacher> teachers = new ArrayList<>();
        
        // 添加20位教师数据
        for (int i = 1; i <= 20; i++) {
            String id = String.format("T%03d", i);
            teachers.add(new Teacher(id, "教师" + i, i % 2 == 0 ? "男" : "女",
                "1980-01-01", "13800" + String.format("%06d", i)));
        }
        
        teacherDao.save(teachers);
    }

    private static void initializeStudents() {
        StudentDao studentDao = new StudentDao();
        List<Student> students = new ArrayList<>();
        
        // 添加5个班，每班32人
        for (int classNum = 1; classNum <= 5; classNum++) {
            String className = "计科230" + classNum;
            for (int i = 1; i <= 32; i++) {
                String id = String.format("2023%d%02d", classNum, i);
                students.add(new Student(id, "学生" + id, i % 2 == 0 ? "男" : "女",
                    "2005-01-01", "13900" + String.format("%06d", i), className));
            }
        }
        
        studentDao.save(students);
    }

    private static void initializeClassrooms() {
        ClassroomDao classroomDao = new ClassroomDao();
        List<Classroom> classrooms = new ArrayList<>();
        
        // 添加20个教室
        for (int i = 1; i <= 20; i++) {
            String id = String.format("R%03d", i);
            classrooms.add(new Classroom(id, i + "号教室", 40));
        }
        
        classroomDao.save(classrooms);
    }

    private static void initializeCourses() {
        CourseDao courseDao = new CourseDao();
        List<Course> courses = new ArrayList<>();
        
        String[] courseNames = {"高等数学", "大学物理", "程序设计", "数据结构", "计算机网络"};
        
        // 为每个班添加这些课程
        for (int classNum = 1; classNum <= 5; classNum++) {
            String className = "计科230" + classNum;
            for (int i = 0; i < courseNames.length; i++) {
                String id = String.format("C%d%d", classNum, i+1);
                Course course = new Course(id, courseNames[i], 48, 3.0);
                course.setClassName(className);
                course.setTeacherId(String.format("T%03d", (classNum-1)*4 + i + 1));
                courses.add(course);
            }
        }
        
        courseDao.save(courses);
    }
}
