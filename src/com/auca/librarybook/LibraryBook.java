package com.auca.librarybook;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.util.Checks;

public class LibraryBook implements Identifiable, Registrable {
    private String bookId;
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public LibraryBook(String bookId, String isbn, String title, String author, int totalCopies, int availableCopies) {
        this.bookId = bookId; this.isbn = isbn; this.title = title; this.author = author;
        this.totalCopies = totalCopies; this.availableCopies = availableCopies;
    }
    @Override public String getId() {
        return bookId;
    }
    @Override public String getDisplayName() {
        return title;
    }
    @Override public boolean isActive() {
        return availableCopies > 0;
    }
    @Override public void validate() throws ValidationException {
        Checks.text(bookId, "Book ID", 4, 30); Checks.text(isbn, "ISBN", 10, 20);
        Checks.text(title, "Book title", 2, 200); Checks.text(author, "Author", 2, 120);
        if (totalCopies < 0 || availableCopies < 0 || availableCopies > totalCopies)
            throw new ValidationException("Copy counts must be nonnegative and available copies cannot exceed total copies.");
    }
    @Override public void printDetails() { System.out.println(this); }
    @Override public String getRegistrationSummary() { return "Book " + bookId + " - " + title + " (" + availableCopies + " available)"; }
    public String getBookId() { return bookId; } public void setBookId(String v) { bookId = v; }
    public String getIsbn() { return isbn; } public void setIsbn(String v) { isbn = v; }
    public String getTitle() { return title; } public void setTitle(String v) { title = v; }
    public String getAuthor() { return author; } public void setAuthor(String v) { author = v; }
    public int getTotalCopies() { return totalCopies; } public void setTotalCopies(int v) { totalCopies = v; }
    public int getAvailableCopies() { return availableCopies; } public void setAvailableCopies(int v) { availableCopies = v; }
    @Override public String toString() { return "LibraryBook{id='" + bookId + "', isbn='" + isbn + "', title='" + title
            + "', author='" + author + "', totalCopies=" + totalCopies + ", availableCopies=" + availableCopies + "}"; }
}
