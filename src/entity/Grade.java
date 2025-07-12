package entity;

public class Grade {
    private String courseId;
    private String studentId;
    private Double score;
    
    public Grade(String courseId, String studentId, Double score) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.score = score;
    }
    
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public Double getScore() {
        return score;
    }
    
    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return courseId + "," + studentId + "," + score;
    }
}
