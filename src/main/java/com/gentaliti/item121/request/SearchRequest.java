package com.gentaliti.item121.request;

import com.gentaliti.item121.builder.Condition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {
    private List<Condition> conditions;
}
