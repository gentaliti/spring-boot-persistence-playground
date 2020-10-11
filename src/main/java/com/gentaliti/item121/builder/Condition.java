package com.gentaliti.item121.builder;

import com.gentaliti.item121.builder.type.LogicalOperatorType;
import com.gentaliti.item121.builder.type.OperationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Condition {
    private final String leftHand;
    private final String rightHand;
    private final OperationType operation;
    private final LogicalOperatorType operator;

    public Condition(String leftHand, String rightHand, OperationType operation, LogicalOperatorType operator) {
        this.leftHand = leftHand;
        this.rightHand = rightHand;
        this.operation = operation;
        this.operator = operator;
    }
}
