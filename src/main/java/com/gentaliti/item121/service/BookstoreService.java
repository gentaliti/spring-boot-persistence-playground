package com.gentaliti.item121.service;

import com.gentaliti.item1.entity.Author;
import com.gentaliti.item1.entity.Book;
import com.gentaliti.item1.repository.AuthorRepository;
import com.gentaliti.item1.repository.BookRepository;
import com.gentaliti.item121.builder.Condition;
import com.gentaliti.item121.builder.SpecificationBuilder;
import com.gentaliti.item121.builder.SpecificationChunk;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.now;


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

    public Specification<Book> getBooksPaginated(List<Condition> conditions, Pageable pagination) {
        Specification<Book> specification = new SpecificationBuilder<Book>(conditions).build();
        return specification;
    }

    public Specification<Author> tableSpecification() {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            Join<Author, Book> authorJoin = (Join<Author, Book>) SpecificationChunk.getOrCreateFetch(root, "books");
            Fetch<?, ?> pageFetch = (Fetch<?, ?>) SpecificationChunk.getOrCreateFetch(authorJoin, "pages");
            return criteriaBuilder.literal(true).isNotNull();
        });
    }

    public Page<Author> mergeSpecification(List<Condition> conditions, Pageable pagination) {
        Specification<Author> specification = new SpecificationBuilder<Author>(conditions).build();
        return this.authorRepository.findAll(Specification.where(specification).and(tableSpecification()), pagination);
    }

    public Page<Author> fetchAuthors(Specification<Author> filterSpecification, Pageable pageable) {
        return this.authorRepository.findAll(Specification.where(filterSpecification).and(tableSpecification()), pageable);
    }

    public Page<Author> fetchAuthorsManual(String isbn, Pageable pageable) {
        Specification<Author> bookSpecification = ((root, criteriaQuery, criteriaBuilder) -> {
           return criteriaBuilder.equal(root.join("books").get("isbn"), isbn);
        });
        return this.authorRepository.findAll(Specification.where(bookSpecification).and(tableSpecification()), pageable);
    }

    @Data
    public class BookDto {
        String isbn;
        int price;
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
                    .publishedDate(new Date())
                    .title("Title " + i)
                    .build();

            Book book2 = Book.builder()
                    .author(author)
                    .isbn("ISBN-0" + i*100)
                    .price(60 + i)
                    .title("Title " + i*100)
                    .publishedDate(new Date())
                    .build();

            com.gentaliti.item1.entity.Page page = com.gentaliti.item1.entity.Page.builder()
                    .pageNumber(i)
                    .build();

            book.setPages(Collections.singleton(page));

            author.addBook(book);
            author.addBook(book2);
            authorRepository.save(author);
        }
    }
}
