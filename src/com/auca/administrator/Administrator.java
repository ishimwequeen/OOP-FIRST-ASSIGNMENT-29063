package com.auca.administrator;

import com.auca.abstracts.Person;
import com.auca.exceptions.ValidationException;
import com.auca.util.Checks;

import java.time.LocalDate;

public class Administrator extends Person {
    private String staffNumber;
    private String jobTitle;
    private String assignedOffice;
    private LocalDate employmentDate;
    private int accessLevel;
    private boolean active;

    public Administrator(String nationalId, String firstName, String lastName, String email, String phoneNumber,
                         LocalDate dateOfBirth, String staffNumber, String jobTitle, String assignedOffice,
                         LocalDate employmentDate, int accessLevel, boolean active) {
        super(nationalId, firstName, lastName, email, phoneNumber, dateOfBirth);
        this.staffNumber = staffNumber;
        this.jobTitle = jobTitle;
        this.assignedOffice = assignedOffice;
        this.employmentDate = employmentDate;
        this.accessLevel = accessLevel;
        this.active = active;
    }

    @Override public String getRoleDescription() { return "Administrator"; }
    @Override public boolean isActive() { return active; }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        Checks.text(staffNumber, "Staff number", 4, 30);
        Checks.text(jobTitle, "Job title", 2, 120);
        Checks.text(assignedOffice, "Assigned office", 1, 60);
        Checks.notFuture(employmentDate, "Employment date");
        if (accessLevel < 1 || accessLevel > 10) throw new ValidationException("Access level must be from 1 to 10.");
    }

    public String getStaffNumber() { return staffNumber; }
    public void setStaffNumber(String staffNumber) { this.staffNumber = staffNumber; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getAssignedOffice() { return assignedOffice; }
    public void setAssignedOffice(String assignedOffice) { this.assignedOffice = assignedOffice; }
    public LocalDate getEmploymentDate() { return employmentDate; }
    public void setEmploymentDate(LocalDate employmentDate) { this.employmentDate = employmentDate; }
    public int getAccessLevel() { return accessLevel; }
    public void setAccessLevel(int accessLevel) { this.accessLevel = accessLevel; }
    public boolean isActiveStatus() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override public String toString() {
        return super.toString() + ", Administrator{staffNumber='" + staffNumber + "', title='" + jobTitle
                + "', office='" + assignedOffice + "', employmentDate=" + employmentDate + ", accessLevel=" + accessLevel + ", active=" + active + "}";
    }
}
