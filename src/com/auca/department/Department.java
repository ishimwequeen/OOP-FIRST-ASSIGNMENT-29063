package com.auca.department;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.teacher.Teacher;
import com.auca.util.Checks;

public class Department implements Identifiable, Registrable {
    private String departmentId;
    private String departmentName;
    private String departmentCode;
    private String officeLocation;
    private String contactEmail;
    private Teacher headTeacher;

    public Department(String departmentId, String departmentName, String departmentCode, String officeLocation,
                      String contactEmail, Teacher headTeacher) {
        this.departmentId = departmentId; this.departmentName = departmentName; this.departmentCode = departmentCode;
        this.officeLocation = officeLocation; this.contactEmail = contactEmail; this.headTeacher = headTeacher;
    }

    @Override public String getId() { return departmentId; }
    @Override public String getDisplayName() { return departmentName; }
    @Override public boolean isActive() { return true; }
    @Override public void validate() throws ValidationException {
        Checks.text(departmentId, "Department ID", 4, 30); Checks.text(departmentName, "Department name", 2, 60);
        Checks.text(departmentCode, "Department code", 2, 20); Checks.text(officeLocation, "Office location", 2, 60);
        Checks.email(contactEmail, "Contact email");
    }
    @Override public void printDetails() { System.out.println(this); }
    @Override public String getRegistrationSummary() { return "Department " + departmentId + " - " + departmentName; }
    public String getDepartmentId() { return departmentId; } public void setDepartmentId(String v) { departmentId = v; }
    public String getDepartmentName() { return departmentName; } public void setDepartmentName(String v) { departmentName = v; }
    public String getDepartmentCode() { return departmentCode; } public void setDepartmentCode(String v) { departmentCode = v; }
    public String getOfficeLocation() { return officeLocation; } public void setOfficeLocation(String v) { officeLocation = v; }
    public String getContactEmail() { return contactEmail; } public void setContactEmail(String v) { contactEmail = v; }
    public Teacher getHeadTeacher() { return headTeacher; } public void setHeadTeacher(Teacher v) { headTeacher = v; }
    @Override public String toString() { return "Department{id='" + departmentId + "', name='" + departmentName
            + "', code='" + departmentCode + "', office='" + officeLocation + "', email='" + contactEmail
            + "', head=" + (headTeacher == null ? "unassigned" : headTeacher.getDisplayName()) + "}"; }
}
