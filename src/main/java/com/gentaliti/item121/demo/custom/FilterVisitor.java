package com.gentaliti.item121.demo.custom;

import com.gentaliti.item121.demo.custom.request.Filter;
import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

import java.util.List;

public class FilterVisitor<T> implements RSQLVisitor<List<Filter>, Void> {

    private FilterBuilder builder;

    public FilterVisitor() {
        builder = new FilterBuilder();
    }

    @Override
    public List<Filter> visit(AndNode node, Void param) {
        return builder.createFilter(node);
    }

    @Override
    public List<Filter> visit(OrNode node, Void param) {
        return builder.createFilter(node);
    }

    @Override
    public List<Filter> visit(ComparisonNode node, Void params) {
        return builder.createFilter(node);
    }
}
