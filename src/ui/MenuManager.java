package ui;

import java.util.*;
import entity.*;
import service.*;
import service.impl.*;

public class MenuManager {
    private static AdminService adminService = new AdminServiceImpl();
    private static StudentService studentService = new StudentServiceImpl();
    private static TeacherService teacherService = new TeacherServiceImpl();
    private static Scanner scanner = new Scanner(System.in);

    public static void showAdminMenu() {
        while (true) {
            System.out.println("\n管理员功能菜单:");
            System.out.println("1. 教师管理");
            System.out.println("2. 学生管理");
            System.out.println("3. 课程管理");
            System.out.println("4. 自动排课");
            System.out.println("5. 检查课表冲突");
            System.out.println("6. 修改课表");
            System.out.println("0. 返回主菜单");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    showTeacherManagement();
                    break;
                case 2:
                    showStudentManagement();
                    break;
                case 3:
                    showCourseManagement();
                    break;
                case 4:
                    handleArrangeCourses();
                    break;
                case 5:
                    checkScheduleConflicts();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入无效，请重试");
            }
        }
    }

    public static void showTeacherMenu() {
        System.out.print("请输入教师工号：");
        String teacherId = scanner.next();
        
        while (true) {
            System.out.println("\n教师功能菜单:");
            System.out.println("1. 录入学生成绩");
            System.out.println("2. 查看教学反馈");
            System.out.println("3. 查看课程成绩排名");
            System.out.println("4. 查看个人课表");
            System.out.println("5. 导出课表");
            System.out.println("0. 返回主菜单");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    handleInputGrades(teacherId);
                    break;
                case 2:
                    showFeedbacks(teacherId);
                    break;
                case 3:
                    showGradeRanking(teacherId);
                    break;
                case 4:
                    showTeacherSchedule(teacherId);
                    break;
                case 5:
                    handleExportTeacherSchedule(teacherId);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入无效，请重试");
            }
        }
    }

    public static void showStudentMenu() {
        System.out.print("请输入学号：");
        String studentId = scanner.next();
        Student student = studentService.getPersonalInfo(studentId);
        if (student == null) {
            System.out.println("未找到学生信息！");
            return;
        }
        
        while (true) {
            System.out.println("\n学生功能菜单:");
            System.out.println("1. 修改个人信息");
            System.out.println("2. 提交教学反馈");
            System.out.println("3. 查看课表");
            System.out.println("4. 导出课表");
            System.out.println("0. 返回主菜单");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    handleUpdateStudentInfo(student);
                    break;
                case 2:
                    handleSubmitFeedback(studentId);
                    break;
                case 3:
                    showClassSchedule(student.getClassName());
                    break;
                case 4:
                    handleExportStudentSchedule(student.getClassName());
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入无效，请重试");
            }
        }
    }

    // 私有辅助方法，从原Main类迁移过来的功能实现
    private static void showTeacherManagement() {
        while (true) {
            System.out.println("\n教师管理菜单:");
            System.out.println("1. 添加教师");
            System.out.println("2. 修改教师信息");
            System.out.println("3. 删除教师");
            System.out.println("4. 查看所有教师");
            System.out.println("0. 返回上级菜单");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    handleAddTeacher();
                    break;
                case 2:
                    handleUpdateTeacher();
                    break;
                case 3:
                    handleDeleteTeacher();
                    break;
                case 4:
                    showAllTeachers();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入无效，请重试");
            }
        }
    }

    private static void showStudentManagement() {
        while (true) {
            System.out.println("\n学生管理菜单:");
            System.out.println("1. 添加学生");
            System.out.println("2. 修改学生信息");
            System.out.println("3. 删除学生");
            System.out.println("4. 查看所有学生");
            System.out.println("0. 返回上级菜单");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    handleAddStudent();
                    break;
                case 2:
                    handleUpdateStudent();
                    break;
                case 3:
                    handleDeleteStudent();
                    break;
                case 4:
                    showAllStudents();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入无效，请重试");
            }
        }
    }

    private static void showCourseManagement() {
        while (true) {
            System.out.println("\n课程管理菜单:");
            System.out.println("1. 添加课程");
            System.out.println("2. 修改课程信息");
            System.out.println("3. 删除课程");
            System.out.println("4. 查看所有课程");
            System.out.println("0. 返回上级菜单");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    handleAddCourse();
                    break;
                case 2:
                    handleUpdateCourse();
                    break;
                case 3:
                    handleDeleteCourse();
                    break;
                case 4:
                    showAllCourses();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("输入无效，请重试");
            }
        }
    }

    // 教师管理相关方法
    private static void handleAddTeacher() {
        System.out.println("请输入教师信息（工号,姓名,性别,出生日期,电话）：");
        scanner.nextLine();
        String[] info = scanner.nextLine().split(",");
        Teacher teacher = new Teacher(info[0], info[1], info[2], info[3], info[4]);
        if (adminService.addTeacher(teacher)) {
            System.out.println("添加成功！");
        } else {
            System.out.println("添加失败！");
        }
    }

    private static void handleUpdateTeacher() {
        System.out.println("请输入要修改的教师工号：");
        String teacherId = scanner.next();
        Teacher teacher = adminService.getTeacher(teacherId);
        if (teacher == null) {
            System.out.println("教师不存在！");
            return;
        }
        System.out.println("请输入新的信息（姓名,性别,出生日期,电话）：");
        scanner.nextLine();
        String[] info = scanner.nextLine().split(",");
        teacher.setName(info[0]);
        teacher.setGender(info[1]);
        teacher.setBirthDate(info[2]);
        teacher.setPhone(info[3]);
        if (adminService.updateTeacher(teacher)) {
            System.out.println("修改成功！");
        } else {
            System.out.println("修改失败！");
        }
    }

    private static void handleDeleteTeacher() {
        System.out.println("请输入要删除的教师工号：");
        String teacherId = scanner.next();
        if (adminService.deleteTeacher(teacherId)) {
            System.out.println("删除成功！");
        } else {
            System.out.println("删除失败！");
        }
    }

    private static void showAllTeachers() {
        List<Teacher> teachers = adminService.getAllTeachers();
        System.out.println("工号\t姓名\t性别\t出生日期\t\t电话");
        for (Teacher teacher : teachers) {
            System.out.printf("%s\t%s\t%s\t%s\t%s\n",
                teacher.getTeacherId(), teacher.getName(),
                teacher.getGender(), teacher.getBirthDate(),
                teacher.getPhone());
        }
    }

    // 学生管理相关方法
    private static void handleAddStudent() {
        System.out.println("请输入学生信息（学号,姓名,性别,出生日期,电话,班级）：");
        scanner.nextLine();
        String[] info = scanner.nextLine().split(",");
        Student student = new Student(info[0], info[1], info[2], info[3], info[4], info[5]);
        if (adminService.addStudent(student)) {
            System.out.println("添加成功！");
        } else {
            System.out.println("添加失败！");
        }
    }

    private static void handleUpdateStudent() {
        System.out.println("请输入要修改的学生学号：");
        String studentId = scanner.next();
        Student student = adminService.getStudent(studentId);
        if (student == null) {
            System.out.println("学生不存在！");
            return;
        }
        System.out.println("请输入新的信息（姓名,性别,出生日期,电话,班级）：");
        scanner.nextLine();
        String[] info = scanner.nextLine().split(",");
        student.setName(info[0]);
        student.setGender(info[1]);
        student.setBirthDate(info[2]);
        student.setPhone(info[3]);
        student.setClassName(info[4]);
        if (adminService.updateStudent(student)) {
            System.out.println("修改成功！");
        } else {
            System.out.println("修改失败！");
        }
    }

    private static void handleDeleteStudent() {
        System.out.println("请输入要删除的学生学号：");
        String studentId = scanner.next();
        if (adminService.deleteStudent(studentId)) {
            System.out.println("删除成功！");
        } else {
            System.out.println("删除失败！");
        }
    }

    private static void showAllStudents() {
        List<Student> students = adminService.getAllStudents();
        System.out.println("学号\t姓名\t性别\t出生日期\t\t电话\t\t班级");
        for (Student student : students) {
            System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\n",
                student.getStudentId(), student.getName(),
                student.getGender(), student.getBirthDate(),
                student.getPhone(), student.getClassName());
        }
    }

    private static void handleArrangeCourses() {
        if (adminService.arrangeCourses()) {
            System.out.println("排课成功！");
        } else {
            System.out.println("排课失败，请检查课程信息。");
        }
    }

    private static void checkScheduleConflicts() {
        List<String> conflicts = adminService.getConflicts();
        if (conflicts.isEmpty()) {
            System.out.println("没有发现课程冲突。");
        } else {
            System.out.println("发现以下冲突：");
            conflicts.forEach(System.out::println);
        }
    }

    // 课程管理相关方法
    private static void handleAddCourse() {
        System.out.println("请输入课程信息（课程号,名称,学时,学分,班级,教师工号）：");
        scanner.nextLine();
        String[] info = scanner.nextLine().split(",");
        Course course = new Course(info[0], info[1], 
            Integer.parseInt(info[2]), Double.parseDouble(info[3]));
        course.setClassName(info[4]);
        course.setTeacherId(info[5]);
        if (adminService.addCourse(course)) {
            System.out.println("添加成功！");
        } else {
            System.out.println("添加失败！");
        }
    }

    private static void handleUpdateCourse() {
        System.out.println("请输入要修改的课程号：");
        String courseId = scanner.next();
        Course course = adminService.getCourse(courseId);
        if (course == null) {
            System.out.println("课程不存在！");
            return;
        }
        System.out.println("请输入新的信息（名称,学时,学分,班级,教师工号）：");
        scanner.nextLine();
        String[] info = scanner.nextLine().split(",");
        course.setName(info[0]);
        course.setHours(Integer.parseInt(info[1]));
        course.setCredits(Double.parseDouble(info[2]));
        course.setClassName(info[3]);
        course.setTeacherId(info[4]);
        if (adminService.updateCourse(course)) {
            System.out.println("修改成功！");
        } else {
            System.out.println("修改失败！");
        }
    }

    private static void handleDeleteCourse() {
        System.out.println("请输入要删除的课程号：");
        String courseId = scanner.next();
        if (adminService.deleteCourse(courseId)) {
            System.out.println("删除成功！");
        } else {
            System.out.println("删除失败！");
        }
    }

    private static void showAllCourses() {
        List<Course> courses = adminService.getAllCourses();
        System.out.println("课程号\t名称\t学时\t学分\t班级\t教师工号\t时间段\t教室");
        for (Course course : courses) {
            System.out.printf("%s\t%s\t%d\t%.1f\t%s\t%s\t%s\t%s\n",
                course.getCourseId(), course.getName(),
                course.getHours(), course.getCredits(),
                course.getClassName(), course.getTeacherId(),
                course.getTimeSlot() != null ? course.getTimeSlot() : "未安排",
                course.getClassroom() != null ? course.getClassroom() : "未安排");
        }
    }

    private static void handleInputGrades(String teacherId) {
        System.out.println("请输入课程号：");
        String courseId = scanner.next();
        System.out.println("请输入学号和成绩（格式：学号,成绩）：");
        scanner.nextLine();
        String[] info = scanner.nextLine().split(",");
        Grade grade = new Grade(courseId, info[0], Double.parseDouble(info[1]));
        if (teacherService.inputGrade(grade)) {
            System.out.println("成绩录入成功！");
        } else {
            System.out.println("成绩录入失败！");
        }
    }

    private static void showGradeRanking(String teacherId) {
        System.out.println("请输入课程号和班级：");
        String courseId = scanner.next();
        String className = scanner.next();
        List<Grade> sortedGrades = teacherService.getSortedGrades(courseId, className);
        System.out.println("成绩排名：");
        System.out.println("学号\t成绩");
        for (Grade grade : sortedGrades) {
            System.out.printf("%s\t%.1f\n", 
                grade.getStudentId(), grade.getScore());
        }
    }

    private static void showFeedbacks(String teacherId) {
        List<Feedback> feedbacks = teacherService.getFeedbacks(teacherId);
        if (feedbacks.isEmpty()) {
            System.out.println("暂无反馈信息。");
            return;
        }
        System.out.println("教学反馈列表：");
        for (Feedback feedback : feedbacks) {
            System.out.printf("课程号：%s\n学生学号：%s\n反馈日期：%s\n反馈内容：%s\n\n",
                feedback.getCourseId(), feedback.getStudentId(),
                feedback.getDate(), feedback.getContent());
        }
    }

    private static void handleSubmitFeedback(String studentId) {
        System.out.println("请输入反馈信息（教师工号,课程号,反馈内容）：");
        scanner.nextLine();
        String[] info = scanner.nextLine().split(",");
        Feedback feedback = new Feedback(
            studentId, info[0], info[1], info[2],
            new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())
        );
        if (studentService.submitFeedback(feedback)) {
            System.out.println("反馈提交成功！");
        } else {
            System.out.println("反馈提交失败！");
        }
    }

    private static void handleUpdateStudentInfo(Student student) {
        System.out.println("请输入新的个人信息（姓名,性别,出生日期,电话）：");
        scanner.nextLine();
        String[] info = scanner.nextLine().split(",");
        student.setName(info[0]);
        student.setGender(info[1]);
        student.setBirthDate(info[2]);
        student.setPhone(info[3]);
        if (studentService.updatePersonalInfo(student)) {
            System.out.println("信息更新成功！");
        } else {
            System.out.println("信息更新失败！");
        }
    }

    private static void handleExportTeacherSchedule(String teacherId) {
        System.out.println("请输入导出文件路径：");
        String filePath = scanner.next();
        if (teacherService.exportSchedule(teacherId, filePath)) {
            System.out.println("课表导出成功！");
        } else {
            System.out.println("课表导出失败！");
        }
    }

    private static void handleExportStudentSchedule(String className) {
        System.out.println("请输入导出文件路径：");
        String filePath = scanner.next();
        if (studentService.exportSchedule(className, filePath)) {
            System.out.println("课表导出成功！");
        } else {
            System.out.println("课表导出失败！");
        }
    }

    private static void showTeacherSchedule(String teacherId) {
        List<Course> courses = teacherService.getTeacherSchedule(teacherId);
        if (courses.isEmpty()) {
            System.out.println("暂无课程安排。");
            return;
        }
        
        System.out.println("\n个人课表：");
        System.out.println("课程号\t课程名称\t班级\t上课时间\t教室");
        for (Course course : courses) {
            System.out.printf("%s\t%s\t%s\t%s\t%s\n",
                course.getCourseId(),
                course.getName(),
                course.getClassName(),
                course.getTimeSlot() != null ? course.getTimeSlot() : "未安排",
                course.getClassroom() != null ? course.getClassroom() : "未安排");
        }
    }

    private static void showClassSchedule(String className) {
        List<Course> courses = studentService.getClassSchedule(className);
        if (courses.isEmpty()) {
            System.out.println("暂无课程安排。");
            return;
        }
        
        System.out.println("\n班级课表 - " + className);
        System.out.println("课程号\t课程名称\t教师工号\t上课时间\t教室");
        for (Course course : courses) {
            System.out.printf("%s\t%s\t%s\t%s\t%s\n",
                course.getCourseId(),
                course.getName(),
                course.getTeacherId(),
                course.getTimeSlot() != null ? course.getTimeSlot() : "未安排",
                course.getClassroom() != null ? course.getClassroom() : "未安排");
        }
    }
}
