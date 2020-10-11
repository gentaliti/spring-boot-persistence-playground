package com.gentaliti.item121;

import com.gentaliti.item121.service.BookstoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    private final BookstoreService bookstoreService;

    public BookController(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    @GetMapping("/fetchAuthors")
    public void fetchAuthors() {
        bookstoreService.fetchAuthors();
    }

    @GetMapping("/fetchBooksPage")
    public void fetchBooksPage() {
        bookstoreService.fetchBooksPage(0, 10);
    }
}
