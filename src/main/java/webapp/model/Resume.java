package main.java.webapp.model;

import java.util.*;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private final String uuid;
    private final String fullName;
    private Map<ContactType, String> contact = new EnumMap<>(ContactType.class);
    private Map<SectionType, Section> section = new EnumMap<>(SectionType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public Map<ContactType, String> getContact() {
        return contact;
    }

    public void setContact(Map<ContactType, String> contact) {
        this.contact = contact;
    }

    public Map<SectionType, Section> getSection() {
        return section;
    }

    public void setSection(Map<SectionType, Section> selection) {
        this.section = selection;
    }
    public void addContact(ContactType type, String info){
        contact.put(type, info);
    }
    public void addSection(SectionType type, String info){
        section.put(type, new TextSection(info));
    }

    public void addSection(SectionType type, String... info){
        section.put(type, new ListSection(Arrays.asList(info)));
    }
    public StringBuilder getAllResume(){
        StringBuilder str = new StringBuilder();
        str.append(getFullName() + "\n");
        str.append(getContact() + "\n");
        str.append(getSection());
        return str;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) && Objects.equals(fullName, resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName);
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }

    @Override
    public int compareTo(Resume o) {
        int cmp = fullName.compareTo(o.fullName);
        return cmp != 0 ? cmp : uuid.compareTo(o.uuid);
    }


}
