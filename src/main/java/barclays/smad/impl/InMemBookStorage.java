package barclays.smad.impl;

import barclays.smad.api.BookStorage;
import barclays.smad.api.Key;
import barclays.smad.model.Author;
import barclays.smad.model.Book;
import barclays.smad.model.Title;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static java.util.Optional.empty;
import static org.slf4j.LoggerFactory.getLogger;

public final class InMemBookStorage implements BookStorage {

    private final HashMap<Key, Books> bookCache;

    private static final Books DUMMY_BOOK = new Books();
    private final static Logger log = getLogger(InMemBookStorage.class);
    private static final Set<Book> DUMMY_SET = unmodifiableSet(new HashSet<>());

    public InMemBookStorage(int size, int numberOfKeys) {//TODO Do we want to make it factory based?!
        bookCache = new HashMap<>(size * numberOfKeys, 1);
        log.info("Book Storage initialized...");
    }

    @Override
    public Optional<Book> findByTitle(Title title) {

        if (title.isInvalid()) {
            log.warn("Invalid Title {}, cannot process request", title);
            return empty();
        }

        return bookCache.getOrDefault(title, DUMMY_BOOK).getAny();
    }

    @Override
    public Set<Book> findByAuthor(Author author) {

        if (author.isInvalid()) {
            log.warn("Invalid Author {}, cannot process request", author);
            return DUMMY_SET;
        }

        return bookCache.getOrDefault(author, DUMMY_BOOK).getBooks();
    }

    @Override
    public boolean add(Book book) {

        if (book.isInvalid()) {
            log.warn("Invalid book {}, cannot store", book);
            return false;
        }

        if (bookCache.containsKey(book.getTitle())) {
            log.warn("Book {} is already added to the store", book);
            return false;
        }

        boolean storedAgainstAllKeys = addForAllKeys(book);
        if (!storedAgainstAllKeys) {
            log.error("Failed to remove book {}, cache is in invalid state for it", book);
        }

        return storedAgainstAllKeys;
    }

    @Override
    public boolean removeByTile(Title title) {

        if (title.isInvalid()) {
            log.warn("Invalid Title {}, cannot process request", title);
            return false;
        }

        Optional<Book> byTitle = findByTitle(title);
        return byTitle.filter(this::removeForAllKeys).isPresent();
    }


    @Override
    public boolean removeByAuthor(Author author) {

        if (author.isInvalid()) {
            log.warn("Invalid Author {}, cannot process request", author);
            return false;
        }

        Set<Book> byAuthor = findByAuthor(author);
        if (byAuthor.isEmpty()) return false;

        boolean removedFromAllKeys = false;
        for (Book book : byAuthor)
            removedFromAllKeys = removeForAllKeys(book);

        return removedFromAllKeys;
    }

    @Override
    public int size() {
        return bookCache.size();
    }

    private boolean addForAllKeys(Book book) {
        for (Key key : book.getKeys()) {
            bookCache.putIfAbsent(key, new Books());
            if (!bookCache.get(key).add(book)) return false;
        }
        return true;
    }

    private boolean removeForAllKeys(Book book) {

        if (book.isInvalid()) {
            log.warn("Invalid book {}, cannot process request", book);
            return false;
        }

        boolean removedFromAllKeys = false;
        for (Key key : book.getKeys()) {
            Books books = bookCache.getOrDefault(key, DUMMY_BOOK);
            removedFromAllKeys = books.size() != 0 && ((books.size() > 1) ? books.remove(book) : bookCache.remove(key) != null);
        }

        if (!removedFromAllKeys) {
            log.error("Failed to remove book {}, cache is in invalid state for it", book);
        }

        return removedFromAllKeys;
    }

}