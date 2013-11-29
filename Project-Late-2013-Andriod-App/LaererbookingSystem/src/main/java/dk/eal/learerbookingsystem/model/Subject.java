package dk.eal.learerbookingsystem.model;

/**
 * Created by Trine on 28-11-13.
 */
public class Subject {
    private long _id;
    private String _name;
    private Semester _semester;
    private Teacher _teacher;

    public Subject() {}

    public Subject(String name, Semester semester, Teacher teacher) {
        _name = name;
        _semester = semester;
        _teacher = teacher;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public Semester getSemester() {
        return _semester;
    }

    public void setSemester(Semester semester) {
        _semester = semester;
    }

    public Teacher getTeacher() {
        return _teacher;
    }

    public void setTeacher(Teacher teacher) {
        _teacher = teacher;
    }

}
