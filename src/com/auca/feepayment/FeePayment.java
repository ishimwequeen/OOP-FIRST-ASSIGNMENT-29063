package com.auca.feepayment;

import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.student.Student;
import com.auca.util.Checks;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FeePayment implements Identifiable, Registrable {
    private String paymentId;
    private Student student;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String receiptNumber;

    public FeePayment(String paymentId, Student student, BigDecimal amount, LocalDate paymentDate,
                      String paymentMethod, String receiptNumber) {
        this.paymentId = paymentId; this.student = student; this.amount = amount; this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod; this.receiptNumber = receiptNumber;
    }
    @Override public String getId() { return paymentId; }
    @Override public String getDisplayName() { return "Payment " + receiptNumber + " by " + student.getDisplayName(); }
    @Override public boolean isActive() { return true; }
    @Override public void validate() throws ValidationException {
        Checks.text(paymentId, "Payment ID", 4, 30); Checks.required(student, "Student"); Checks.positive(amount, "Payment amount");
        Checks.notFuture(paymentDate, "Payment date");
        if (!"CASH".equals(paymentMethod) && !"MOBILE_MONEY".equals(paymentMethod)
                && !"BANK_TRANSFER".equals(paymentMethod) && !"CARD".equals(paymentMethod))
            throw new ValidationException("Payment method must be CASH, MOBILE_MONEY, BANK_TRANSFER or CARD.");
        Checks.text(receiptNumber, "Receipt number", 1, 50);
    }
    @Override public void printDetails() { System.out.println(this); }
    @Override public String getRegistrationSummary() { return "Payment " + paymentId + " - " + amount; }
    public String getPaymentId() { return paymentId; } public void setPaymentId(String v) { paymentId = v; }
    public Student getStudent() { return student; } public void setStudent(Student v) { student = v; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal v) { amount = v; }
    public LocalDate getPaymentDate() { return paymentDate; } public void setPaymentDate(LocalDate v) { paymentDate = v; }
    public String getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(String v) { paymentMethod = v; }
    public String getReceiptNumber() { return receiptNumber; } public void setReceiptNumber(String v) { receiptNumber = v; }
    @Override public String toString() { return "FeePayment{id='" + paymentId + "', student=" + student.getStudentNumber()
            + ", amount=" + amount + ", date=" + paymentDate + ", method='" + paymentMethod + "', receipt='" + receiptNumber + "'}"; }
}
