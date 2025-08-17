package main.java.webapp.model;

public enum ContactType {
    TELEPHONE ("Тел.: "),
    EMAIL("Почта: "),
    SKYPE("Skype: "),
    LINKEDIN("Профиль в LinkedIn"),
    GITHUB("Профиль в GitHub"),
    STACKOVERFLOW("Профиль в Stackoverflow"),
    HOME_PAGE("Домашняя страница");
    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title + ": ";
    }
}
