package com.gentaliti.item121.builder;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
                    root.join(joinName);
                    return cbuilder.equal(root.join(joinName).get(leftHand), condition.getRightHand());
                case NOT_EQUAL:
                    return cbuilder.notEqual(root.join(joinName).get(leftHand), condition.getRightHand());
                case GREATER_THAN:
                    return cbuilder.greaterThan(root.join(joinName).get(leftHand), condition.getRightHand());
                case LESS_THAN:
                    return cbuilder.lessThan(root.join(joinName).get(leftHand), condition.getRightHand());
                case LIKE:
                    return cbuilder.like(root.join(joinName).get(leftHand), condition.getRightHand());
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
}
