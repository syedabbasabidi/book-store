package barclays.smad.impl;

import barclays.smad.model.Book;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public final class Books {


    private final Set<Book> books = new HashSet<>(10);

    boolean add(Book book) {
        return books.add(book);
    }

    boolean remove(Book book) {
        return books.remove(book);
    }

    Set<Book> getBooks() {
        return new HashSet<>(books);
    }

    Optional<Book> getAny() {
        return !books.isEmpty() ? of(books.iterator().next()) : empty();
    }

    int size() {
        return books.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Books books1 = (Books) o;

        return books.equals(books1.books);
    }

    @Override
    public int hashCode() {
        return books.hashCode();
    }
}
