<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${resume.fullName} — Резюме</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<section>
    <h1>${resume.fullName}</h1>
    <p><strong>UUID:</strong> ${resume.uuid}</p>

    <h2>Контакты</h2>
    <ul>
        <#list resume.contacts as type, value>
            <li><strong>${type.title}:</strong> ${value}</li>
        </#list>
    </ul>

    <h2>Секции</h2>
    <#list resume.sections as type, section>
        <h3>${type.title}</h3>

        <#if section.class.simpleName == "TextSection">
            <p>${section.content}</p>

        <#elseif section.class.simpleName == "ListSection">
            <ul>
                <#list section.items as item>
                    <li>${item}</li>
                </#list>
            </ul>

        <#elseif section.class.simpleName == "OrganizationSection">
            <#list section.organizations as org>
                <div style="margin-bottom: 20px; padding: 10px; background: #181825; border-radius: 8px;">
                    <#if org.name?? && org.name != "">
                        <strong style="font-size: 1.2em; color: #cba6f7;">
                            ${org.name}
                        </strong>
                    </#if>
                    <#if org.website?? && org.website != "">
                        — <a href="${org.website}" target="_blank">${org.website}</a>
                    </#if>
                    <br><br>

                    <#list org.positions as pos>
                        <div style="margin-left: 20px;">
                            <strong>${pos.title}</strong>
                            <em>(${pos.startDate} — ${pos.endDate})</em><br>
                            <#if pos.description?? && pos.description != "">
                                ${pos.description}<br>
                            </#if>
                        </div>
                        <#if pos_has_next><br></#if>
                    </#list>
                </div>
            </#list>
        </#if>
    </#list>

    <p style="margin-top: 40px;">
        <a href="resume" class="btn">← Назад к списку</a>
        <a href="resume?uuid=${resume.uuid}&action=edit" class="btn edit">Редактировать</a>
        <a href="resume?uuid=${resume.uuid}&action=delete"
           class="btn delete"
           onclick="return confirm('Удалить резюме «${resume.fullName}»?')">Удалить</a>
    </p>
</section>
</body>
</html>