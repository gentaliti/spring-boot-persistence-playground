package com.gentaliti.item121.builder;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.*;

public class SpecificationChunk<T> implements Specification<T> {
    private final Condition condition;

    public SpecificationChunk(Condition condition) {
        this.condition = condition;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> cquery, @NonNull CriteriaBuilder cbuilder) {
        Root<T> conditionRoot = root;
        if (condition.getLeftHand().contains(".")) {
            String[] split = condition.getLeftHand().split("\\.");
            String joinName = split[0];
            String leftHand = split[1];
            // TODO: 10/11/20 Make this cleaner ;-}
            switch (condition.getOperation()) {
                case EQUAL:
                    return cbuilder.equal(this.getOrCreateFetch(root, joinName).get(leftHand), condition.getRightHand());
                case NOT_EQUAL:
                    return cbuilder.notEqual(this.getOrCreateFetch(root, joinName).get(leftHand), condition.getRightHand());
                case GREATER_THAN:
                    return cbuilder.greaterThan(this.getOrCreateFetch(root, joinName).get(leftHand), condition.getRightHand());
                case LESS_THAN:
                    return cbuilder.lessThan(this.getOrCreateFetch(root, joinName).get(leftHand), condition.getRightHand());
                case LIKE:
                    return cbuilder.like(this.getOrCreateFetch(root, joinName).get(leftHand), condition.getRightHand());
                default:
                    return null;
            }
        }
        switch (condition.getOperation()) {
            case EQUAL:
                return cbuilder.equal(root.get(condition.getLeftHand()), condition.getRightHand());
            case NOT_EQUAL:
                return cbuilder.notEqual(root.get(condition.getLeftHand()), condition.getRightHand());
            case GREATER_THAN:
                return cbuilder.greaterThan(root.get(condition.getLeftHand()), condition.getRightHand());
            case LESS_THAN:
                return cbuilder.lessThan(root.get(condition.getLeftHand()), condition.getRightHand());
            case LIKE:
                return cbuilder.like(root.get(condition.getLeftHand()), condition.getRightHand());
            default:
                return null;
        }
    }

    public static Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute) {
        for (Join<?, ?> join : from.getJoins()) {

            boolean sameName = join.getAttribute().getName().equals(attribute);

            if (sameName && join.getJoinType().equals(JoinType.LEFT)) {
                return join;
            }
        }

        return from.join(attribute, JoinType.LEFT);
    }

    public static Join<?, ?> getOrCreateFetch(From<?, ?> from, String attribute) {
        for (Fetch<?, ?> fetch : from.getFetches()) {

            boolean sameName = fetch.getAttribute().getName().equals(attribute);

            if (sameName && fetch.getJoinType().equals(JoinType.LEFT)) {
                return (Join<?, ?>) fetch;
            }
        }
        Fetch<?, ?> a = from.fetch(attribute, JoinType.LEFT);
        return (Join<?, ?>) a;
    }
}
