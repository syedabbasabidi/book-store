package com.abidi.api;

import com.abidi.model.Author;
import com.abidi.model.Book;
import com.abidi.model.Title;

import java.util.Optional;
import java.util.Set;

public interface BookStorage {

    Optional<Book> findByTitle(Title title);

    Set<Book> findByAuthor(Author author);

    boolean add(Book book);

    boolean removeByTile(Title title);

    boolean removeByAuthor(Author author);

    int size();
}
