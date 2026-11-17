package com.auca.assessment;

import com.auca.enrollment.Enrollment;
import com.auca.exceptions.ValidationException;
import com.auca.interfaces.Identifiable;
import com.auca.interfaces.Registrable;
import com.auca.util.Checks;

import java.time.LocalDate;

public class Assessment implements Identifiable, Registrable {
    private String assessmentId;
    private Enrollment enrollment;
    private String assessmentType;
    private double maximumScore;
    private double obtainedScore;
    private LocalDate assessmentDate;

    public Assessment(String assessmentId, Enrollment enrollment, String assessmentType, double maximumScore,
                      double obtainedScore, LocalDate assessmentDate) {
        this.assessmentId = assessmentId; this.enrollment = enrollment; this.assessmentType = assessmentType;
        this.maximumScore = maximumScore; this.obtainedScore = obtainedScore; this.assessmentDate = assessmentDate;
    }
    @Override public String getId() { return assessmentId; }
    @Override public String getDisplayName() { return assessmentType + " - " + enrollment.getStudent().getDisplayName(); }
    @Override public boolean isActive() { return true; }
    @Override public void validate() throws ValidationException {
        Checks.text(assessmentId, "Assessment ID", 4, 30); Checks.required(enrollment, "Enrollment");
        if (!"QUIZ".equals(assessmentType) && !"ASSIGNMENT".equals(assessmentType)
                && !"MIDTERM".equals(assessmentType) && !"FINAL".equals(assessmentType))
            throw new ValidationException("Assessment type must be QUIZ, ASSIGNMENT, MIDTERM or FINAL.");
        if (maximumScore <= 0) throw new ValidationException("Maximum score must be greater than zero.");
        if (obtainedScore < 0 || obtainedScore > maximumScore)
            throw new ValidationException("Obtained score must be between zero and the maximum score.");
        Checks.notFuture(assessmentDate, "Assessment date");
    }
    @Override public void printDetails() {
        System.out.println(this);
    }
    @Override public String getRegistrationSummary() {
        return "Assessment " + assessmentId + " - " + getDisplayName();
    }
    public String getAssessmentId() {
        return assessmentId;
    }
    public void setAssessmentId(String v) {
        assessmentId = v;
    }
    public Enrollment getEnrollment() {
        return enrollment;
    }
    public void setEnrollment(Enrollment v) {
        enrollment = v;
    }
    public String getAssessmentType() {
        return assessmentType;
    }
    public void setAssessmentType(String v) {
        assessmentType = v;
    }
    public double getMaximumScore() {
        return maximumScore;
    }
    public void setMaximumScore(double v) {
        maximumScore = v;
    }
    public double getObtainedScore() {
        return obtainedScore;
    }
    public void setObtainedScore(double v) {
        obtainedScore = v;
    }
    public LocalDate getAssessmentDate() {
        return assessmentDate;
    }
    public void setAssessmentDate(LocalDate v) {
        assessmentDate = v;
    }
    @Override public String toString() { return "Assessment{id='" + assessmentId + "', enrollment=" + enrollment.getEnrollmentId()
            + ", type='" + assessmentType + "', score=" + obtainedScore + "/" + maximumScore + ", date=" + assessmentDate + "}"; }
}
