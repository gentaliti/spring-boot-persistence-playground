package com.gentaliti.item1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    @Id
    @GeneratedValue
    private Long id;

    private Integer pageNumber;

    @ManyToMany(mappedBy = "pages", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Book> books;
}
