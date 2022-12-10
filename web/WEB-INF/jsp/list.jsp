<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Рога и копыта</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link href="css/style.css" rel="stylesheet" type="text/css" media="all">
</head>
<body id="top">
<jsp:include page="header.jsp"/>
<div class="wrapper row3">
    <main class="hoc container clear">
        <div class="content">
            <h1>Найдено резюме: ${listSize}</h1>
            <div class="scrollable">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Имя Фамилия</th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${resumeList}" var="resume">
                        <tr>
                            <td><a href="/resumes/resume?action=view&uuid=${resume.uuid}">${resume.uuid}</a></td>
                            <td>${resume.fullName}</td>
                            <td><a href="/resumes/resume?action=edit&uuid=${resume.uuid}"><img src="img/edit.png" title="Редактировать"></a></td>
                            <td><a href="/resumes/resume?action=delete&uuid=${resume.uuid}"><img src="img/delete.png" title="Удалить"></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="clear"></div>
    </main>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>