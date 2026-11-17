package com.auca.bookloan;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.util.Checks;

import java.time.LocalDate;

public class BookLoan implements Identifiable, Registrable {
    private String loanId;
    private Student student;
    private LibraryBook book;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;

    public BookLoan(String loanId, Student student, LibraryBook book, LocalDate borrowedDate,
                    LocalDate dueDate, LocalDate returnedDate) {
        this.loanId = loanId; this.student = student; this.book = book; this.borrowedDate = borrowedDate;
        this.dueDate = dueDate; this.returnedDate = returnedDate;
    }
    @Override public String getId() { return loanId; }
    @Override public String getDisplayName() { return book.getTitle() + " borrowed by " + student.getDisplayName(); }
    @Override public boolean isActive() { return returnedDate == null; }
    @Override public void validate() throws ValidationException {
        Checks.text(loanId, "Loan ID", 4, 30); Checks.required(student, "Student"); Checks.required(book, "Book");
        Checks.date(borrowedDate, "Borrowed date"); Checks.date(dueDate, "Due date");
        if (!dueDate.isAfter(borrowedDate)) throw new ValidationException("Due date must be after borrowed date.");
        if (returnedDate != null && returnedDate.isBefore(borrowedDate))
            throw new ValidationException("Return date cannot be before borrowed date.");
    }
    @Override public void printDetails() { System.out.println(this); }
    @Override public String getRegistrationSummary() { return "Loan " + loanId + " - " + getDisplayName(); }
    public String getLoanId() { return loanId; } public void setLoanId(String v) { loanId = v; }
    public Student getStudent() { return student; } public void setStudent(Student v) { student = v; }
    public LibraryBook getBook() { return book; } public void setBook(LibraryBook v) { book = v; }
    public LocalDate getBorrowedDate() { return borrowedDate; } public void setBorrowedDate(LocalDate v) { borrowedDate = v; }
    public LocalDate getDueDate() { return dueDate; } public void setDueDate(LocalDate v) { dueDate = v; }
    public LocalDate getReturnedDate() { return returnedDate; } public void setReturnedDate(LocalDate v) { returnedDate = v; }
    @Override public String toString() { return "BookLoan{id='" + loanId + "', student=" + student.getStudentNumber()
            + ", book='" + book.getTitle() + "', borrowed=" + borrowedDate + ", due=" + dueDate + ", returned=" + returnedDate + "}"; }
}
