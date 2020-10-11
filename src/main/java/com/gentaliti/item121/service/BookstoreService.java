package com.gentaliti.item121.service;

import com.gentaliti.item1.entity.Author;
import com.gentaliti.item1.entity.Book;
import com.gentaliti.item1.repository.AuthorRepository;
import com.gentaliti.item1.repository.BookRepository;
import com.gentaliti.item121.builder.Condition;
import com.gentaliti.item121.builder.SpecificationBuilder;
import com.gentaliti.item121.builder.SpecificationChunk;
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

    public List<Author> fetchAuthors(List<Condition> conditions) {
        List<Condition> authorsConditions = appendPermissionsConditions(conditions);
        Specification<Author> specAuthor = new SpecificationBuilder<Author>(authorsConditions).build();
        return authorRepository.findAll(specAuthor);
    }

    private List<Condition> appendPermissionsConditions(List<Condition> conditions) {
        // Permission based conditions
        return conditions;
    }

    public List<Book> fetchBooksPage(List<Condition> conditions) {
        Specification<Book> specBook = new SpecificationBuilder<Book>(conditions).build();
        return bookRepository.findAll(specBook);
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
