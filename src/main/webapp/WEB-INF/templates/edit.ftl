<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title><#if resume??>Редактировать резюме ${resume.fullName!"Новый"}<#else>Новое резюме</#if></title>
    <link rel="stylesheet" href="css/style.css">
    <script>
        let orgCounters = {'EXPERIENCE': 0, 'EDUCATION': 0};

        <#if resume?? && resume.sections??>
        <#if resume.sections['EXPERIENCE']?? && resume.sections['EXPERIENCE'].organizations??>
        orgCounters['EXPERIENCE'] = ${resume.sections['EXPERIENCE'].organizations?size};
        </#if>
        <#if resume.sections['EDUCATION']?? && resume.sections['EDUCATION'].organizations??>
        orgCounters['EDUCATION'] = ${resume.sections['EDUCATION'].organizations?size};
        </#if>
        </#if>

        function addOrg(sectionType) {
            let container = document.getElementById(sectionType + '-orgs');
            let index = orgCounters[sectionType]++;
            let prefix = sectionType.toLowerCase() + '_org' + index + '_';
            let newOrg = document.createElement('div');
            newOrg.className = 'org-block';
            newOrg.innerHTML = `
                <label>Название: <input type="text" name="` + prefix + `name"></label><br>
                <label>Сайт: <input type="text" name="` + prefix + `url"></label><br>
                <div id="` + sectionType + `-org` + index + `-periods"></div>
                <button type="button" onclick="addPeriod(` + index + `, '` + sectionType + `')">Добавить период</button>
                <button type="button" onclick="this.parentElement.remove()">Удалить организацию</button><br><br>
            `;
            container.appendChild(newOrg);
        }

        function addPeriod(orgIndex, sectionType) {
            let periodsDiv = document.getElementById(sectionType + '-org' + orgIndex + '-periods');
            let prefix = sectionType.toLowerCase() + '_org' + orgIndex + '_period_';
            let newPeriod = document.createElement('div');
            newPeriod.className = 'period-block';
            newPeriod.innerHTML = `
                <label>С <input type="date" name="` + prefix + `start[]"></label>
                по <input type="date" name="` + prefix + `end[]"></label><br>
                <label>Должность/Название: <input type="text" name="` + prefix + `title[]"></label><br>
                <label>Описание: <textarea name="` + prefix + `desc[]"></textarea></label><br>
                <button type="button" onclick="this.parentElement.remove()">Удалить период</button>
            `;
            periodsDiv.appendChild(newPeriod);
        }
    </script>
</head>
<body>
<h1><#if resume??>Редактировать<#else>Создать</#if> резюме</h1>

<form method="post" action="resume?uuid=${resume.uuid!''}&action=save">
    <input type="hidden" name="uuid" value="${resume.uuid!''}">

    <label>Полное имя: <input type="text" name="fullName" value="${resume.fullName!''}" required></label><br>

    <h2>Контакты</h2>
    <#list ['TELEPHONE', 'EMAIL', 'SKYPE', 'LINKEDIN', 'GITHUB', 'STACKOVERFLOW'] as typeName>
        <#assign type = typeName>
        <label>${type}: <input type="text" name="contact_${type}" value="${resume.contacts[type]!''}"></label><br>
    </#list>

    <h2>Секции</h2>
    <#list ['PERSONAL', 'OBJECTIVE'] as typeName>
        <#assign type = typeName>
        <label>${type}: <textarea name="section_${type}">${(resume.sections[type]!{}).content!''}</textarea></label><br>
    </#list>
    <#list ['ACHIEVEMENT', 'QUALIFICATIONS'] as typeName>
        <#assign type = typeName>
        <label>${type}: <textarea name="section_${type}">${((resume.sections[type]!{}).items![])?join("\n")}</textarea></label><br>
    </#list>

    <h2>Опыт работы (EXPERIENCE)</h2>
    <div id="EXPERIENCE-orgs">
        <#if resume?? && resume.sections?? && resume.sections['EXPERIENCE']?? && resume.sections['EXPERIENCE'].organizations??>
            <#list resume.sections['EXPERIENCE'].organizations as org>
                <#assign prefix = "experience_org" + org_index + "_">
                <div class="org-block">
                    <label>Название: <input type="text" name="${prefix}name" value="${org.name!''}"></label><br>
                    <label>Сайт: <input type="text" name="${prefix}url" value="${org.url!''}"></label><br>
                    <div id="EXPERIENCE-org${org_index}-periods">
                        <#list org.periods![] as p>
                            <div class="period-block">
                                <label>С <input type="date" name="${prefix}period_start[]" value="${p.startDate!''}"></label>
                                по <input type="date" name="${prefix}period_end[]" value="${p.endDate!''}"></label><br>
                                <label>Должность: <input type="text" name="${prefix}period_title[]" value="${p.title!''}"></label><br>
                                <label>Описание: <textarea name="${prefix}period_desc[]">${p.description!''}</textarea></label><br>
                                <button type="button" onclick="this.parentElement.remove()">Удалить период</button>
                            </div>
                        </#list>
                    </div>
                    <button type="button" onclick="addPeriod(${org_index}, 'EXPERIENCE')">Добавить период</button>
                    <button type="button" onclick="this.parentElement.remove()">Удалить организацию</button><br><br>
                </div>
            </#list>
        </#if>
    </div>
    <button type="button" onclick="addOrg('EXPERIENCE')">Добавить организацию</button><br>

    <h2>Образование (EDUCATION)</h2>
    <div id="EDUCATION-orgs">
        <#if resume?? && resume.sections?? && resume.sections['EDUCATION']?? && resume.sections['EDUCATION'].organizations??>
            <#list resume.sections['EDUCATION'].organizations as org>
                <#assign prefix = "education_org" + org_index + "_">
                <div class="org-block">
                    <label>Название: <input type="text" name="${prefix}name" value="${org.name!''}"></label><br>
                    <label>Сайт: <input type="text" name="${prefix}url" value="${org.url!''}"></label><br>
                    <div id="EDUCATION-org${org_index}-periods">
                        <#list org.periods![] as p>
                            <div class="period-block">
                                <label>С <input type="date" name="${prefix}period_start[]" value="${p.startDate!''}"></label>
                                по <input type="date" name="${prefix}period_end[]" value="${p.endDate!''}"></label><br>
                                <label>Название: <input type="text" name="${prefix}period_title[]" value="${p.title!''}"></label><br>
                                <label>Описание: <textarea name="${prefix}period_desc[]">${p.description!''}</textarea></label><br>
                                <button type="button" onclick="this.parentElement.remove()">Удалить период</button>
                            </div>
                        </#list>
                    </div>
                    <button type="button" onclick="addPeriod(${org_index}, 'EDUCATION')">Добавить период</button>
                    <button type="button" onclick="this.parentElement.remove()">Удалить организацию</button><br><br>
                </div>
            </#list>
        </#if>
    </div>
    <button type="button" onclick="addOrg('EDUCATION')">Добавить организацию</button><br>

    <button type="submit">Сохранить</button>
    <a href="resume">Отмена</a>
</form>
</body>
</html>