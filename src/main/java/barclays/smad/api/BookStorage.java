package barclays.smad.api;

import barclays.smad.model.Author;
import barclays.smad.model.Book;
import barclays.smad.model.Title;

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
