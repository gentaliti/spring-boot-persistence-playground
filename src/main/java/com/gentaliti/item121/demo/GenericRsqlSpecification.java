package com.gentaliti.item121.demo;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class GenericRsqlSpecification<T> implements Specification<T> {

    private String property;
    private ComparisonOperator operator;
    private List<String> arguments;

    public GenericRsqlSpecification(final String property, final ComparisonOperator operator, final List<String> arguments) {
        super();
        this.property = property;
        this.operator = operator;
        this.arguments = arguments;
    }

    @Override
    public Predicate toPredicate(@NonNull final Root<T> root, @NonNull final CriteriaQuery<?> query, @NonNull final CriteriaBuilder builder) {
        From<?, ?> conditionsRoot = buildConditionRoot(root);

        final List<Object> args = castArguments(conditionsRoot);
        final Object argument = args.get(0);
        query.distinct(true);
        switch (RsqlSearchOperation.getSimpleOperator(operator)) {
            case EQUAL: {
                if (argument instanceof String) {
                    return builder.like(conditionsRoot.get(property), argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNull(conditionsRoot.get(property));
                } else {
                    return builder.equal(conditionsRoot.get(property), argument);
                }
            }
            case NOT_EQUAL: {
                if (argument instanceof String) {
                    return builder.notLike(conditionsRoot.<String> get(property), argument.toString().replace('*', '%'));
                } else if (argument == null) {
                    return builder.isNotNull(conditionsRoot.get(property));
                } else {
                    return builder.notEqual(conditionsRoot.get(property), argument);
                }
            }
            case GREATER_THAN: {
                if (argument instanceof Timestamp) {
                    return builder.greaterThan(conditionsRoot.get(property), (Timestamp) argument);
                }
                return builder.greaterThan(conditionsRoot.get(property), argument.toString());
            }
            case GREATER_THAN_OR_EQUAL: {
                if (argument instanceof Timestamp) {
                    return builder.greaterThanOrEqualTo(conditionsRoot.get(property), (Timestamp) argument);
                }
                return builder.greaterThanOrEqualTo(conditionsRoot.get(property), argument.toString());
            }
            case LESS_THAN: {
                if (argument instanceof Timestamp) {
                    return builder.lessThan(conditionsRoot.get(property), (Timestamp) argument);
                }
                return builder.lessThan(conditionsRoot.<String> get(property), argument.toString());
            }
            case LESS_THAN_OR_EQUAL: {
                if (argument instanceof Timestamp) {
                    return builder.lessThanOrEqualTo(conditionsRoot.get(property), (Timestamp) argument);
                }
                return builder.lessThanOrEqualTo(conditionsRoot.<String> get(property), argument.toString());
            }
            case IN:
                return conditionsRoot.get(property).in(args);
            case NOT_IN:
                return builder.not(conditionsRoot.get(property).in(args));
        }

        return null;
    }

    private From<?, ?> buildConditionRoot(Root<T> root) {
        if (property.contains(".")) {
            String joinName = property.substring(0, property.lastIndexOf('.'));
            property = property.substring(property.lastIndexOf('.') + 1);
            String[] joins = joinName.split("\\.");
            Join<?, ?> join = getOrCreateFetch(root, joins[0]);
            for (int i = 1; i < joins.length; i++) {
                join = getOrCreateFetch(join, joins[i]);
            }

            return join;
        }
        return root;
    }

    private List<Object> castArguments(final From<?, ?> root) {

        final Class<? extends Object> type = root.get(property).getJavaType();

        final List<Object> args = arguments.stream().map(arg -> {
            if (type.equals(Integer.class)) {
                return Integer.parseInt(arg);
            } else if (type.equals(Long.class)) {
                return Long.parseLong(arg);
            } else {
                Timestamp timestamp = getDateIfExists(arg);
                if (timestamp != null) {
                    return timestamp;
                }
            }
            return arg;
        }).collect(Collectors.toList());

        return args;
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

    public static Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute) {
        for (Join<?, ?> join : from.getJoins()) {
            boolean sameName = join.getAttribute().getName().equals(attribute);
            if (sameName && join.getJoinType().equals(JoinType.INNER)) {
                return join;
            }
        }
        return from.join(attribute, JoinType.INNER);
    }

    public static Join<?, ?> getOrCreateFetch(From<?, ?> from, String attribute) {
        for (Fetch<?, ?> fetch : from.getFetches()) {
            boolean sameName = fetch.getAttribute().getName().equals(attribute);
            if (sameName && fetch.getJoinType().equals(JoinType.LEFT)) {
                return (Join<?, ?>) fetch;
            }
        }
        return (Join<?, ?>) from.fetch(attribute, JoinType.LEFT);
    }
}
