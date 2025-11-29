<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список всех резюме</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<section>
    <table class="resume-table">
        <tr>
            <th>Имя</th>
            <th>Email</th>
        </tr>
        <#list resumes as r>
            <tr>
                <td><a href="resume?uuid=${r.uuid}">${r.fullName}</a></td>
                <td>${r.email}</td>
            </tr>
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