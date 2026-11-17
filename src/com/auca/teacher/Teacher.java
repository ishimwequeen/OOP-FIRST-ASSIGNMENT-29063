package com.auca.teacher;

import com.auca.abstracts.AcademicMember;
import com.auca.department.Department;
import com.auca.exceptions.ValidationException;
import com.auca.util.Checks;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Teacher extends AcademicMember {
    private String employeeNumber;
    private String specialization;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String officeNumber;
    private String employmentType;

    public Teacher(String nationalId, String firstName, String lastName, String email, String phoneNumber,
                   LocalDate dateOfBirth, String memberNumber, Department department, LocalDate joinDate,
                   String academicStatus, String institutionalEmail, String advisorName, String employeeNumber,
                   String specialization, LocalDate hireDate, BigDecimal salary, String officeNumber,
                   String employmentType) {
        super(nationalId, firstName, lastName, email, phoneNumber, dateOfBirth, memberNumber, department,
                joinDate, academicStatus, institutionalEmail, advisorName);
        this.employeeNumber = employeeNumber;
        this.specialization = specialization;
        this.hireDate = hireDate;
        this.salary = salary;
        this.officeNumber = officeNumber;
        this.employmentType = employmentType;
    }

    @Override public String getRoleDescription() { return "Teacher"; }
    @Override public String getAcademicCategory() { return "Teaching faculty"; }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Checks.text(employeeNumber, "Employee number", 4, 30);
        Checks.text(specialization, "Specialization", 2, 120);
        Checks.notFuture(hireDate, "Hire date");
        Checks.positive(salary, "Salary");
        Checks.text(officeNumber, "Office number", 1, 30);
        if (!"FULL_TIME".equals(employmentType) && !"PART_TIME".equals(employmentType)
                && !"CONTRACT".equals(employmentType))
            throw new ValidationException("Employment type must be FULL_TIME, PART_TIME or CONTRACT.");
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public void setEmployeeNumber(String employeeNumber) { this.employeeNumber = employeeNumber; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public String getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(String officeNumber) { this.officeNumber = officeNumber; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    @Override public String toString() {
        return super.toString() + ", Teacher{employeeNumber='" + employeeNumber + "', specialization='"
                + specialization + "', hireDate=" + hireDate + ", salary=" + salary + ", office='" + officeNumber + "'}";
    }
}
