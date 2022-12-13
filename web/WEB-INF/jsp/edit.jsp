<%@ page import="com.urise.webapp.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Рога и копыта</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <c:if test="${resume != null}">
        <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    </c:if>
    <link href="css/style.css" rel="stylesheet" type="text/css" media="all">
</head>
<body id="top">
<jsp:include page="header.jsp"/>
<div class="wrapper row3">
    <main class="hoc container clear">
        <div class="content">
            <c:if test="${resume == null}">
                <h1>Создать резюме</h1>
            </c:if>
            <c:if test="${resume != null}">
                <h1>Редактировать резюме</h1>
            </c:if>
            <form action="resume" method="POST">
                <input type="hidden" name="uuid" value="${resume.uuid}">
                Имя Фамилия: <input type="text" name="fullName" required pattern="(^[\S].*[\S]$)|([\S])" value="${resume.fullName}"/><br>
                <h1>Контакты:</h1>
                <p>
                    <c:forEach var="type" items="<%=ContactType.values()%>">
                        ${type.title}<input type="text" name="${type.name()}" value="${resume.contacts.get(type)}"><br>
                    </c:forEach>
                </p>
                <p>
                    <c:forEach var="type" items="<%=SectionType.values()%>">
                        <h3>${type.title}</h3>
                        <c:choose>
                            <c:when test="${type == SectionType.OBJECTIVE || type == SectionType.PERSONAL}">
                                <textarea type="text" name="${type.name()}" cols="100" rows="4">${TextSection.cast(resume.getSections().get(type)).getText()}</textarea><br>
                            </c:when>
                            <c:when test="${type == SectionType.ACHIEVEMENTS || type == SectionType.QUALIFICATIONS}">
                                <c:set var="lines" value="${ListSection.cast(resume.getSections().get(type)).getList()}"/>
                                <c:choose>
                                    <c:when test="${lines == null}">
                                        <textarea type="text" name="${type.name()}" cols="100" rows="4"></textarea><br>
                                    </c:when>
                                    <c:when test="${lines != null}">
                                        <c:forEach var="line" items="${lines}">
                                            <textarea type="text" name="${type.name()}" cols="100" rows="4">${line}</textarea><br>
                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                            </c:when>
                            <c:when test="${type == SectionType.EXPERIENCE || type == SectionType.EDUCATION}">
                                <p style="color: red">TODO</p>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                </p>
                <button type="submit">Сохранить</button>
            </form>
            <br>
            <button onclick="window.history.back()">Отменить</button>
        </div>
        <div class="clear"></div>
    </main>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
