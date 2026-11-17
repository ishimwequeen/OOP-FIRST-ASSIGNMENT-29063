package com.auca.abstracts;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.util.Checks;

import java.time.LocalDate;

public abstract class Person implements Identifiable, Registrable {
    private String nationalId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    protected Person(String nationalId, String firstName, String lastName, String email,
                     String phoneNumber, LocalDate dateOfBirth) {
        this.nationalId = nationalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public abstract String getRoleDescription();

    @Override public String getId() {
        return nationalId;
    }
    @Override public String getDisplayName() {
        return firstName + " " + lastName;
    }
    @Override public boolean isActive() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        Checks.nationalId(nationalId, "National ID");
        Checks.text(firstName, "First name", 2, 60);
        Checks.text(lastName, "Last name", 2, 60);
        Checks.email(email, "Email");
        Checks.phone(phoneNumber, "Phone number");
        Checks.past(dateOfBirth, "Date of birth");
    }

    @Override public void printDetails() {
        System.out.println(this);
    }
    @Override public String getRegistrationSummary() {
        return getRoleDescription() + " " + nationalId + " - " + getDisplayName();
    }

    public String getNationalId() {
        return nationalId;
    }
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override public String toString() {
        return getRoleDescription() + "{nationalId='" + nationalId + "', name='" + getDisplayName()
                + "', email='" + email + "', phone='" + phoneNumber + "', dateOfBirth=" + dateOfBirth + "}";
    }
}
