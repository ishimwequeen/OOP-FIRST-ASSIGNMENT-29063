package com.auca.main;

import com.auca.abstracts.Person;
import com.auca.administrator.Administrator;
import com.auca.abstracts.AcademicMember;
import com.auca.assessment.Assessment;
import com.auca.bookloan.BookLoan;
import com.auca.classroom.Classroom;
import com.auca.course.Course;
import com.auca.department.Department;
import com.auca.enrollment.Enrollment;
import com.auca.exceptions.BookUnavailableException;
import com.auca.exceptions.DuplicateRecordException;
import com.auca.exceptions.PrerequisiteMissingException;
import com.auca.exceptions.RecordNotFoundException;
import com.auca.exceptions.ValidationException;
import com.auca.feepayment.FeePayment;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.librarybook.LibraryBook;
import com.auca.student.Student;
import com.auca.teacher.Teacher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Department> departments = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<Teacher> teachers = new ArrayList<>();
    private final List<Administrator> administrators = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<Classroom> classrooms = new ArrayList<>();
    private final List<Enrollment> enrollments = new ArrayList<>();
    private final List<Assessment> assessments = new ArrayList<>();
    private final List<FeePayment> payments = new ArrayList<>();
    private final List<LibraryBook> books = new ArrayList<>();
    private final List<BookLoan> loans = new ArrayList<>();

    private static final class FormCancelledException extends RuntimeException {
        private FormCancelledException() { super("Form cancelled by user."); }
    }
    private static final class EndOfInputException extends RuntimeException { }

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = number("Select an option: ", 0, 19);
            try {
                switch (choice) {
                    case 1: registerDepartment();
                    break;
                    case 2: registerStudent();
                    break;
                    case 3: registerTeacher();
                    break;
                    case 4: registerAdministrator();
                    break;
                    case 5: registerCourse();
                    break;
                    case 6: registerClassroom();
                    break;
                    case 7: enrollStudent();
                    break;
                    case 8: recordAssessment();
                    break;
                    case 9: recordPayment();
                    break;
                    case 10: registerBook();
                    break;
                    case 11: borrowBook();
                    break;
                    case 12: returnBook();
                    break;
                    case 13: viewAll();
                    break;
                    case 14: searchById();
                    break;
                    case 15: updateRecord();
                    break;
                    case 16: academicSummary();
                    break;
                    case 17: paymentHistory();
                    break;
                    case 18: availableBooks();
                    break;
                    case 19: demonstratePolymorphism();
                    break;
                    case 0:
                        if (confirm("Exit the application?")) running = false;
                        break;
                    default: System.out.println("Choose a menu number from 0 to 19.");
                }
            } catch (FormCancelledException ex) {
                System.out.println("Form cancelled; no changes were saved.");
            } catch (EndOfInputException ex) {
                System.out.println("\nInput closed; exiting safely.");
                return;
            } catch (ValidationException | RecordNotFoundException | BookUnavailableException
                     | PrerequisiteMissingException ex) {
                System.out.println("Unable to complete operation: " + ex.getMessage());
            } catch (RuntimeException ex) {
                System.out.println("Unexpected operation error: " + (ex.getMessage() == null ? "invalid state" : ex.getMessage()));
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
        System.out.println("\n========== AUCA_ID SCHOOL MANAGEMENT ==========");
        System.out.println(" 1. Register Department       11. Borrow Library Book");
        System.out.println(" 2. Register Student          12. Return Library Book");
        System.out.println(" 3. Register Teacher          13. View All Records");
        System.out.println(" 4. Register Administrator    14. Search Record by ID");
        System.out.println(" 5. Register Course           15. Update Existing Record");
        System.out.println(" 6. Register Classroom        16. Student Academic Summary");
        System.out.println(" 7. Enroll Student            17. Student Payment History");
        System.out.println(" 8. Record Assessment         18. View Available Books");
        System.out.println(" 9. Record Fee Payment        19. Demonstrate Polymorphism");
        System.out.println("10. Register Library Book      0. Exit");
    }

    private String text(String prompt, int min, int max) {
        while (true) {
            String value = ask(prompt);
            if (value.length() >= min && value.length() <= max) return value;
            System.out.println("Enter " + min + "-" + max + " characters.");
        }
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) throw new EndOfInputException();
        String value = scanner.nextLine().trim();
        if ("CANCEL".equalsIgnoreCase(value) || "BACK".equalsIgnoreCase(value))
            throw new FormCancelledException();
        return value;
    }

    private int number(String prompt, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(ask(prompt));
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException ignored) {
                // Re-prompt on malformed console input.
            }
            System.out.println("Enter a whole number from " + min + " to " + max + ".");
        }
    }

    private double decimal(String prompt, double min, double max) {
        while (true) {
            try {
                double value = Double.parseDouble(ask(prompt));
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException ignored) {
                // Re-prompt on malformed console input.
            }
            System.out.println("Enter a number from " + min + " to " + max + ".");
        }
    }

    private BigDecimal money(String prompt) {
        while (true) {
            try {
                BigDecimal value = new BigDecimal(ask(prompt));
                if (value.compareTo(BigDecimal.ZERO) > 0) return value;
            } catch (NumberFormatException ignored) {
                // Re-prompt on malformed console input.
            }
            System.out.println("Enter a valid amount greater than zero.");
        }
    }

    private LocalDate date(String prompt, boolean mustBePast) {
        while (true) {
            try {
                LocalDate value = LocalDate.parse(ask(prompt));
                if (mustBePast && !value.isBefore(LocalDate.now())) {
                    System.out.println("Date must be in the past.");
                    continue;
                }
                if (!mustBePast && value.isAfter(LocalDate.now())) {
                    System.out.println("Date cannot be in the future.");
                    continue;
                }
                return value;
            } catch (DateTimeParseException ex) {
                System.out.println("Use a valid date in YYYY-MM-DD format.");
            }
        }
    }

    private LocalDate dueDate(String prompt, LocalDate borrowedDate) {
        while (true) {
            try {
                LocalDate value = LocalDate.parse(ask(prompt));
                if (value.isAfter(borrowedDate)) return value;
                System.out.println("Due date must be after the borrowed date.");
            } catch (DateTimeParseException ex) {
                System.out.println("Use a valid date in YYYY-MM-DD format.");
            }
        }
    }

    private String email(String prompt) {
        while (true) {
            String value = text(prompt, 3, 120);
            int atPosition = -1;
            int dotAfterAt = -1;
            boolean valid = true;

            for (int i = 0; i < value.length(); i++) {
                char character = value.charAt(i);
                if (Character.isWhitespace(character)) valid = false;
                if (character == '@') {
                    if (atPosition != -1) valid = false;
                    atPosition = i;
                }
                if (character == '.' && atPosition != -1) dotAfterAt = i;
            }

            if (valid && atPosition > 0 && dotAfterAt > atPosition + 1
                    && dotAfterAt < value.length() - 1) return value;
            System.out.println("Enter a valid email address.");
        }
    }

    private String phone(String prompt) {
        while (true) {
            String value = text(prompt, 10, 16);
            int firstDigit = value.charAt(0) == '+' ? 1 : 0;
            int digitCount = value.length() - firstDigit;
            boolean valid = digitCount >= 10 && digitCount <= 15;

            for (int i = firstDigit; valid && i < value.length(); i++) {
                if (value.charAt(i) < '0' || value.charAt(i) > '9') valid = false;
            }

            if (valid) return value;
            System.out.println("Enter 10-15 digits, optionally prefixed by +.");
        }
    }

    private boolean yesNo(String prompt) {
        while (true) {
            String value = ask(prompt + " (yes/no): ");
            if ("yes".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value)) return true;
            if ("no".equalsIgnoreCase(value) || "n".equalsIgnoreCase(value)) return false;
            System.out.println("Enter yes or no.");
        }
    }

    private boolean confirm(String prompt) { return yesNo(prompt); }

    private String status() {
        return number("Status: 1 ACTIVE, 2 INACTIVE: ", 1, 2) == 1 ? "ACTIVE" : "INACTIVE";
    }

    private boolean review(Registrable record) {
        while (true) {
            System.out.println("\nReview entered data:");
            record.printDetails();
            int choice = number("1 Save record   2 Edit field   0 Cancel: ", 0, 2);
            if (choice == 1) return true;
            if (choice == 0) return false;
            editDraft(record);
            try {
                record.validate();
            } catch (ValidationException ex) {
                System.out.println("The draft is not valid: " + ex.getMessage());
            }
        }
    }

    private void editDraft(Registrable record) {
        if (record instanceof Student) {
            Student s = (Student) record;
            int field = number("Edit field 1 Person ID  2 First name  3 Last name  4 Email  5 Phone  6 Birth date"
                    + "  7 Member number  8 Department  9 Join date  10 Academic status  11 Institutional email"
                    + "  12 Advisor  13 Student number  14 Program  15 Admission date  16 Study year"
                    + "  17 Guardian phone  18 Scholarship: ", 1, 18);
            switch (field) {
                case 1: s.setPersonId(text("Person ID (4-30 characters): ", 4, 30)); break;
                case 2: s.setFirstName(text("First name: ", 2, 60)); break;
                case 3: s.setLastName(text("Last name: ", 2, 60)); break;
                case 4: s.setEmail(email("Email: ")); break;
                case 5: s.setPhoneNumber(phone("Phone: ")); break;
                case 6: s.setDateOfBirth(date("Birth date (YYYY-MM-DD): ", true)); break;
                case 7: s.setMemberNumber(text("Member number: ", 4, 30)); break;
                case 8: { Department selected = chooseDepartment(); if (selected != null) s.setDepartment(selected); break; }
                case 9: s.setJoinDate(date("Join date (YYYY-MM-DD): ", false)); break;
                case 10: s.setAcademicStatus(status()); break;
                case 11: s.setInstitutionalEmail(email("Institutional email: ")); break;
                case 12: s.setAdvisorName(text("Advisor name: ", 2, 120)); break;
                case 13: s.setStudentNumber(text("Student number: ", 4, 30)); break;
                case 14: s.setProgramName(text("Program name: ", 2, 120)); break;
                case 15: s.setAdmissionDate(date("Admission date (YYYY-MM-DD): ", false)); break;
                case 16: s.setStudyYear(number("Study year (1-8): ", 1, 8)); break;
                case 17: s.setGuardianPhone(phone("Guardian phone: ")); break;
                case 18: s.setScholarshipStatus(number("Scholarship? 1 YES, 2 NO: ", 1, 2) == 1); break;
                default: break;
            }
        } else if (record instanceof Teacher) {
            Teacher t = (Teacher) record;
            int field = number("Edit field 1 Person ID  2 First name  3 Last name  4 Email  5 Phone  6 Birth date"
                    + "  7 Member number  8 Department  9 Join date  10 Academic status  11 Institutional email"
                    + "  12 Advisor  13 Employee number  14 Specialization  15 Hire date  16 Salary  17 Office"
                    + "  18 Employment type: ", 1, 18);
            switch (field) {
                case 1: t.setPersonId(text("Person ID (4-30 characters): ", 4, 30)); break;
                case 2: t.setFirstName(text("First name: ", 2, 60)); break;
                case 3: t.setLastName(text("Last name: ", 2, 60)); break;
                case 4: t.setEmail(email("Email: ")); break;
                case 5: t.setPhoneNumber(phone("Phone: ")); break;
                case 6: t.setDateOfBirth(date("Birth date (YYYY-MM-DD): ", true)); break;
                case 7: t.setMemberNumber(text("Member number: ", 4, 30)); break;
                case 8: { Department selected = chooseDepartment(); if (selected != null) t.setDepartment(selected); break; }
                case 9: t.setJoinDate(date("Join date (YYYY-MM-DD): ", false)); break;
                case 10: t.setAcademicStatus(status()); break;
                case 11: t.setInstitutionalEmail(email("Institutional email: ")); break;
                case 12: t.setAdvisorName(text("Advisor name: ", 2, 120)); break;
                case 13: t.setEmployeeNumber(text("Employee number: ", 4, 30)); break;
                case 14: t.setSpecialization(text("Specialization: ", 2, 120)); break;
                case 15: t.setHireDate(date("Hire date (YYYY-MM-DD): ", false)); break;
                case 16: t.setSalary(money("Salary: ")); break;
                case 17: t.setOfficeNumber(text("Office number: ", 1, 30)); break;
                case 18: t.setEmploymentType(enumValue("Type (FULL_TIME, PART_TIME, CONTRACT): ",
                        "FULL_TIME", "PART_TIME", "CONTRACT")); break;
                default: break;
            }
        } else if (record instanceof Administrator) {
            Administrator a = (Administrator) record;
            int field = number("Edit field 1 Person ID  2 First name  3 Last name  4 Email  5 Phone  6 Birth date"
                    + "  7 Staff number  8 Job title  9 Office  10 Employment date  11 Access level  12 Active: ", 1, 12);
            switch (field) {
                case 1: a.setPersonId(text("Person ID (4-30 characters): ", 4, 30)); break;
                case 2: a.setFirstName(text("First name: ", 2, 60)); break;
                case 3: a.setLastName(text("Last name: ", 2, 60)); break;
                case 4: a.setEmail(email("Email: ")); break;
                case 5: a.setPhoneNumber(phone("Phone: ")); break;
                case 6: a.setDateOfBirth(date("Birth date (YYYY-MM-DD): ", true)); break;
                case 7: a.setStaffNumber(text("Staff number: ", 4, 30)); break;
                case 8: a.setJobTitle(text("Job title: ", 2, 120)); break;
                case 9: a.setAssignedOffice(text("Office: ", 1, 60)); break;
                case 10: a.setEmploymentDate(date("Employment date (YYYY-MM-DD): ", false)); break;
                case 11: a.setAccessLevel(number("Access level (1-10): ", 1, 10)); break;
                case 12: a.setActive(number("Active? 1 YES, 2 NO: ", 1, 2) == 1); break;
                default: break;
            }
        } else if (record instanceof Department) {
            Department d = (Department) record;
            switch (number("Edit field 1 ID  2 Name  3 Code  4 Office  5 Contact email  6 Head teacher: ", 1, 6)) {
                case 1: d.setDepartmentId(text("Department ID: ", 4, 30)); break;
                case 2: d.setDepartmentName(text("Department name: ", 2, 60)); break;
                case 3: d.setDepartmentCode(text("Department code: ", 2, 20)); break;
                case 4: d.setOfficeLocation(text("Office location: ", 2, 60)); break;
                case 5: d.setContactEmail(email("Contact email: ")); break;
                case 6: d.setHeadTeacher(teachers.isEmpty() ? null : choose("head teacher", teachers)); break;
                default: break;
            }
        } else if (record instanceof Course) {
            Course c = (Course) record;
            switch (number("Edit field 1 ID  2 Code  3 Title  4 Credit hours  5 Department  6 Teacher: ", 1, 6)) {
                case 1: c.setCourseId(text("Course ID: ", 4, 30)); break;
                case 2: c.setCourseCode(text("Course code: ", 2, 30)); break;
                case 3: c.setCourseTitle(text("Course title: ", 2, 120)); break;
                case 4: c.setCreditHours(number("Credit hours (1-6): ", 1, 6)); break;
                case 5: { Department d = chooseDepartment(); if (d != null) c.setDepartment(d); break; }
                case 6: { Teacher t = choose("teacher", teachers); if (t != null) c.setAssignedTeacher(t); break; }
                default: break;
            }
        } else if (record instanceof Classroom) {
            Classroom c = (Classroom) record;
            switch (number("Edit field 1 ID  2 Building  3 Room  4 Capacity  5 Floor  6 Availability: ", 1, 6)) {
                case 1: c.setClassroomId(text("Classroom ID: ", 4, 30)); break;
                case 2: c.setBuildingName(text("Building: ", 2, 60)); break;
                case 3: c.setRoomNumber(text("Room number: ", 1, 30)); break;
                case 4: c.setCapacity(number("Capacity: ", 1, Integer.MAX_VALUE)); break;
                case 5: c.setFloorNumber(number("Floor (0 or greater): ", 0, Integer.MAX_VALUE)); break;
                case 6: c.setAvailable(number("Available? 1 YES, 2 NO: ", 1, 2) == 1); break;
                default: break;
            }
        } else if (record instanceof Enrollment) {
            Enrollment e = (Enrollment) record;
            switch (number("Edit field 1 ID  2 Student  3 Course  4 Date  5 Semester  6 Status: ", 1, 6)) {
                case 1: e.setEnrollmentId(text("Enrollment ID: ", 4, 30)); break;
                case 2: { Student s = choose("student", students); if (s != null) e.setStudent(s); break; }
                case 3: { Course c = choose("course", courses); if (c != null) e.setCourse(c); break; }
                case 4: e.setEnrollmentDate(date("Enrollment date: ", false)); break;
                case 5: e.setSemester(enumValue("Semester (SEMESTER_1, SEMESTER_2, SUMMER): ",
                        "SEMESTER_1", "SEMESTER_2", "SUMMER")); break;
                case 6: e.setEnrollmentStatus(enumValue("Status (ACTIVE, DROPPED, COMPLETED): ",
                        "ACTIVE", "DROPPED", "COMPLETED")); break;
                default: break;
            }
        } else if (record instanceof Assessment) {
            Assessment a = (Assessment) record;
            switch (number("Edit field 1 ID  2 Enrollment  3 Type  4 Maximum  5 Obtained  6 Date: ", 1, 6)) {
                case 1: a.setAssessmentId(text("Assessment ID: ", 4, 30)); break;
                case 2: { Enrollment e = choose("enrollment", enrollments); if (e != null) a.setEnrollment(e); break; }
                case 3: a.setAssessmentType(enumValue("Type (QUIZ, ASSIGNMENT, MIDTERM, FINAL): ",
                        "QUIZ", "ASSIGNMENT", "MIDTERM", "FINAL")); break;
                case 4: a.setMaximumScore(decimal("Maximum score: ", 0.000001, Double.MAX_VALUE)); break;
                case 5: a.setObtainedScore(decimal("Obtained score: ", 0, a.getMaximumScore())); break;
                case 6: a.setAssessmentDate(date("Assessment date: ", false)); break;
                default: break;
            }
        } else if (record instanceof FeePayment) {
            FeePayment p = (FeePayment) record;
            switch (number("Edit field 1 ID  2 Student  3 Amount  4 Date  5 Method  6 Receipt: ", 1, 6)) {
                case 1: p.setPaymentId(text("Payment ID: ", 4, 30)); break;
                case 2: { Student s = choose("student", students); if (s != null) p.setStudent(s); break; }
                case 3: p.setAmount(money("Amount: ")); break;
                case 4: p.setPaymentDate(date("Payment date: ", false)); break;
                case 5: p.setPaymentMethod(enumValue("Method (CASH, MOBILE_MONEY, BANK_TRANSFER, CARD): ",
                        "CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CARD")); break;
                case 6: p.setReceiptNumber(text("Receipt number: ", 1, 50)); break;
                default: break;
            }
        } else if (record instanceof LibraryBook) {
            LibraryBook b = (LibraryBook) record;
            switch (number("Edit field 1 ID  2 ISBN  3 Title  4 Author  5 Total copies  6 Available copies: ", 1, 6)) {
                case 1: b.setBookId(text("Book ID: ", 4, 30)); break;
                case 2: b.setIsbn(text("ISBN: ", 10, 20)); break;
                case 3: b.setTitle(text("Title: ", 2, 200)); break;
                case 4: b.setAuthor(text("Author: ", 2, 120)); break;
                case 5: b.setTotalCopies(number("Total copies: ", b.getAvailableCopies(), Integer.MAX_VALUE)); break;
                case 6: b.setAvailableCopies(number("Available copies: ", 0, b.getTotalCopies())); break;
                default: break;
            }
        } else if (record instanceof BookLoan) {
            System.out.println("Change returned date through the Return Library Book workflow.");
        }
    }

    private <T extends Identifiable & Registrable> void save(List<T> records, T record)
            throws ValidationException, DuplicateRecordException {
        record.validate();
        for (Identifiable existing : allRecords()) {
            if (existing.getId().equalsIgnoreCase(record.getId()))
                throw new DuplicateRecordException("ID " + record.getId() + " already exists.");
        }
        records.add(record);
        System.out.println("Saved successfully: " + record.getRegistrationSummary());
    }

    private void unique(String candidate, String label, List<String> existing) throws DuplicateRecordException {
        for (String value : existing) if (value.equalsIgnoreCase(candidate))
            throw new DuplicateRecordException(label + " " + candidate + " already exists.");
    }

    private List<String> personIds() {
        List<String> values = new ArrayList<>();
        for (Person p : people()) values.add(p.getPersonId());
        return values;
    }

    private List<String> staffNumbers() {
        List<String> values = new ArrayList<>();
        for (Administrator item : administrators) values.add(item.getStaffNumber());
        return values;
    }

    private List<String> emails() {
        List<String> values = new ArrayList<>();
        for (Person p : people()) {
            values.add(p.getEmail());
            if (p instanceof AcademicMember) values.add(((AcademicMember) p).getInstitutionalEmail());
        }
        return values;
    }

    private List<Person> people() {
        List<Person> values = new ArrayList<>();
        values.addAll(students); values.addAll(teachers); values.addAll(administrators);
        return values;
    }

    private boolean ensureDepartments() {
        while (departments.isEmpty()) {
            System.out.println("No department exists. Register a department before continuing.");
            if (!yesNo("Register a department now?"))
                throw new PrerequisiteMissingException("A department is required for this registration.");
            registerDepartment();
        }
        return true;
    }

    private boolean ensureStudents() {
        while (students.isEmpty()) {
            System.out.println("No student exists. Register a student before continuing.");
            if (!yesNo("Register a student now?"))
                throw new PrerequisiteMissingException("A student is required for this operation.");
            registerStudent();
        }
        return true;
    }

    private boolean ensureTeachers() {
        while (teachers.isEmpty()) {
            System.out.println("No teacher exists. Register a teacher before continuing.");
            if (!yesNo("Register a teacher now?"))
                throw new PrerequisiteMissingException("A teacher is required for this operation.");
            registerTeacher();
        }
        return !teachers.isEmpty();
    }

    private boolean ensureCourses() {
        while (courses.isEmpty()) {
            System.out.println("No course exists. Register a course before enrolling a student.");
            if (!yesNo("Register a course now?"))
                throw new PrerequisiteMissingException("A course is required for student enrollment.");
            registerCourse();
        }
        return !courses.isEmpty();
    }

    private Department chooseDepartment() {
        return choose("Department", departments);
    }

    private <T extends Identifiable & Registrable> T choose(String label, List<T> records) {
        if (records.isEmpty()) {
            System.out.println("No " + label.toLowerCase() + " records are available.");
            return null;
        }
        System.out.println("Select " + label + ":");
        for (int i = 0; i < records.size(); i++)
            System.out.println((i + 1) + ". " + records.get(i).getRegistrationSummary());
        System.out.println("0. Cancel");
        int selected = number("Select: ", 0, records.size());
        return selected == 0 ? null : records.get(selected - 1);
    }

    private void registerDepartment() {
        try {
            String id = text("Department ID: ", 4, 30);
            String name = text("Department name (2-60 characters): ", 2, 60);
            String code = text("Department code: ", 2, 20);
            String office = text("Office location: ", 2, 60);
            String contact = email("Contact email: ");
            Teacher head = teachers.isEmpty() ? null : choose("head teacher (0 leaves unassigned)", teachers);
            Department record = new Department(id, name, code, office, contact, head);
            if (review(record)) {
                unique(record.getDepartmentCode(), "Department code", departmentCodes());
                unique(record.getContactEmail(), "Email", emails());
                save(departments, record);
            } else System.out.println("Department registration cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private List<String> departmentCodes() {
        List<String> values = new ArrayList<>();
        for (Department item : departments) values.add(item.getDepartmentCode());
        return values;
    }

    private Department prerequisiteDepartment() {
        if (!ensureDepartments()) return null;
        Department department = chooseDepartment();
        if (department == null) System.out.println("Registration cancelled.");
        return department;
    }

    private void registerStudent() {
        try {
            Department department = prerequisiteDepartment();
            if (department == null) return;
            System.out.println("\n--- STUDENT: PERSONAL DETAILS ---");
            String personId = text("Person ID (4-30 characters): ", 4, 30);
            String first = text("First name (2-60): ", 2, 60);
            String last = text("Last name (2-60): ", 2, 60);
            String email = email("Personal email: ");
            String phone = phone("Phone: ");
            LocalDate dob = date("Date of birth (YYYY-MM-DD): ", true);
            System.out.println("--- ACADEMIC MEMBER DETAILS ---");
            String memberNo = text("Member number (4-30): ", 4, 30);
            LocalDate join = date("Join date (YYYY-MM-DD): ", false);
            String academicStatus = status();
            String institutionalEmail = email("Institutional email: ");
            String advisor = text("Advisor name (2-120): ", 2, 120);
            System.out.println("--- STUDENT DETAILS ---");
            String studentNo = text("Student number (4-30): ", 4, 30);
            String program = text("Program name: ", 2, 120);
            LocalDate admission = date("Admission date (YYYY-MM-DD): ", false);
            int year = number("Study year (1-8): ", 1, 8);
            String guardian = phone("Guardian phone: ");
            boolean scholarship = number("Scholarship: 1 YES, 2 NO: ", 1, 2) == 1;
            Student record = new Student(personId, first, last, email, phone, dob, memberNo, department, join,
                    academicStatus, institutionalEmail, advisor, studentNo, program, admission, year, guardian, scholarship);
            if (review(record)) {
                unique(record.getPersonId(), "Person ID", personIds());
                unique(record.getStudentNumber(), "Student number", studentNumbers());
                unique(record.getEmail(), "Email", emails());
                if (record.getEmail().equalsIgnoreCase(record.getInstitutionalEmail()))
                    throw new DuplicateRecordException("Personal and institutional email addresses must differ.");
                unique(record.getInstitutionalEmail(), "Email", emails());
                save(students, record);
            } else System.out.println("Student registration cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private List<String> studentNumbers() {
        List<String> values = new ArrayList<>();
        for (Student item : students) values.add(item.getStudentNumber());
        return values;
    }

    private void registerTeacher() {
        try {
            Department department = prerequisiteDepartment();
            if (department == null) return;
            System.out.println("\n--- TEACHER: PERSONAL DETAILS ---");
            String personId = text("Person ID (4-30 characters): ", 4, 30);
            String first = text("First name: ", 2, 60); String last = text("Last name: ", 2, 60);
            String email = email("Personal email: "); String phone = phone("Phone: ");
            LocalDate dob = date("Date of birth (YYYY-MM-DD): ", true);
            System.out.println("--- ACADEMIC MEMBER DETAILS ---");
            String memberNo = text("Member number: ", 4, 30); LocalDate join = date("Join date (YYYY-MM-DD): ", false);
            String academicStatus = status(); String institutionalEmail = email("Institutional email: ");
            String advisor = text("Advisor name: ", 2, 120);
            System.out.println("--- TEACHER DETAILS ---");
            String employeeNo = text("Employee number: ", 4, 30);
            String specialization = text("Specialization: ", 2, 120);
            LocalDate hireDate = date("Hire date (YYYY-MM-DD): ", false);
            BigDecimal salary = money("Salary: ");
            String office = text("Office number: ", 1, 30);
            String employment = enumValue("Employment type (FULL_TIME, PART_TIME, CONTRACT): ",
                    "FULL_TIME", "PART_TIME", "CONTRACT");
            Teacher record = new Teacher(personId, first, last, email, phone, dob, memberNo, department, join,
                    academicStatus, institutionalEmail, advisor, employeeNo, specialization, hireDate, salary, office, employment);
            if (review(record)) {
                unique(record.getPersonId(), "Person ID", personIds());
                unique(record.getEmployeeNumber(), "Employee number", employeeNumbers());
                unique(record.getEmail(), "Email", emails());
                if (record.getEmail().equalsIgnoreCase(record.getInstitutionalEmail()))
                    throw new DuplicateRecordException("Personal and institutional email addresses must differ.");
                unique(record.getInstitutionalEmail(), "Email", emails());
                save(teachers, record);
            } else System.out.println("Teacher registration cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private List<String> employeeNumbers() {
        List<String> values = new ArrayList<>();
        for (Teacher item : teachers) values.add(item.getEmployeeNumber());
        return values;
    }

    private void registerAdministrator() {
        try {
            System.out.println("\n--- ADMINISTRATOR: PERSONAL DETAILS ---");
            String personId = text("Person ID (4-30 characters): ", 4, 30); String first = text("First name: ", 2, 60);
            String last = text("Last name: ", 2, 60); String email = email("Email: ");
            String phone = phone("Phone: "); LocalDate dob = date("Date of birth (YYYY-MM-DD): ", true);
            System.out.println("--- ADMINISTRATOR DETAILS ---");
            String staff = text("Staff number: ", 4, 30); String title = text("Job title: ", 2, 120);
            String office = text("Assigned office: ", 1, 60); LocalDate employed = date("Employment date (YYYY-MM-DD): ", false);
            int access = number("Access level (1-10): ", 1, 10); boolean active = number("Active? 1 YES, 2 NO: ", 1, 2) == 1;
            Administrator record = new Administrator(personId, first, last, email, phone, dob, staff, title, office, employed, access, active);
            if (review(record)) {
                unique(record.getPersonId(), "Person ID", personIds());
                unique(record.getEmail(), "Email", emails());
                unique(record.getStaffNumber(), "Staff number", staffNumbers());
                save(administrators, record);
            } else System.out.println("Administrator registration cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private void registerCourse() {
        try {
            Department department = prerequisiteDepartment();
            if (department == null || !ensureTeachers()) return;
            List<Teacher> eligible = new ArrayList<>();
            for (Teacher teacher : teachers) if (teacher.getDepartment() == department) eligible.add(teacher);
            if (eligible.isEmpty()) {
                System.out.println("No teacher belongs to that department. Register a teacher in it before creating the course.");
                if (yesNo("Register a teacher now?")) registerTeacher();
                eligible.clear();
                for (Teacher teacher : teachers) if (teacher.getDepartment() == department) eligible.add(teacher);
                if (eligible.isEmpty()) return;
            }
            Teacher teacher = choose("teacher", eligible);
            if (teacher == null) return;
            String id = text("Course ID: ", 4, 30); String code = text("Course code: ", 2, 30);
            String title = text("Course title: ", 2, 120); int credits = number("Credit hours (1-6): ", 1, 6);
            Course record = new Course(id, code, title, credits, department, teacher);
            if (review(record)) {
                List<String> codes = new ArrayList<>(); for (Course c : courses) codes.add(c.getCourseCode());
                unique(record.getCourseCode(), "Course code", codes); save(courses, record);
            } else System.out.println("Course registration cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private void registerClassroom() {
        try {
            Classroom record = new Classroom(text("Classroom ID: ", 4, 30), text("Building name: ", 2, 60),
                    text("Room number: ", 1, 30), number("Capacity (greater than 0): ", 1, Integer.MAX_VALUE),
                    number("Floor number (0 or greater): ", 0, Integer.MAX_VALUE),
                    number("Available? 1 YES, 2 NO: ", 1, 2) == 1);
            if (review(record)) save(classrooms, record);
            else System.out.println("Classroom registration cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private void enrollStudent() {
        try {
            if (!ensureStudents() || !ensureCourses()) return;
            Student student = choose("student", students); Course course = choose("course", courses);
            if (student == null || course == null) return;
            String semester = enumValue("Semester (SEMESTER_1, SEMESTER_2, SUMMER): ", "SEMESTER_1", "SEMESTER_2", "SUMMER");
            for (Enrollment enrollment : enrollments)
                if (enrollment.isActive() && enrollment.getStudent() == student && enrollment.getCourse() == course
                        && enrollment.getSemester().equals(semester))
                    throw new DuplicateRecordException("This student is already actively enrolled in that course and semester.");
            Enrollment record = new Enrollment(text("Enrollment ID: ", 4, 30), student, course,
                    date("Enrollment date (YYYY-MM-DD): ", false), semester, "ACTIVE");
            if (review(record)) save(enrollments, record);
            else System.out.println("Enrollment cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private void recordAssessment() {
        try {
            List<Enrollment> active = new ArrayList<>();
            for (Enrollment item : enrollments) if (item.isActive()) active.add(item);
            if (active.isEmpty()) {
                System.out.println("No active enrollment exists. Register a student and course, then enroll the student first.");
                if (yesNo("Register an enrollment now?")) enrollStudent();
                active.clear(); for (Enrollment item : enrollments) if (item.isActive()) active.add(item);
                if (active.isEmpty()) return;
            }
            Enrollment enrollment = choose("active enrollment", active);
            if (enrollment == null) return;
            Assessment record = new Assessment(text("Assessment ID: ", 4, 30), enrollment,
                    enumValue("Assessment type (QUIZ, ASSIGNMENT, MIDTERM, FINAL): ", "QUIZ", "ASSIGNMENT", "MIDTERM", "FINAL"),
                    decimal("Maximum score: ", 0.000001, Double.MAX_VALUE),
                    decimal("Obtained score (0 to maximum): ", 0, Double.MAX_VALUE),
                    date("Assessment date (YYYY-MM-DD): ", false));
            if (review(record)) save(assessments, record);
            else System.out.println("Assessment cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private void recordPayment() {
        try {
            if (!ensureStudents()) return;
            Student student = choose("student", students); if (student == null) return;
            String receipt = text("Receipt number: ", 1, 50);
            FeePayment record = new FeePayment(text("Payment ID: ", 4, 30), student, money("Amount: "),
                    date("Payment date (YYYY-MM-DD): ", false),
                    enumValue("Payment method (CASH, MOBILE_MONEY, BANK_TRANSFER, CARD): ",
                            "CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CARD"), receipt);
            if (review(record)) {
                List<String> receipts = new ArrayList<>(); for (FeePayment p : payments) receipts.add(p.getReceiptNumber());
                unique(record.getReceiptNumber(), "Receipt number", receipts);
                save(payments, record);
            }
            else System.out.println("Payment cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private void registerBook() {
        try {
            int copies = number("Total copies (0 or greater): ", 0, Integer.MAX_VALUE);
            LibraryBook record = new LibraryBook(text("Book ID: ", 4, 30), text("ISBN: ", 10, 20),
                    text("Title: ", 2, 200), text("Author: ", 2, 120), copies, copies);
            if (review(record)) save(books, record);
            else System.out.println("Book registration cancelled; nothing was saved.");
        } catch (ValidationException | DuplicateRecordException ex) { System.out.println(ex.getMessage()); }
    }

    private String enumValue(String prompt, String... values) {
        while (true) {
            String value = ask(prompt).toUpperCase().replace(' ', '_').replace('-', '_');
            for (String allowed : values) if (allowed.equals(value)) return value;
            System.out.print("Allowed values: ");
            for (int i = 0; i < values.length; i++) System.out.print((i == 0 ? "" : ", ") + values[i]);
            System.out.println();
        }
    }

    private boolean ensureBooks() {
        while (books.isEmpty()) {
            System.out.println("No library book exists. Register a book before borrowing.");
            if (!yesNo("Register a library book now?"))
                throw new PrerequisiteMissingException("A library book is required before borrowing.");
            registerBook();
        }
        return !books.isEmpty();
    }

    private void borrowBook() throws BookUnavailableException {
        if (!ensureStudents() || !ensureBooks()) return;
        List<LibraryBook> available = new ArrayList<>();
        for (LibraryBook book : books) if (book.getAvailableCopies() > 0) available.add(book);
        if (available.isEmpty()) throw new BookUnavailableException("No copies of any registered book are currently available.");
        Student student = choose("student", students);
        LibraryBook book = choose("available book", available);
        if (student == null || book == null) return;
        BookLoan loan = new BookLoan(text("Loan ID: ", 4, 30), student, book,
                date("Borrowed date (YYYY-MM-DD): ", false), null, null);
        loan.setDueDate(dueDate("Due date (YYYY-MM-DD): ", loan.getBorrowedDate()));
        if (review(loan)) {
            try {
                save(loans, loan);
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                System.out.println("Book stock updated. Copies available: " + book.getAvailableCopies());
            } catch (ValidationException | DuplicateRecordException ex) {
                System.out.println(ex.getMessage());
            }
        } else System.out.println("Borrowing cancelled; stock was not changed.");
    }

    private void returnBook() throws RecordNotFoundException, ValidationException {
        List<BookLoan> active = new ArrayList<>();
        for (BookLoan loan : loans) if (loan.isActive()) active.add(loan);
        if (active.isEmpty()) throw new RecordNotFoundException("There are no active book loans to return.");
        BookLoan loan = choose("active loan", active);
        if (loan == null) return;
        LocalDate returned = date("Return date (YYYY-MM-DD): ", false);
        if (returned.isBefore(loan.getBorrowedDate()))
            throw new ValidationException("Return date cannot be before borrowed date.");
        System.out.println("Return this loan? " + loan);
        if (!confirm("Confirm return")) return;
        loan.setReturnedDate(returned);
        loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);
        System.out.println("Book returned successfully. Copies available: " + loan.getBook().getAvailableCopies());
    }

    private List<Identifiable> allRecords() {
        List<Identifiable> records = new ArrayList<>();
        records.addAll(departments); records.addAll(students); records.addAll(teachers); records.addAll(administrators);
        records.addAll(courses); records.addAll(classrooms); records.addAll(enrollments); records.addAll(assessments);
        records.addAll(payments); records.addAll(books); records.addAll(loans);
        return records;
    }

    private void viewAll() {
        List<Identifiable> records = allRecords();
        if (records.isEmpty()) {
            System.out.println("No records have been registered.");
            return;
        }
        System.out.println("\n--- ALL SCHOOL RECORDS ---");
        for (Identifiable record : records) {
            System.out.println(record instanceof Registrable
                    ? ((Registrable) record).getRegistrationSummary() : record.getDisplayName());
        }
    }

    private void searchById() throws RecordNotFoundException {
        String id = text("Enter record ID: ", 1, 50);
        for (Identifiable record : allRecords()) {
            if (record.getId().equalsIgnoreCase(id)) {
                if (record instanceof Registrable) ((Registrable) record).printDetails();
                return;
            }
        }
        throw new RecordNotFoundException("No record was found with ID " + id + ".");
    }

    private void updateRecord() throws RecordNotFoundException, ValidationException {
        String id = text("Enter the ID of the record to update: ", 1, 50);
        Identifiable record = null;
        for (Identifiable item : allRecords()) if (item.getId().equalsIgnoreCase(id)) { record = item; break; }
        if (record == null) throw new RecordNotFoundException("No record was found with ID " + id + ".");
        if (!(record instanceof Registrable)) return;
        ((Registrable) record).printDetails();
        if (!confirm("Update this record")) return;

        if (record instanceof Student) {
            Student item = (Student) record;
            switch (number("Edit: 1 Program  2 Study year  3 Academic status  4 Guardian phone: ", 1, 4)) {
                case 1: item.setProgramName(text("New program: ", 2, 120)); break;
                case 2: item.setStudyYear(number("Study year (1-8): ", 1, 8)); break;
                case 3: item.setAcademicStatus(status()); break;
                case 4: item.setGuardianPhone(phone("Guardian phone: ")); break;
            }
            item.validate();
        } else if (record instanceof Teacher) {
            Teacher item = (Teacher) record;
            switch (number("Edit: 1 Specialization  2 Salary  3 Status  4 Office: ", 1, 4)) {
                case 1: item.setSpecialization(text("New specialization: ", 2, 120)); break;
                case 2: item.setSalary(money("New salary: ")); break;
                case 3: item.setAcademicStatus(status()); break;
                case 4: item.setOfficeNumber(text("New office: ", 1, 30)); break;
            }
            item.validate();
        } else if (record instanceof Administrator) {
            Administrator item = (Administrator) record;
            switch (number("Edit: 1 Job title  2 Office  3 Access level  4 Active status: ", 1, 4)) {
                case 1: item.setJobTitle(text("New job title: ", 2, 120)); break;
                case 2: item.setAssignedOffice(text("New office: ", 1, 60)); break;
                case 3: item.setAccessLevel(number("Access level (1-10): ", 1, 10)); break;
                case 4: item.setActive(number("Active? 1 YES, 2 NO: ", 1, 2) == 1); break;
            }
            item.validate();
        } else if (record instanceof Department) {
            Department item = (Department) record;
            switch (number("Edit: 1 Name  2 Office  3 Contact email  4 Head teacher: ", 1, 4)) {
                case 1: item.setDepartmentName(text("New name: ", 2, 60)); break;
                case 2: item.setOfficeLocation(text("New office: ", 2, 60)); break;
                case 3: item.setContactEmail(email("New contact email: ")); break;
                case 4: item.setHeadTeacher(choose("head teacher", teachers)); break;
            }
            item.validate();
        } else if (record instanceof Course) {
            Course item = (Course) record;
            switch (number("Edit: 1 Title  2 Credit hours: ", 1, 2)) {
                case 1: item.setCourseTitle(text("New title: ", 2, 120)); break;
                case 2: item.setCreditHours(number("Credit hours (1-6): ", 1, 6)); break;
            }
            item.validate();
        } else if (record instanceof Classroom) {
            Classroom item = (Classroom) record;
            switch (number("Edit: 1 Capacity  2 Available: ", 1, 2)) {
                case 1: item.setCapacity(number("Capacity: ", 1, Integer.MAX_VALUE)); break;
                case 2: item.setAvailable(number("Available? 1 YES, 2 NO: ", 1, 2) == 1); break;
            }
            item.validate();
        } else if (record instanceof Enrollment) {
            Enrollment item = (Enrollment) record;
            item.setEnrollmentStatus(enumValue("Status (ACTIVE, DROPPED, COMPLETED): ", "ACTIVE", "DROPPED", "COMPLETED"));
            item.validate();
        } else if (record instanceof Assessment) {
            Assessment item = (Assessment) record;
            item.setObtainedScore(decimal("New obtained score (0 to " + item.getMaximumScore() + "): ", 0, item.getMaximumScore()));
            item.validate();
        } else if (record instanceof FeePayment) {
            FeePayment item = (FeePayment) record;
            item.setPaymentMethod(enumValue("Payment method (CASH, MOBILE_MONEY, BANK_TRANSFER, CARD): ",
                    "CASH", "MOBILE_MONEY", "BANK_TRANSFER", "CARD"));
            item.validate();
        } else if (record instanceof LibraryBook) {
            LibraryBook item = (LibraryBook) record;
            switch (number("Edit: 1 Title  2 Author  3 Total copies  4 Available copies: ", 1, 4)) {
                case 1: item.setTitle(text("New title: ", 2, 200)); break;
                case 2: item.setAuthor(text("New author: ", 2, 120)); break;
                case 3:
                    item.setTotalCopies(number("Total copies: ", item.getAvailableCopies(), Integer.MAX_VALUE));
                    break;
                case 4:
                    item.setAvailableCopies(number("Available copies: ", 0, item.getTotalCopies()));
                    break;
            }
            item.validate();
        } else if (record instanceof BookLoan) {
            System.out.println("Use Return Library Book to update a loan.");
            return;
        }
        System.out.println("Record updated:");
        ((Registrable) record).printDetails();
    }

    private void academicSummary() throws RecordNotFoundException {
        if (students.isEmpty()) throw new RecordNotFoundException("No students are registered.");
        Student student = choose("student", students);
        if (student == null) return;
        System.out.println("\nAcademic summary for " + student.getDisplayName());
        System.out.println("Student number: " + student.getStudentNumber() + " | Program: " + student.getProgramName()
                + " | Year: " + student.getStudyYear() + " | Department: " + student.getDepartment().getDepartmentName());
        System.out.println("Enrollments:");
        boolean found = false;
        for (Enrollment enrollment : enrollments) if (enrollment.getStudent() == student) {
            System.out.println("  " + enrollment);
            for (Assessment assessment : assessments) if (assessment.getEnrollment() == enrollment)
                System.out.println("    " + assessment.getAssessmentType() + ": " + assessment.getObtainedScore()
                        + "/" + assessment.getMaximumScore());
            found = true;
        }
        if (!found) System.out.println("  No enrollments recorded.");
    }

    private void paymentHistory() throws RecordNotFoundException {
        if (students.isEmpty()) throw new RecordNotFoundException("No students are registered.");
        Student student = choose("student", students);
        if (student == null) return;
        System.out.println("Payment history for " + student.getDisplayName() + ":");
        BigDecimal total = BigDecimal.ZERO;
        boolean found = false;
        for (FeePayment payment : payments) if (payment.getStudent() == student) {
            System.out.println("  " + payment);
            total = total.add(payment.getAmount());
            found = true;
        }
        if (!found) System.out.println("No payments recorded.");
        System.out.println("Total paid: " + total);
    }

    private void availableBooks() {
        boolean found = false;
        System.out.println("Available library books:");
        for (LibraryBook book : books) if (book.getAvailableCopies() > 0) {
            System.out.println(book.getRegistrationSummary());
            found = true;
        }
        if (!found) System.out.println("No books are currently available.");
    }

    private void demonstratePolymorphism() {
        List<Person> personRecords = new ArrayList<>();
        personRecords.addAll(students); personRecords.addAll(teachers); personRecords.addAll(administrators);
        if (personRecords.isEmpty()) {
            System.out.println("Register a student, teacher or administrator to demonstrate Person polymorphism.");
        } else {
            System.out.println("Person reference polymorphism:");
            for (Person person : personRecords)
                System.out.println(person.getRoleDescription() + ": " + person.getDisplayName()
                        + " | active=" + person.isActive());
        }
        List<Identifiable> interfaceRecords = allRecords();
        if (interfaceRecords.isEmpty()) {
            System.out.println("Register any record to demonstrate Identifiable polymorphism.");
        } else {
            System.out.println("Identifiable interface polymorphism:");
            for (Identifiable item : interfaceRecords)
                System.out.println(item.getId() + " | " + item.getDisplayName() + " | active=" + item.isActive());
        }
    }
}
