package com.gentaliti.item121.controller;

import com.gentaliti.aspect.ServiceA;
import com.gentaliti.item1.entity.Author;
import com.gentaliti.item1.entity.Book;
import com.gentaliti.item121.demo.FilterDataRsqlVisitor;
import com.gentaliti.item121.demo.custom.FilterVisitor;
import com.gentaliti.item121.demo.custom.request.Filter;
import com.gentaliti.item121.demo.custom.request.FilterRequest;
import com.gentaliti.item121.request.SearchRequest;
import com.gentaliti.item121.service.BookstoreService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    private final BookstoreService bookstoreService;
    private final ServiceA serviceA;

    public BookController(BookstoreService bookstoreService, ServiceA serviceA) {
        this.bookstoreService = bookstoreService;
        this.serviceA = serviceA;
    }

    @PostMapping("/fetchAuthors")
    public List<Author> fetchAuthors(@RequestBody SearchRequest request) {
        return bookstoreService.fetchAuthors(request.getConditions());
    }

    @GetMapping("/fetchBooksPage")
    public List<Book> fetchBooksPage(@RequestBody SearchRequest request) {
        return bookstoreService.fetchBooksPage(request.getConditions());
    }

    @GetMapping("/fetchAuthors")
    public Page<Author> fetchBooks(@RequestBody SearchRequest request, Pageable pageable) {
        return bookstoreService.mergeSpecification(request.getConditions(), pageable);
    }

    @GetMapping("/fetchAuthorsNew")
    public Page<Author> fetchBooksNew(String search, Pageable pageable) {
        Node rootNode = new RSQLParser().parse(search);
        Specification<Author> specification = rootNode.accept(new FilterDataRsqlVisitor<>());
        return bookstoreService.fetchAuthors(Specification.where(specification).and(distinct()), pageable);
    }

    @GetMapping("/fetchAuthorsNew2")
    public FilterRequest fetchBooksNew2(FilterRequest filterRequest) {
        return filterRequest;
    }

    @GetMapping("/fetchAuthorsNew3")
    public FilterRequest fetchBooksNew3(String search, Pageable pageable) {
        return null;
    }

    public static Specification<Author> distinct() {
        return (root, query, cb) -> {
            query.distinct(true);
            return null;
        };
    }

    @GetMapping("/fetchAuthorsManual")
    public Page<Author> fetchBooksPage(String isbn, Pageable pageable) {
        return bookstoreService.fetchAuthorsManual(isbn, pageable);
    }

    @GetMapping("/aop/test")
    public void testAop(){
        this.serviceA.call();
    }
}
