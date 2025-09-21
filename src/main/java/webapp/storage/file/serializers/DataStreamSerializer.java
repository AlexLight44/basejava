package webapp.storage.file.serializers;

import webapp.exeption.CorruptedDataFormatException;
import webapp.exeption.UnsupportedFormatVersionException;
import webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements ISerializer {
    private static final String RESUME_HEADER = Resume.class.getSimpleName();
    private static final String RESUME_CONTACTS_COUNT_HEADER = "contacts";
    private static final String RESUME_SECTIONS_COUNT_HEADER = "sections";

    private static final int CURRENT_VERSION = 1;

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(os)) {
            dataOutputStream.writeUTF(RESUME_HEADER);
            dataOutputStream.writeInt(CURRENT_VERSION);

            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            writeContacts(resume, dataOutputStream);
            writeSections(resume, dataOutputStream);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(is)) {
            var header = dataInputStream.readUTF();
            var version = dataInputStream.readInt();

            if (version != CURRENT_VERSION || !RESUME_HEADER.equals(header)) {
                throw new UnsupportedFormatVersionException(version);
            }
            var uuid = dataInputStream.readUTF();
            var fullName = dataInputStream.readUTF();
            var contacts = readContacts(dataInputStream);
            var sections = readSections(dataInputStream);
            return new Resume(uuid, fullName, contacts, sections);
        }
    }

    public void writeContacts(Resume resume, DataOutputStream dos) throws IOException {
        dos.writeUTF(RESUME_CONTACTS_COUNT_HEADER);
        dos.writeInt(resume.getContacts().size());
        for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
            dos.writeUTF(entry.getKey().name());
            dos.writeUTF(entry.getValue());
        }
    }

    public Map<ContactType, String> readContacts(DataInputStream dis) throws IOException {
        var header = dis.readUTF();
        var contactCount = dis.readInt();

        if (contactCount < 0 || !header.equals(RESUME_CONTACTS_COUNT_HEADER)) {
            var errorMessage = "Invalid header or contact count: header=" + header + ", count=" + contactCount;
            throw new CorruptedDataFormatException(errorMessage);
        }

        var contacts = new EnumMap<ContactType, String>(ContactType.class);
        for (int i = 0; i < contactCount; i++) {
            var type = ContactType.valueOf(dis.readUTF());
            var value = dis.readUTF();
            contacts.put(type, value);
        }
        return contacts;
    }

    public void writeSections(Resume resume, DataOutputStream dos) throws IOException {
        dos.writeUTF(RESUME_SECTIONS_COUNT_HEADER);
        dos.writeInt(resume.getSections().size());
        for (Map.Entry<SectionType, Section> entry : resume.getSections().entrySet()) {
            var type = entry.getKey();
            var section = entry.getValue();

            Objects.requireNonNull(section, "Section must mot be null");

            dos.writeUTF(type.name());
            switch (type) {
                case OBJECTIVE, PERSONAL -> writeTextSection(section, dos);
                case QUALIFICATIONS, ACHIEVEMENT -> writeListSection(section, dos);
                case EXPERIENCE, EDUCATION -> writeOrganizationSections(section, dos);
            }
        }
    }

    public Map<SectionType, Section> readSections(DataInputStream dis) throws IOException {
        var header = dis.readUTF();
        int sectionCount = dis.readInt();

        if (sectionCount < 0 || !header.equals(RESUME_SECTIONS_COUNT_HEADER)) {
            var errorMassage = "Invalid header or section count header= " + header + ", count=" + sectionCount;
            throw new CorruptedDataFormatException(errorMassage);
        }

        var sections = new EnumMap<SectionType, Section>(SectionType.class);
        for (int i = 0; i < sectionCount; i++) {
            var typeName = dis.readUTF();
            var type = SectionType.valueOf(typeName);
            var section = switch (type) {
                case OBJECTIVE, PERSONAL -> readTextSection(dis, type);
                case QUALIFICATIONS, ACHIEVEMENT -> readListSection(dis, type);
                case EXPERIENCE, EDUCATION -> readOrganizationSection(dis, type);
            };
            sections.put(type, section);
        }
        return sections;
    }

    private void writeTextSection(Section section, DataOutputStream dos) throws IOException {
        if (section instanceof TextSection textSection) {
            dos.writeUTF(textSection.getContent());
            return;
        }
        var name = section == null ? "null" : section.getClass().getSimpleName();
        throw new IllegalArgumentException("Expected TextSection, but got " + name);
    }

    private TextSection readTextSection(DataInputStream dis, SectionType type) throws IOException {
        var text = dis.readUTF();
        return new TextSection(type, text);
    }

    private void writeListSection(Section section, DataOutputStream dos) throws IOException {
        if (section instanceof ListSection listSection) {
            dos.writeInt(listSection.getItems().size());
            for (String string : listSection.getItems()) {
                dos.writeUTF(string);
            }
            return;
        }
        var name = section == null ? "null" : section.getClass().getSimpleName();
        throw new IllegalArgumentException("Expected ListSection, but got " + name);
    }

    private ListSection readListSection(DataInputStream dis, SectionType type) throws IOException {
        int count = dis.readInt();
        if (count < 0) {
            var errorMassage = "Invalid items count: " + count;
            throw new CorruptedDataFormatException(errorMassage);
        }
        var list = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            var item = dis.readUTF();
            list.add(item);
        }
        return new ListSection(type, list);
    }

    private void writeOrganizationSections(Section section, DataOutputStream dos) throws IOException {
        if (section instanceof OrganizationSection organizationSection) {
            dos.writeInt(organizationSection.getOrganizations().size());
            for (Organization organization : organizationSection.getOrganizations()) {
                writeOrganization(organization, dos);
            }
            return;
        }
        var name = section == null ? "null" : section.getClass().getSimpleName();
        throw new IllegalArgumentException("Expected OrganizationSection, but got " + name);
    }

    private OrganizationSection readOrganizationSection(DataInputStream dis, SectionType type) throws IOException {
        int count = dis.readInt();
        if (count < 0) {
            var errorMassage = "Invalid count of organizations: " + count;
            throw new CorruptedDataFormatException(errorMassage);
        }

        var organizations = new ArrayList<Organization>();
        for (int i = 0; i < count; i++) {
            var organization = readOrganization(dis);
        }
        return new OrganizationSection(type, organizations);
    }

    private void writeOrganization(Organization organization, DataOutputStream dos) throws IOException {
        Objects.requireNonNull(organization, "Organization must not be null");

        dos.writeUTF(organization.getName());
        dos.writeUTF(organization.getUrl());
        dos.writeInt(organization.getPeriods().size());

        for (Organization.Period period : organization.getPeriods()) {
            writePeriod(period, dos);
        }
    }

    private Organization readOrganization(DataInputStream dis) throws IOException {
        var name = dis.readUTF();
        var url = dis.readUTF();

        var count = dis.readInt();
        if (count < 0) {
            var errorMassage = "Invalid count of periods: " + count;
            throw new CorruptedDataFormatException(errorMassage);
        }

        var periods = new ArrayList<Organization.Period>();
        for (int i = 0; i < count; i++) {
            var period = readPeriod(dis);
            periods.add(period);
        }
        return new Organization(name, url, periods);
    }

    private void writePeriod(Organization.Period period, DataOutputStream dos) throws IOException {
        Objects.requireNonNull(period, "Period must not be null");

        dos.writeUTF(period.getTitle());
        dos.writeUTF(period.getDescription());
        dos.writeUTF(period.getStartDate().toString());
        dos.writeUTF(period.getEndDate().toString());
    }

    private Organization.Period readPeriod(DataInputStream dis) throws IOException {
        var title = dis.readUTF();
        var description = dis.readUTF();
        var startDate = LocalDate.parse(dis.readUTF());
        var endDate = LocalDate.parse(dis.readUTF());
        return new Organization.Period(startDate, endDate, title, description);
    }

}
