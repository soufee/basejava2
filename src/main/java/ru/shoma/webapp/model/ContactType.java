package ru.shoma.webapp.model;

/**
 * Created by Shoma on 04.03.2018.
 */
public enum ContactType {
    ADRESS("Адрес"),
    CELLPHONE("Сотовый телефон"),
    HOMEPHONE("Домашний телефон"),
    WORKPHONE("Рабочий телефон"),
    EMAIL("E-mail") {
        public String toHtml0(String value) {
            return "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    SKYPE("Скайп") {
        public String toHtml0(String value) {
            return "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    SOCNETWORKS("Страницы в соц.сетях");

    private String title;

    public String getTitle() {
        return title;
    }

    ContactType(String title) {
        this.title = title;
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }
}
