package com.auca.util;

import com.auca.exceptions.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

public final class Checks {
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PHONE = Pattern.compile("^\\+?\\d{10,15}$");
    private static final Pattern NATIONAL_ID = Pattern.compile("^\\d{16}$");

    private Checks() { }

    public static void nationalId(String value, String label) throws ValidationException {
        if (value == null || !NATIONAL_ID.matcher(value.trim()).matches())
            throw new ValidationException(label + " must contain exactly 16 digits.");
    }

    public static void text(String value, String label, int min, int max) throws ValidationException {
        if (value == null || value.trim().length() < min || value.trim().length() > max)
            throw new ValidationException(label + " must contain " + min + "-" + max + " characters.");
    }

    public static void email(String value, String label) throws ValidationException {
        if (value == null || value.length() > 120 || !EMAIL.matcher(value.trim()).matches())
            throw new ValidationException(label + " must be a valid email address (maximum 120 characters).");
    }

    public static void phone(String value, String label) throws ValidationException {
        if (value == null || !PHONE.matcher(value.trim()).matches())
            throw new ValidationException(label + " must contain 10-15 digits, optionally prefixed by +.");
    }

    public static void date(LocalDate value, String label) throws ValidationException {
        if (value == null) throw new ValidationException(label + " is required.");
    }

    public static void past(LocalDate value, String label) throws ValidationException {
        date(value, label);
        if (!value.isBefore(LocalDate.now())) throw new ValidationException(label + " must be in the past.");
    }

    public static void notFuture(LocalDate value, String label) throws ValidationException {
        date(value, label);
        if (value.isAfter(LocalDate.now())) throw new ValidationException(label + " cannot be in the future.");
    }

    public static void positive(BigDecimal value, String label) throws ValidationException {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException(label + " must be greater than zero.");
    }

    public static void required(Object value, String label) throws ValidationException {
        if (value == null) throw new ValidationException(label + " is required.");
    }
}
