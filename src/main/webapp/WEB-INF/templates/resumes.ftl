<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список всех резюме</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<section>
    <a href="resume?action=edit" class="btn create" style="display: block; margin: 20px 0; padding: 12px 24px; background: #2196F3; color: white; text-decoration: none; border-radius: 4px;">+ Добавить новое резюме</a>
    <table class="resume-table">
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th>Действия</th>
        </tr>
        <#list resumes as r>
            <tr>
                <td><a href="resume?uuid=${r.uuid}">${r.fullName}</a></td>
                <td>${r.email!"-"}</td>
                <td class="actions">
                    <a href="resume?uuid=${r.uuid}&action=edit" class="btn edit">Редактировать</a>
                    <a href="resume?uuid=${r.uuid}&action=delete"
                       class="btn delete"
                       onclick="return confirm('Точно удалить резюме «${r.fullName}»?')">Удалить</a>
                </td>
            </tr>
        <#else>
            <tr><td colspan="3">Нет резюме в базе</td></tr>
        </#list>
        <#if !resumes?has_content>
            <tr>
                <td colspan="2">Нет резюме</td>
            </tr>
        </#if>
    </table>
</section>
</body>
</html>