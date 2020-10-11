package com.gentaliti.item121.controller;

import com.gentaliti.item1.entity.Author;
import com.gentaliti.item1.entity.Book;
import com.gentaliti.item121.request.SearchRequest;
import com.gentaliti.item121.service.BookstoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    private final BookstoreService bookstoreService;

    public BookController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @PostMapping("/fetchAuthors")
    public List<Author> fetchAuthors(@RequestBody SearchRequest request) {
        return bookstoreService.fetchAuthors(request.getConditions());
    }

    @GetMapping("/fetchBooksPage")
    public List<Book> fetchBooksPage(@RequestBody SearchRequest request) {
        return bookstoreService.fetchBooksPage(request.getConditions());
    }
}
