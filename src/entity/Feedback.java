package entity;

public class Feedback {
    private String studentId;
    private String teacherId;
    private String courseId;
    private String content;
    private String date;

    public Feedback(String studentId, String teacherId, String courseId, String content, String date) {
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.courseId = courseId;
        this.content = content;
        this.date = date;
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return studentId + "," + teacherId + "," + courseId + "," + content + "," + date;
    }
}
