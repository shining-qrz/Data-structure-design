package entity;

public class Teacher {
    private String teacherId;
    private String name;
    private String gender;
    private String birthDate;
    private String phone;

    // 构造方法
    public Teacher(String teacherId, String name, String gender, String birthDate, String phone) {
        this.teacherId = teacherId;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    // getter和setter方法
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return teacherId + "," + name + "," + gender + "," + birthDate + "," + phone;
    }
}
