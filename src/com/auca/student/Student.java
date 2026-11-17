package com.auca.student;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.util.Checks;

import java.time.LocalDate;

public class Student extends AcademicMember {
    private String studentNumber;
    private String programName;
    private LocalDate admissionDate;
    private int studyYear;
    private String guardianPhone;
    private boolean scholarshipStatus;

    public Student(String nationalId, String firstName, String lastName, String email, String phoneNumber,
                   LocalDate dateOfBirth, String memberNumber, Department department, LocalDate joinDate,
                   String academicStatus, String institutionalEmail, String advisorName, String studentNumber,
                   String programName, LocalDate admissionDate, int studyYear, String guardianPhone,
                   boolean scholarshipStatus) {
        super(nationalId, firstName, lastName, email, phoneNumber, dateOfBirth, memberNumber, department,
                joinDate, academicStatus, institutionalEmail, advisorName);
        this.studentNumber = studentNumber;
        this.programName = programName;
        this.admissionDate = admissionDate;
        this.studyYear = studyYear;
        this.guardianPhone = guardianPhone;
        this.scholarshipStatus = scholarshipStatus;
    }

    @Override public String getRoleDescription() { return "Student"; }
    @Override public String getAcademicCategory() { return "Undergraduate student"; }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Checks.text(studentNumber, "Student number", 4, 30);
        Checks.text(programName, "Program name", 2, 120);
        Checks.notFuture(admissionDate, "Admission date");
        if (studyYear < 1 || studyYear > 8) throw new ValidationException("Study year must be from 1 to 8.");
        Checks.phone(guardianPhone, "Guardian phone");
    }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }
    public LocalDate getAdmissionDate() { return admissionDate; }
    public void setAdmissionDate(LocalDate admissionDate) { this.admissionDate = admissionDate; }
    public int getStudyYear() { return studyYear; }
    public void setStudyYear(int studyYear) { this.studyYear = studyYear; }
    public String getGuardianPhone() { return guardianPhone; }
    public void setGuardianPhone(String guardianPhone) { this.guardianPhone = guardianPhone; }
    public boolean isScholarshipStatus() { return scholarshipStatus; }
    public void setScholarshipStatus(boolean scholarshipStatus) { this.scholarshipStatus = scholarshipStatus; }

    @Override public String toString() {
        return super.toString() + ", Student{studentNumber='" + studentNumber + "', program='" + programName
                + "', admissionDate=" + admissionDate + ", studyYear=" + studyYear + ", scholarship=" + scholarshipStatus + "}";
    }
}
