package com.gentaliti.item121.builder;

import com.gentaliti.item121.builder.type.OperationType;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

public class PredicateBuilder {
    private PredicateBuilder() {
    }

    public static Predicate buildPredicate(@NonNull From<?, ?> root, @NonNull CriteriaBuilder criteriaBuilder, OperationType operation, String leftHand,
                                           String rightHand, String secondRightHand) {
        switch (operation) {
            case EQUAL:
                return PredicateBuilder.buildEqualPredicate(criteriaBuilder, root, leftHand, rightHand);
            case NOT_EQUAL:
                return PredicateBuilder.buildNotEqualPredicate(criteriaBuilder, root, leftHand, rightHand);
            case GREATER_THAN:
                return PredicateBuilder.buildGreaterThanPredicate(criteriaBuilder, root, leftHand, rightHand);
            case LESS_THAN:
                return PredicateBuilder.buildLessThanPredicate(criteriaBuilder, root, leftHand, rightHand);
            case LIKE:
                return criteriaBuilder.like(root.get(leftHand), rightHand);
            case BETWEEN:
                return PredicateBuilder.buildBetweenPredicate(criteriaBuilder, root, leftHand, rightHand, secondRightHand);
            default:
                return null;
        }
    }

    public static Predicate buildBetweenPredicate(@NonNull CriteriaBuilder criteriaBuilder, From<?, ?> root, String leftHand, String rightHand, String secondRightHand) {
        Timestamp firstDate = getDateIfExists(rightHand);
        Timestamp secondDate = getDateIfExists(secondRightHand);
        if (firstDate != null && secondDate != null) {
            return criteriaBuilder.between(root.get(leftHand), firstDate, secondDate);
        }
        return criteriaBuilder.between(root.get(leftHand), rightHand, secondRightHand);
    }

    public static Predicate buildEqualPredicate(@NonNull CriteriaBuilder criteriaBuilder, From<?, ?> root, String leftHand, String rightHand) {
        Timestamp firstDate = getDateIfExists(rightHand);
        if (firstDate != null) {
            return criteriaBuilder.equal(root.get(leftHand), firstDate);
        }
        return criteriaBuilder.equal(root.get(leftHand), rightHand);
    }

    public static Predicate buildNotEqualPredicate(@NonNull CriteriaBuilder criteriaBuilder, From<?, ?> root, String leftHand, String rightHand) {
        Timestamp firstDate = getDateIfExists(rightHand);
        if (firstDate != null) {
            return criteriaBuilder.notEqual(root.get(leftHand), firstDate);
        }
        return criteriaBuilder.notEqual(root.get(leftHand), rightHand);
    }

    public static Predicate buildGreaterThanPredicate(@NonNull CriteriaBuilder criteriaBuilder, From<?, ?> root, String leftHand, String rightHand) {
        Timestamp firstDate = getDateIfExists(rightHand);
        if (firstDate != null) {
            return criteriaBuilder.greaterThan(root.get(leftHand), firstDate);
        }
        return criteriaBuilder.greaterThan(root.get(leftHand), rightHand);
    }

    public static Predicate buildLessThanPredicate(@NonNull CriteriaBuilder criteriaBuilder, From<?, ?> root, String leftHand, String rightHand) {
        Timestamp firstDate = getDateIfExists(rightHand);
        if (firstDate != null) {
            return criteriaBuilder.lessThan(root.get(leftHand), firstDate);
        }
        return criteriaBuilder.lessThan(root.get(leftHand), rightHand);
    }

    public static Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute) {
        for (Join<?, ?> join : from.getJoins()) {
            boolean sameName = join.getAttribute().getName().equals(attribute);
            if (sameName && join.getJoinType().equals(JoinType.INNER)) {
                return join;
            }
        }
        return from.join(attribute, JoinType.INNER);
    }

    public static Fetch<?, ?> getOrCreateFetch(From<?, ?> from, String attribute) {
        for (Fetch<?, ?> fetch : from.getFetches()) {
            boolean sameName = fetch.getAttribute().getName().equals(attribute);
            if (sameName && fetch.getJoinType().equals(JoinType.INNER)) {
                return fetch;
            }
        }
        return from.fetch(attribute, JoinType.INNER);
    }

    private static Timestamp getDateIfExists(String s) {
        return localToTimeStamp(parseDate(s));
    }

    private static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static Timestamp localToTimeStamp(LocalDate date) {
        if (date == null) {
            return null;
        }
        return Timestamp.from(date.atStartOfDay().toInstant(ZoneOffset.UTC));
    }
}
