package main.java.webapp.model;

public enum ContactType {
    TELEPHONE ("Тел.: "),
    EMAIL("Почта: "),
    SKYPE("Skype: "),
    LINKEDIN("Профиль в LinkedIn"),
    GITHUB("Профиль в GitHub"),
    STACKOVERFLOW("Профиль в Stackoverflow"),
    HOME_PAGE("Домашняя страница");
    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
