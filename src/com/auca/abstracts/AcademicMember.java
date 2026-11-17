package com.auca.abstracts;

import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.util.Checks;

import java.time.LocalDate;

public abstract class AcademicMember extends Person {
    private String memberNumber;
    private Department department;
    private LocalDate joinDate;
    private String academicStatus;
    private String institutionalEmail;
    private String advisorName;

    protected AcademicMember(String nationalId, String firstName, String lastName, String email,
                             String phoneNumber, LocalDate dateOfBirth, String memberNumber,
                             Department department, LocalDate joinDate, String academicStatus,
                             String institutionalEmail, String advisorName) {
        super(nationalId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.memberNumber = memberNumber;
        this.department = department;
        this.joinDate = joinDate;
        this.academicStatus = academicStatus;
        this.institutionalEmail = institutionalEmail;
        this.advisorName = advisorName;
    }

    public abstract String getAcademicCategory();

    @Override public boolean isActive() { return "ACTIVE".equalsIgnoreCase(academicStatus); }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Checks.text(memberNumber, "Member number", 4, 30);
        Checks.required(department, "Department");
        Checks.notFuture(joinDate, "Join date");
        if (!"ACTIVE".equals(academicStatus) && !"INACTIVE".equals(academicStatus))
            throw new ValidationException("Academic status must be ACTIVE or INACTIVE.");
        Checks.email(institutionalEmail, "Institutional email");
        Checks.text(advisorName, "Advisor name", 2, 120);
    }

    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    public String getAcademicStatus() { return academicStatus; }
    public void setAcademicStatus(String academicStatus) { this.academicStatus = academicStatus; }
    public String getInstitutionalEmail() { return institutionalEmail; }
    public void setInstitutionalEmail(String institutionalEmail) { this.institutionalEmail = institutionalEmail; }
    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }

    @Override public String toString() {
        return super.toString() + ", AcademicMember{memberNumber='" + memberNumber + "', department="
                + (department == null ? "none" : department.getDepartmentName()) + ", joinDate=" + joinDate
                + ", status='" + academicStatus + "', institutionalEmail='" + institutionalEmail
                + "', advisor='" + advisorName + "'}";
    }
}
