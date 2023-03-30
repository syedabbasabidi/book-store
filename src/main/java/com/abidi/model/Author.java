package com.abidi.model;

import com.abidi.api.Key;
import org.apache.commons.lang3.StringUtils;

public class Author implements Key {

    private final String firstName;

    public Author(String firstName) {
        this.firstName = firstName;
    }

    public boolean isInvalid() {
        return StringUtils.isBlank(this.firstName) || !(this instanceof Key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return firstName.equalsIgnoreCase(author.firstName);
    }

    @Override
    public String toString() {
        return "Author{" +
                "firstName='" + firstName + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return firstName.hashCode();
    }
}
