package com.gentaliti.item121.builder;

import com.gentaliti.item121.builder.type.LogicalOperatorType;
import com.gentaliti.item121.builder.type.OperationType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SpecificationBuilder<T> {

    private final List<Condition> conditions;

    public SpecificationBuilder() {
        conditions = new ArrayList<>();
    }

    public SpecificationBuilder(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public SpecificationBuilder<T> with(String leftHand, String rightHand, String secondRight, OperationType operation, LogicalOperatorType operator) {
        conditions.add(new Condition(leftHand, rightHand, secondRight, operation, operator));
        return this;
    }

    public Specification<T> build() {
        if (conditions.isEmpty()) {
            return null;
        }

        List<Specification<T>> specifications = new ArrayList<>();

        this.conditions.forEach(condition -> specifications.add(new SpecificationChunk<T>(condition)));

        Specification<T> finalSpecification = specifications.get(0);
        for (int i = 1; i < conditions.size(); i++) {
            if (!conditions.get(i - 1).getOperator().equals(LogicalOperatorType.END)) {
                finalSpecification = conditions.get(i - 1).getOperator().equals(LogicalOperatorType.OR)
                        ? Specification.where(finalSpecification).or(specifications.get(i))
                        : Specification.where(finalSpecification).and(specifications.get(i));
            }
        }

        return finalSpecification;
    }
}
