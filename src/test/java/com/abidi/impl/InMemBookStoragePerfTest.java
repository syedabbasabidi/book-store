package com.abidi.impl;

import com.abidi.api.BookStorage;
import com.abidi.model.Author;
import com.abidi.model.Book;
import com.abidi.model.Title;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;

import java.util.*;

import static java.lang.System.nanoTime;
import static java.util.stream.IntStream.rangeClosed;

public class InMemBookStoragePerfTest implements JLBHTask {

    private static int ONE_MIL = 1_000_000;
    private static int TEN_MIL = ONE_MIL * 10;

    private JLBH jlbh;
    private BookStorage bookStorage;

    private Random random = new Random();
    private List<Title> titles = new ArrayList<>(1000);
    private List<Author> authors = new ArrayList<>(1000);

    private long count = 0;

    //TODO Check for duplicates entries
    public static void main(String[] args) {
        JLBHOptions jlbhOptions = new JLBHOptions().throughput(ONE_MIL / 10).iterations(ONE_MIL).runs(5)
                .warmUpIterations(10_000).recordOSJitter(false).accountForCoordinatedOmission(false)
                .jlbhTask(new InMemBookStoragePerfTest());
        new JLBH(jlbhOptions).start();
    }

    @Override
    public void init(JLBH jlbh) {

        this.jlbh = jlbh;
        this.bookStorage = new InMemBookStorage(TEN_MIL, 4);
        rangeClosed(0, TEN_MIL).forEach(this::initCache);
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run(long startTimeNS) {
        //byAuthor();
        //byTitle();
        addBooks();
        //removeByTitle();
        jlbh.sampleNanos((nanoTime() - 25) - startTimeNS);

    }


    @Override
    public void complete() {
        System.out.println("Number of records read " + count);
    }

    private void removeByTitle() {
        count = this.bookStorage.removeByTile(titles.get(random.nextInt(1000))) ? count + 1 : count;
    }

    private void addBooks() {
        Set<Author> books = new HashSet<>();
        books.add(new Author("Uncle Bob"));
        books.add(new Author("Abbas"));
        this.bookStorage.add(new Book(new Title("Intro of Java" + count++), books));
    }

    private void byTitle() {
        Optional<Book> by = this.bookStorage.findByTitle(titles.get(random.nextInt(1000)));
        count = by.isPresent() ? count + 1 : count;
    }

    private void byAuthor() {
        Set<Book> by = this.bookStorage.findByAuthor(authors.get(random.nextInt(1000)));
        count += by.size();
    }


    private void initCache(int value) {

        HashSet<Author> set = new HashSet<>();

        Author authorAA = new Author("AA" + value);
        Author authorBB = new Author("BB" + value);
        Title title = new Title("T" + value);

        if (titles.size() < 1000) {
            titles.add(title);
        }

        if (authors.size() < 1000) {
            authors.add(authorAA);
        }
        set.add(authorAA);
        set.add(authorBB);
        bookStorage.add(new Book(title, set));
        set.clear();
    }

}