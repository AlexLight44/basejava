package main.java.webapp.storage;


import webapp.model.Resume;

public class ResumeTestData {

    public static Resume createResume(String uuid, String fullName) {
        Resume r = new Resume(uuid, fullName);
//        r.addContact(ContactType.TELEPHONE, "+7(921) 855-0482");
//        r.addContact(ContactType.EMAIL, "gkislin@yandex.ru");
//        r.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin/");
//        r.addContact(ContactType.GITHUB, "https://github.com/gkislin");
//        r.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473/grigory-kislin");
//        r.addContact(ContactType.HOME_PAGE, "http://gkislin.ru/");
//        r.addSection(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
//        r.addSection(SectionType.PERSONAL, new TextSection("Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры."));
//        r.addSection(SectionType.ACHIEVEMENT, new ListSection("Достижение1", "Достижение2"));
//        r.addSection(SectionType.QUALIFICATIONS, new ListSection("Квалификация1", "Квалификация2"));
//        r.addSection(SectionType.EXPERIENCE,
//                new OrganizationSection(List.of(
//                        new Organization("Organization1", "http1",
//                                new Organization.Period(2010, Month.JANUARY, 2012, Month.FEBRUARY, "period1", "description1"),
//                                new Organization.Period(2012, Month.JANUARY, 2014, Month.FEBRUARY, "period2", "description2")
//                        ), new Organization("Organization2", "http2",
//                                Arrays.asList(
//                                        new Organization.Period(2014, Month.JANUARY, 2017, Month.JANUARY, "period3", "description3"),
//                                        new Organization.Period(2017, Month.JANUARY, 2020, Month.AUGUST, "period4", "description4")
//
//                                )))));
//        r.addSection(SectionType.EDUCATION,
//                new OrganizationSection(List.of(
//                        new Organization("Education1", "httpE1",
//                                Arrays.asList(
//                                        new Organization.Period(2020, Month.JANUARY, 2022, Month.DECEMBER, "period5", "description5"),
//                                        new Organization.Period(2022, Month.JANUARY, 2024, Month.JANUARY, "period6", "description6")
//                                )), new Organization("Education2", "httpE2",
//                                Arrays.asList(
//                                        new Organization.Period(2024, Month.JANUARY, "period7", "description7"),
//                                        new Organization.Period(2025, Month.JANUARY, "period8", "description8")
//
//                                )))));
        return r;
    }
}
