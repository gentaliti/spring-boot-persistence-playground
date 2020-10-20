package com.gentaliti.item1.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue
    private Long id;
    private int age;
    private String name;
    private String genre;
    private int rating;

    /**
     * 1.- Always cascade from parent to child
     * 2.- Don't forget to add mappedBy on parentSide
     * 3.- Dont't forget to add orphanRemoval = true. To delete not referenced children
     * ..
     * 6.- Use fetchType=Lazy on both sides of association
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Book> books;


    /**
     * 4.- Keep Both Sides of the Association in Sync
     */
    public void addBook(Book book) {
        if (this.books == null) {
            this.books = new HashSet<>();
        }
        this.books.add(book);
        book.setAuthor(this);
    }

    public void removeBook(Book book) {
        book.setAuthor(null);
        this.books.remove(book);
    }

    public void removeBooks() {
        this.books.forEach(book -> book.setAuthor(null));
        this.books = new HashSet<>();
    }

    /**
     * 5.- Override equals and hashcode
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(id, author.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    /**
     * 7.- Pay attention of how toString is overridden. It shouldn't does not involve lazy children
     */
    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
