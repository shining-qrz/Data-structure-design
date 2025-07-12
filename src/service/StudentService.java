package service;

import java.util.List;
import entity.Student;
import entity.Course;
import entity.Feedback;

public interface StudentService {
    // 个人信息管理
    boolean updatePersonalInfo(Student student);
    Student getPersonalInfo(String studentId);
    
    // 教学反馈
    boolean submitFeedback(Feedback feedback);
    
    // 课表查询
    List<Course> getClassSchedule(String className);
    
    // 课表导出
    boolean exportSchedule(String className, String filePath);
}
