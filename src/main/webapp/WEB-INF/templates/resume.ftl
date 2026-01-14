<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${resume.fullName} — Резюме</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        body {
            background: #1e1e2e;
            color: #cdd6f4;
            font-family: Arial;
            margin: 40px;
            line-height: 1.6;
        }

        h1, h2, h3 {
            color: #cba6f7;
        }

        a {
            color: #89b4fa;
        }

        .section {
            margin-bottom: 30px;
        }

        .org {
            margin: 15px 0;
            padding: 10px;
            background: #11111b;
            border-radius: 8px;
        }

        .period {
            font-weight: bold;
            color: #a6e3a1;
        }
    </style>
</head>
<body>

<h1>${resume.fullName}</h1>

<#if resume.contacts?has_content>
    <h2>Контакты</h2>
    <ul>
        <#list resume.contacts as type, value>
            <li><strong>${type.title}:</strong> ${value}</li>
        </#list>
    </ul>
</#if>

<#list resume.sections as type, section>
    <div class="section">
        <h2>${type.title}</h2>

        <#if section.class.simpleName == "TextSection">
            <p>${section.content}</p>
        </#if>

        <#if section.class.simpleName == "ListSection">
            <ul>
                <#list section.items as item>
                    <li>${item}</li>
                </#list>
            </ul>
        </#if>

        <#if section.class.simpleName == "OrganizationSection">
            <#list section.organizations as org>
                <div class="org">
                    <#if org.url?? && org.url?has_content>
                        <strong><a href="${org.url}" target="_blank">${org.name}</a></strong>
                    <#else>
                        <strong>${org.name}</strong>
                    </#if>
                    <br>
                    <#if org.periods?? && org.periods?size gt 0>
                        <#list org.periods![] as period>
                            <span class="period">
                                <#assign startParts = period.startDate?split("-") />
                                ${startParts[1]}.${startParts[0]} —
                              <#if period.endDate??>
                                  <#assign endParts = period.endDate?split("-") />
                                  ${endParts[1]}.${endParts[0]}
                              <#else>
                                  по настоящее время
                              </#if>
                                    </span><br>
                            <strong>${period.title}</strong><br>
                            <#if period.description?? && period.description ?has_content>
                                ${period.description}<br>
                            </#if>
                        </#list>
                    <#else>
                        <p>Нет периодов (отладка: проверьте БД или загрузку)</p>
                    </#if>
                </div>
            </#list>
        </#if>
    </div>
</#list>

<p>
    <a href="resume" class="btn">Назад к списку</a>
</p>

</body>
</html>