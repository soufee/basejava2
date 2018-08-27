package ru.shoma.webapp.model;

import java.util.*;

public class ListSection extends Section {

    private static final long serialVersionUid = 1L;
    public static final ListSection EMPTY = new ListSection("");

    private List<String> contents = new ArrayList<>();

    public ListSection(String... contents) {
        this(Arrays.asList(contents));
    }

    public ListSection(List<String> contents) {
        Objects.requireNonNull(contents, "List of contents must not be null");
        this.contents = contents;
    }

    public ListSection() {
    }

    public List<String> getItems() {
        return contents;
    }

    public void addItem(String item) {
        contents.add(item);
    }

    public void deleteItem(String item) {
        contents.remove(item);
    }

    public List<String> getAll() {
        return new ArrayList<>(contents);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListSection)) return false;
        ListSection that = (ListSection) o;
        return contents != null ? contents.equals(that.contents) : that.contents == null;
    }

    @Override
    public int hashCode() {
        return contents != null ? contents.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : contents) {
            sb.append("- " + s + "\n");
        }
        return sb.toString();
    }
}
