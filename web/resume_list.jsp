<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.Resume" %>
<!DOCTYPE html>
<html>
<head>
    <title>Рога и копыта</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link href="css/style.css" rel="stylesheet" type="text/css" media="all">
</head>
<body id="top">
<div class="bgded" style="background-image:url('img/background/imgbin_low-polygon-background-green-banner-png.png');">
    <div class="wrapper overlay">
        <header id="header" class="hoc clear">
            <nav id="mainav" class="clear">
                <ul class="clear">
                    <li><a href="/resumes">Главная</a></li>
                    <li class="active"><a class="drop" href="/resumes/resume">Резюме</a>
                        <ul>
                            <li><a href="/resumes/resume">Показать все</a></li>
                            <li><a href="https://ya.ru/">Яндекс</a></li>
                            <li><a href="https://www.google.com/">Google</a></li>
                        </ul>
                    </li>
                </ul>
            </nav>
            <div id="logo">
                <h2><a href="/resumes">Рекрутинговое агенство "Рога и копыта"</a></h2>
            </div>
        </header>
    </div>
</div>
<div class="wrapper row3">
    <main class="hoc container clear">
        <div class="content">
            <%
                List<Resume> resumeList = (List<Resume>) request.getAttribute("resumeList");
            %>
            <h1>Найдено резюме: <%=resumeList.size()%></h1>
            <div class="scrollable">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Имя Фамилия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        for (Resume resume : resumeList) {
                    %>
                    <tr>
                        <td><a href="/resumes/resume?uuid=<%=resume.getUuid()%>"><%=resume.getUuid()%>
                        </a></td>
                        <td><%=resume.getFullName()%>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="clear"></div>
    </main>
</div>
<a id="backtotop" href="#top"><i class="fa fa-chevron-up"></i></a>
<script src="js/jquery.min.js"></script>
<script src="js/jquery.backtotop.js"></script>
<script src="js/jquery.mobilemenu.js"></script>
<script src="js/jquery.placeholder.min.js"></script>
</body>
</html>