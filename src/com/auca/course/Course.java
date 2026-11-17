package com.auca.course;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;
import com.auca.util.Checks;

public class Course implements Identifiable, Registrable {
    private String courseId;
    private String courseCode;
    private String courseTitle;
    private int creditHours;
    private Department department;
    private Teacher assignedTeacher;

    public Course(String courseId, String courseCode, String courseTitle, int creditHours,
                  Department department, Teacher assignedTeacher) {
        this.courseId = courseId; this.courseCode = courseCode; this.courseTitle = courseTitle;
        this.creditHours = creditHours; this.department = department; this.assignedTeacher = assignedTeacher;
    }
    @Override public String getId() { return courseId; }
    @Override public String getDisplayName() { return courseTitle; }
    @Override public boolean isActive() { return true; }
    @Override public void validate() throws ValidationException {
        Checks.text(courseId, "Course ID", 4, 30); Checks.text(courseCode, "Course code", 2, 30);
        Checks.text(courseTitle, "Course title", 2, 120);
        if (creditHours < 1 || creditHours > 6) throw new ValidationException("Credit hours must be from 1 to 6.");
        Checks.required(department, "Department"); Checks.required(assignedTeacher, "Assigned teacher");
        if (assignedTeacher.getDepartment() != department)
            throw new ValidationException("The assigned teacher must belong to the selected department.");
    }
    @Override public void printDetails() { System.out.println(this); }
    @Override public String getRegistrationSummary() { return "Course " + courseCode + " - " + courseTitle; }
    public String getCourseId() { return courseId; } public void setCourseId(String v) { courseId = v; }
    public String getCourseCode() { return courseCode; } public void setCourseCode(String v) { courseCode = v; }
    public String getCourseTitle() { return courseTitle; } public void setCourseTitle(String v) { courseTitle = v; }
    public int getCreditHours() { return creditHours; } public void setCreditHours(int v) { creditHours = v; }
    public Department getDepartment() { return department; } public void setDepartment(Department v) { department = v; }
    public Teacher getAssignedTeacher() { return assignedTeacher; } public void setAssignedTeacher(Teacher v) { assignedTeacher = v; }
    @Override public String toString() { return "Course{id='" + courseId + "', code='" + courseCode + "', title='"
            + courseTitle + "', credits=" + creditHours + ", department=" + department.getDepartmentName()
            + ", teacher=" + assignedTeacher.getDisplayName() + "}"; }
}
