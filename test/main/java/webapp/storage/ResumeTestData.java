package main.java.webapp.storage;

import main.java.webapp.model.ContactType;
import main.java.webapp.model.Resume;
import main.java.webapp.model.SectionType;

public class ResumeTestData {
    public static void main(String[] args) {
        Resume r = new Resume("uuid1", "Григорий Кислин");
        r.addContact(ContactType.TELEPHONE, "+7(921) 855-0482");
        r.addContact(ContactType.SKYPE, "skype:grigory.kislin");
        r.addContact(ContactType.EMAIL, "gkislin@yandex.ru");
        r.addContact(ContactType.LINKEDIN, "https://www.linkedin.com/in/gkislin/");
        r.addContact(ContactType.GITHUB, "https://github.com/gkislin");
        r.addContact(ContactType.STACKOVERFLOW, "https://stackoverflow.com/users/548473/grigory-kislin");
        r.addContact(ContactType.HOME_PAGE, "http://gkislin.ru/");
        r.addSection(SectionType.OBJECTIVE, "Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям");
        r.addSection(SectionType.PERSONAL, "Аналитический склад ума, сильная логика, креативность, инициативность. Пурист кода и архитектуры.");
    }
}
