package entity;

public class Course {
    private String courseId;
    private String name;
    private int hours;
    private double credits;
    private String timeSlot;
    private String classroom;
    private String teacherId;
    private String className;
    
    // 构造方法
    public Course(String courseId, String name, int hours, double credits) {
        this.courseId = courseId;
        this.name = name;
        this.hours = hours;
        this.credits = credits;
    }

    // getter和setter方法
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%d,%.1f,%s,%s,%s,%s",
            courseId,
            name,
            hours,
            credits,
            timeSlot != null ? timeSlot : "",
            classroom != null ? classroom : "",
            teacherId != null ? teacherId : "",
            className != null ? className : "");
    }
}
