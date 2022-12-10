<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <h1>Резюме</h1>
            <a href="/resumes/resume?action=edit&uuid=${resume.uuid}"><img src="img/edit.png" title="Редактировать"></a>
            <p>${htmlFormattedResume}</p>
        </div>
        <div class="clear"></div>
    </main>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>