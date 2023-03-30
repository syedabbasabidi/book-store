package com.abidi.model;

import com.abidi.api.Key;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Title implements Key {

    private final String title;

    public Title(String title) {
        this.title = title;
    }

    public boolean isInvalid() {
        return isBlank(this.title) || !(this instanceof Key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Title title1 = (Title) o;

        return title.equalsIgnoreCase(title1.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public String toString() {
        return "Title{" +
                "title='" + title + '\'' +
                '}';
    }
}
