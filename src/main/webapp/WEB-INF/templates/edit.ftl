<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if resume??>Редактировать резюме ${resume.fullName!"Новый"}<#else>Новое резюме</#if></title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h1><#if resume??>Редактировать<#else>Создать</#if> резюме</h1>

<form method="post"
      action="resume<#if resume?? && resume.uuid??>?uuid=${resume.uuid}&action=save<#else>?action=create</#if>">
    <label>Полное имя: <input type="text" name="fullName" value="<#if resume??>${resume.fullName!''}</#if>"
                              required></label><br>

    <h2>Контакты</h2>
    <label>Телефон: <input type="text" name="contact_TELEPHONE"
                           value="<#if resume?? && resume.contacts?? && resume.contacts['TELEPHONE']??>${resume.contacts['TELEPHONE']}</#if>"></label><br>
    <label>Email: <input type="text" name="contact_EMAIL"
                         value="<#if resume?? && resume.contacts?? && resume.contacts['EMAIL']??>${resume.contacts['EMAIL']}</#if>"></label><br>
    <label>Skype: <input type="text" name="contact_SKYPE"
                         value="<#if resume?? && resume.contacts?? && resume.contacts['SKYPE']??>${resume.contacts['SKYPE']}</#if>"></label><br>
    <label>LinkedIn: <input type="text" name="contact_LINKEDIN"
                            value="<#if resume?? && resume.contacts?? && resume.contacts['LINKEDIN']??>${resume.contacts['LINKEDIN']}</#if>"></label><br>
    <label>GitHub: <input type="text" name="contact_GITHUB"
                          value="<#if resume?? && resume.contacts?? && resume.contacts['GITHUB']??>${resume.contacts['GITHUB']}</#if>"></label><br>
    <label>StackOverflow: <input type="text" name="contact_STACKOVERFLOW"
                                 value="<#if resume?? && resume.contacts?? && resume.contacts['STACKOVERFLOW']??>${resume.contacts['STACKOVERFLOW']}</#if>"></label><br>

    <h2>Секции</h2>
    <label>Личные качества (PERSONAL): <textarea
                name="section_PERSONAL"><#if resume?? && resume.sections?? && resume.sections['PERSONAL']??>${(resume.sections['PERSONAL']).content!''}</#if></textarea></label><br>
    <label>Позиция (OBJECTIVE): <textarea
                name="section_OBJECTIVE"><#if resume?? && resume.sections?? && resume.sections['OBJECTIVE']??>${(resume.sections['OBJECTIVE']).content!''}</#if></textarea></label><br>
    <label>Достижения (ACHIEVEMENT): <textarea
                name="section_ACHIEVEMENT"><#if resume?? && resume.sections?? && resume.sections['ACHIEVEMENT']??>${((resume.sections['ACHIEVEMENT']).items![])?join("\n")}</#if></textarea></label><br>
    <label>Квалификация (QUALIFICATIONS): <textarea
                name="section_QUALIFICATIONS"><#if resume?? && resume.sections?? && resume.sections['QUALIFICATIONS']??>${((resume.sections['QUALIFICATIONS']).items![])?join("\n")}</#if></textarea></label><br>

    <h2>Опыт работы</h2>

    <!-- Организация 1 -->
    <div class="org-block">
        <label>Название компании: <input type="text" name="org1_name" value="<#if resume?? && resume.sections?? && resume.sections['EXPERIENCE']?? && resume.sections['EXPERIENCE'].organizations?? && resume.sections['EXPERIENCE'].organizations[0]??>${resume.sections['EXPERIENCE'].organizations[0].name!''}</#if>"></label><br>
        <label>Сайт: <input type="text" name="org1_url" value="<#if resume?? && resume.sections?? && resume.sections['EXPERIENCE']?? && resume.sections['EXPERIENCE'].organizations?? && resume.sections['EXPERIENCE'].organizations[0]??>${resume.sections['EXPERIENCE'].organizations[0].url!''}</#if>"></label><br>

        <!-- Период 1 для этой организации -->
        <label>Период 1: с <input type="date" name="org1_period1_start" value="<#if resume?? && resume.sections?? && resume.sections['EXPERIENCE']?? && resume.sections['EXPERIENCE'].organizations?? && resume.sections['EXPERIENCE'].organizations[0]?? && resume.sections['EXPERIENCE'].organizations[0].periods?? && resume.sections['EXPERIENCE'].organizations[0].periods[0]??>${resume.sections['EXPERIENCE'].organizations[0].periods[0].startDate!''}</#if>">
            по <input type="date" name="org1_period1_end" value="<#if resume?? && resume.sections?? && resume.sections['EXPERIENCE']?? && resume.sections['EXPERIENCE'].organizations?? && resume.sections['EXPERIENCE'].organizations[0]?? && resume.sections['EXPERIENCE'].organizations[0].periods?? && resume.sections['EXPERIENCE'].organizations[0].periods[0]??>${resume.sections['EXPERIENCE'].organizations[0].periods[0].endDate!''}</#if>"></label><br>
        <label>Должность: <input type="text" name="org1_period1_title" value="<#if resume?? && resume.sections?? && resume.sections['EXPERIENCE']?? && resume.sections['EXPERIENCE'].organizations?? && resume.sections['EXPERIENCE'].organizations[0]?? && resume.sections['EXPERIENCE'].organizations[0].periods?? && resume.sections['EXPERIENCE'].organizations[0].periods[0]??>${resume.sections['EXPERIENCE'].organizations[0].periods[0].title!''}</#if>"></label><br>
        <label>Описание: <textarea name="org1_period1_desc"><#if resume?? && resume.sections?? && resume.sections['EXPERIENCE']?? && resume.sections['EXPERIENCE'].organizations?? && resume.sections['EXPERIENCE'].organizations[0]?? && resume.sections['EXPERIENCE'].organizations[0].periods?? && resume.sections['EXPERIENCE'].organizations[0].periods[0]??>${resume.sections['EXPERIENCE'].organizations[0].periods[0].description!''}</#if></textarea></label>
    </div>



    <h2>Образование (EDUCATION)</h2>
    <textarea name="org_EDUCATION" rows="10">
<#if resume?? && resume.sections?? && resume.sections['EDUCATION']??>
    <#list resume.sections['EDUCATION'].organizations![] as org>
        ${org.name}|${org.url!''}
        <#list org.periods![] as period>
            ${period.startDate?string('yyyy-MM-dd')}-${period.endDate?string('yyyy-MM-dd')! 'NOW'}|${period.title}|${period.description!''}
        </#list>

    </#list>
</#if>
</textarea><br>

    <button type="submit" style="padding: 10px 20px; background: #4CAF50; color: white; border: none; cursor: pointer;">Сохранить</button>
    <a href="resume" style="margin-left: 10px;">Отмена</a>
</form>
</body>
</html>