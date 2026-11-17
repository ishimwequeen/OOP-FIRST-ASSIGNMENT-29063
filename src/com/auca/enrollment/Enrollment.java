package com.auca.enrollment;

import com.auca.course.Course;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;
import com.auca.util.Checks;

import java.time.LocalDate;

public class Enrollment implements Identifiable, Registrable {
    private String enrollmentId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private String semester;
    private String enrollmentStatus;

    public Enrollment(String enrollmentId, Student student, Course course, LocalDate enrollmentDate, String semester, String enrollmentStatus) {
        this.enrollmentId = enrollmentId; this.student = student; this.course = course;
        this.enrollmentDate = enrollmentDate; this.semester = semester; this.enrollmentStatus = enrollmentStatus;
    }
    @Override public String getId() { return enrollmentId; }
    @Override public String getDisplayName() { return student.getDisplayName() + " - " + course.getCourseTitle(); }
    @Override public boolean isActive() { return "ACTIVE".equals(enrollmentStatus); }
    @Override public void validate() throws ValidationException {
        Checks.text(enrollmentId, "Enrollment ID", 4, 30); Checks.required(student, "Student"); Checks.required(course, "Course");
        Checks.notFuture(enrollmentDate, "Enrollment date"); Checks.text(semester, "Semester", 2, 30);
        if (!"ACTIVE".equals(enrollmentStatus) && !"DROPPED".equals(enrollmentStatus) && !"COMPLETED".equals(enrollmentStatus))
            throw new ValidationException("Enrollment status must be ACTIVE, DROPPED or COMPLETED.");
    }
    @Override public void printDetails() { System.out.println(this); }
    @Override public String getRegistrationSummary() { return "Enrollment " + enrollmentId + " - " + getDisplayName(); }
    public String getEnrollmentId() { return enrollmentId; } public void setEnrollmentId(String v) { enrollmentId = v; }
    public Student getStudent() { return student; } public void setStudent(Student v) { student = v; }
    public Course getCourse() { return course; } public void setCourse(Course v) { course = v; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; } public void setEnrollmentDate(LocalDate v) { enrollmentDate = v; }
    public String getSemester() { return semester; } public void setSemester(String v) { semester = v; }
    public String getEnrollmentStatus() { return enrollmentStatus; } public void setEnrollmentStatus(String v) { enrollmentStatus = v; }
    @Override public String toString() { return "Enrollment{id='" + enrollmentId + "', student=" + student.getStudentNumber()
            + ", course=" + course.getCourseCode() + ", date=" + enrollmentDate + ", semester='" + semester + "', status='" + enrollmentStatus + "'}"; }
}
