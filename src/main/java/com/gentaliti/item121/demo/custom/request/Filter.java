package com.gentaliti.item121.demo.custom.request;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Filter {
    private RsqlSearchOperation operation;
    private LogicalOperator logicalOperator;
    private String selector;
    private List<String> arguments;

    public Filter(String selector, ComparisonOperator operation, List<String> arguments, LogicalOperator logicalOperator) {
        this.selector = selector;
        this.arguments = arguments;
        this.operation = RsqlSearchOperation.getSimpleOperator(operation);
        this.logicalOperator = logicalOperator;
    }
}
