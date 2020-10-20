package com.gentaliti.item121.service;

import com.gentaliti.item1.entity.Author;
import com.gentaliti.item1.entity.Book;
import com.gentaliti.item1.entity.Category;
import com.gentaliti.item1.repository.AuthorRepository;
import com.gentaliti.item1.repository.BookRepository;
import com.gentaliti.item121.builder.Condition;
import com.gentaliti.item121.builder.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public BookstoreService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<Author> fetchAuthors(List<Condition> conditions) {

        Specification<Author> otherSpecifications = joinTest("name");


        List<Condition> authorsConditions = appendPermissionsConditions(conditions);
        Specification<Author> specAuthor = new SpecificationBuilder<Author>(authorsConditions).build();

        return authorRepository.findAll(otherSpecifications.and(specAuthor));
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
                    .name("Author 1")
                    .genre("Anthology")
                    .birthDate(new Date())
                    .build();

            Category category = Category.builder()
                    .name("CATEGORY-" + i)
                    .build();

            Book book = Book.builder()
                    .author(author)
                    .isbn("ISBN-0" + i)
                    .price(60 + i)
                    .title("Title 1")
                    .categories(Set.of(category))
                    .build();

            author.addBook(book);
            authorRepository.save(author);
        }
    }


    public static Specification<Author> titleSpecification() {
        return (Specification<Author>) (root, query, cb) -> {
            query.distinct(true); // <--- HERE
            Join userProd = (Join) root.fetch("books");
            return cb.equal(userProd.get("title"), "Title 1");
        };
    }


    public static Specification<Author> isTrue() {
        return (Specification<Author>) (root, query, cb) -> {
            Subquery<Author> subquery = query.subquery(Author.class);
            Root<Author> subRoot = subquery.from(Author.class);
            subquery.select(subRoot);
            subquery.distinct(true);

            return cb.exists(subquery);
        };
    }


}
