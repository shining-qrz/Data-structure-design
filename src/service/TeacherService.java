package service;

import java.util.List;
import entity.Grade;
import entity.Course;
import entity.Feedback;

public interface TeacherService {
    // 成绩管理
    boolean inputGrade(Grade grade);
    boolean updateGrade(Grade grade);
    List<Grade> getGradesByClass(String courseId, String className);
    
    // 反馈查看
    List<Feedback> getFeedbacks(String teacherId);
    
    // 成绩排序
    List<Grade> getSortedGrades(String courseId, String className);
    
    // 课表管理
    List<Course> getTeacherSchedule(String teacherId);
    boolean exportSchedule(String teacherId, String filePath);
}
