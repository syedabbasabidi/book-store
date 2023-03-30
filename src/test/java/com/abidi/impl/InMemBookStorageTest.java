package com.abidi.impl;

import com.abidi.model.Author;
import com.abidi.model.Book;
import com.abidi.model.Title;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static java.util.Set.of;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class InMemBookStorageTest {

    private final Author abbas = new Author("Abbas");
    private final Author james = new Author("James");
    private final Author ben = new Author("Ben");
    private Book javaGC = book("Java GC", of(abbas, james, ben));
    private Book javaIsFast = book("Java Is Fast", of(abbas, james));
    private Book java19 = book("Java 19", of(abbas));


    @Test(expected = IllegalArgumentException.class)
    public void inValidBookIsRejected() {
        InMemBookStorage inMemBookStorage = new InMemBookStorage(10, 2);
        assertFalse(inMemBookStorage.add(book("", new HashSet<>())));
        assertEquals(0, inMemBookStorage.size());
    }

    @Test
    public void addSameTitleTwice() {
        InMemBookStorage inMemBookStorage = new InMemBookStorage(10, 2);
        assertTrue(inMemBookStorage.add(javaIsFast));
        assertFalse(inMemBookStorage.add(javaIsFast));
        assertEquals(3, inMemBookStorage.size());

    }

    @Test
    public void clientModifyingResponseShouldNotAffectTheCache() {

        InMemBookStorage inMemBookStorage = new InMemBookStorage(10, 2);
        inMemBookStorage.add(javaIsFast);
        inMemBookStorage.add(java19);
        Set<Book> byAuthor = inMemBookStorage.findByAuthor(abbas);

        assertEquals(2, byAuthor.size());
        byAuthor.remove(javaIsFast);
        assertEquals(1, byAuthor.size());
        Set<Book> byAuthorAfterRemovalFromClient = inMemBookStorage.findByAuthor(abbas);
        assertEquals(2, byAuthorAfterRemovalFromClient.size());

    }

    @Test
    public void addTitlesEnsureTheyCanBeFound() {

        InMemBookStorage inMemBookStorage = new InMemBookStorage(10, 3);

        inMemBookStorage.add(java19);
        inMemBookStorage.add(javaIsFast);
        inMemBookStorage.add(javaGC);

        assertEquals(1, inMemBookStorage.findByAuthor(ben).size());
        assertTrue(inMemBookStorage.findByTitle(java19.getTitle()).isPresent());
        assertTrue(inMemBookStorage.findByTitle(javaIsFast.getTitle()).isPresent());
        assertTrue(inMemBookStorage.findByTitle(javaGC.getTitle()).isPresent());
        assertEquals(3, inMemBookStorage.findByAuthor(abbas).size());
        assertEquals(2, inMemBookStorage.findByAuthor(james).size());
        assertEquals(6, inMemBookStorage.size());
    }

    @Test
    public void addTitlesEnsureAfterRemovalTheyAreNotFound() {

        InMemBookStorage inMemBookStorage = new InMemBookStorage(10, 3);

        inMemBookStorage.add(java19);
        inMemBookStorage.add(javaIsFast);
        inMemBookStorage.add(javaGC);
        assertEquals(6, inMemBookStorage.size());

        inMemBookStorage.removeByTile(javaGC.getTitle());

        assertEquals(4, inMemBookStorage.size());
        assertEquals(0, inMemBookStorage.findByAuthor(ben).size());
        assertEquals(2, inMemBookStorage.findByAuthor(abbas).size());
        assertEquals(1, inMemBookStorage.findByAuthor(james).size());

        inMemBookStorage.removeByAuthor(abbas);

        assertEquals(0, inMemBookStorage.findByAuthor(ben).size());
        assertEquals(0, inMemBookStorage.findByAuthor(abbas).size());
        assertEquals(0, inMemBookStorage.findByAuthor(james).size());
        assertEquals(0, inMemBookStorage.size());


    }

    private Book book(String title, Set<Author> authors) {
        return new Book(new Title(title), authors);
    }
}