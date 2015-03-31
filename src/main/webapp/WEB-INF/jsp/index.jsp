<%--suppress ALL --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:page title="home">

    <jsp:body>
        <fmt:message key="test" bundle="${pageScope.msg}" />
        <fmt:message key="test" bundle="${pageScope.msg}" />
        <h2 class="page-header">Covoiturage</h2>
            <t:journey_form journeyForm="${journeyForm}" creation="true" />
    </jsp:body>

</t:page>