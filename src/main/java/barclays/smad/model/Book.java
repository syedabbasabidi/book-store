package barclays.smad.model;

import barclays.smad.api.Key;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class Book {

    private final Title title;
    private final Set<Author> authors;

    private final Set<Key> keys;

    public Book(Title title, Set<Author> authors) {


        if (title == null || authors == null || authors.size() == 0)
            throw new IllegalArgumentException();

        //take copy and make effectively mutable, don't allow addition or removal of authors
        this.title = title;
        this.authors = new HashSet<>(authors);
        this.keys = new HashSet<>(this.authors);
        keys.add(title);
    }

    public boolean isInvalid() {
        return title.isInvalid() || this.authors.isEmpty() || this.authors.stream().anyMatch(Author::isInvalid);
    }

    public Title getTitle() {
        return title;
    }

    public Set<Author> getAuthors() {
        return unmodifiableSet(authors);
    }

    public Set<Key> getKeys() {
        return unmodifiableSet(keys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!title.equals(book.title)) return false;
        return authors.equals(book.authors);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + authors.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title=" + title +
                ", authors=" + authors +
                '}';
    }
}