package main.java.webapp.storage;

import main.java.webapp.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ResumeTestData {

    public static Resume createResume(String uuid, String fullName) {
        Resume r = new Resume(uuid, fullName);
        r.addContact(ContactType.TELEPHONE, "+7(921) 855-0482");
        r.addContact(ContactType.EMAIL, "gkislin@yandex.ru");
        r.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin/");
        r.addContact(ContactType.GITHUB, "https://github.com/gkislin");
        r.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473/grigory-kislin");
        r.addContact(ContactType.HOME_PAGE, "http://gkislin.ru/");
        r.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        r.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
        r.addSection(SectionType.ACHIEVEMENT, new ListSection("Достижение1", "Достижение2"));
        r.addSection(SectionType.QUALIFICATIONS, new ListSection("Квалификация1", "Квалификация2"));
        r.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(List.of(
                        new Organization("Organization1", "http1",
                                Arrays.asList(
                                        new Period(LocalDate.of(2015, 1, 12), LocalDate.of(2016, 1, 12), "title1", "description1"),
                                        new Period(LocalDate.of(2016, 1, 12), LocalDate.of(2022, 1, 12), "title2", "description2")
                                )), new Organization("Organization2", "http2",
                                Arrays.asList(
                                        new Period(LocalDate.of(2022, 1, 12), LocalDate.of(2023, 1, 12), "title3", "description3"),
                                        new Period(LocalDate.of(2023, 1, 12), LocalDate.of(2025, 1, 12), "title4", "description4")

                                )))));
        r.addSection(SectionType.EDUCATION,
                new OrganizationSection(List.of(
                        new Organization("Education1", "httpE1",
                                Arrays.asList(
                                        new Period(LocalDate.of(2010, 1, 12), LocalDate.of(2012, 1, 12), "title5", "description5"),
                                        new Period(LocalDate.of(2016, 1, 12), LocalDate.of(2022, 1, 12), "title6", "description6")
                                )), new Organization("Education2", "httpE2",
                                Arrays.asList(
                                        new Period(LocalDate.of(2022, 1, 12), LocalDate.of(2023, 1, 12), "title7", "description7"),
                                        new Period(LocalDate.of(2023, 1, 12), LocalDate.of(2025, 1, 12), "title8", "description8")

                                )))));
        return r;
    }
}
