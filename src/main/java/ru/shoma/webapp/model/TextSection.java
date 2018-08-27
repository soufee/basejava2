package ru.shoma.webapp.model;

public class TextSection extends Section {

    private static final long serialVersionUid = 1L;
    public static final TextSection EMPTY = new TextSection("");
    private String content;

    public TextSection() {
    }

    public TextSection(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextSection)) return false;

        TextSection that = (TextSection) o;

        return content != null ? content.equals(that.content) : that.content == null;
    }

    @Override
    public int hashCode() {
        return content != null ? content.hashCode() : 0;
    }

    @Override
    public String toString() {
        return content;
    }
}
