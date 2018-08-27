package ru.shoma.webapp.model;

import ru.shoma.webapp.util.DateUtil;
import ru.shoma.webapp.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.shoma.webapp.util.DateUtil.NOW;
import static ru.shoma.webapp.util.DateUtil.of;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
public static final Organization EMPTY = new Organization("", "", Position.EMPTY);
    private static final long serialVersionUid = 1L;

    private Link homepage;
    private String name;
    private List<Position> positions = new ArrayList<>();

    public Organization(Link homepage, List<Position> positions) {
        Objects.requireNonNull(homepage, "The homepage of organization must not be null");
        this.homepage = homepage;
        this.positions = positions;
    }

    public Organization(String name, String url, Position... positions) {
        this(new Link(name, url), Arrays.asList(positions));
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-YYYY");

        return "Organisation: " + homepage + "\nPosition: " + positions;
    }

    public Link getHomepage() {
        return homepage;
    }

    public void setHomepage(Link homepage) {
        this.homepage = homepage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public Organization() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization)) return false;

        Organization that = (Organization) o;

        if (homepage != null ? !homepage.equals(that.homepage) : that.homepage != null) return false;
        return (name != null ? name.equals(that.name) : that.name == null) && (positions != null ? positions.equals(that.positions) : that.positions == null);
    }

    @Override
    public int hashCode() {
        int result = homepage != null ? homepage.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (positions != null ? positions.hashCode() : 0);
        return result;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        public static final Position EMPTY = new Position();
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate endDate;
        private String title;
        private String description;
        private static final long serialVersionUid = 1L;

        public Position(int year, Month month, String title, String description) {
            this(title, DateUtil.of(year, month), NOW, description);
        }

        public Position(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
            this(title, DateUtil.of(startYear, startMonth), DateUtil.of(endYear, endMonth), description);
        }

        public Position() {
        }

        public Position(String title, LocalDate startDate, LocalDate endDate, String description) {
            Objects.requireNonNull(title, "The title must not be null");
            Objects.requireNonNull(startDate, "The start date must not be null");
            this.title = title;
            this.startDate = startDate;
            if (endDate == null)
                this.endDate = NOW;
            else this.endDate = endDate;
            this.description = description== null? " ":description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;

            Position position = (Position) o;

            if (title != null ? !title.equals(position.title) : position.title != null) return false;
            if (startDate != null ? !startDate.equals(position.startDate) : position.startDate != null) return false;
            return (endDate != null ? endDate.equals(position.endDate) : position.endDate == null) && (description != null ? description.equals(position.description) : position.description == null);
        }

        @Override
        public int hashCode() {
            int result = title != null ? title.hashCode() : 0;
            result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
            result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "Position{" +
                    "title='" + title + '\'' +
                    ", startDate=" + startDate +
                    ", endDate=" + endDate +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}

