package com.gentaliti.item121.controller.converter;

import com.gentaliti.item121.demo.custom.FilterVisitor;
import com.gentaliti.item121.demo.custom.request.Filter;
import com.gentaliti.item121.demo.custom.request.FilterRequest;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class FilterArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(FilterRequest.class);
    }

    @Override
    public FilterRequest resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                         WebDataBinderFactory binderFactory) {
        String filters = webRequest.getParameter("filters");
        String search = webRequest.getParameter("search");
        Integer page = Integer.parseInt(webRequest.getParameter("page"));
        Integer pageSize = Integer.parseInt(webRequest.getParameter("pageSize"));
        Node rootNode = new RSQLParser().parse(filters);
        List<Filter> builtFilters = rootNode.accept(new FilterVisitor<>());
        return new FilterRequest(page, pageSize, search, builtFilters);
    }
}
