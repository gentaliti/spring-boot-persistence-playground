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
    public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> criteriaQuery, @NonNull CriteriaBuilder criteriaBuilder) {
        From<?, ?> conditionRoot = root;
        String leftHand = condition.getLeftHand();
        String rightHand = condition.getRightHand();
        String secondRightHand = condition.getSecondRightHand();
        if (condition.getLeftHand().contains(".")) {
            String joinName = condition.getLeftHand().substring(0, condition.getLeftHand().lastIndexOf('.'));
            leftHand = condition.getLeftHand().substring(condition.getLeftHand().lastIndexOf('.') + 1);
            String[] joins = joinName.split("\\.");
            Join join = (Join) PredicateBuilder.getOrCreateFetch(root, joins[0]);
            for (int i = 1; i < joins.length; i++) {
                join = (Join) PredicateBuilder.getOrCreateFetch(join, joins[i]);
            }
            conditionRoot = join;
        }
        return PredicateBuilder.buildPredicate(conditionRoot, criteriaBuilder, condition.getOperation(), leftHand, rightHand, secondRightHand);
    }
}
