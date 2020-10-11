package com.gentaliti.item121.service;

import com.gentaliti.item1.entity.Author;
import com.gentaliti.item1.entity.Book;
import com.gentaliti.item1.repository.AuthorRepository;
import com.gentaliti.item1.repository.BookRepository;
import com.gentaliti.item121.builder.SpecificationBuilder;
import com.gentaliti.item121.builder.type.LogicalOperatorType;
import com.gentaliti.item121.builder.type.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookstoreService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public void fetchAuthors() {
        SpecificationBuilder<Author> specBuilder = new SpecificationBuilder<>();

        Specification<Author> specAuthor = specBuilder
                .with("age", "40", OperationType.GREATER_THAN, LogicalOperatorType.AND)
                .with("genre", "Anthology", OperationType.EQUAL, LogicalOperatorType.AND)
                .with("books.title", "Title 0", OperationType.EQUAL, LogicalOperatorType.END)
                .build();

        List<Author> authors = authorRepository.findAll(specAuthor);

        System.out.println(authors);
    }

    public void fetchBooksPage(int page, int size) {
        SpecificationBuilder<Book> specBuilder = new SpecificationBuilder<>();

        Specification<Book> specBook = specBuilder
                .with("price", "70", OperationType.LESS_THAN, LogicalOperatorType.END)
                .build();

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.ASC, "title"));

        Page<Book> books = bookRepository.findAll(specBook, pageable);

        System.out.println(books);
        books.forEach(System.out::println);
    }

    @PostConstruct
    public void insertTestingData() {
        for (int i = 0; i < 10; i++) {

            Author author = Author.builder()
                    .age(50 + i)
                    .name("Author " + i)
                    .genre("Anthology")
                    .build();

            Book book = Book.builder()
                    .author(author)
                    .isbn("ISBN-0" + i)
                    .price(60 + i)
                    .title("Title " + i)
                    .build();

            author.addBook(book);
            authorRepository.save(author);
        }
    }
}
