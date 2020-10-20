package com.gentaliti.item121.demo.custom.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FilterRequest {
    private Integer page = 0;
    private Integer pageSize = 20;
    private String searchBox;
    private List<Filter> filters;
}

