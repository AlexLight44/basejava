package main.java.webapp.model;

import java.util.List;
import java.util.Objects;

public class Organization {
    private final String name;
    private final String url;
    private final List<Period> periods;


    public Organization(String name, String url, List<Period> periods) {
        this.name = name;
        this.url = url;
        this.periods = periods;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(name, that.name) && Objects.equals(url, that.url) && Objects.equals(periods, that.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, periods);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", periods=" + periods +
                '}';
    }
}





