<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.shoma.webapp.model.*" %>
<%@ page import="ru.shoma.webapp.util.HtmlUtil" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.shoma.webapp.model.Resume" scope="request"/>
    <title>Просмотр резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.shoma.webapp.model.ContactType, java.lang.String>"/>
            <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    </p>
    <hr>
    <table cellpadding="2">
        <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<ru.shoma.webapp.model.SectionType, ru.shoma.webapp.model.Section>"/>
        <c:set var="type" value="${sectionEntry.key}"/>
        <c:set var="section" value="${sectionEntry.value}"/>
        <jsp:useBean id="section" type="ru.shoma.webapp.model.Section"/>
        <tr>
            <td><h3><a name="type.name">${type.title}</a></h3></td>
            <c:if test="${type=='OBJECTIVE'}">
                <td>
                    <h3><%=((TextSection) section).getContent()%>
                    </h3>
                </td>
            </c:if>
        </tr>
        <c:if test="${type!='OBJECTIVE'}">
            <c:choose>
                <c:when test="${type=='PERSONAL'}">
                    <tr>
                        <td>
                            <%=((TextSection) section).getContent()%>
                        </td>
                    </tr>
                </c:when>
                <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENT'}">
                    <tr>
                        <td>
                            <ul>
                                <c:forEach var="item" items="<%=((ListSection) section).getItems()%>"></c:forEach>
                                <li>${item}</li>
                            </ul>
                        </td>
                    </tr>
                </c:when>
                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrgSection) section).getOrganizations()%>">
                        <tr>
                            <td colspan="2">
                                <c:choose>
                                    <c:when test="${empty org.homepage.url}">
                                        <h3>${org.homepage.name}</h3>
                                    </c:when>
                                    <c:otherwise>
                                        <h3><a href="${org.homepage.url}">${org.homepage.name}</a></h3>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <c:forEach var="position" items="${org.positions}">
                            <jsp:useBean id="position" type="ru.shoma.webapp.model.Organization.Position"/>
                            <tr>
                                <td width="15%" style="vertical-align: top"><%=HtmlUtil.formatDates(position)%>
                                </td>
                                <td><b>${position.title}</b><br>${position.description}</td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:if>
    </table>
    </c:forEach>
    <button onclick="window.history.back()">OK</button>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
