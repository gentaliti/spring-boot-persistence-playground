package com.gentaliti.item121.demo.custom;

import com.gentaliti.item121.demo.custom.request.Filter;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FilterBuilder {

    public List<Filter> createFilter(Node node) {
        return this.createFilter(node, null);
    }

    public List<Filter> createFilter(Node node, LogicalOperator logicalOperator) {
        if (node instanceof LogicalNode) {
            return createFilter((LogicalNode) node);
        }
        if (node instanceof ComparisonNode) {
            return List.of(createSingleFilter((ComparisonNode) node, logicalOperator));
        }
        return null;
    }

    public List<Filter> createFilter(LogicalNode logicalNode) {
        return logicalNode.getChildren()
                .stream()
                .map(cNode -> createFilter(cNode, logicalNode.getOperator()))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Filter createSingleFilter(ComparisonNode comparisonNode, LogicalOperator logicalOperator) {
        return new Filter(comparisonNode.getSelector(), comparisonNode.getOperator(), comparisonNode.getArguments(), logicalOperator);
    }
}
