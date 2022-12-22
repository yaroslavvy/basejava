<%@ page import="com.urise.webapp.model.*" %>
<%@ page import="com.urise.webapp.util.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page buffer="900kb" %>
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
                    <c:set var="formatter" value='${DateTimeFormatter.ofPattern("MM/yyyy")}' scope="page"/>
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
                                <c:set var="companies" value="${CompanyListSection.cast(resume.getSections().get(type)).getCompanies()}"/>
                                <c:choose>
                                    <c:when test="${companies == null}">
                                        <input type="hidden" name="${type.name()}" value="1">
                                        Название организации:<input type="text" name="companyName"/><br>
                                        Сайт организации:<input type="text" name="companyUrl"/><br>
                                        <input type="hidden" name="periodCounter" value="1">
                                        Период с <input type="text" name="periodBeginDate"/>
                                        по <input type="text" name="periodEndDate"/><br>
                                        Должность:<textarea type="text" name="periodTitle" cols="100" rows="2"></textarea><br>
                                        Описание:<textarea type="text" name="periodDescription" cols="100" rows="4"></textarea><br>
                                    </c:when>
                                    <c:when test="${companies != null}">
                                        <c:set var="companyCounter" value="0" scope="page"/>
                                        <c:forEach var="company" items="${companies}">
                                            Название организации:<input type="text" name="companyName" value="${company.getName()}"/><br>
                                            Сайт организации:<input type="text" name="companyUrl" value="${company.getUrl()}"/><br>
                                            <c:set var="periodCounter" value="0" scope="page"/>
                                            <c:forEach var="period" items="${company.getPeriods()}">
                                                Период с <input type="text" name="periodBeginDate" value="${period.getBeginDate().format(formatter)}"/>
                                                по <input type="text" name="periodEndDate" value="${DateUtil.NOW.isEqual(period.getEndDate()) ? "Сейчас" : period.getEndDate().format(formatter)}"/><br>
                                                Должность:<textarea type="text" name="periodTitle" cols="100" rows="2">${period.getTitle()}</textarea><br>
                                                Описание:<textarea type="text" name="periodDescription" cols="100" rows="4">${period.getDescription()}</textarea><br><br>
                                                <c:set var="periodCounter" value="${periodCounter + 1}" scope="page"/>
                                            </c:forEach>
                                            <input type="hidden" name="periodCounter" value="${periodCounter}">
                                            <c:set var="companyCounter" value="${companyCounter + 1}" scope="page"/>
                                        </c:forEach>
                                        <input type="hidden" name="${type.name()}" value="${companyCounter}">
                                    </c:when>
                                </c:choose>
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
